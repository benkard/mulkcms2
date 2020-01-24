package eu.mulk.mulkcms2.benki.accesscontrol;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "role_subroles", schema = "public", catalog = "benki")
@IdClass(RoleSubrolePK.class)
public class RoleSubrole extends PanacheEntityBase {

  @Id
  @Column(name = "superrole", nullable = false)
  public int superroleId;

  @Id
  @Column(name = "subrole", nullable = false)
  public int subroleId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "superrole", referencedColumnName = "id", nullable = false)
  public Role superrole;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subrole", referencedColumnName = "id", nullable = false)
  public Role subrole;
}
