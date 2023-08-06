package eu.mulk.mulkcms2.benki.accesscontrol;

import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "page_keys", schema = "benki")
@IdClass(PageKeyPK.class)
public class PageKey extends PanacheEntityBase {

  @Id
  @Column(name = "page", nullable = false, length = -1)
  public String page;

  @Id
  @Column(name = "key", nullable = false, precision = 0)
  public BigInteger key;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "\"user\"", referencedColumnName = "id", nullable = false)
  public User user;
}
