package eu.mulk.mulkcms2.benki.bookmarks;

import eu.mulk.mulkcms2.benki.posts.Post;
import eu.mulk.mulkcms2.benki.users.User;
import eu.mulk.mulkcms2.common.markdown.MarkdownConverter;
import io.quarkus.security.identity.SecurityIdentity;
import java.util.List;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Session;

@Entity
@Table(name = "bookmarks", schema = "benki")
public class Bookmark extends Post {

  @Column(name = "uri", nullable = false, length = -1)
  public String uri;

  @Column(name = "title", nullable = true, length = -1)
  public String title;

  @Column(name = "description", nullable = true, length = -1)
  public String description;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "bookmark_tags",
      schema = "benki",
      joinColumns = @JoinColumn(name = "bookmark"))
  @Column(name = "tag")
  public Set<String> tags;

  @Transient
  public String getDescriptionHtml() {
    return new MarkdownConverter().htmlify(description);
  }

  public static List<Bookmark> findViewable(
      Session session, SecurityIdentity viewer, @CheckForNull User owner) {
    return findViewable(Bookmark.class, session, viewer, owner, null, null).posts;
  }

  public static PostPage<Bookmark> findViewable(
      Session session,
      SecurityIdentity viewer,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      @CheckForNull Integer count) {
    return findViewable(Bookmark.class, session, viewer, owner, cursor, count);
  }

  @Override
  public boolean isBookmark() {
    return true;
  }

  @Override
  public boolean isLazychatMessage() {
    return false;
  }
}
