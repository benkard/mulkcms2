package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
  private Set<ArticleRevision> children;
  private Set<ArticleRevision> parents;
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

  @ManyToMany
  @JoinTable(name = "article_revision_parenthood",
      joinColumns = @JoinColumn(name = "parent"),
      inverseJoinColumns = @JoinColumn(name = "child")
  )
  public Set<ArticleRevision> getChildren() {
    return children;
  }

  public void setChildren(Set<ArticleRevision> children) {
    this.children = children;
  }

  @ManyToMany(mappedBy = "children")
  public Set<ArticleRevision> getParents() {
    return parents;
  }

  public void setParents(Set<ArticleRevision> parents) {
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
