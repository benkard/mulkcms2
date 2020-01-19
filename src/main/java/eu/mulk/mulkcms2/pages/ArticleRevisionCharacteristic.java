package eu.mulk.mulkcms2.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "article_revision_characteristics", schema = "public", catalog = "mulkcms")
@IdClass(ArticleRevisionCharacteristicPK.class)
public class ArticleRevisionCharacteristic extends PanacheEntityBase {

  @Column(name = "characteristic", nullable = false, length = -1)
  @Id
  public String characteristic;

  @Id
  @Column(name = "revision", nullable = false)
  public int articleRevisionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "revision",
      referencedColumnName = "id",
      nullable = false,
      insertable = false,
      updatable = false)
  public ArticleRevision articleRevision;

  @Column(name = "value", nullable = true, length = -1)
  public String value;
}
