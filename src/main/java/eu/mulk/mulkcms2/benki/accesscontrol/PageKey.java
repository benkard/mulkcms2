package eu.mulk.mulkcms2.benki.accesscontrol;

import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "page_keys", schema = "public", catalog = "benki")
@IdClass(PageKeyPK.class)
public class PageKey extends PanacheEntityBase {

  @Id
  @Column(name = "page", nullable = false, length = -1)
  public String page;

  @Id
  @Column(name = "key", nullable = false, precision = 0)
  public BigInteger key;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User user;
}
