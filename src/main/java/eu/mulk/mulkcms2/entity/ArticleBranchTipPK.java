package eu.mulk.mulkcms2.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class ArticleBranchTipPK implements Serializable {

  private int articleId;
  private int revisionId;

  @Id
  @Column(name = "article", nullable = false)
  public int getArticleId() {
    return articleId;
  }

  @Id
  @Column(name = "revision", nullable = false)
  public int getRevisionId() {
    return revisionId;
  }

  public void setArticleId(int articleId) {
    this.articleId = articleId;
  }

  public void setRevisionId(int revisionId) {
    this.revisionId = revisionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArticleBranchTipPK)) {
      return false;
    }
    ArticleBranchTipPK that = (ArticleBranchTipPK) o;
    return articleId == that.articleId && revisionId == that.revisionId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(articleId, revisionId);
  }
}
