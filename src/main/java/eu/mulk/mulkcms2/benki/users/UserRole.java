package eu.mulk.mulkcms2.benki.users;

import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_roles", schema = "benki")
@IdClass(UserRolePK.class)
public class UserRole extends PanacheEntityBase {

  @Id
  @Column(name = "user", nullable = false)
  public int userId;

  @Id
  @Column(name = "role", nullable = false)
  public int roleId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role", referencedColumnName = "id", nullable = false)
  public Role role;
}
