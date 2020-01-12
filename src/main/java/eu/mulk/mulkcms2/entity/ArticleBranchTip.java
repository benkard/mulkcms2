package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "article_branch_tips", schema = "public", catalog = "mulkcms")
@IdClass(ArticleBranchTipPK.class)
public class ArticleBranchTip extends PanacheEntityBase {

  @Column(name = "article", nullable = true)
  @Id
  private Integer articleId;

  @Column(name = "revision", nullable = true)
  @Id
  private Integer revisionId;
}
