package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles", schema = "public", catalog = "benki")
@IdClass(UserRolePK.class)
public class UserRole extends PanacheEntityBase {

  @Id
  @Column(name = "user", nullable = false)
  public int userId;

  @Id
  @Column(name = "role", nullable = false)
  public int roleId;

  @ManyToOne
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User user;

  @ManyToOne
  @JoinColumn(name = "role", referencedColumnName = "id", nullable = false)
  public Role role;
}
