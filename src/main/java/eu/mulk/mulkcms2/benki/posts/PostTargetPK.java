package eu.mulk.mulkcms2.benki.posts;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;

public class PostTargetPK implements Serializable {

  @Column(name = "message", nullable = false)
  private int message;

  @Column(name = "target", nullable = false)
  private int targetId;

  @Column(name = "message", nullable = false)
  @Id
  public int getMessage() {
    return message;
  }

  public void setMessage(int message) {
    this.message = message;
  }

  @Column(name = "target", nullable = false)
  @Id
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

    PostTargetPK that = (PostTargetPK) o;

    if (message != that.message) {
      return false;
    }
    return targetId == that.targetId;
  }

  @Override
  public int hashCode() {
    int result = message;
    result = 31 * result + targetId;
    return result;
  }
}
