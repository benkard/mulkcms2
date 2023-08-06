package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "article_comment_counts", schema = "public")
@Immutable
public class ArticleCommentCount extends PanacheEntityBase {

  @Column(name = "article", nullable = false)
  @Id
  public int articleId;

  @Column(name = "comment_count", nullable = false)
  public long commentCount;
}
