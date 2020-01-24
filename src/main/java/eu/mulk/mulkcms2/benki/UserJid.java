package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_jids", schema = "public", catalog = "benki")
@IdClass(UserJidPK.class)
public class UserJid {

  private int userId;
  private String jid;
  private User user;

  @Id
  @Column(name = "user", nullable = false)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Id
  @Column(name = "jid", nullable = false, length = -1)
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

    UserJid userJid = (UserJid) o;

    if (userId != userJid.userId) {
      return false;
    }
    if (jid != null ? !jid.equals(userJid.jid) : userJid.jid != null) {
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

  @ManyToOne
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
