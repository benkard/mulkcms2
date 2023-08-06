package eu.mulk.mulkcms2.benki.posts;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PostTextPK implements Serializable {

  @Id
  @Column(name = "post", nullable = false)
  private Integer postId;

  @Id
  @Column(name = "language", nullable = false, length = -1)
  private String language;

  public Integer getPostId() {
    return postId;
  }

  public void setPostId(Integer postId) {
    this.postId = postId;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PostTextPK)) {
      return false;
    }
    PostTextPK that = (PostTextPK) o;
    return Objects.equals(getPostId(), that.getPostId())
        && getLanguage().equals(that.getLanguage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPostId(), getLanguage());
  }
}
