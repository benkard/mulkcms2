package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_permissions", schema = "public", catalog = "mulkcms")
@IdClass(UserPermissionPK.class)
public class UserPermission extends PanacheEntityBase {

  private int userId;
  private String permission;
  private Boolean status;
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
  @Column(name = "permission", nullable = false, length = -1)
  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  @Basic
  @Column(name = "status", nullable = true)
  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserPermission that = (UserPermission) o;
    return userId == that.userId &&
        Objects.equals(permission, that.permission) &&
        Objects.equals(status, that.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, permission, status);
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
