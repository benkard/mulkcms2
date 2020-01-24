package eu.mulk.mulkcms2.benki;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

public class RoleSubrolePK implements Serializable {

  private int superroleId;
  private int subroleId;

  @Column(name = "superrole", nullable = false)
  @Id
  public int getSuperroleId() {
    return superroleId;
  }

  public void setSuperroleId(int superroleId) {
    this.superroleId = superroleId;
  }

  @Column(name = "subrole", nullable = false)
  @Id
  public int getSubroleId() {
    return subroleId;
  }

  public void setSubroleId(int subroleId) {
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

    RoleSubrolePK that = (RoleSubrolePK) o;

    if (superroleId != that.superroleId) {
      return false;
    }
    if (subroleId != that.subroleId) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = superroleId;
    result = 31 * result + subroleId;
    return result;
  }
}
