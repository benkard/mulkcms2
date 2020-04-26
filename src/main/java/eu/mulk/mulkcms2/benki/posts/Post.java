package eu.mulk.mulkcms2.benki.posts;

import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.bookmarks.Bookmark;
import eu.mulk.mulkcms2.benki.lazychat.LazychatMessage;
import eu.mulk.mulkcms2.benki.users.User;
import eu.mulk.mulkcms2.benki.users.User_;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.hibernate.Session;
import org.jboss.logging.Logger;

@Entity
@Table(name = "posts", schema = "benki")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Post extends PanacheEntityBase {

  private static final Logger log = Logger.getLogger(Post.class);

  @Id
  @SequenceGenerator(
      allocationSize = 1,
      sequenceName = "posts_id_seq",
      name = "posts_id_seq",
      schema = "benki")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_id_seq")
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "date", nullable = true)
  @CheckForNull
  public OffsetDateTime date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner", referencedColumnName = "id")
  @CheckForNull
  @JsonbTransient
  public User owner;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_visible_posts",
      schema = "benki",
      joinColumns = @JoinColumn(name = "message"),
      inverseJoinColumns = @JoinColumn(name = "user"))
  @JsonbTransient
  public Set<User> visibleTo;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "post_targets",
      schema = "benki",
      joinColumns = @JoinColumn(name = "message"),
      inverseJoinColumns = @JoinColumn(name = "target"))
  @JsonbTransient
  public Set<Role> targets;

  public abstract boolean isBookmark();

  public abstract boolean isLazychatMessage();

  @CheckForNull
  public abstract String getTitle();

  @CheckForNull
  public abstract String getDescriptionHtml();

  @CheckForNull
  public abstract String getUri();

  public Visibility getVisibility() {
    if (targets.isEmpty()) {
      return Visibility.PRIVATE;
    } else if (targets.contains(Role.getWorld())) {
      return Visibility.PUBLIC;
    } else {
      // FIXME: There should really be a check whether targets.equals(owner.defaultTargets) here.
      // Otherwise the actual visibility is DISCRETIONARY.
      return Visibility.SEMIPRIVATE;
    }
  }

  protected static <T extends Post> CriteriaQuery<T> queryViewable(
      Class<T> entityClass,
      @CheckForNull User reader,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      CriteriaBuilder cb,
      boolean forward) {
    CriteriaQuery<T> query = cb.createQuery(entityClass);

    var conditions = new ArrayList<Predicate>();

    From<?, T> post;
    if (reader == null) {
      post = query.from(entityClass);
      var target = post.join(Post_.targets);
      conditions.add(cb.equal(target, Role.getWorld()));
    } else {
      var root = query.from(User.class);
      conditions.add(cb.equal(root, reader));
      if (entityClass.isAssignableFrom(Post.class)) {
        post = (From<?, T>) root.join(User_.visiblePosts);
      } else if (entityClass.isAssignableFrom(Bookmark.class)) {
        post = (From<?, T>) root.join(User_.visibleBookmarks);
      } else if (entityClass.isAssignableFrom(LazychatMessage.class)) {
        post = (From<?, T>) root.join(User_.visibleLazychatMessages);
      } else {
        throw new IllegalArgumentException();
      }
    }

    query.select(post);
    post.fetch(Post_.owner, JoinType.LEFT);

    if (owner != null) {
      conditions.add(cb.equal(post.get(Post_.owner), owner));
    }

    if (forward) {
      query.orderBy(cb.desc(post.get(Post_.id)));
    } else {
      query.orderBy(cb.asc(post.get(Post_.id)));
    }

    if (cursor != null) {
      if (forward) {
        conditions.add(cb.le(post.get(Post_.id), cursor));
      } else {
        conditions.add(cb.gt(post.get(Post_.id), cursor));
      }
    }

    query.where(conditions.toArray(new Predicate[0]));

    return query;
  }

  public final boolean isVisibleTo(@Nullable User user) {
    // FIXME: Make this more efficient.
    return getVisibility() == Visibility.PUBLIC || (user != null && visibleTo.contains(user));
  }

  public static class PostPage<T extends Post> {
    public @CheckForNull final Integer prevCursor;
    public @CheckForNull final Integer cursor;
    public @CheckForNull final Integer nextCursor;
    public final List<T> posts;

    private static final TimeZone timeZone = TimeZone.getDefault();

    public PostPage(
        @CheckForNull Integer c0,
        @CheckForNull Integer c1,
        @CheckForNull Integer c2,
        List<T> resultList) {
      this.prevCursor = c0;
      this.cursor = c1;
      this.nextCursor = c2;
      this.posts = resultList;
    }

    public class Day {
      public final @CheckForNull LocalDate date;
      public final List<T> posts;

      private Day(LocalDate date, List<T> posts) {
        this.date = date;
        this.posts = posts;
      }
    }

    public List<Day> days() {
      return posts.stream()
          .collect(Collectors.groupingBy(post -> post.date.toLocalDate()))
          .entrySet()
          .stream()
          .map(x -> new Day(x.getKey(), x.getValue()))
          .sorted(Comparator.comparing((Day day) -> day.date).reversed())
          .collect(Collectors.toUnmodifiableList());
    }
  }

  public static List<Post> findViewable(
      PostFilter postFilter, Session session, @CheckForNull User viewer, @CheckForNull User owner) {
    return findViewable(postFilter, session, viewer, owner, null, null).posts;
  }

  public static PostPage<Post> findViewable(
      PostFilter postFilter,
      Session session,
      @CheckForNull User viewer,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      @CheckForNull Integer count) {
    Class<? extends Post> entityClass;
    switch (postFilter) {
      case BOOKMARKS_ONLY:
        entityClass = Bookmark.class;
        break;
      case LAZYCHAT_MESSAGES_ONLY:
        entityClass = LazychatMessage.class;
        break;
      default:
        entityClass = Post.class;
    }
    return findViewable(entityClass, session, viewer, owner, cursor, count);
  }

  protected static <T extends Post> PostPage<T> findViewable(
      Class<? extends T> entityClass,
      Session session,
      @CheckForNull User viewer,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      @CheckForNull Integer count) {

    if (cursor != null) {
      Objects.requireNonNull(count);
    }

    var cb = session.getCriteriaBuilder();

    var forwardCriteria = queryViewable(entityClass, viewer, owner, cursor, cb, true);
    var forwardQuery = session.createQuery(forwardCriteria);

    if (count != null) {
      forwardQuery.setMaxResults(count + 1);
    }

    log.debug(forwardQuery.unwrap(org.hibernate.query.Query.class).getQueryString());

    @CheckForNull Integer prevCursor = null;
    @CheckForNull Integer nextCursor = null;

    if (cursor != null) {
      // Look backwards as well so we can find the prevCursor.
      var backwardCriteria = queryViewable(entityClass, viewer, owner, cursor, cb, false);
      var backwardQuery = session.createQuery(backwardCriteria);
      backwardQuery.setMaxResults(count);
      var backwardResults = backwardQuery.getResultList();
      if (!backwardResults.isEmpty()) {
        prevCursor = backwardResults.get(backwardResults.size() - 1).id;
      }
    }

    var forwardResults = (List<T>) forwardQuery.getResultList();
    if (count != null) {
      if (forwardResults.size() == count + 1) {
        nextCursor = forwardResults.get(count).id;
        forwardResults.remove((int) count);
      }
    }

    return new PostPage<>(prevCursor, cursor, nextCursor, forwardResults);
  }

  public enum Visibility {
    PUBLIC,
    SEMIPRIVATE,
    DISCRETIONARY,
    PRIVATE,
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Post)) {
      return false;
    }
    Post post = (Post) o;
    return Objects.equals(id, post.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
