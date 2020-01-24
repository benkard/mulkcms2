package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "role_subroles", schema = "public", catalog = "benki")
@IdClass(RoleSubrolePK.class)
public class RoleSubrole {

  private int superroleId;
  private int subroleId;
  private Role superrole;
  private Role subrole;

  @Id
  @Column(name = "superrole", nullable = false)
  public int getSuperroleId() {
    return superroleId;
  }

  public void setSuperroleId(int superroleId) {
    this.superroleId = superroleId;
  }

  @Id
  @Column(name = "subrole", nullable = false)
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

    RoleSubrole that = (RoleSubrole) o;

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

  @ManyToOne
  @JoinColumn(name = "superrole", referencedColumnName = "id", nullable = false)
  public Role getSuperrole() {
    return superrole;
  }

  public void setSuperrole(Role superrole) {
    this.superrole = superrole;
  }

  @ManyToOne
  @JoinColumn(name = "subrole", referencedColumnName = "id", nullable = false)
  public Role getSubrole() {
    return subrole;
  }

  public void setSubrole(Role subrole) {
    this.subrole = subrole;
  }
}
