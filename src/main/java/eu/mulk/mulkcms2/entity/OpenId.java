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
@Table(name = "openids", schema = "public", catalog = "mulkcms")
@IdClass(OpenIdPK.class)
public class OpenId extends PanacheEntityBase {

  private int userId;
  private String openid;
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
  @Column(name = "openid", nullable = false, length = -1)
  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OpenId openId = (OpenId) o;
    return userId == openId.userId &&
        Objects.equals(openid, openId.openid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, openid);
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
