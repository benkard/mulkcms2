package eu.mulk.mulkcms2.cms.users;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class LoginCertificatePK implements Serializable {

  private int userId;
  private byte[] certificate;

  @Column(name = "user", nullable = false)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "certificate", nullable = false)
  @Id
  public byte[] getCertificate() {
    return certificate;
  }

  public void setCertificate(byte[] certificate) {
    this.certificate = certificate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginCertificatePK that = (LoginCertificatePK) o;
    return userId == that.userId && Arrays.equals(certificate, that.certificate);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(userId);
    result = 31 * result + Arrays.hashCode(certificate);
    return result;
  }
}
