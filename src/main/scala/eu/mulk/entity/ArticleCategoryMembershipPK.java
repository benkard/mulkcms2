package eu.mulk.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class ArticleCategoryMembershipPK implements Serializable {

  private int articleId;
  private String category;

  @Column(name = "article", nullable = false)
  @Id
  public int getArticleId() {
    return articleId;
  }

  public void setArticleId(int articleId) {
    this.articleId = articleId;
  }

  @Column(name = "category", nullable = false, length = -1)
  @Id
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
    ArticleCategoryMembershipPK that = (ArticleCategoryMembershipPK) o;
    return articleId == that.articleId &&
        Objects.equals(category, that.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(articleId, category);
  }
}
