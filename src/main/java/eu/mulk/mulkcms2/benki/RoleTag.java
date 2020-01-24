package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "role_tags", schema = "public", catalog = "benki")
@IdClass(RoleTagPK.class)
public class RoleTag extends PanacheEntityBase {

  @Id
  @Column(name = "role", nullable = false)
  public int roleId;

  @Id
  @Column(name = "tag", nullable = false, length = -1)
  public String tag;
}
