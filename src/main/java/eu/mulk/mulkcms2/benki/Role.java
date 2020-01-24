package eu.mulk.mulkcms2.benki;

import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles", schema = "public", catalog = "benki")
public class Role {

  private int id;
  private String name;
  private Collection<PostTarget> targetedPosts;
  private Collection<RoleSubrole> subroles;
  private Collection<RoleSubrole> superroles;
  private Collection<UserDefaultTarget> usersUsedByAsDefaultTarget;
  private Collection<UserRole> users;
  private Collection<User> owningUsers;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "name", nullable = true, length = -1)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Role role = (Role) o;

    if (id != role.id) {
      return false;
    }
    if (name != null ? !name.equals(role.name) : role.name != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @OneToMany(mappedBy = "target")
  public Collection<PostTarget> getTargetedPosts() {
    return targetedPosts;
  }

  public void setTargetedPosts(Collection<PostTarget> targetedPosts) {
    this.targetedPosts = targetedPosts;
  }

  @OneToMany(mappedBy = "superrole")
  public Collection<RoleSubrole> getSubroles() {
    return subroles;
  }

  public void setSubroles(Collection<RoleSubrole> subroles) {
    this.subroles = subroles;
  }

  @OneToMany(mappedBy = "subrole")
  public Collection<RoleSubrole> getSuperroles() {
    return superroles;
  }

  public void setSuperroles(Collection<RoleSubrole> superroles) {
    this.superroles = superroles;
  }

  @OneToMany(mappedBy = "target")
  public Collection<UserDefaultTarget> getUsersUsedByAsDefaultTarget() {
    return usersUsedByAsDefaultTarget;
  }

  public void setUsersUsedByAsDefaultTarget(
      Collection<UserDefaultTarget> usersUsedByAsDefaultTarget) {
    this.usersUsedByAsDefaultTarget = usersUsedByAsDefaultTarget;
  }

  @OneToMany(mappedBy = "role")
  public Collection<UserRole> getUsers() {
    return users;
  }

  public void setUsers(Collection<UserRole> users) {
    this.users = users;
  }

  @OneToMany(mappedBy = "ownedRole")
  public Collection<User> getOwningUsers() {
    return owningUsers;
  }

  public void setOwningUsers(Collection<User> owningUsers) {
    this.owningUsers = owningUsers;
  }
}
