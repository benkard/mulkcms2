package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "article_comment_counts", schema = "public", catalog = "mulkcms")
public class ArticleCommentCount extends PanacheEntityBase {

  @Id
  private Integer article;
  private Long commentCount;

  @Basic
  @Column(name = "article", nullable = true)
  public Integer getArticle() {
    return article;
  }

  public void setArticle(Integer article) {
    this.article = article;
  }

  @Basic
  @Column(name = "comment_count", nullable = true)
  public Long getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Long commentCount) {
    this.commentCount = commentCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleCommentCount that = (ArticleCommentCount) o;
    return Objects.equals(article, that.article) &&
        Objects.equals(commentCount, that.commentCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(article, commentCount);
  }
}
