package eu.mulk.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "cached_pages", schema = "public", catalog = "mulkcms")
@IdClass(CachedPagePK.class)
public class CachedPage extends PanacheEntityBase {

  private String alias;
  private int characteristicHash;
  private Timestamp date;
  private String content;

  @Id
  @Column(name = "alias", nullable = false, length = -1)
  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  @Id
  @Column(name = "characteristic_hash", nullable = false)
  public int getCharacteristicHash() {
    return characteristicHash;
  }

  public void setCharacteristicHash(int characteristicHash) {
    this.characteristicHash = characteristicHash;
  }

  @Basic
  @Column(name = "date", nullable = false)
  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  @Basic
  @Column(name = "content", nullable = false, length = -1)
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CachedPage that = (CachedPage) o;
    return characteristicHash == that.characteristicHash &&
        Objects.equals(alias, that.alias) &&
        Objects.equals(date, that.date) &&
        Objects.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alias, characteristicHash, date, content);
  }
}
