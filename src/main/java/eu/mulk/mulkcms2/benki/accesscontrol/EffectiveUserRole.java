package eu.mulk.mulkcms2.benki.accesscontrol;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "effective_user_roles", schema = "public", catalog = "benki")
public class EffectiveUserRole extends PanacheEntityBase implements Serializable {

  @Id
  @Column(name = "user", nullable = true)
  public Integer userId;

  @Id
  @Column(name = "role", nullable = true)
  public Integer roleId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EffectiveUserRole)) {
      return false;
    }
    EffectiveUserRole that = (EffectiveUserRole) o;
    return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, roleId);
  }
}
