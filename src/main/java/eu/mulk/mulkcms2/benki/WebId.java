package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "webids", schema = "public", catalog = "benki")
public class WebId {

  private String webid;
  private User user;

  @Id
  @Column(name = "webid", nullable = false, length = -1)
  public String getWebid() {
    return webid;
  }

  public void setWebid(String webid) {
    this.webid = webid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WebId webId = (WebId) o;

    if (webid != null ? !webid.equals(webId.webid) : webId.webid != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return webid != null ? webid.hashCode() : 0;
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
