package eu.mulk.mulkcms2.benki.users;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "user_rsa_keys", schema = "benki")
@IdClass(UserRsaKeyPK.class)
public class UserRsaKey extends PanacheEntityBase {

  @Id
  @Column(name = "user", nullable = false)
  public int userId;

  @Id
  @Column(name = "modulus", nullable = false, precision = 0)
  public BigInteger modulus;

  @Id
  @Column(name = "exponent", nullable = false, precision = 0)
  public BigInteger exponent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(
        name = "modulus",
        referencedColumnName = "modulus",
        nullable = false,
        insertable = false,
        updatable = false),
    @JoinColumn(
        name = "exponent",
        referencedColumnName = "exponent",
        nullable = false,
        insertable = false,
        updatable = false)
  })
  public RsaKey rsaKey;
}
