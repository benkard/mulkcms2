package eu.mulk.mulkcms2.cms.users;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "passwords", schema = "public")
@IdClass(PasswordPK.class)
public class Password extends PanacheEntityBase {

  @Id
  @Column(name = "user", nullable = false)
  public int userId;

  @Id
  @Column(name = "password", nullable = false, length = -1)
  public String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user",
      referencedColumnName = "id",
      nullable = false,
      insertable = false,
      updatable = false)
  public User user;
}
