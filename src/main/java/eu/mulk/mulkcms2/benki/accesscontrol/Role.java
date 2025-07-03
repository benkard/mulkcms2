package eu.mulk.mulkcms2.benki.accesscontrol;

import eu.mulk.mulkcms2.benki.posts.PostTarget;
import eu.mulk.mulkcms2.benki.users.User;
import eu.mulk.mulkcms2.benki.users.UserRole;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "roles", schema = "benki")
public class Role extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "name", nullable = true, length = -1)
  @CheckForNull
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

  @ManyToMany(mappedBy = "defaultTargets", fetch = FetchType.LAZY)
  public Set<User> usersUsedByAsDefaultTarget;

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  public Collection<UserRole> directUsers;

  @OneToOne(mappedBy = "ownedRole", fetch = FetchType.LAZY)
  public User owningUsers;

  @ManyToMany(mappedBy = "effectiveRoles", fetch = FetchType.LAZY)
  public Set<User> effectiveUsers;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "role_tags", schema = "benki", joinColumns = @JoinColumn(name = "role"))
  @Column(name = "tag")
  public Set<String> tags;

  public static Role getWorld() {
    return find("select r from Role r join r.tags tag where tag = 'world'").singleResult();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Role)) {
      return false;
    }
    Role role = (Role) o;
    return Objects.equals(id, role.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
