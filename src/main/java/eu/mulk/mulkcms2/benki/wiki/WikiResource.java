package eu.mulk.mulkcms2.benki.wiki;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_HTML;

import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.spi.JsonProvider;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import javax.annotation.CheckForNull;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@Path("/wiki")
@Blocking
public class WikiResource {

  private static final JsonProvider jsonProvider = JsonProvider.provider();

  @CheckedTemplate(basePath = "benki/wiki")
  static class Templates {

    public static native TemplateInstance wikiPage(WikiPageRevision page);

    public static native TemplateInstance wikiPageRevisionList(WikiPage page, String title);
  }

  @Inject SecurityIdentity identity;

  @GET
  public Response getRoot() throws URISyntaxException {
    return Response.seeOther(new URI("/wiki/Home")).build();
  }

  @GET
  @Path("{pageName}")
  @Produces(TEXT_HTML)
  @Authenticated
  public TemplateInstance getPage(@PathParam("pageName") String pageName) {
    WikiPageRevision page;

    Optional<WikiPageRevision> maybePage =
        WikiPageRevision.find(
                "select rev from WikiPageRevision rev join fetch rev.author where rev.title = ?1",
                Sort.by("date").descending(),
                pageName)
            .firstResultOptional();
    if (maybePage.isPresent()) {
      page = maybePage.get();
    } else {
      var userName = identity.getPrincipal().getName();
      User user =
          User.find("select u from BenkiUser u join u.nicknames n where ?1 = n", userName)
              .singleResult();
      page = new WikiPageRevision();
      page.content = "";
      page.title = pageName;
      page.date = OffsetDateTime.now(ZoneOffset.UTC);
      page.format = "html5";
      page.author = user;
    }

    return Templates.wikiPage(page);
  }

  @POST
  @Path("{pageName}")
  @Authenticated
  @Transactional
  @Produces(APPLICATION_JSON)
  public JsonObject updatePage(
      @PathParam("pageName") String pageName,
      @FormParam("wiki-title") @CheckForNull String title,
      @FormParam("wiki-content") @CheckForNull String content) {

    if (title == null && content == null) {
      // No changes, nothing to do.
      return jsonProvider.createObjectBuilder().add("status", "ok").build();
    }

    if (title != null) {
      // Remove markup.  Reject whitespace.
      title = Jsoup.clean(title, Safelist.none());
      if (!title.matches("\\w+")) {
        throw new BadRequestException("title does not match \"\\w+\"");
      }
    }

    var userName = identity.getPrincipal().getName();
    User user =
        User.find("select u from BenkiUser u join u.nicknames n where ?1 = n", userName)
            .singleResult();

    Optional<WikiPageRevision> maybeCurrentRevision =
        WikiPageRevision.find(
                "select rev from WikiPageRevision rev join fetch rev.author where rev.title = ?1",
                Sort.by("date").descending(),
                pageName)
            .firstResultOptional();

    final WikiPage page;
    if (maybeCurrentRevision.isPresent()) {
      // Update the existing page.
      var currentRevision = maybeCurrentRevision.get();
      page = currentRevision.page;

      title = title != null ? title : currentRevision.title;
      content = content != null ? content : currentRevision.content;
    } else {
      // Create a new page.
      page = new WikiPage();
      page.persist();

      title = title != null ? title : pageName;
      content = content != null ? content : "";
    }

    var pageRevision =
        new WikiPageRevision(OffsetDateTime.now(), title, content, "html5", page, user);
    pageRevision.persist();

    return jsonProvider
        .createObjectBuilder()
        .add("status", "ok")
        .add("content", pageRevision.enrichedContent())
        .add("title", pageRevision.title)
        .build();
  }

  @GET
  @Path("{pageName}/revisions")
  @Produces(TEXT_HTML)
  @Authenticated
  public TemplateInstance getPageRevisions(@PathParam("pageName") String pageName) {
    Optional<WikiPageRevision> maybePrimaryRevision =
        WikiPageRevision.find(
                "select rev from WikiPageRevision rev join fetch rev.author where rev.title = ?1",
                Sort.by("date").descending(),
                pageName)
            .firstResultOptional();
    if (maybePrimaryRevision.isEmpty()) {
      throw new NotFoundException();
    }
    var primaryRevision = maybePrimaryRevision.get();

    WikiPage page =
        WikiPageRevision.find(
                "select p from WikiPage p"
                    + " join fetch p.revisions rev"
                    + " join fetch rev.author"
                    + " where p.id = ?1",
                primaryRevision.page.id)
            .singleResult();

    return Templates.wikiPageRevisionList(page, pageName);
  }
}
