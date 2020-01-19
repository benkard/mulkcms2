package eu.mulk.mulkcms2.legacyjournal;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "journal_comment", schema = "public", catalog = "mulkcms")
public class LegacyJournalComment extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "uuid", nullable = false, length = 36)
  public String uuid;

  @Column(name = "date", nullable = false)
  public long date;

  @Column(name = "body", nullable = false, length = -1)
  public String body;

  @Column(name = "author", nullable = true, length = -1)
  public String author;

  @Column(name = "email", nullable = true, length = -1)
  public String email;

  @Column(name = "website", nullable = true, length = -1)
  public String website;

  @Column(name = "spam_p", nullable = true)
  public Boolean spamP;

  @Column(name = "submitter_ip", nullable = false, length = -1)
  public String submitterIp;

  @Column(name = "submitter_user_agent", nullable = false, length = -1)
  public String submitterUserAgent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "entry_id", referencedColumnName = "id", nullable = false)
  public LegacyJournalEntry journalEntry;
}
