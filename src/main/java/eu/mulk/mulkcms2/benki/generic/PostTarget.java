package eu.mulk.mulkcms2.benki.generic;

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
@Table(name = "post_targets", schema = "benki")
@IdClass(PostTargetPK.class)
public class PostTarget extends PanacheEntityBase {

  @Id
  @Column(name = "message", nullable = false)
  public int message;

  @Id
  @Column(name = "target", nullable = false)
  public int targetId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target", referencedColumnName = "id", nullable = false)
  public Role target;
}
