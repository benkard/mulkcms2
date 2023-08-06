package eu.mulk.mulkcms2.cms.users;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
