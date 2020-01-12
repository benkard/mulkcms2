package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "article_branch_tips", schema = "public", catalog = "mulkcms")
@IdClass(ArticleBranchTipPK.class)
public class ArticleBranchTip extends PanacheEntityBase {

  private Integer articleId;
  private Integer revisionId;

  @Basic
  @Column(name = "article", nullable = true)
  @Id
  public Integer getArticleId() {
    return articleId;
  }

  public void setArticleId(Integer articleId) {
    this.articleId = articleId;
  }

  @Basic
  @Column(name = "revision", nullable = true)
  @Id
  public Integer getRevisionId() {
    return revisionId;
  }

  public void setRevisionId(Integer revision) {
    this.revisionId = revision;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleBranchTip that = (ArticleBranchTip) o;
    return Objects.equals(articleId, that.articleId) &&
        Objects.equals(revisionId, that.revisionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(articleId, revisionId);
  }
}
