package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "passwords", schema = "public", catalog = "mulkcms")
@IdClass(PasswordPK.class)
public class Password extends PanacheEntityBase {

  private int userId;
  private String password;
  private User user;

  @Id
  @Column(name = "user", nullable = false)
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Id
  @Column(name = "password", nullable = false, length = -1)
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Password password1 = (Password) o;
    return userId == password1.userId &&
        Objects.equals(password, password1.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, password);
  }

  @ManyToOne
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
