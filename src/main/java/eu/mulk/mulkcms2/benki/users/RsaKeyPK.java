package eu.mulk.mulkcms2.benki.users;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Id;

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

    if (modulus != null ? !modulus.equals(rsaKeyPK.modulus) : rsaKeyPK.modulus != null) {
      return false;
    }
    if (exponent != null ? !exponent.equals(rsaKeyPK.exponent) : rsaKeyPK.exponent != null) {
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
}
