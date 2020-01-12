package eu.mulk.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class UserPermissionPK implements Serializable {

  private int userId;
  private String permission;

  @Column(name = "user", nullable = false)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "permission", nullable = false, length = -1)
  @Id
  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserPermissionPK that = (UserPermissionPK) o;
    return userId == that.userId &&
        Objects.equals(permission, that.permission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, permission);
  }
}
