package eu.mulk.mulkcms2.benki.bookmarks;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.WILDCARD;

import eu.mulk.mulkcms2.benki.posts.Post;
import eu.mulk.mulkcms2.benki.posts.PostFilter;
import eu.mulk.mulkcms2.benki.posts.PostResource;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.jsoup.Jsoup;

@Path("/bookmarks")
public class BookmarkResource extends PostResource {

  @CheckedTemplate(basePath = "benki/bookmarks")
  static class Templates {

    public static native TemplateInstance newBookmark(
        @CheckForNull String uri, @CheckForNull String title, @CheckForNull String description);
  }

  public BookmarkResource() throws NoSuchAlgorithmException {
    super(PostFilter.BOOKMARKS_ONLY, "Bookmarks");
  }

  @POST
  @Transactional
  @Authenticated
  @Produces(WILDCARD)
  @Consumes({APPLICATION_FORM_URLENCODED, MULTIPART_FORM_DATA})
  public Response postBookmark(
      @FormParam("uri") @NotNull URI uri,
      @FormParam("title") @NotEmpty String title,
      @FormParam("description") @CheckForNull String description,
      @FormParam("visibility") @NotNull Post.Visibility visibility)
      throws URISyntaxException {

    var user = Objects.requireNonNull(getCurrentUser());

    var bookmark = new Bookmark();
    bookmark.uri = uri.toString();
    bookmark.tags = Set.of();
    bookmark.setTitle(title);
    bookmark.setDescription(description);
    bookmark.owner = user;
    bookmark.date = OffsetDateTime.now();

    assignPostTargets(visibility, user, bookmark);

    bookmark.persistAndFlush();

    return Response.seeOther(new URI("/bookmarks")).build();
  }

  @POST
  @Transactional
  @Authenticated
  @Produces(WILDCARD)
  @Consumes({APPLICATION_FORM_URLENCODED, MULTIPART_FORM_DATA})
  @Path("{id}/edit")
  public Response patchMessage(
      @PathParam("id") int id,
      @FormParam("uri") @NotNull URI uri,
      @FormParam("title") @NotEmpty String title,
      @FormParam("description") @CheckForNull String description,
      @FormParam("visibility") Post.Visibility visibility)
      throws URISyntaxException {

    var user = Objects.requireNonNull(getCurrentUser());

    var bookmark = getSession().byId(Bookmark.class).load(id);

    if (bookmark == null) {
      throw new NotFoundException();
    }

    if (bookmark.owner == null || !Objects.equals(bookmark.owner.id, user.id)) {
      throw new ForbiddenException();
    }

    bookmark.uri = uri.toString();
    bookmark.tags = Set.of();
    bookmark.setTitle(title);
    bookmark.setDescription(description);
    bookmark.owner = user;

    assignPostTargets(visibility, user, bookmark);

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
    return Templates.newBookmark(uri, title, description);
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
