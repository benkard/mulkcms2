package eu.mulk.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "article_revision_parenthood", schema = "public", catalog = "mulkcms")
@IdClass(ArticleRevisionParenthoodPK.class)
public class ArticleRevisionParenthood extends PanacheEntityBase {

  private int parentId;
  private int childId;
  private ArticleRevision parent;
  private ArticleRevision child;

  @Id
  @Column(name = "parent", nullable = false)
  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  @Id
  @Column(name = "child", nullable = false)
  public int getChildId() {
    return childId;
  }

  public void setChildId(int childId) {
    this.childId = childId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleRevisionParenthood that = (ArticleRevisionParenthood) o;
    return parentId == that.parentId &&
        childId == that.childId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(parentId, childId);
  }

  @ManyToOne
  @JoinColumn(name = "parent", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  public ArticleRevision getParent() {
    return parent;
  }

  public void setParent(ArticleRevision parent) {
    this.parent = parent;
  }

  @ManyToOne
  @JoinColumn(name = "child", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  public ArticleRevision getChild() {
    return child;
  }

  public void setChild(ArticleRevision child) {
    this.child = child;
  }
}
