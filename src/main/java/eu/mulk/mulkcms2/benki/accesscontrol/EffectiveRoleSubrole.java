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
@Table(name = "effective_role_subroles", schema = "public", catalog = "benki")
public class EffectiveRoleSubrole extends PanacheEntityBase implements Serializable {

  @Id
  @Column(name = "superrole", nullable = true)
  public Integer superroleId;

  @Id
  @Column(name = "subrole", nullable = true)
  public Integer subroleId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EffectiveRoleSubrole)) {
      return false;
    }
    EffectiveRoleSubrole that = (EffectiveRoleSubrole) o;
    return Objects.equals(superroleId, that.superroleId)
        && Objects.equals(subroleId, that.subroleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(superroleId, subroleId);
  }
}
