package eu.mulk.mulkcms2.benki.bookmarks;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

public class BookmarkTextPK implements Serializable {

  @Id
  @Column(name = "language", nullable = false, length = -1)
  private String language;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bookmark", referencedColumnName = "id", nullable = false)
  private Bookmark bookmark;

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Bookmark getBookmark() {
    return bookmark;
  }

  public void setBookmark(Bookmark bookmark) {
    this.bookmark = bookmark;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BookmarkTextPK)) {
      return false;
    }
    BookmarkTextPK that = (BookmarkTextPK) o;
    return Objects.equals(getBookmark(), that.getBookmark())
        && getLanguage().equals(that.getLanguage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getBookmark(), getLanguage());
  }
}
