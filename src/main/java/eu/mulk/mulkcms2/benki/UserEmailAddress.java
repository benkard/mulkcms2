package eu.mulk.mulkcms2.benki;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_email_addresses", schema = "public", catalog = "benki")
public class UserEmailAddress {

  private String email;
  private User user;

  @Id
  @Column(name = "email", nullable = false, length = -1)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserEmailAddress that = (UserEmailAddress) o;

    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return email != null ? email.hashCode() : 0;
  }

  @ManyToOne
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
