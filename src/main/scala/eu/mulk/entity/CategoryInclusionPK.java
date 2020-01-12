package eu.mulk.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class CategoryInclusionPK implements Serializable {

  private int subcategoryId;
  private int supercategoryId;

  @Column(name = "category", nullable = false)
  @Id
  public int getSubcategoryId() {
    return subcategoryId;
  }

  public void setSubcategoryId(int subcategoryId) {
    this.subcategoryId = subcategoryId;
  }

  @Column(name = "supercategory", nullable = false)
  @Id
  public int getSupercategoryId() {
    return supercategoryId;
  }

  public void setSupercategoryId(int supercategoryId) {
    this.supercategoryId = supercategoryId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CategoryInclusionPK that = (CategoryInclusionPK) o;
    return subcategoryId == that.subcategoryId &&
        supercategoryId == that.supercategoryId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(subcategoryId, supercategoryId);
  }
}
