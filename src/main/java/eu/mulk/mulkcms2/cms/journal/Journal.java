package eu.mulk.mulkcms2.cms.journal;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import javax.annotation.CheckForNull;

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
