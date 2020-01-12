package eu.mulk.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class ArticleRevisionParenthoodPK implements Serializable {

  private int parentId;
  private int childId;

  @Column(name = "parent", nullable = false)
  @Id
  public int getParentId() {
    return parentId;
  }

  public void setParentId(int parentId) {
    this.parentId = parentId;
  }

  @Column(name = "child", nullable = false)
  @Id
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
    ArticleRevisionParenthoodPK that = (ArticleRevisionParenthoodPK) o;
    return parentId == that.parentId &&
        childId == that.childId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(parentId, childId);
  }
}
