package eu.mulk.mulkcms2.benki.generic;

import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.bookmarks.Bookmark;
import eu.mulk.mulkcms2.benki.lazychat.LazychatMessage;
import eu.mulk.mulkcms2.benki.users.User;
import eu.mulk.mulkcms2.benki.users.User_;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.identity.SecurityIdentity;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;
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

  private static Logger log = Logger.getLogger(Post.class);

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
  public OffsetDateTime date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner", referencedColumnName = "id")
  public User owner;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_visible_posts",
      schema = "benki",
      joinColumns = @JoinColumn(name = "message"),
      inverseJoinColumns = @JoinColumn(name = "user"))
  public Set<User> visibleTo;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "post_targets",
      schema = "benki",
      joinColumns = @JoinColumn(name = "message"),
      inverseJoinColumns = @JoinColumn(name = "target"))
  public Set<Role> targets;

  protected static <T extends Post> CriteriaQuery<T> queryViewable(
      Class<T> entityClass,
      SecurityIdentity readerIdentity,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      CriteriaBuilder cb,
      boolean forward) {
    CriteriaQuery<T> query = cb.createQuery(entityClass);

    var conditions = new ArrayList<Predicate>();

    From<?, T> post;
    if (readerIdentity.isAnonymous()) {
      post = query.from(entityClass);
      var target = post.join(Post_.targets);
      conditions.add(cb.equal(target, Role.getWorld()));
    } else {
      var userName = readerIdentity.getPrincipal().getName();
      var user = User.findByNickname(userName);

      var root = query.from(User.class);
      conditions.add(cb.equal(root, user));
      if (entityClass.isAssignableFrom(Bookmark.class)) {
        post = (From<?, T>) root.join(User_.visibleBookmarks);
      } else {
        assert entityClass.isAssignableFrom(LazychatMessage.class) : entityClass;
        post = (From<?, T>) root.join(User_.visibleLazychatMessages);
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

  public static class PostPage<T extends Post> {
    public @CheckForNull Integer prevCursor;
    public @CheckForNull Integer cursor;
    public @CheckForNull Integer nextCursor;
    public List<T> posts;

    private PostPage(
        @CheckForNull Integer c0,
        @CheckForNull Integer c1,
        @CheckForNull Integer c2,
        List<T> resultList) {
      this.prevCursor = c0;
      this.cursor = c1;
      this.nextCursor = c2;
      this.posts = resultList;
    }
  }

  protected static <T extends Post> List<T> findViewable(
      Class<T> entityClass, Session session, SecurityIdentity viewer, @CheckForNull User owner) {
    return findViewable(entityClass, session, viewer, owner, null, null).posts;
  }

  protected static <T extends Post> PostPage<T> findViewable(
      Class<T> entityClass,
      Session session,
      SecurityIdentity viewer,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      @CheckForNull Integer count) {

    if (cursor != null) {
      Objects.requireNonNull(count);
    }

    var cb = session.getCriteriaBuilder();

    var forwardCriteria = Bookmark.queryViewable(entityClass, viewer, owner, cursor, cb, true);
    var forwardQuery = session.createQuery(forwardCriteria);

    if (count != null) {
      forwardQuery.setMaxResults(count + 1);
    }

    log.debug(forwardQuery.unwrap(org.hibernate.query.Query.class).getQueryString());

    @CheckForNull Integer prevCursor = null;
    @CheckForNull Integer nextCursor = null;

    if (cursor != null) {
      // Look backwards as well so we can find the prevCursor.
      var backwardCriteria = Bookmark.queryViewable(entityClass, viewer, owner, cursor, cb, false);
      var backwardQuery = session.createQuery(backwardCriteria);
      backwardQuery.setMaxResults(count);
      var backwardResults = backwardQuery.getResultList();
      if (!backwardResults.isEmpty()) {
        prevCursor = backwardResults.get(backwardResults.size() - 1).id;
      }
    }

    var forwardResults = forwardQuery.getResultList();
    if (count != null) {
      if (forwardResults.size() == count + 1) {
        nextCursor = forwardResults.get(count).id;
        forwardResults.remove((int) count);
      }
    }

    return new PostPage(prevCursor, cursor, nextCursor, forwardResults);
  }
}
