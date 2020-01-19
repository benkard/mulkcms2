package eu.mulk.mulkcms2.pages;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class CachedPagePK implements Serializable {

  private String alias;
  private int characteristicHash;

  @Column(name = "alias", nullable = false, length = -1)
  @Id
  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  @Column(name = "characteristic_hash", nullable = false)
  @Id
  public int getCharacteristicHash() {
    return characteristicHash;
  }

  public void setCharacteristicHash(int characteristicHash) {
    this.characteristicHash = characteristicHash;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CachedPagePK that = (CachedPagePK) o;
    return characteristicHash == that.characteristicHash && Objects.equals(alias, that.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alias, characteristicHash);
  }
}
