package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bookmark_tags", schema = "public", catalog = "benki")
@IdClass(BookmarkTagPK.class)
public class BookmarkTag {

  private int bookmarkId;
  private String tag;
  private Bookmark bookmark;

  @Id
  @Column(name = "bookmark", nullable = false)
  public int getBookmarkId() {
    return bookmarkId;
  }

  public void setBookmarkId(int bookmarkId) {
    this.bookmarkId = bookmarkId;
  }

  @Id
  @Column(name = "tag", nullable = false, length = -1)
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

    BookmarkTag that = (BookmarkTag) o;

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

  @ManyToOne
  @JoinColumn(name = "bookmark", referencedColumnName = "id", nullable = false)
  public Bookmark getBookmark() {
    return bookmark;
  }

  public void setBookmark(Bookmark bookmark) {
    this.bookmark = bookmark;
  }
}
