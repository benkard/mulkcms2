package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Openids {

  private String openid;
  private User user;

  @Id
  @Column(name = "openid", nullable = false, length = -1)
  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Openids openids = (Openids) o;

    if (openid != null ? !openid.equals(openids.openid) : openids.openid != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return openid != null ? openid.hashCode() : 0;
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
