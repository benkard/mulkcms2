package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "article_revision_characteristics", schema = "public", catalog = "mulkcms")
@IdClass(ArticleRevisionCharacteristicPK.class)
public class ArticleRevisionCharacteristic extends PanacheEntityBase {

  private String characteristic;
  private int articleRevisionId;

  private ArticleRevision articleRevision;
  private String value;

  @Basic
  @Column(name = "characteristic", nullable = false, length = -1)
  @Id
  public String getCharacteristic() {
    return characteristic;
  }

  public void setCharacteristic(String characteristic) {
    this.characteristic = characteristic;
  }

  @Basic
  @Column(name = "value", nullable = true, length = -1)
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArticleRevisionCharacteristic that = (ArticleRevisionCharacteristic) o;
    return Objects.equals(characteristic, that.characteristic) &&
        Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characteristic, value);
  }

  @ManyToOne
  @JoinColumn(name = "revision", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  public ArticleRevision getArticleRevision() {
    return articleRevision;
  }

  public void setArticleRevision(ArticleRevision articleRevision) {
    this.articleRevision = articleRevision;
  }

  @Id
  @Column(name = "revision", nullable = false)
  public int getArticleRevisionId() {
    return articleRevisionId;
  }

  public void setArticleRevisionId(int articleRevisionId) {
    this.articleRevisionId = articleRevisionId;
  }
}
