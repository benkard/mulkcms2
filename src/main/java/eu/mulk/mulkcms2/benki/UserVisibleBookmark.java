package eu.mulk.mulkcms2.benki;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_visible_bookmarks", schema = "public", catalog = "benki")
public class UserVisibleBookmark {

  private Integer userId;
  private Integer messageId;

  @Basic
  @Column(name = "user", nullable = true)
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "message", nullable = true)
  public Integer getMessageId() {
    return messageId;
  }

  public void setMessageId(Integer messageId) {
    this.messageId = messageId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserVisibleBookmark that = (UserVisibleBookmark) o;

    if (userId != null ? !userId.equals(that.userId) : that.userId != null) {
      return false;
    }
    if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = userId != null ? userId.hashCode() : 0;
    result = 31 * result + (messageId != null ? messageId.hashCode() : 0);
    return result;
  }
}
