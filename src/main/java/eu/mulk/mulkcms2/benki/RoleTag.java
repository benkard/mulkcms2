package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "role_tags", schema = "public", catalog = "benki")
@IdClass(RoleTagPK.class)
public class RoleTag {

  private int roleId;
  private String tag;

  @Id
  @Column(name = "role", nullable = false)
  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  @Id
  @Column(name = "tag", nullable = false, length = -1)
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

    RoleTag roleTag = (RoleTag) o;

    if (roleId != roleTag.roleId) {
      return false;
    }
    if (tag != null ? !tag.equals(roleTag.tag) : roleTag.tag != null) {
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
