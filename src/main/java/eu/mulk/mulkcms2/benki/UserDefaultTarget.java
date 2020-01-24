package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_default_target", schema = "public", catalog = "benki")
@IdClass(UserDefaultTargetPK.class)
public class UserDefaultTarget {

  private int userId;
  private int targetId;
  private User user;
  private Role target;

  @Id
  @Column(name = "user", nullable = false)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Id
  @Column(name = "target", nullable = false)
  public int getTargetId() {
    return targetId;
  }

  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserDefaultTarget that = (UserDefaultTarget) o;

    if (userId != that.userId) {
      return false;
    }
    if (targetId != that.targetId) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + targetId;
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

  @ManyToOne
  @JoinColumn(name = "target", referencedColumnName = "id", nullable = false)
  public Role getTarget() {
    return target;
  }

  public void setTarget(Role target) {
    this.target = target;
  }
}
