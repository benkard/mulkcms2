package eu.mulk.mulkcms2.benki.posts;

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
  @JoinColumn(
      name = "target",
      referencedColumnName = "id",
      nullable = false,
      insertable = false,
      updatable = false)
  public Role target;
}
