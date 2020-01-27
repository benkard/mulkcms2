package eu.mulk.mulkcms2.benki.wiki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

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
