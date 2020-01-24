package eu.mulk.mulkcms2.benki.generic;

import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "posts", schema = "public", catalog = "benki")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Post extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "date", nullable = true)
  public OffsetDateTime date;

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id")
  public User owner;
}
