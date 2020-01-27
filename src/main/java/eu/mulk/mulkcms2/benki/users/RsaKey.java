package eu.mulk.mulkcms2.benki.users;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rsa_keys", schema = "benki")
@IdClass(RsaKeyPK.class)
public class RsaKey extends PanacheEntityBase {

  @Id
  @Column(name = "modulus", nullable = false, precision = 0)
  public BigInteger modulus;

  @Id
  @Column(name = "exponent", nullable = false, precision = 0)
  public BigInteger exponent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_rsa_keys",
      schema = "benki",
      joinColumns = {@JoinColumn(name = "modulus"), @JoinColumn(name = "exponent")},
      inverseJoinColumns = @JoinColumn(name = "user"))
  public User user;
}
