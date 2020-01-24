package eu.mulk.mulkcms2.benki;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "effective_role_subroles", schema = "public", catalog = "benki")
public class EffectiveRoleSubrole {

  private Integer superroleId;
  private Integer subroleId;

  @Basic
  @Column(name = "superrole", nullable = true)
  public Integer getSuperroleId() {
    return superroleId;
  }

  public void setSuperroleId(Integer superroleId) {
    this.superroleId = superroleId;
  }

  @Basic
  @Column(name = "subrole", nullable = true)
  public Integer getSubroleId() {
    return subroleId;
  }

  public void setSubroleId(Integer subroleId) {
    this.subroleId = subroleId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EffectiveRoleSubrole that = (EffectiveRoleSubrole) o;

    if (superroleId != null ? !superroleId.equals(that.superroleId) : that.superroleId != null) {
      return false;
    }
    if (subroleId != null ? !subroleId.equals(that.subroleId) : that.subroleId != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = superroleId != null ? superroleId.hashCode() : 0;
    result = 31 * result + (subroleId != null ? subroleId.hashCode() : 0);
    return result;
  }
}
