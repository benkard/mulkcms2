package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.annotation.CheckForNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "article_branch_tips", schema = "public")
@IdClass(ArticleBranchTipPK.class)
@Immutable
public class ArticleBranchTip extends PanacheEntityBase {

  @Column(name = "article", nullable = false)
  @Id
  private int articleId;

  @Column(name = "revision", nullable = true)
  @Id
  @CheckForNull
  private Integer revisionId;
}
