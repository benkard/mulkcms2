package eu.mulk.mulkcms2.benki.wiki;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import javax.annotation.CheckForNull;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@Path("/wiki")
public class WikiResource {

  private static final DateTimeFormatter htmlDateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private static final DateTimeFormatter humanDateFormatter =
      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

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
    Optional<WikiPageRevision> maybePage =
        WikiPageRevision.find(
                "from WikiPageRevision rev join fetch rev.author where rev.title = ?1",
                Sort.by("date").descending(),
                pageName)
            .firstResultOptional();
    if (maybePage.isEmpty()) {
      throw new NotFoundException();
    }
    var page = maybePage.get();
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

    Optional<WikiPageRevision> maybeCurrentRevision =
        WikiPageRevision.find(
                "from WikiPageRevision rev join fetch rev.author where rev.title = ?1",
                Sort.by("date").descending(),
                pageName)
            .firstResultOptional();
    if (maybeCurrentRevision.isEmpty()) {
      throw new NotFoundException();
    }
    var currentRevision = maybeCurrentRevision.get();

    var pageRevision =
        new WikiPageRevision(
            OffsetDateTime.now(),
            title != null ? title : currentRevision.title,
            content != null ? content : currentRevision.content,
            "html5",
            currentRevision.page,
            User.find("from BenkiUser u join u.nicknames n where ?1 = n", userName).singleResult());

    pageRevision.persistAndFlush();

    return jsonProvider
        .createObjectBuilder()
        .add("status", "ok")
        .add("content", pageRevision.enrichedContent())
        .build();
  }

  @GET
  @Path("{pageName}/revisions")
  @Produces(TEXT_HTML)
  @Authenticated
  public TemplateInstance getPageRevisions(@PathParam("pageName") String pageName) {
    Optional<WikiPageRevision> maybePrimaryRevision =
        WikiPageRevision.find(
                "from WikiPageRevision rev join fetch rev.author where rev.title = ?1",
                Sort.by("date").descending(),
                pageName)
            .firstResultOptional();
    if (maybePrimaryRevision.isEmpty()) {
      throw new NotFoundException();
    }
    var primaryRevision = maybePrimaryRevision.get();

    WikiPage page =
        WikiPageRevision.find(
                "from WikiPage p"
                    + " join fetch p.revisions rev"
                    + " join fetch rev.author"
                    + " where p.id = ?1",
                primaryRevision.page.id)
            .singleResult();

    return Templates.wikiPageRevisionList(page, pageName);
  }

  @TemplateExtension
  @CheckForNull
  static String humanDateTime(@CheckForNull TemporalAccessor x) {
    if (x == null) {
      return null;
    }
    return humanDateFormatter.format(x);
  }

  @TemplateExtension
  @CheckForNull
  static String htmlDateTime(@CheckForNull TemporalAccessor x) {
    if (x == null) {
      return null;
    }
    return htmlDateFormatter.format(x);
  }
}
