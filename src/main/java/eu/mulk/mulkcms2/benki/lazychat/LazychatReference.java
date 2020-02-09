package eu.mulk.mulkcms2.benki.lazychat;

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
@Table(name = "lazychat_references", schema = "benki")
@IdClass(LazychatReferencePK.class)
public class LazychatReference extends PanacheEntityBase {

  @Id
  @Column(name = "referrer", nullable = false)
  public int referrerId;

  @Id
  @Column(name = "referee", nullable = false)
  public int refereeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "referrer", referencedColumnName = "id", nullable = false)
  public LazychatMessage referrer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "referee", referencedColumnName = "id", nullable = false)
  public LazychatMessage referee;
}
