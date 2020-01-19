package eu.mulk.mulkcms2.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "article_publishing_dates", schema = "public", catalog = "mulkcms")
@Immutable
public class ArticlePublishingDate extends PanacheEntityBase {

  @Column(name = "article", nullable = true)
  @Id
  public Integer article;

  @Column(name = "publishing_date", nullable = true)
  public Timestamp publishingDate;
}
