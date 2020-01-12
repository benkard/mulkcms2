package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "categories", schema = "public", catalog = "mulkcms")
public class Category extends PanacheEntityBase {

  private int id;
  private String name;
  private Set<Category> supercategories;
  private Set<Category> subcategories;

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

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "category_inclusions",
      joinColumns = @JoinColumn(name = "category"),
      inverseJoinColumns = @JoinColumn(name = "supercategory")
  )
  public Set<Category> getSupercategories() {
    return supercategories;
  }

  public void setSupercategories(Set<Category> supercategories) {
    this.supercategories = supercategories;
  }

  @ManyToMany(mappedBy = "supercategories", fetch = FetchType.LAZY)
  public Set<Category> getSubcategories() {
    return subcategories;
  }

  public void setSubcategories(Set<Category> subcategories) {
    this.subcategories = subcategories;
  }
}
