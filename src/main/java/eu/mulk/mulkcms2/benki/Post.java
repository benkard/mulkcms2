package eu.mulk.mulkcms2.benki;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "posts", schema = "public", catalog = "benki")
public class Post {

  private int id;
  private Object date;
  private User owner;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "date", nullable = true)
  public Object getDate() {
    return date;
  }

  public void setDate(Object date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Post post = (Post) o;

    if (id != post.id) {
      return false;
    }
    if (date != null ? !date.equals(post.date) : post.date != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    return result;
  }

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id")
  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }
}
