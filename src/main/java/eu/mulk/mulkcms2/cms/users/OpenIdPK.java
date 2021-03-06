package eu.mulk.mulkcms2.cms.users;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class OpenIdPK implements Serializable {

  private int userId;
  private String openid;

  @Column(name = "user", nullable = false)
  @Id
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Column(name = "openid", nullable = false, length = -1)
  @Id
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
    OpenIdPK openIdPK = (OpenIdPK) o;
    return userId == openIdPK.userId && Objects.equals(openid, openIdPK.openid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, openid);
  }
}
