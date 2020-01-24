package eu.mulk.mulkcms2.cms.legacyjournal;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "journal_entry", schema = "public")
public class LegacyJournalEntry extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "uuid", nullable = false, length = 36)
  public String uuid;

  @Column(name = "title", nullable = false, length = -1)
  public String title;

  @Column(name = "date", nullable = false)
  public long date;

  @Column(name = "last_modification", nullable = true)
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
