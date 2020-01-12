package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_settings", schema = "public", catalog = "mulkcms")
@IdClass(UserSettingPK.class)
public class UserSetting extends PanacheEntityBase {

  private int userId;
  private String setting;
  private String value;
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
  @Column(name = "setting", nullable = false, length = -1)
  public String getSetting() {
    return setting;
  }

  public void setSetting(String setting) {
    this.setting = setting;
  }

  @Basic
  @Column(name = "value", nullable = true, length = -1)
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserSetting that = (UserSetting) o;
    return userId == that.userId &&
        Objects.equals(setting, that.setting) &&
        Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, setting, value);
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
