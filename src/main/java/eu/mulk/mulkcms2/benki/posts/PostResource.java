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
import eu.mulk.mulkcms2.benki.accesscontrol.PageKey;
import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.security.identity.SecurityIdentity;
import java.math.BigInteger;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.json.spi.JsonProvider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

  private static final int pageKeyBytes = 32;

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

  private final SecureRandom secureRandom;

  private final PostFilter postFilter;
  private final String pageTitle;

  public PostResource(PostFilter postFilter, String pageTitle) throws NoSuchAlgorithmException {
    this.postFilter = postFilter;
    this.pageTitle = pageTitle;
    secureRandom = SecureRandom.getInstanceStrong();
  }

  @GET
  @Produces(TEXT_HTML)
  public TemplateInstance getIndex(
      @QueryParam("i") @CheckForNull Integer cursor,
      @QueryParam("n") @CheckForNull Integer maxResults) {

    maxResults = maxResults == null ? defaultMaxResults : maxResults;

    @CheckForNull var reader = getCurrentUser();
    var session = entityManager.unwrap(Session.class);
    var q = Post.findViewable(postFilter, session, reader, null, cursor, maxResults);

    var feedUri = "/posts/feed";
    if (reader != null) {
      var pageKey = ensurePageKey(reader, feedUri);
      feedUri += "?page-key=" + pageKey.key.toString(36);
    }

    return postList
        .data("posts", q.posts)
        .data("feedUri", feedUri)
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

    @CheckForNull var reader = getCurrentUser();
    var owner = User.findByNickname(ownerName);
    var session = entityManager.unwrap(Session.class);
    var q = Post.findViewable(postFilter, session, reader, owner, cursor, maxResults);

    var feedUri = String.format("/posts/~%s/feed", ownerName);
    if (reader != null) {
      var pageKey = ensurePageKey(reader, feedUri);
      feedUri += "?page-key=" + pageKey.key.toString(36);
    }

    return postList
        .data("posts", q.posts)
        .data("feedUri", feedUri)
        .data("pageTitle", pageTitle)
        .data("showBookmarkForm", showBookmarkForm())
        .data("showLazychatForm", showLazychatForm())
        .data("hasPreviousPage", q.prevCursor != null)
        .data("hasNextPage", q.nextCursor != null)
        .data("previousCursor", q.prevCursor)
        .data("nextCursor", q.nextCursor)
        .data("pageSize", maxResults);
  }

  @Transactional
  protected final PageKey ensurePageKey(User reader, String pagePath) {
    PageKey pageKey = PageKey.find("page = ?1 AND user = ?2", pagePath, reader).firstResult();
    if (pageKey == null) {
      pageKey = new PageKey();
      byte[] keyBytes = new byte[pageKeyBytes];
      secureRandom.nextBytes(keyBytes);
      pageKey.key = new BigInteger(keyBytes);
      pageKey.page = pagePath;
      pageKey.user = reader;
      pageKey.persist();
    }
    return pageKey;
  }

  @GET
  @Path("feed")
  @Produces(APPLICATION_ATOM_XML)
  public String getFeed(@QueryParam("page-key") @CheckForNull String pageKeyBase36)
      throws FeedException {
    @CheckForNull var pageKey = pageKeyBase36 == null ? null : new BigInteger(pageKeyBase36, 36);
    return makeFeed(pageKey, null, null);
  }

  @GET
  @Path("~{ownerName}/feed")
  @Produces(APPLICATION_ATOM_XML)
  public String getUserFeed(
      @QueryParam("page-key") @CheckForNull String pageKeyBase36,
      @PathParam("ownerName") String ownerName)
      throws FeedException {
    var owner = User.findByNickname(ownerName);
    @CheckForNull var pageKey = pageKeyBase36 == null ? null : new BigInteger(pageKeyBase36, 36);
    return makeFeed(pageKey, ownerName, owner);
  }

  private String makeFeed(
      @CheckForNull BigInteger pageKey, @CheckForNull String ownerName, @CheckForNull User owner)
      throws FeedException {
    if (pageKey == null) {
      return makeFeed(getCurrentUser(), owner, ownerName);
    }

    Optional<PageKey> pageKeyInfo =
        PageKey.find("page = ?1 AND key = ?2", uri.getPath(), pageKey).singleResultOptional();
    if (pageKeyInfo.isEmpty()) {
      throw new ForbiddenException();
    }

    var pageKeyOwner = pageKeyInfo.get().user;
    return makeFeed(pageKeyOwner, owner, ownerName);
  }

  private String makeFeed(
      @CheckForNull User reader, @Nullable User owner, @Nullable String ownerName)
      throws FeedException {
    var posts = Post.findViewable(postFilter, entityManager.unwrap(Session.class), reader, owner);
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
                .filter(Objects::nonNull)
                .max(Comparator.comparing(x -> x))
                .orElse(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC))
                .toInstant()));

    var selfLink = new Link();
    selfLink.setHref(uri.getRequestUri().toString());
    selfLink.setRel("self");
    feed.setOtherLinks(List.of(selfLink));

    var htmlAltLink = new Link();
    var htmlAltPath = ownerName == null ? "/posts" : String.format("~%s/posts", ownerName);
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
                  if (post.date != null) {
                    entry.setPublished(Date.from(post.date.toInstant()));
                    entry.setUpdated(Date.from(post.date.toInstant()));
                  }

                  if (post.owner != null) {
                    var author = new SyndPersonImpl();
                    author.setName(post.owner.getFirstAndLastName());
                    entry.setAuthors(List.of(author));
                  }

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

  protected final Post getPostIfVisible(int id) {
    @CheckForNull var user = getCurrentUser();
    var message = getSession().byId(Post.class).load(id);

    if (!message.isVisibleTo(user)) {
      throw new ForbiddenException();
    }

    return message;
  }

  @CheckForNull
  protected final User getCurrentUser() {
    return identity.isAnonymous() ? null : User.findByNickname(identity.getPrincipal().getName());
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
