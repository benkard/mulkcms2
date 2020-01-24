package eu.mulk.mulkcms2.benki.wiki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "wiki_pages", schema = "benki")
public class WikiPage extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @OneToMany(mappedBy = "page", fetch = FetchType.LAZY)
  public Collection<WikiPageRevision> revisions;
}
