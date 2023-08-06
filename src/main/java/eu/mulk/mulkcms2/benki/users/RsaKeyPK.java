package eu.mulk.mulkcms2.benki.users;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public class RsaKeyPK implements Serializable {

  private BigInteger modulus;
  private BigInteger exponent;

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

    RsaKeyPK rsaKeyPK = (RsaKeyPK) o;

    if (!Objects.equals(modulus, rsaKeyPK.modulus)) {
      return false;
    }
    return Objects.equals(exponent, rsaKeyPK.exponent);
  }

  @Override
  public int hashCode() {
    int result = modulus != null ? modulus.hashCode() : 0;
    result = 31 * result + (exponent != null ? exponent.hashCode() : 0);
    return result;
  }
}
