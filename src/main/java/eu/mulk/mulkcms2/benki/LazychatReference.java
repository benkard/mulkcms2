package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lazychat_references", schema = "public", catalog = "benki")
@IdClass(LazychatReferencePK.class)
public class LazychatReference {

  private int referrerId;
  private int refereeId;
  private LazychatMessage referrer;

  @Id
  @Column(name = "referrer", nullable = false)
  public int getReferrerId() {
    return referrerId;
  }

  public void setReferrerId(int referrerId) {
    this.referrerId = referrerId;
  }

  @Id
  @Column(name = "referee", nullable = false)
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

    LazychatReference that = (LazychatReference) o;

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

  @ManyToOne
  @JoinColumn(name = "referrer", referencedColumnName = "id", nullable = false)
  public LazychatMessage getReferrer() {
    return referrer;
  }

  public void setReferrer(LazychatMessage referrer) {
    this.referrer = referrer;
  }
}
