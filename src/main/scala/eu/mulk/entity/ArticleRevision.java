package eu.mulk.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "article_revisions", schema = "public", catalog = "mulkcms")
public class ArticleRevision extends PanacheEntityBase {

  private int id;
  private Timestamp date;
  private String title;
  private String content;
  private String format;
  private String status;
  private String globalId;
  private Collection<ArticleRevisionCharacteristic> characteristics;
  private Collection<ArticleRevisionParenthood> children;
  private Collection<ArticleRevisionParenthood> parents;
  private Article article;
  private User authors;

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
  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  @Basic
  @Column(name = "title", nullable = false, length = -1)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "content", nullable = false, length = -1)
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Basic
  @Column(name = "format", nullable = false, length = -1)
  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  @Basic
  @Column(name = "status", nullable = false, length = -1)
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "global_id", nullable = true, length = -1)
  public String getGlobalId() {
    return globalId;
  }

  public void setGlobalId(String globalId) {
    this.globalId = globalId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleRevision that = (ArticleRevision) o;
    return id == that.id &&
        Objects.equals(date, that.date) &&
        Objects.equals(title, that.title) &&
        Objects.equals(content, that.content) &&
        Objects.equals(format, that.format) &&
        Objects.equals(status, that.status) &&
        Objects.equals(globalId, that.globalId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, date, title, content, format, status, globalId);
  }

  @OneToMany(mappedBy = "articleRevision")
  public Collection<ArticleRevisionCharacteristic> getCharacteristics() {
    return characteristics;
  }

  public void setCharacteristics(
      Collection<ArticleRevisionCharacteristic> characteristics) {
    this.characteristics = characteristics;
  }

  @OneToMany(mappedBy = "parent")
  public Collection<ArticleRevisionParenthood> getChildren() {
    return children;
  }

  public void setChildren(Collection<ArticleRevisionParenthood> children) {
    this.children = children;
  }

  @OneToMany(mappedBy = "child")
  public Collection<ArticleRevisionParenthood> getParents() {
    return parents;
  }

  public void setParents(Collection<ArticleRevisionParenthood> parents) {
    this.parents = parents;
  }

  @ManyToOne
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  @ManyToOne
  @JoinColumn(name = "author", referencedColumnName = "id")
  public User getAuthors() {
    return authors;
  }

  public void setAuthors(User authors) {
    this.authors = authors;
  }
}
