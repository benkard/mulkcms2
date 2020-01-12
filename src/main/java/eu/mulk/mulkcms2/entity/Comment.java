package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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
@Table(name = "comments", schema = "public", catalog = "mulkcms")
public class Comment extends PanacheEntityBase {

  private int id;
  private String globalId;
  private Collection<CommentRevision> revisions;
  private Article article;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
    Comment comment = (Comment) o;
    return id == comment.id &&
        Objects.equals(globalId, comment.globalId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, globalId);
  }

  @OneToMany(mappedBy = "comment")
  public Collection<CommentRevision> getRevisions() {
    return revisions;
  }

  public void setRevisions(Collection<CommentRevision> revisions) {
    this.revisions = revisions;
  }

  @ManyToOne
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }
}
