package eu.mulk.mulkcms2.benki;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wiki_page_revisions", schema = "public", catalog = "benki")
public class WikiPageRevision {

  private int id;
  private Object date;
  private String title;
  private String content;
  private String format;
  private WikiPage page;
  private User author;

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
  @Column(name = "title", nullable = true, length = -1)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "content", nullable = true, length = -1)
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Basic
  @Column(name = "format", nullable = true, length = -1)
  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WikiPageRevision that = (WikiPageRevision) o;

    if (id != that.id) {
      return false;
    }
    if (date != null ? !date.equals(that.date) : that.date != null) {
      return false;
    }
    if (title != null ? !title.equals(that.title) : that.title != null) {
      return false;
    }
    if (content != null ? !content.equals(that.content) : that.content != null) {
      return false;
    }
    if (format != null ? !format.equals(that.format) : that.format != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (content != null ? content.hashCode() : 0);
    result = 31 * result + (format != null ? format.hashCode() : 0);
    return result;
  }

  @ManyToOne
  @JoinColumn(name = "page", referencedColumnName = "id", nullable = false)
  public WikiPage getPage() {
    return page;
  }

  public void setPage(WikiPage page) {
    this.page = page;
  }

  @ManyToOne
  @JoinColumn(name = "author", referencedColumnName = "id")
  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }
}
