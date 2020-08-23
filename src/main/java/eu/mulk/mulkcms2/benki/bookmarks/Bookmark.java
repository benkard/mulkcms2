package eu.mulk.mulkcms2.benki.bookmarks;

import eu.mulk.mulkcms2.benki.posts.Post;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

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
      text.language = "";
      texts.put(text.language, text);
    }

    text.description = x;
    text.cachedDescriptionHtml = null;
  }
}
