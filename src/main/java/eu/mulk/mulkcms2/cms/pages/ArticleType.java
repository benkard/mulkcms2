package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "article_types", schema = "public", catalog = "mulkcms")
public class ArticleType extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "name", nullable = true, length = -1)
  public String name;

  @Column(name = "page_template", nullable = true, length = -1)
  public String pageTemplate;

  @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
  public Collection<Article> articles;
}
