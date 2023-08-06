package eu.mulk.mulkcms2.benki.newsletter;

import eu.mulk.mulkcms2.benki.posts.Post;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Collection;

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
  public Collection<Post> posts;
}
