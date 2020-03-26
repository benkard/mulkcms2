package eu.mulk.mulkcms2.benki.bookmarks;

import static javax.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.synd.SyndPersonImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedOutput;
import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.Session;
import org.jboss.logging.Logger;
import org.jsoup.Jsoup;

@Path("/bookmarks")
public class BookmarkResource {

  private static final Logger log = Logger.getLogger(BookmarkResource.class);

  private static final DateTimeFormatter htmlDateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private static final DateTimeFormatter humanDateFormatter =
      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

  private static final JsonProvider jsonProvider = JsonProvider.provider();

  @ConfigProperty(name = "mulkcms.bookmarks.default-max-results")
  int defaultMaxResults;

  @ResourcePath("benki/posts/postList.html")
  @Inject
  Template postList;

  @ResourcePath("benki/bookmarks/newBookmark.html")
  @Inject
  Template newBookmark;

  @Inject SecurityIdentity identity;

  @Context UriInfo uri;

  @Inject
  @ConfigProperty(name = "mulkcms.tag-base")
  String tagBase;

  @PersistenceContext EntityManager entityManager;

  @GET
  @Produces(TEXT_HTML)
  public TemplateInstance getIndex(
      @QueryParam("i") @CheckForNull Integer cursor,
      @QueryParam("n") @CheckForNull Integer maxResults) {

    maxResults = maxResults == null ? defaultMaxResults : maxResults;

    var session = entityManager.unwrap(Session.class);
    var q = Bookmark.findViewable(session, identity, null, cursor, maxResults);

    return postList
        .data("posts", q.posts)
        .data("feedUri", "/bookmarks/feed")
        .data("pageTitle", "Bookmarks")
        .data("showBookmarkForm", false)
        .data("showLazychatForm", false)
        .data("authenticated", !identity.isAnonymous())
        .data("hasPreviousPage", q.prevCursor != null)
        .data("hasNextPage", q.nextCursor != null)
        .data("previousCursor", q.prevCursor)
        .data("nextCursor", q.nextCursor)
        .data("pageSize", maxResults);
  }

  @GET
  @Path("~{ownerName}")
  @Produces(TEXT_HTML)
  public TemplateInstance getUserIndex(
      @PathParam("ownerName") String ownerName,
      @QueryParam("i") @CheckForNull Integer cursor,
      @QueryParam("n") @CheckForNull Integer maxResults) {

    maxResults = maxResults == null ? defaultMaxResults : maxResults;

    var owner = User.findByNickname(ownerName);
    var session = entityManager.unwrap(Session.class);
    var q = Bookmark.findViewable(session, identity, owner, cursor, maxResults);

    return postList
        .data("posts", q.posts)
        .data("feedUri", String.format("/bookmarks/~%s/feed", ownerName))
        .data("pageTitle", "Bookmarks")
        .data("showBookmarkForm", false)
        .data("showLazychatForm", false)
        .data("authenticated", !identity.isAnonymous())
        .data("hasPreviousPage", q.prevCursor != null)
        .data("hasNextPage", q.nextCursor != null)
        .data("previousCursor", q.prevCursor)
        .data("nextCursor", q.nextCursor)
        .data("pageSize", maxResults);
  }

  @GET
  @Path("feed")
  @Produces(APPLICATION_ATOM_XML)
  public String getFeed() throws FeedException {
    return makeFeed(null, null);
  }

  @GET
  @Path("~{ownerName}/feed")
  @Produces(APPLICATION_ATOM_XML)
  public String getUserFeed(@PathParam("ownerName") String ownerName) throws FeedException {
    var owner = User.findByNickname(ownerName);
    return makeFeed(owner, ownerName);
  }

  private String makeFeed(@Nullable User owner, @Nullable String ownerName) throws FeedException {
    var bookmarks = Bookmark.findViewable(entityManager.unwrap(Session.class), identity, owner);
    var feed = new Feed("atom_1.0");

    var feedSubId = owner == null ? "" : String.format("/%d", owner.id);

    feed.setTitle("Book Marx");
    feed.setId(
        String.format(
            "tag:%s,2019:marx%s:%s",
            tagBase,
            feedSubId,
            identity.isAnonymous() ? "world" : identity.getPrincipal().getName()));
    feed.setUpdated(
        Date.from(
            bookmarks.stream()
                .map(x -> x.date)
                .max(Comparator.comparing(x -> x))
                .orElse(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC))
                .toInstant()));

    var selfLink = new Link();
    selfLink.setHref(uri.getRequestUri().toString());
    selfLink.setRel("self");
    feed.setOtherLinks(List.of(selfLink));

    var htmlAltLink = new Link();
    var htmlAltPath = owner == null ? "/bookmarks" : String.format("~%s/bookmarks", ownerName);
    htmlAltLink.setHref(uri.resolve(URI.create(htmlAltPath)).toString());
    htmlAltLink.setRel("alternate");
    htmlAltLink.setType("text/html");
    feed.setAlternateLinks(List.of(htmlAltLink));

    feed.setEntries(
        bookmarks.stream()
            .map(
                bookmark -> {
                  var entry = new Entry();

                  entry.setId(String.format("tag:%s,2012:/marx/%d", tagBase, bookmark.id));
                  entry.setPublished(Date.from(bookmark.date.toInstant()));
                  entry.setUpdated(Date.from(bookmark.date.toInstant()));

                  var author = new SyndPersonImpl();
                  author.setName(bookmark.owner.getFirstAndLastName());
                  entry.setAuthors(List.of(author));

                  var title = new Content();
                  title.setType("text");
                  title.setValue(bookmark.title);
                  entry.setTitleEx(title);

                  var summary = new Content();
                  summary.setType("html");
                  summary.setValue(bookmark.getDescriptionHtml());
                  entry.setSummary(summary);

                  var link = new Link();
                  link.setHref(bookmark.uri);
                  link.setRel("alternate");
                  entry.setAlternateLinks(List.of(link));

                  return entry;
                })
            .collect(Collectors.toUnmodifiableList()));

    var wireFeedOutput = new WireFeedOutput();
    return wireFeedOutput.outputString(feed);
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
  @Path("page-info")
  @Authenticated
  @Produces(APPLICATION_JSON)
  public JsonObject getPageInfo(@QueryParam("uri") URI uri) throws IOException {
    var document = Jsoup.connect(uri.toString()).get();
    return jsonProvider.createObjectBuilder().add("title", document.title()).build();
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
