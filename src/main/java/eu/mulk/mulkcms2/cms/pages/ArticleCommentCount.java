package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "article_comment_counts", schema = "public", catalog = "mulkcms")
@Immutable
public class ArticleCommentCount extends PanacheEntityBase {

  @Column(name = "article", nullable = true)
  @Id
  public Integer article;

  @Column(name = "comment_count", nullable = true)
  public Long commentCount;
}
