package eu.mulk.mulkcms2.benki.bookmarks;

import eu.mulk.mulkcms2.benki.posts.Post;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Set;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "bookmarks", schema = "benki")
public class Bookmark extends Post<BookmarkText> {

  @Column(name = "uri", nullable = false, length = -1)
  public String uri;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "bookmark_tags",
      schema = "benki",
      joinColumns = @JoinColumn(name = "bookmark"))
  @Column(name = "tag")
  public Set<String> tags;

  @CheckForNull
  private String getDescription() {
    var text = getText();
    return text == null ? null : text.description;
  }

  @CheckForNull
  @Override
  public String getUri() {
    return uri;
  }

  @CheckForNull
  @Override
  public String getTitle() {
    var text = getText();
    return text == null ? null : text.title;
  }

  @Override
  public boolean isBookmark() {
    return true;
  }

  @Override
  public boolean isLazychatMessage() {
    return false;
  }

  public void setTitle(String x) {
    var text = getText();
    if (text == null) {
      text = new BookmarkText();
      text.post = this;
      text.postId = id;
      text.language = "";
      texts.put(text.language, text);
    }

    text.title = x;
  }

  public void setDescription(String x) {
    var text = getText();
    if (text == null) {
      text = new BookmarkText();
      text.post = this;
      text.postId = id;
      text.language = "";
      texts.put(text.language, text);
    }

    text.description = x;
    text.cachedDescriptionHtml = null;
  }
}
