package eu.mulk.mulkcms2.benki;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

public class UserRolePK implements Serializable {

  private int userId;
  private int roleId;

  @Column(name = "user", nullable = false)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "role", nullable = false)
  @Id
  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserRolePK that = (UserRolePK) o;

    if (userId != that.userId) {
      return false;
    }
    if (roleId != that.roleId) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + roleId;
    return result;
  }
}
