package eu.mulk.mulkcms2.benki.accesscontrol;

import eu.mulk.mulkcms2.benki.generic.PostTarget;
import eu.mulk.mulkcms2.benki.users.User;
import eu.mulk.mulkcms2.benki.users.UserDefaultTarget;
import eu.mulk.mulkcms2.benki.users.UserRole;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles", schema = "public", catalog = "benki")
public class Role extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "name", nullable = true, length = -1)
  public String name;

  @OneToMany(mappedBy = "target")
  public Collection<PostTarget> targetedPosts;

  @OneToMany(mappedBy = "superrole")
  public Collection<RoleSubrole> subroles;

  @OneToMany(mappedBy = "subrole")
  public Collection<RoleSubrole> superroles;

  @OneToMany(mappedBy = "target")
  public Collection<UserDefaultTarget> usersUsedByAsDefaultTarget;

  @OneToMany(mappedBy = "role")
  public Collection<UserRole> users;

  @OneToMany(mappedBy = "ownedRole")
  public Collection<User> owningUsers;
}
