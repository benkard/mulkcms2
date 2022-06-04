package eu.mulk.mulkcms2.benki.posts;

import static java.util.stream.Collectors.toList;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.bookmarks.Bookmark;
import eu.mulk.mulkcms2.benki.lazychat.LazychatMessage;
import eu.mulk.mulkcms2.benki.newsletter.Newsletter;
import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "posts", schema = "benki")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@TypeDef(name = "pg_enum", typeClass = PostgreSQLEnumType.class)
public abstract class Post<Text extends PostText<?>> extends PanacheEntityBase {

  public enum Scope {
    top_level,
    comment
  }

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

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Type(type = "pg_enum")
  public Scope scope = Scope.top_level;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "newsletter", referencedColumnName = "id", nullable = true)
  @CheckForNull
  @JsonbTransient
  public Newsletter newsletter;

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

  @ManyToMany(mappedBy = "referees")
  @JsonbTransient
  public Collection<LazychatMessage> referrers;

  @ManyToMany(mappedBy = "referees")
  @OrderBy("date DESC")
  @Where(clause = "scope = 'comment'")
  @JsonbTransient
  public Collection<LazychatMessage> comments;

  @OneToMany(
      mappedBy = "post",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      targetEntity = PostText.class)
  @MapKey(name = "language")
  public Map<String, Text> texts = new HashMap<>();

  public Map<String, Text> getTexts() {
    return texts;
  }

  public abstract boolean isBookmark();

  public abstract boolean isLazychatMessage();

  @CheckForNull
  public abstract String getTitle();

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

  protected static <T extends Post> CriteriaBuilder<T> queryViewable(
      Class<T> entityClass,
      @CheckForNull User reader,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      EntityManager em,
      CriteriaBuilderFactory cbf,
      boolean forward,
      @CheckForNull String searchQuery) {

    CriteriaBuilder<T> cb = cbf.create(em, entityClass).select("post");

    if (reader == null) {
      cb =
          cb.from(entityClass, "post")
              .innerJoin("post.targets", "role")
              .where("'world'")
              .isMemberOf("role.tags");
    } else {
      cb = cb.from(User.class, "user").where("user").eq(reader);
      if (entityClass.isAssignableFrom(Post.class)) {
        cb = cb.innerJoin("user.visiblePosts", "post");
      } else if (entityClass.isAssignableFrom(Bookmark.class)) {
        cb = cb.innerJoin("user.visibleBookmarks", "post");
      } else if (entityClass.isAssignableFrom(LazychatMessage.class)) {
        cb = cb.innerJoin("user.visibleLazychatMessages", "post");
      } else {
        throw new IllegalArgumentException();
      }
    }

    cb = cb.fetch("post.owner");

    if (owner != null) {
      cb = cb.where("post.owner").eq(owner);
    }

    if (forward) {
      cb = cb.orderByDesc("post.id");
    } else {
      cb = cb.orderByAsc("post.id");
    }

    if (cursor != null) {
      if (forward) {
        cb = cb.where("post.id").le(cursor);
      } else {
        cb = cb.where("post.id").gt(cursor);
      }
    }

    if (searchQuery != null && !searchQuery.isBlank()) {
      cb =
          cb.whereExists()
              .from(PostText.class, "postText")
              .where("postText.post")
              .eqExpression("post")
              .whereOr()
              .whereExpression(
                  "post_matches_websearch(postText.searchTerms, 'de', :searchQueryText) = true")
              .whereExpression(
                  "post_matches_websearch(postText.searchTerms, 'en', :searchQueryText) = true")
              .endOr()
              .end()
              .setParameter("searchQueryText", searchQuery);
    }

    cb = cb.where("post.scope").eq(Scope.top_level);

    cb = cb.leftJoinFetch("post.comments", "comment");
    cb = cb.fetch("comment.texts");

    return cb;
  }

  public final boolean isVisibleTo(@Nullable User user) {
    // FIXME: Make this more efficient.
    return getVisibility() == Visibility.PUBLIC || (user != null && visibleTo.contains(user));
  }

  @CheckForNull
  public final String getDescriptionHtml() {
    var text = getText();
    if (text == null) {
      return null;
    }
    return text.getDescriptionHtml();
  }

  public final boolean isTopLevel() {
    return scope == Scope.top_level;
  }

  public static class Day<T extends Post<? extends PostText<?>>> {
    public final @CheckForNull LocalDate date;
    public final List<T> posts;

    private Day(LocalDate date, List<T> posts) {
      this.date = date;
      this.posts = posts;
    }

    public void cacheDescriptions() {
      for (var post : posts) {
        post.getTexts().values().forEach(PostText::getDescriptionHtml);
      }
    }
  }

  public static class PostPage<T extends Post<? extends PostText<?>>> {
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

    public void cacheDescriptions() {
      days().forEach(Day::cacheDescriptions);
    }

    public List<Day<T>> days() {
      return posts.stream()
          .collect(Collectors.groupingBy(post -> post.date.toLocalDate()))
          .entrySet()
          .stream()
          .map(x -> new Day<T>(x.getKey(), x.getValue()))
          .sorted(Comparator.comparing((Day<T> day) -> day.date).reversed())
          .collect(Collectors.toUnmodifiableList());
    }
  }

  public static PostPage<Post<? extends PostText<?>>> findViewable(
      PostFilter postFilter,
      EntityManager em,
      CriteriaBuilderFactory cbf,
      @CheckForNull User viewer,
      @CheckForNull User owner) {
    return findViewable(postFilter, em, cbf, viewer, owner, null, null, null);
  }

  public static PostPage<Post<? extends PostText<?>>> findViewable(
      PostFilter postFilter,
      EntityManager em,
      CriteriaBuilderFactory cbf,
      @CheckForNull User viewer,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      @CheckForNull Integer count,
      @CheckForNull String searchQuery) {
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
    return findViewable(entityClass, em, cbf, viewer, owner, cursor, count, searchQuery);
  }

  protected static <T extends Post<? extends PostText<?>>> PostPage<T> findViewable(
      Class<? extends T> entityClass,
      EntityManager em,
      CriteriaBuilderFactory cbf,
      @CheckForNull User viewer,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      @CheckForNull Integer count,
      @CheckForNull String searchQuery) {

    if (cursor != null) {
      Objects.requireNonNull(count);
    }

    var forwardCriteria =
        queryViewable(entityClass, viewer, owner, cursor, em, cbf, true, searchQuery);
    var forwardQuery = forwardCriteria.getQuery();

    if (count != null) {
      forwardQuery.setMaxResults(count + 1);
    }

    @CheckForNull Integer prevCursor = null;
    @CheckForNull Integer nextCursor = null;

    if (cursor != null) {
      // Look backwards as well so we can find the prevCursor.
      var backwardCriteria =
          queryViewable(entityClass, viewer, owner, cursor, em, cbf, false, searchQuery);
      var backwardQuery = backwardCriteria.getQuery();
      backwardQuery.setMaxResults(count);
      var backwardResults = backwardQuery.getResultList();
      if (!backwardResults.isEmpty()) {
        prevCursor = backwardResults.get(backwardResults.size() - 1).id;
      }
    }

    var forwardResults = new ArrayList<T>(forwardQuery.getResultList());
    if (count != null) {
      if (forwardResults.size() == count + 1) {
        nextCursor = forwardResults.get(count).id;
        forwardResults.remove((int) count);
      }
    }

    // Fetch texts (to avoid n+1 selects).
    fetchTexts(forwardResults);

    return new PostPage<>(prevCursor, cursor, nextCursor, forwardResults);
  }

  public static <T extends Post<?>> void fetchTexts(Collection<T> posts) {
    var postIds = posts.stream().map(x -> x.id).collect(toList());

    if (!postIds.isEmpty()) {
      find("SELECT p FROM Post p LEFT JOIN FETCH p.texts WHERE p.id IN (?1)", postIds).stream()
          .count();
    }
  }

  @CheckForNull
  public Text getText() {
    var texts = getTexts();
    if (texts.isEmpty()) {
      return null;
    } else if (texts.containsKey("")) {
      return texts.get("");
    } else if (texts.containsKey("en")) {
      return texts.get("en");
    } else {
      return texts.values().stream().findAny().get();
    }
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
    Post<?> post = (Post<?>) o;
    return Objects.equals(id, post.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
