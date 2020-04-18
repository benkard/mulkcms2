package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Timestamp;
import javax.annotation.CheckForNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "article_publishing_dates", schema = "public")
@Immutable
public class ArticlePublishingDate extends PanacheEntityBase {

  @Column(name = "article", nullable = false)
  @Id
  public int article;

  @Column(name = "publishing_date", nullable = true)
  @CheckForNull
  public Timestamp publishingDate;
}
