package eu.mulk.mulkcms2.benki;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

public class BookmarkTagPK implements Serializable {

  private int bookmarkId;
  private String tag;

  @Column(name = "bookmark", nullable = false)
  @Id
  public int getBookmarkId() {
    return bookmarkId;
  }

  public void setBookmarkId(int bookmarkId) {
    this.bookmarkId = bookmarkId;
  }

  @Column(name = "tag", nullable = false, length = -1)
  @Id
  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BookmarkTagPK that = (BookmarkTagPK) o;

    if (bookmarkId != that.bookmarkId) {
      return false;
    }
    if (tag != null ? !tag.equals(that.tag) : that.tag != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = bookmarkId;
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    return result;
  }
}
