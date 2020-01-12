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
@Table(name = "article_category_memberships", schema = "public", catalog = "mulkcms")
@IdClass(ArticleCategoryMembershipPK.class)
public class ArticleCategoryMembership extends PanacheEntityBase {

  private int articleId;
  private String category;
  private Article article;

  @Id
  @Column(name = "article", nullable = false)
  public int getArticleId() {
    return articleId;
  }

  public void setArticleId(int articleId) {
    this.articleId = articleId;
  }

  @Id
  @Column(name = "category", nullable = false, length = -1)
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleCategoryMembership that = (ArticleCategoryMembership) o;
    return articleId == that.articleId &&
        Objects.equals(category, that.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(articleId, category);
  }

  @ManyToOne
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }
}
