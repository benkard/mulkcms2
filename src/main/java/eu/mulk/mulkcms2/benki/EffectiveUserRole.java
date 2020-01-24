package eu.mulk.mulkcms2.benki;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "effective_user_roles", schema = "public", catalog = "benki")
public class EffectiveUserRole {

  private Integer userId;
  private Integer roleId;

  @Basic
  @Column(name = "user", nullable = true)
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "role", nullable = true)
  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
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

    EffectiveUserRole that = (EffectiveUserRole) o;

    if (userId != null ? !userId.equals(that.userId) : that.userId != null) {
      return false;
    }
    if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId != null ? userId.hashCode() : 0;
    result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
    return result;
  }
}
