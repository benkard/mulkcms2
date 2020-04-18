package eu.mulk.mulkcms2.cms.journal;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.annotation.CheckForNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "journals", schema = "public")
public class Journal extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "path_prefix", nullable = true, length = -1)
  @CheckForNull
  public String pathPrefix;

  @OneToMany(mappedBy = "journal", fetch = FetchType.LAZY)
  public Collection<JournalEntry> entries;
}
