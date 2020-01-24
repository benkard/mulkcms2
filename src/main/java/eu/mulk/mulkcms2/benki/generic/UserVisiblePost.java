package eu.mulk.mulkcms2.benki.generic;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "user_visible_posts", schema = "public", catalog = "benki")
public class UserVisiblePost extends PanacheEntityBase implements Serializable {

  @Id
  @Column(name = "user", nullable = true)
  public Integer userId;

  @Id
  @Column(name = "message", nullable = true)
  public Integer messageId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserVisiblePost)) {
      return false;
    }
    UserVisiblePost that = (UserVisiblePost) o;
    return Objects.equals(userId, that.userId) && Objects.equals(messageId, that.messageId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, messageId);
  }
}
