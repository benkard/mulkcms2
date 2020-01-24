package eu.mulk.mulkcms2.cms.journal;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "journals", schema = "public")
public class Journal extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "path_prefix", nullable = true, length = -1)
  public String pathPrefix;

  @OneToMany(mappedBy = "journal", fetch = FetchType.LAZY)
  public Collection<JournalEntry> entries;
}
