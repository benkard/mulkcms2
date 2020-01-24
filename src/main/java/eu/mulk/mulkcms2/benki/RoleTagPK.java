package eu.mulk.mulkcms2.benki;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

public class RoleTagPK implements Serializable {

  private int roleId;
  private String tag;

  @Column(name = "role", nullable = false)
  @Id
  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  @Column(name = "tag", nullable = false, length = -1)
  @Id
  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RoleTagPK roleTagPK = (RoleTagPK) o;

    if (roleId != roleTagPK.roleId) {
      return false;
    }
    if (tag != null ? !tag.equals(roleTagPK.tag) : roleTagPK.tag != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = roleId;
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    return result;
  }
}
