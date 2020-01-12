package eu.mulk.mulkcms2.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class UserSettingPK implements Serializable {

  private int userId;
  private String setting;

  @Column(name = "user", nullable = false)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "setting", nullable = false, length = -1)
  @Id
  public String getSetting() {
    return setting;
  }

  public void setSetting(String setting) {
    this.setting = setting;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSettingPK that = (UserSettingPK) o;
    return userId == that.userId &&
        Objects.equals(setting, that.setting);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, setting);
  }
}
