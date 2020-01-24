package eu.mulk.mulkcms2.benki.accesscontrol;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Id;

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

    if (page != null ? !page.equals(pageKeyPK.page) : pageKeyPK.page != null) {
      return false;
    }
    if (key != null ? !key.equals(pageKeyPK.key) : pageKeyPK.key != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = page != null ? page.hashCode() : 0;
    result = 31 * result + (key != null ? key.hashCode() : 0);
    return result;
  }
}
