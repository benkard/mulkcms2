package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "article_types", schema = "public")
public class ArticleType extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "name", nullable = true, length = -1)
  @CheckForNull
  public String name;

  @Column(name = "page_template", nullable = true, length = -1)
  @CheckForNull
  public String pageTemplate;

  @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
  public Collection<Article> articles;
}
