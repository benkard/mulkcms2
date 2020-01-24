package eu.mulk.mulkcms2.benki.users;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_email_addresses", schema = "public", catalog = "benki")
public class UserEmailAddress extends PanacheEntityBase {

  @Id
  @Column(name = "email", nullable = false, length = -1)
  public String email;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
  public User user;
}
