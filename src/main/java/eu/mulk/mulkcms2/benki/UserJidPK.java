package eu.mulk.mulkcms2.benki;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

public class UserJidPK implements Serializable {

  private int userId;
  private String jid;

  @Column(name = "user", nullable = false)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "jid", nullable = false, length = -1)
  @Id
  public String getJid() {
    return jid;
  }

  public void setJid(String jid) {
    this.jid = jid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserJidPK userJidPK = (UserJidPK) o;

    if (userId != userJidPK.userId) {
      return false;
    }
    if (jid != null ? !jid.equals(userJidPK.jid) : userJidPK.jid != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + (jid != null ? jid.hashCode() : 0);
    return result;
  }
}
