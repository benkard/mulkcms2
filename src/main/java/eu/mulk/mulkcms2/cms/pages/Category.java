package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "categories", schema = "public")
public class Category extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

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

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
  public Set<Article> articles;
}
