package eu.mulk.mulkcms2.benki.posts;

import static jakarta.ws.rs.core.MediaType.APPLICATION_ATOM_XML;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_HTML;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.synd.SyndPersonImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedOutput;
import eu.mulk.mulkcms2.benki.accesscontrol.PageKey;
import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.lazychat.LazychatMessage;
import eu.mulk.mulkcms2.benki.login.LoginRoles;
import eu.mulk.mulkcms2.benki.posts.Post.PostPage;
import eu.mulk.mulkcms2.benki.posts.Post.Scope;
import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.json.spi.JsonProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.Session;
import org.jsoup.Jsoup;

@Blocking
public abstract class PostResource {

  private static final String hashcashDigestAlgorithm = "SHA-256";

  private static final int pageKeyBytes = 32;

  private static final int AUTOTITLE_WORDS = 10;

  protected static final JsonProvider jsonProvider = JsonProvider.provider();

  @ConfigProperty(name = "mulkcms.posts.default-max-results")
  int defaultMaxResults;

  @ConfigProperty(name = "quarkus.mailer.from")
  String mailSenderAddress;

  @CheckedTemplate
  static class Templates {

    public static native <P extends Post<?>> TemplateInstance postList(
        List<Post.Day<P>> postDays,
        @CheckForNull String feedUri,
        String pageTitle,
        Boolean showBookmarkForm,
        Boolean showLazychatForm,
        Boolean showCommentBox,
        Boolean hasPreviousPage,
        Boolean hasNextPage,
        @CheckForNull Integer previousCursor,
        @CheckForNull Integer nextCursor,
        @CheckForNull Integer pageSize,
        @CheckForNull String searchQuery);

    public static native MailTemplateInstance commentNotificationMail(
        int postId, LazychatMessage comment);
  }

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
  @Transactional
  public TemplateInstance getIndex(
      @QueryParam("i") @CheckForNull Integer cursor,
      @QueryParam("n") @CheckForNull Integer maxResults,
      @QueryParam("search-query") @CheckForNull String searchQuery) {

    maxResults = maxResults == null ? defaultMaxResults : maxResults;

    @CheckForNull var reader = getCurrentUser();
    var session = entityManager.unwrap(Session.class);
    var q = Post.findViewable(postFilter, session, reader, null, cursor, maxResults, searchQuery);

    q.cacheDescriptions();

    var feedUri = uri.getPath() + "/feed";
    if (reader != null) {
      var pageKey = ensurePageKey(reader, feedUri);
      feedUri += "?page-key=" + pageKey.key.toString(36);
    }

    return Templates.postList(
        q.days(),
        feedUri,
        pageTitle,
        showBookmarkForm(),
        showLazychatForm(),
        false,
        q.prevCursor != null,
        q.nextCursor != null,
        q.prevCursor,
        q.nextCursor,
        maxResults,
        searchQuery);
  }

