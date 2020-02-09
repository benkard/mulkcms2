package eu.mulk.mulkcms2.benki.lazychat;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

public class LazychatReferencePK implements Serializable {

  private int referrerId;
  private int refereeId;

  @Column(name = "referrer", nullable = false)
  @Id
  public int getReferrerId() {
    return referrerId;
  }

  public void setReferrerId(int referrerId) {
    this.referrerId = referrerId;
  }

  @Column(name = "referee", nullable = false)
  @Id
  public int getRefereeId() {
    return refereeId;
  }

  public void setRefereeId(int refereeId) {
    this.refereeId = refereeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LazychatReferencePK that = (LazychatReferencePK) o;

    if (referrerId != that.referrerId) {
      return false;
    }
    if (refereeId != that.refereeId) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = referrerId;
    result = 31 * result + refereeId;
    return result;
  }
}
