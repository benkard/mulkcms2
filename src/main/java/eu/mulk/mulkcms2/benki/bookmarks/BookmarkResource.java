package eu.mulk.mulkcms2.benki.bookmarks;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.security.identity.SecurityIdentity;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.json.spi.JsonProvider;
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
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/bookmarks")
public class BookmarkResource {

  private static Logger log = Logger.getLogger(BookmarkResource.class);

  private static DateTimeFormatter htmlDateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private static DateTimeFormatter humanDateFormatter =
      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

  private static JsonProvider jsonProvider = JsonProvider.provider();

  @ResourcePath("benki/bookmarks/bookmarkList.html")
  @Inject
  Template bookmarkList;

  @Inject SecurityIdentity identity;

  @GET
  @Produces(TEXT_HTML)
  public TemplateInstance getIndex() {
    List<Bookmark> bookmarks;
    if (identity.isAnonymous()) {
      Role world = Role.find("from Role r join r.tags tag where tag = 'world'").singleResult();
      bookmarks =
          Bookmark.find(
                  "select bm from Bookmark bm join bm.targets target left join fetch bm.owner where target = ?1",
                  Sort.by("date").descending(),
                  world)
              .list();
    } else {
      var userName = identity.getPrincipal().getName();
      User user =
          User.find("from BenkiUser u join u.nicknames n where ?1 = n", userName).singleResult();
      bookmarks =
          Bookmark.find(
                  "select bm from BenkiUser u inner join u.visibleBookmarks bm left join fetch bm.owner where u.id = ?1",
                  Sort.by("date").descending(),
                  user.id)
              .list();
    }
    return bookmarkList
        .data("bookmarks", bookmarks)
        .data("authenticated", !identity.isAnonymous());
  }

  @POST
  @Transactional
  public Response postBookmark(
      @FormParam("uri") URI uri,
      @FormParam("title") @NotEmpty String title,
      @FormParam("description") String description,
      @FormParam("visibility") @NotNull @Pattern(regexp = "public|semiprivate|private") String visibility)
      throws URISyntaxException {

    var userName = identity.getPrincipal().getName();
    User user =
        User.find("from BenkiUser u join u.nicknames n where ?1 = n", userName).singleResult();

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

  @TemplateExtension
  static String humanDateTime(TemporalAccessor x) {
    return humanDateFormatter.format(x);
  }

  @TemplateExtension
  static String htmlDateTime(TemporalAccessor x) {
    return htmlDateFormatter.format(x);
  }
}
