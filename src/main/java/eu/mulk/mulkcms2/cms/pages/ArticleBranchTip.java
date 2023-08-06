package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import javax.annotation.CheckForNull;
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
