package eu.mulk.mulkcms2.benki.users;

import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_default_target", schema = "benki")
@IdClass(UserDefaultTargetPK.class)
public class UserDefaultTarget extends PanacheEntityBase {

  @Id
  @Column(name = "user", nullable = false)
  public int userId;

  @Id
  @Column(name = "target", nullable = false)
  public int targetId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target", referencedColumnName = "id", nullable = false)
  public Role target;
}
