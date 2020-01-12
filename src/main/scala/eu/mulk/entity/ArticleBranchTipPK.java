package eu.mulk.entity;

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
}
