package eu.mulk.mulkcms2.benki;

import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "bookmarks", schema = "public", catalog = "benki")
public class Bookmark {

  private int id;
  private Object date;
  private String uri;
  private String title;
  private String description;
  private Collection<BookmarkTag> tags;
  private User owner;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "date", nullable = true)
  public Object getDate() {
    return date;
  }

  public void setDate(Object date) {
    this.date = date;
  }

  @Basic
  @Column(name = "uri", nullable = false, length = -1)
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  @Basic
  @Column(name = "title", nullable = true, length = -1)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "description", nullable = true, length = -1)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Bookmark bookmark = (Bookmark) o;

    if (id != bookmark.id) {
      return false;
    }
    if (date != null ? !date.equals(bookmark.date) : bookmark.date != null) {
      return false;
    }
    if (uri != null ? !uri.equals(bookmark.uri) : bookmark.uri != null) {
      return false;
    }
    if (title != null ? !title.equals(bookmark.title) : bookmark.title != null) {
      return false;
    }
    if (description != null ? !description.equals(bookmark.description)
        : bookmark.description != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + (uri != null ? uri.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }

  @OneToMany(mappedBy = "bookmark")
  public Collection<BookmarkTag> getTags() {
    return tags;
  }

  public void setTags(Collection<BookmarkTag> tags) {
    this.tags = tags;
  }

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id")
  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }
}
