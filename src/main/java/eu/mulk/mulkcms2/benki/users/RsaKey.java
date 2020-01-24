package eu.mulk.mulkcms2.benki.users;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "rsa_keys", schema = "public", catalog = "benki")
@IdClass(RsaKeyPK.class)
public class RsaKey extends PanacheEntityBase {

  @Id
  @Column(name = "modulus", nullable = false, precision = 0)
  public BigInteger modulus;

  @Id
  @Column(name = "exponent", nullable = false, precision = 0)
  public BigInteger exponent;

  @OneToMany(mappedBy = "rsaKey", fetch = FetchType.LAZY)
  public Collection<UserRsaKey> users;
}
