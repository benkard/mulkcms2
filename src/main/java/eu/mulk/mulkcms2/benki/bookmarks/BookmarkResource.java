package eu.mulk.mulkcms2.benki.bookmarks;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.posts.PostFilter;
import eu.mulk.mulkcms2.benki.posts.PostResource;
import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.security.Authenticated;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.jsoup.Jsoup;

@Path("/bookmarks")
public class BookmarkResource extends PostResource {

  @ResourcePath("benki/bookmarks/newBookmark.html")
  @Inject
  Template newBookmark;

  public BookmarkResource() {
    super(PostFilter.BOOKMARKS_ONLY, "Bookmarks");
  }

  @POST
  @Transactional
  @Authenticated
  public Response postBookmark(
      @FormParam("uri") URI uri,
      @FormParam("title") @NotEmpty String title,
      @FormParam("description") String description,
      @FormParam("visibility") @NotNull @Pattern(regexp = "public|semiprivate|private")
          String visibility)
      throws URISyntaxException {

    var userName = identity.getPrincipal().getName();
    var user = User.findByNickname(userName);

    var bookmark = new Bookmark();
    bookmark.uri = uri.toString();
    bookmark.title = title;
    bookmark.tags = Set.of();
    bookmark.description = description != null ? description : "";
    bookmark.owner = user;
    bookmark.date = OffsetDateTime.now();

    if (visibility.equals("public")) {
      Role world = Role.find("from Role r join r.tags tag where tag = 'world'").singleResult();
      bookmark.targets = Set.of(world);
    } else if (visibility.equals("semiprivate")) {
      bookmark.targets = Set.copyOf(user.defaultTargets);
    } else if (!visibility.equals("private")) {
      throw new BadRequestException(String.format("invalid visibility “%s”", visibility));
    }

    bookmark.persistAndFlush();

    return Response.seeOther(new URI("/bookmarks")).build();
  }

  @GET
  @Authenticated
  @Path("new")
  @Produces(TEXT_HTML)
  public TemplateInstance getNewBookmarkForm(
      @QueryParam("uri") @CheckForNull String uri,
      @QueryParam("title") @CheckForNull String title,
      @QueryParam("description") @CheckForNull String description) {
    return newBookmark.data("uri", uri).data("title", title).data("description", description);
  }

  @GET
  @Path("page-info")
  @Authenticated
  @Produces(APPLICATION_JSON)
  public JsonObject getPageInfo(@QueryParam("uri") URI uri) throws IOException {
    var document = Jsoup.connect(uri.toString()).get();
    return jsonProvider.createObjectBuilder().add("title", document.title()).build();
  }
}
