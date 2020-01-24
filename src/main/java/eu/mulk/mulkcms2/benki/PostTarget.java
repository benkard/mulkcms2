package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "post_targets", schema = "public", catalog = "benki")
@IdClass(PostTargetPK.class)
public class PostTarget {

  private int message;
  private int targetId;
  private Role target;

  @Id
  @Column(name = "message", nullable = false)
  public int getMessage() {
    return message;
  }

  public void setMessage(int message) {
    this.message = message;
  }

  @Id
  @Column(name = "target", nullable = false)
  public int getTargetId() {
    return targetId;
  }

  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PostTarget that = (PostTarget) o;

    if (message != that.message) {
      return false;
    }
    if (targetId != that.targetId) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = message;
    result = 31 * result + targetId;
    return result;
  }

  @ManyToOne
  @JoinColumn(name = "target", referencedColumnName = "id", nullable = false)
  public Role getTarget() {
    return target;
  }

  public void setTarget(Role target) {
    this.target = target;
  }
}
