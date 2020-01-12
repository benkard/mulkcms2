package eu.mulk.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "article_aliases", schema = "public", catalog = "mulkcms")
public class ArticleAlias extends PanacheEntityBase {

  private String alias;
  private Article article;

  @Id
  @Column(name = "alias", nullable = false, length = -1)
  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleAlias that = (ArticleAlias) o;
    return Objects.equals(alias, that.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alias);
  }

  @ManyToOne
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }
}
