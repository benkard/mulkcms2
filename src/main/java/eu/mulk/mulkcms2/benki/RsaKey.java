package eu.mulk.mulkcms2.benki;

import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "rsa_keys", schema = "public", catalog = "benki")
@IdClass(RsaKeyPK.class)
public class RsaKey {

  private BigInteger modulus;
  private BigInteger exponent;
  private Collection<UserRsaKey> users;

  @Id
  @Column(name = "modulus", nullable = false, precision = 0)
  public BigInteger getModulus() {
    return modulus;
  }

  public void setModulus(BigInteger modulus) {
    this.modulus = modulus;
  }

  @Id
  @Column(name = "exponent", nullable = false, precision = 0)
  public BigInteger getExponent() {
    return exponent;
  }

  public void setExponent(BigInteger exponent) {
    this.exponent = exponent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RsaKey rsaKey = (RsaKey) o;

    if (modulus != null ? !modulus.equals(rsaKey.modulus) : rsaKey.modulus != null) {
      return false;
    }
    if (exponent != null ? !exponent.equals(rsaKey.exponent) : rsaKey.exponent != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = modulus != null ? modulus.hashCode() : 0;
    result = 31 * result + (exponent != null ? exponent.hashCode() : 0);
    return result;
  }

  @OneToMany(mappedBy = "rsaKey")
  public Collection<UserRsaKey> getUsers() {
    return users;
  }

  public void setUsers(Collection<UserRsaKey> users) {
    this.users = users;
  }
}
