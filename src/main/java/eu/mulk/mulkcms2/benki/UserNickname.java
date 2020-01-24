package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_nicknames", schema = "public", catalog = "benki")
public class UserNickname {

  private String nickname;
  private User user;

  @Id
  @Column(name = "nickname", nullable = false, length = -1)
  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserNickname that = (UserNickname) o;

    if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return nickname != null ? nickname.hashCode() : 0;
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
