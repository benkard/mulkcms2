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
@Table(name = "lazychat_references", schema = "public", catalog = "benki")
@IdClass(LazychatReferencePK.class)
public class LazychatReference extends PanacheEntityBase {

  @Id
  @Column(name = "referrer", nullable = false)
  public int referrerId;

  @Id
  @Column(name = "referee", nullable = false)
  public int refereeId;

  @ManyToOne
  @JoinColumn(name = "referrer", referencedColumnName = "id", nullable = false)
  public LazychatMessage referrer;

  @ManyToOne
  @JoinColumn(name = "referee", referencedColumnName = "id", nullable = false)
  public LazychatMessage referee;
}
