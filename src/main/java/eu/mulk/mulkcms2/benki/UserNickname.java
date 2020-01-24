package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_nicknames", schema = "public", catalog = "benki")
public class UserNickname extends PanacheEntityBase {

  @Id
  @Column(name = "nickname", nullable = false, length = -1)
  public String nickname;

  @ManyToOne
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User user;
}
