package eu.mulk.mulkcms2.benki.accesscontrol;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public class PageKeyPK implements Serializable {

  private String page;
  private BigInteger key;

  @Column(name = "page", nullable = false, length = -1)
  @Id
  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  @Column(name = "key", nullable = false, precision = 0)
  @Id
  public BigInteger getKey() {
    return key;
  }

  public void setKey(BigInteger key) {
    this.key = key;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PageKeyPK pageKeyPK = (PageKeyPK) o;

    if (!Objects.equals(page, pageKeyPK.page)) {
      return false;
    }
    return Objects.equals(key, pageKeyPK.key);
  }

  @Override
  public int hashCode() {
    int result = page != null ? page.hashCode() : 0;
    result = 31 * result + (key != null ? key.hashCode() : 0);
    return result;
  }
}
