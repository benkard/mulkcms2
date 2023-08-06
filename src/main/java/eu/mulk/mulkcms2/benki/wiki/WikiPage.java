package eu.mulk.mulkcms2.benki.wiki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "wiki_pages", schema = "benki")
public class WikiPage extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer id;

  @OneToMany(mappedBy = "page", fetch = FetchType.LAZY)
  @OrderBy("date desc")
  public List<WikiPageRevision> revisions;
}
