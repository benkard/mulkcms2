package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "effective_role_subroles", schema = "public", catalog = "benki")
public class EffectiveRoleSubrole extends PanacheEntityBase {

  @Column(name = "superrole", nullable = true)
  public Integer superroleId;

  @Column(name = "subrole", nullable = true)
  public Integer subroleId;
}
