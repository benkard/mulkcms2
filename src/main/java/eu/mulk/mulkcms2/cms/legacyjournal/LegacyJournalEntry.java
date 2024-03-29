package eu.mulk.mulkcms2.cms.legacyjournal;

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
@Table(name = "journal_entry", schema = "public")
public class LegacyJournalEntry extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "uuid", nullable = false, length = 36)
  public String uuid;

  @Column(name = "title", nullable = false, length = -1)
  public String title;

  @Column(name = "date", nullable = false)
  public long date;

  @Column(name = "last_modification", nullable = true)
  @CheckForNull
  public Long lastModification;

  @Column(name = "body", nullable = false, length = -1)
  public String body;

  @Column(name = "type", nullable = false, length = -1)
  public String type;

  @OneToMany(mappedBy = "journalEntry", fetch = FetchType.LAZY)
  public Collection<LegacyJournalComment> comments;

  @OneToMany(mappedBy = "journalEntry", fetch = FetchType.LAZY)
  public Collection<LegacyJournalPingback> pingbacks;

  @OneToMany(mappedBy = "journalEntry", fetch = FetchType.LAZY)
  public Collection<LegacyJournalTrackback> trackbacks;
}
