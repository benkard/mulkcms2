package eu.mulk.mulkcms2.benki.users;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;

public class UserRolePK implements Serializable {

  @Column(name = "user", nullable = false)
  @Id
  private int userId;

  @Column(name = "role", nullable = false)
  @Id
  private int roleId;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

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
    return roleId == that.roleId;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + roleId;
    return result;
  }
}
