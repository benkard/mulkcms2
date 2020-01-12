package eu.mulk.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "categories", schema = "public", catalog = "mulkcms")
public class Category extends PanacheEntityBase {

  private int id;
  private String name;
  private Collection<CategoryInclusion> supercategories;
  private Collection<CategoryInclusion> subcategories;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "name", nullable = false, length = -1)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Category category = (Category) o;
    return id == category.id &&
        Objects.equals(name, category.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @OneToMany(mappedBy = "subcategory")
  public Collection<CategoryInclusion> getSupercategories() {
    return supercategories;
  }

  public void setSupercategories(Collection<CategoryInclusion> supercategories) {
    this.supercategories = supercategories;
  }

  @OneToMany(mappedBy = "supercategory")
  public Collection<CategoryInclusion> getSubcategories() {
    return subcategories;
  }

  public void setSubcategories(Collection<CategoryInclusion> subcategories) {
    this.subcategories = subcategories;
  }
}
