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
@Table(name = "article_types", schema = "public", catalog = "mulkcms")
public class ArticleType extends PanacheEntityBase {

  private int id;
  private String name;
  private String pageTemplate;
  private Collection<Article> articles;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "name", nullable = true, length = -1)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "page_template", nullable = true, length = -1)
  public String getPageTemplate() {
    return pageTemplate;
  }

  public void setPageTemplate(String pageTemplate) {
    this.pageTemplate = pageTemplate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleType that = (ArticleType) o;
    return id == that.id &&
        Objects.equals(name, that.name) &&
        Objects.equals(pageTemplate, that.pageTemplate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, pageTemplate);
  }

  @OneToMany(mappedBy = "type")
  public Collection<Article> getArticles() {
    return articles;
  }

  public void setArticles(Collection<Article> articles) {
    this.articles = articles;
  }
}
