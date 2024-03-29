package eu.mulk.mulkcms2.benki.users;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public class UserRsaKeyPK implements Serializable {

  private int userId;
  private BigInteger modulus;
  private BigInteger exponent;

  @Column(name = "user", nullable = false)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "modulus", nullable = false, precision = 0)
  @Id
  public BigInteger getModulus() {
    return modulus;
  }

  public void setModulus(BigInteger modulus) {
    this.modulus = modulus;
  }

  @Column(name = "exponent", nullable = false, precision = 0)
  @Id
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

    UserRsaKeyPK that = (UserRsaKeyPK) o;

    if (userId != that.userId) {
      return false;
    }
    if (!Objects.equals(modulus, that.modulus)) {
      return false;
    }
    return Objects.equals(exponent, that.exponent);
  }

  @Override
  public int hashCode() {
    int result = userId;
    result = 31 * result + (modulus != null ? modulus.hashCode() : 0);
    result = 31 * result + (exponent != null ? exponent.hashCode() : 0);
    return result;
  }
}
