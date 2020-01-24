package eu.mulk.mulkcms2.benki;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_rsa_keys", schema = "public", catalog = "benki")
@IdClass(UserRsaKeyPK.class)
public class UserRsaKey {

  private int userId;
  private BigInteger modulus;
  private BigInteger exponent;
  private User user;
  private RsaKey rsaKey;

  @Id
  @Column(name = "user", nullable = false)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

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

    UserRsaKey that = (UserRsaKey) o;

    if (userId != that.userId) {
      return false;
    }
    if (modulus != null ? !modulus.equals(that.modulus) : that.modulus != null) {
      return false;
    }
    if (exponent != null ? !exponent.equals(that.exponent) : that.exponent != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + (modulus != null ? modulus.hashCode() : 0);
    result = 31 * result + (exponent != null ? exponent.hashCode() : 0);
    return result;
  }

  @ManyToOne
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @ManyToOne
  @JoinColumns({
      @JoinColumn(name = "modulus", referencedColumnName = "modulus", nullable = false),
      @JoinColumn(name = "exponent", referencedColumnName = "exponent", nullable = false)})
  public RsaKey getRsaKey() {
    return rsaKey;
  }

  public void setRsaKey(RsaKey rsaKey) {
    this.rsaKey = rsaKey;
  }
}
