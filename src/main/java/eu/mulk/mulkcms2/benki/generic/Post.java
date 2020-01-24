package eu.mulk.mulkcms2.benki.generic;

import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.OffsetDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "posts", schema = "benki")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Post extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "date", nullable = true)
  public OffsetDateTime date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner", referencedColumnName = "id")
  public User owner;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_visible_posts",
      joinColumns = @JoinColumn(name = "message"),
      inverseJoinColumns = @JoinColumn(name = "user"))
  public Set<User> visibleTo;
}
