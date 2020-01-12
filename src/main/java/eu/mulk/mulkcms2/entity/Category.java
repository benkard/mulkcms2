package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Set;
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

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "name", nullable = false, length = -1)
  public String name;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "category_inclusions",
      joinColumns = @JoinColumn(name = "category"),
      inverseJoinColumns = @JoinColumn(name = "supercategory"))
  public Set<Category> supercategories;

  @ManyToMany(mappedBy = "supercategories", fetch = FetchType.LAZY)
  public Set<Category> subcategories;
}
