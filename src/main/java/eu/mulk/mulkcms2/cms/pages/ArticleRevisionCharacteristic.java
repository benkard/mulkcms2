package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "article_revision_characteristics", schema = "public")
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
  @CheckForNull
  public String value;
}
