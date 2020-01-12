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
@Table(name = "category_inclusions", schema = "public", catalog = "mulkcms")
@IdClass(CategoryInclusionPK.class)
public class CategoryInclusion extends PanacheEntityBase {

  private int subcategoryId;
  private int supercategoryId;
  private Category subcategory;
  private Category supercategory;

  @Id
  @Column(name = "category", nullable = false)
  public int getSubcategoryId() {
    return subcategoryId;
  }

  public void setSubcategoryId(int subcategoryId) {
    this.subcategoryId = subcategoryId;
  }

  @Id
  @Column(name = "supercategory", nullable = false)
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
    CategoryInclusion that = (CategoryInclusion) o;
    return subcategoryId == that.subcategoryId &&
        supercategoryId == that.supercategoryId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(subcategoryId, supercategoryId);
  }

  @ManyToOne
  @JoinColumn(name = "category", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  public Category getSubcategory() {
    return subcategory;
  }

  public void setSubcategory(Category subcategory) {
    this.subcategory = subcategory;
  }

  @ManyToOne
  @JoinColumn(name = "supercategory", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  public Category getSupercategory() {
    return supercategory;
  }

  public void setSupercategory(Category supercategory) {
    this.supercategory = supercategory;
  }
}
