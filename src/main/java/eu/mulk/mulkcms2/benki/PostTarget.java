package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "post_targets", schema = "public", catalog = "benki")
@IdClass(PostTargetPK.class)
public class PostTarget extends PanacheEntityBase {

  @Id
  @Column(name = "message", nullable = false)
  public int message;

  @Id
  @Column(name = "target", nullable = false)
  public int targetId;

  @ManyToOne
  @JoinColumn(name = "target", referencedColumnName = "id", nullable = false)
  public Role target;
}