  @GET
  @Path("~{ownerName}")
  @Produces(TEXT_HTML)
  @Transactional
  public TemplateInstance getUserIndex(
      @PathParam("ownerName") String ownerName,
      @QueryParam("i") @CheckForNull Integer cursor,
      @QueryParam("n") @CheckForNull Integer maxResults) {

    maxResults = maxResults == null ? defaultMaxResults : maxResults;

    @CheckForNull var reader = getCurrentUser();
    var owner = User.findByNickname(ownerName);
    var session = entityManager.unwrap(Session.class);
    var q = Post.findViewable(postFilter, session, reader, owner, cursor, maxResults, null);

    q.cacheDescriptions();

    var feedUri = uri.getPath() + "/feed";
    if (reader != null) {
      var pageKey = ensurePageKey(reader, feedUri);
      feedUri += "?page-key=" + pageKey.key.toString(36);
    }

    return Templates.postList(
        q.days(),
        feedUri,
        pageTitle,
        showBookmarkForm(),
        showLazychatForm(),
        false,
        q.prevCursor != null,
        q.nextCursor != null,
        q.prevCursor,
        q.nextCursor,
        maxResults,
        null);
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
  @Produces(APPLICATION_JSON)
  @Path("{id}")
  public Post<?> getPostJson(@PathParam("id") int id) {
    return getPostIfVisible(id);
  }

  @GET
  @Produces(TEXT_HTML)
  @Path("{id}")
  public TemplateInstance getPostHtml(@PathParam("id") int id) {
    var post = getPostIfVisible(id);

    return Templates.postList(
        new PostPage<Post<? extends PostText<?>>>(null, null, null, List.of(post)).days(),
        null,
        String.format("Post #%d", id),
        false,
        false,
        true,
        false,
        false,
        null,
        null,
        null,
        null);
  }

  @GET
  @Path("feed")
  @Produces(APPLICATION_ATOM_XML)
  @Transactional
  public String getFeed(@QueryParam("page-key") @CheckForNull String pageKeyBase36)
      throws FeedException {
    @CheckForNull var pageKey = pageKeyBase36 == null ? null : new BigInteger(pageKeyBase36, 36);
    return makeFeed(pageKey, null, null);
  }

  @GET
  @Path("~{ownerName}/feed")
  @Produces(APPLICATION_ATOM_XML)
  @Transactional
  public String getUserFeed(
      @QueryParam("page-key") @CheckForNull String pageKeyBase36,
      @PathParam("ownerName") String ownerName)
      throws FeedException {
    var owner = User.findByNickname(ownerName);
    @CheckForNull var pageKey = pageKeyBase36 == null ? null : new BigInteger(pageKeyBase36, 36);
    return makeFeed(pageKey, ownerName, owner);
  }

  @POST
  @Produces(TEXT_PLAIN)
  @Path("{id}/comments")
  @Transactional
  public Uni<Response> postComment(
      @PathParam("id") int postId,
      @FormParam("message") @NotEmpty String message,
      @FormParam("hashcash-salt") long hashcashSalt)
      throws NoSuchAlgorithmException, ExecutionException, InterruptedException, TimeoutException {
    var hashcashDigest = MessageDigest.getInstance(hashcashDigestAlgorithm);
    hashcashDigest.update("Hashcash-Salt: ".getBytes(UTF_8));
    hashcashDigest.update(String.valueOf(hashcashSalt).getBytes(UTF_8));
    hashcashDigest.update("\n\n".getBytes(UTF_8));

    for (byte b : message.getBytes(UTF_8)) {
      if (b == '\r') {
        // Skip CR characters.  The JavaScript side does not include them in its computation.
        continue;
      }
      hashcashDigest.update(b);
    }
    var hashcash = hashcashDigest.digest();

    if (hashcash[0] != 0 || hashcash[1] != 0) {
      throw new BadRequestException(
          "invalid hashcash",
          Response.status(Status.BAD_REQUEST).entity("invalid hashcash").build());
    }

    Post<?> post = Post.findById(postId);

    var comment = new LazychatMessage();
    comment.date = OffsetDateTime.now();
    comment.scope = Scope.comment;
    comment.referees = List.of(post);

    comment.persist();

    comment.setContent(message);
    assignPostTargets(post.getVisibility(), post.owner, comment);

    var currentUser = getCurrentUser();
    if (currentUser != null) {
      comment.owner = currentUser;
    }

    var admins = User.findAdmins();

    var mailText = Templates.commentNotificationMail(postId, comment);
    var sendJob =
        mailText
            .subject(String.format("MulkCMS comment #%d", comment.id))
            .to(mailSenderAddress)
            .bcc(admins.stream().map(x -> x.email).toArray(String[]::new))
            .send();
    return sendJob
        .onItem()
        .transform(
            unused -> Response.seeOther(UriBuilder.fromUri("/posts/{id}").build(postId)).build());
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
    var q = Post.findViewable(postFilter, entityManager.unwrap(Session.class), reader, owner);
    q.cacheDescriptions();
    var posts = q.posts;

    var feed = new Feed("atom_1.0");

    var feedSubId = owner == null ? "" : String.format("/%d", owner.id);

    feed.setTitle(String.format("Benki → %s", pageTitle));
    feed.setId(
        String.format(
            "tag:%s,2019:%s:%s:%s",
            URLEncoder.encode(tagBase, UTF_8),
            URLEncoder.encode(pageTitle, UTF_8),
            URLEncoder.encode(feedSubId, UTF_8),
            URLEncoder.encode(
                identity.isAnonymous() ? "world" : identity.getPrincipal().getName(), UTF_8)));
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
    var htmlAltPath =
        ownerName == null
            ? "/posts"
            : String.format("~%s/posts", URLEncoder.encode(ownerName, UTF_8));
    htmlAltLink.setHref(uri.resolve(URI.create(htmlAltPath)).toString());
    htmlAltLink.setRel("alternate");
    htmlAltLink.setType("text/html");
    feed.setAlternateLinks(List.of(htmlAltLink));

    feed.setEntries(
        posts.stream()
            .filter(Post::isTopLevel)
            .map(
                post -> {
                  var entry = new Entry();

                  entry.setId(
                      String.format(
                          "tag:%s,2012:/marx/%d", URLEncoder.encode(tagBase, UTF_8), post.id));
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
                  } else if (post.getDescriptionHtml() != null) {
                    var title = new Content();
                    title.setType("text");
                    var words =
                        Jsoup.parse(post.getDescriptionHtml()).text().split("\\s", AUTOTITLE_WORDS);
                    var titleWords = Arrays.asList(words).subList(0, words.length - 1);
                    title.setValue(String.join(" ", titleWords) + " ...");
                    entry.setTitleEx(title);
                  }

                  if (post.getDescriptionHtml() != null) {
                    var description = new Content();
                    description.setType("html");
                    description.setValue(post.getDescriptionHtml());
                    if (post.getUri() != null) {
                      entry.setSummary(description);
                    } else {
                      entry.setContents(List.of(description));
                    }
                  }

                  var alternateLinks = new ArrayList<Link>();
                  if (post.getUri() != null) {
                    var postUriLink = new Link();
                    postUriLink.setRel("alternate");
                    postUriLink.setHref(post.getUri());
                    alternateLinks.add(postUriLink);
                  } else {
                    var postSelfHref =
                        uri.resolve(URI.create(String.format("/posts/%d", post.id))).toString();
                    var postSelfLink = new Link();
                    postSelfLink.setRel("alternate");
                    postSelfLink.setHref(postSelfHref);
                    alternateLinks.add(postSelfLink);
                  }
                  entry.setAlternateLinks(alternateLinks);

                  return entry;
                })
            .collect(Collectors.toUnmodifiableList()));

    var wireFeedOutput = new WireFeedOutput();
    return wireFeedOutput.outputString(feed);
  }

  private boolean showBookmarkForm() {
    switch (postFilter) {
      case ALL:
      case BOOKMARKS_ONLY:
        return identity.hasRole(LoginRoles.EDITOR);
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
        return identity.hasRole(LoginRoles.EDITOR);
      case BOOKMARKS_ONLY:
        return false;
      default:
        throw new IllegalStateException();
    }
  }

  protected final Session getSession() {
    return entityManager.unwrap(Session.class);
  }

  protected static void assignPostTargets(Post.Visibility visibility, User user, Post<?> post) {
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

  protected final Post<?> getPostIfVisible(int id) {
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
}
