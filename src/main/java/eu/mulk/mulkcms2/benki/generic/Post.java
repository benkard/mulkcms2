package eu.mulk.mulkcms2.benki.generic;

import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.OffsetDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "posts", schema = "benki")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Post extends PanacheEntityBase {

  @Id
  @SequenceGenerator(
      allocationSize = 1,
      sequenceName = "posts_id_seq",
      name = "posts_id_seq",
      schema = "benki")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_id_seq")
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "date", nullable = true)
  public OffsetDateTime date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner", referencedColumnName = "id")
  public User owner;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_visible_posts",
      schema = "benki",
      joinColumns = @JoinColumn(name = "message"),
      inverseJoinColumns = @JoinColumn(name = "user"))
  public Set<User> visibleTo;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "post_targets",
      schema = "benki",
      joinColumns = @JoinColumn(name = "message"),
      inverseJoinColumns = @JoinColumn(name = "target"))
  public Set<Role> targets;
}
