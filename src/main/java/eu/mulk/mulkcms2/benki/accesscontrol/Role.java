package eu.mulk.mulkcms2.benki.accesscontrol;

import eu.mulk.mulkcms2.benki.generic.PostTarget;
import eu.mulk.mulkcms2.benki.users.User;
import eu.mulk.mulkcms2.benki.users.UserDefaultTarget;
import eu.mulk.mulkcms2.benki.users.UserRole;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "roles", schema = "benki")
public class Role extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "name", nullable = true, length = -1)
  public String name;

  @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
  public Collection<PostTarget> targetedPosts;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "role_subroles",
      schema = "benki",
      joinColumns = @JoinColumn(name = "superrole"),
      inverseJoinColumns = @JoinColumn(name = "subrole"))
  public Set<Role> directSubroles;

  @ManyToMany(mappedBy = "directSubroles", fetch = FetchType.LAZY)
  public Set<Role> directSuperroles;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "effective_role_subroles",
      schema = "benki",
      joinColumns = @JoinColumn(name = "superrole"),
      inverseJoinColumns = @JoinColumn(name = "subrole"))
  public Set<Role> effectiveSubroles;

  @ManyToMany(mappedBy = "effectiveSubroles", fetch = FetchType.LAZY)
  public Set<Role> effectiveSuperroles;

  @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
  public Collection<UserDefaultTarget> usersUsedByAsDefaultTarget;

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  public Collection<UserRole> directUsers;

  @OneToOne(mappedBy = "ownedRole", fetch = FetchType.LAZY)
  public User owningUsers;

  @ManyToMany(mappedBy = "effectiveRoles", fetch = FetchType.LAZY)
  public Collection<User> effectiveUsers;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "role_tags", schema = "benki", joinColumns = @JoinColumn(name = "role"))
  @Column(name = "tag")
  public Set<String> tags;
}
