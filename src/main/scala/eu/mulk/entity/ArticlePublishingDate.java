package eu.mulk.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "article_publishing_dates", schema = "public", catalog = "mulkcms")
public class ArticlePublishingDate extends PanacheEntityBase {

  @Id
  private Integer article;
  private Timestamp publishingDate;

  @Basic
  @Column(name = "article", nullable = true)
  public Integer getArticle() {
    return article;
  }

  public void setArticle(Integer article) {
    this.article = article;
  }

  @Basic
  @Column(name = "publishing_date", nullable = true)
  public Timestamp getPublishingDate() {
    return publishingDate;
  }

  public void setPublishingDate(Timestamp publishingDate) {
    this.publishingDate = publishingDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticlePublishingDate that = (ArticlePublishingDate) o;
    return Objects.equals(article, that.article) &&
        Objects.equals(publishingDate, that.publishingDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(article, publishingDate);
  }
}
