package eu.mulk.mulkcms2.benki.newsletter;

import eu.mulk.mulkcms2.benki.posts.Post;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.OffsetDateTime;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "newsletters", schema = "benki")
public class Newsletter extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "date", nullable = false)
  public OffsetDateTime date = OffsetDateTime.now();

  @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
  @OrderBy("date")
  public Collection<Post<?>> posts;
}
