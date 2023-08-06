package eu.mulk.mulkcms2.benki.users;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigInteger;

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
