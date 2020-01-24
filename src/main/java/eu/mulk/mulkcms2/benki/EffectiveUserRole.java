package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "effective_user_roles", schema = "public", catalog = "benki")
public class EffectiveUserRole extends PanacheEntityBase {

  @Column(name = "user", nullable = true)
  public Integer userId;

  @Column(name = "role", nullable = true)
  public Integer roleId;
}
