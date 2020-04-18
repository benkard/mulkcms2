package eu.mulk.mulkcms2.benki.posts;

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
import io.quarkus.security.identity.SecurityIdentity;
import java.net.URI;
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
import javax.json.spi.JsonProvider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.Session;
import org.jboss.logging.Logger;

public abstract class PostResource {

  private static final Logger log = Logger.getLogger(PostResource.class);

  private static final DateTimeFormatter htmlDateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private static final DateTimeFormatter humanDateFormatter =
      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

  protected static final JsonProvider jsonProvider = JsonProvider.provider();

  @ConfigProperty(name = "mulkcms.posts.default-max-results")
  int defaultMaxResults;

  @ResourcePath("benki/posts/postList.html")
  @Inject
  Template postList;

  @Inject protected SecurityIdentity identity;

  @Context protected UriInfo uri;

  @Inject
  @ConfigProperty(name = "mulkcms.tag-base")
  String tagBase;

  @PersistenceContext protected EntityManager entityManager;

  private final PostFilter postFilter;
  private final String pageTitle;

  public PostResource(PostFilter postFilter, String pageTitle) {
    this.postFilter = postFilter;
    this.pageTitle = pageTitle;
  }

  @GET
  @Produces(TEXT_HTML)
  public TemplateInstance getIndex(
      @QueryParam("i") @CheckForNull Integer cursor,
      @QueryParam("n") @CheckForNull Integer maxResults) {

    maxResults = maxResults == null ? defaultMaxResults : maxResults;

    var session = entityManager.unwrap(Session.class);
    var q = Post.findViewable(postFilter, session, identity, null, cursor, maxResults);

    return postList
        .data("posts", q.posts)
        .data("feedUri", "/posts/feed")
        .data("pageTitle", pageTitle)
        .data("showBookmarkForm", showBookmarkForm())
        .data("showLazychatForm", showLazychatForm())
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
    var q = Post.findViewable(postFilter, session, identity, owner, cursor, maxResults);

    return postList
        .data("posts", q.posts)
        .data("feedUri", String.format("/posts/~%s/feed", ownerName))
        .data("pageTitle", pageTitle)
        .data("showBookmarkForm", showBookmarkForm())
        .data("showLazychatForm", showLazychatForm())
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
    var posts = Post.findViewable(postFilter, entityManager.unwrap(Session.class), identity, owner);
    var feed = new Feed("atom_1.0");

    var feedSubId = owner == null ? "" : String.format("/%d", owner.id);

    feed.setTitle(String.format("Benki → %s", pageTitle));
    feed.setId(
        String.format(
            "tag:%s,2019:%s:%s:%s",
            tagBase,
            pageTitle,
            feedSubId,
            identity.isAnonymous() ? "world" : identity.getPrincipal().getName()));
    feed.setUpdated(
        Date.from(
            posts.stream()
                .map(x -> x.date)
                .max(Comparator.comparing(x -> x))
                .orElse(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC))
                .toInstant()));

    var selfLink = new Link();
    selfLink.setHref(uri.getRequestUri().toString());
    selfLink.setRel("self");
    feed.setOtherLinks(List.of(selfLink));

    var htmlAltLink = new Link();
    var htmlAltPath = owner == null ? "/posts" : String.format("~%s/posts", ownerName);
    htmlAltLink.setHref(uri.resolve(URI.create(htmlAltPath)).toString());
    htmlAltLink.setRel("alternate");
    htmlAltLink.setType("text/html");
    feed.setAlternateLinks(List.of(htmlAltLink));

    feed.setEntries(
        posts.stream()
            .map(
                post -> {
                  var entry = new Entry();

                  entry.setId(String.format("tag:%s,2012:/marx/%d", tagBase, post.id));
                  entry.setPublished(Date.from(post.date.toInstant()));
                  entry.setUpdated(Date.from(post.date.toInstant()));

                  var author = new SyndPersonImpl();
                  author.setName(post.owner.getFirstAndLastName());
                  entry.setAuthors(List.of(author));

                  if (post.getTitle() != null) {
                    var title = new Content();
                    title.setType("text");
                    title.setValue(post.getTitle());
                    entry.setTitleEx(title);
                  }

                  if (post.getDescriptionHtml() != null) {
                    var summary = new Content();
                    summary.setType("html");
                    summary.setValue(post.getDescriptionHtml());
                    entry.setSummary(summary);
                  }

                  if (post.getUri() != null) {
                    var link = new Link();
                    link.setHref(post.getUri());
                    link.setRel("alternate");
                    entry.setAlternateLinks(List.of(link));
                  }

                  return entry;
                })
            .collect(Collectors.toUnmodifiableList()));

    var wireFeedOutput = new WireFeedOutput();
    return wireFeedOutput.outputString(feed);
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

  private boolean showBookmarkForm() {
    switch (postFilter) {
      case ALL:
      case BOOKMARKS_ONLY:
        return !identity.isAnonymous();
      case LAZYCHAT_MESSAGES_ONLY:
        return false;
      default:
        throw new IllegalStateException();
    }
  }

  private boolean showLazychatForm() {
    switch (postFilter) {
      case ALL:
      case LAZYCHAT_MESSAGES_ONLY:
        return !identity.isAnonymous();
      case BOOKMARKS_ONLY:
        return false;
      default:
        throw new IllegalStateException();
    }
  }

  protected final Session getSession() {
    return entityManager.unwrap(Session.class);
  }

  protected static void assignPostTargets(Post.Visibility visibility, User user, Post post) {
    switch (visibility) {
      case PUBLIC:
        post.targets = Set.of(Role.getWorld());
        break;
      case SEMIPRIVATE:
        post.targets = Set.copyOf(user.defaultTargets);
        break;
      case PRIVATE:
        post.targets = Set.of();
        break;
      default:
        throw new BadRequestException(String.format("invalid visibility “%s”", visibility));
    }
  }

  @CheckForNull
  protected final User getCurrentUser() {
    if (identity.isAnonymous()) {
      return null;
    }

    var userName = identity.getPrincipal().getName();
    return User.findByNickname(userName);
  }

  protected final Post getPostIfVisible(int id) {
    @CheckForNull var user = getCurrentUser();
    var message = getSession().byId(Post.class).load(id);

    if (!message.isVisibleTo(user)) {
      throw new ForbiddenException();
    }

    return message;
  }

  @GET
  @Produces(APPLICATION_JSON)
  @Path("{id}")
  public Post getPostJson(@PathParam("id") int id) {
    return getPostIfVisible(id);
  }

  @GET
  @Produces(TEXT_HTML)
  @Path("{id}")
  public TemplateInstance getPostHtml(@PathParam("id") int id) {
    var post = getPostIfVisible(id);

    return postList
        .data("posts", List.of(post))
        .data("pageTitle", pageTitle)
        .data("showBookmarkForm", false)
        .data("showLazychatForm", false)
        .data("hasPreviousPage", false)
        .data("hasNextPage", false)
        .data("previousCursor", null)
        .data("nextCursor", null)
        .data("pageSize", null);
  }
}
