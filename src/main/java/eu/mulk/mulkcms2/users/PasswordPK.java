package eu.mulk.mulkcms2.users;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class PasswordPK implements Serializable {

  private int userId;
  private String password;

  @Column(name = "user", nullable = false)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "password", nullable = false, length = -1)
  @Id
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
    PasswordPK that = (PasswordPK) o;
    return userId == that.userId && Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, password);
  }
}
