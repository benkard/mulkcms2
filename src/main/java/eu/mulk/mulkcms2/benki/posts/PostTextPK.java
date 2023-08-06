package eu.mulk.mulkcms2.benki.posts;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@IdClass(PostTextPK.class)
public class PostTextPK implements Serializable {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post", referencedColumnName = "id", nullable = false)
  public Post<?> post;

  @Id
  @Column(name = "language", nullable = false, length = -1)
  private String language;

  public Post<?> getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
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
    return Objects.equals(getPost(), that.getPost()) && getLanguage().equals(that.getLanguage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPost(), getLanguage());
  }
}
