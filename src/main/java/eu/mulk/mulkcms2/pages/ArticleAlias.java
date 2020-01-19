package eu.mulk.mulkcms2.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "article_aliases", schema = "public", catalog = "mulkcms")
public class ArticleAlias extends PanacheEntityBase {

  @Id
  @Column(name = "alias", nullable = false, length = -1)
  public String alias;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
  public Article article;
}
