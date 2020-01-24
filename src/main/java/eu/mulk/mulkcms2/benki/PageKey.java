package eu.mulk.mulkcms2.benki;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "page_keys", schema = "public", catalog = "benki")
@IdClass(PageKeyPK.class)
public class PageKey {

  private String page;
  private BigInteger key;
  private User user;

  @Id
  @Column(name = "page", nullable = false, length = -1)
  public String getPage() {
    return page;
  }

  public void setPage(String page) {
    this.page = page;
  }

  @Id
  @Column(name = "key", nullable = false, precision = 0)
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

    PageKey pageKey = (PageKey) o;

    if (page != null ? !page.equals(pageKey.page) : pageKey.page != null) {
      return false;
    }
    if (key != null ? !key.equals(pageKey.key) : pageKey.key != null) {
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

  @ManyToOne
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
