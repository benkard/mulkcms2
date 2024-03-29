package eu.mulk.mulkcms2.cms.legacyjournal;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "journal_comment", schema = "public")
public class LegacyJournalComment extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "uuid", nullable = false, length = 36)
  public String uuid;

  @Column(name = "date", nullable = false)
  public long date;

  @Column(name = "body", nullable = false, length = -1)
  public String body;

  @Column(name = "author", nullable = true, length = -1)
  @CheckForNull
  public String author;

  @Column(name = "email", nullable = true, length = -1)
  @CheckForNull
  public String email;

  @Column(name = "website", nullable = true, length = -1)
  @CheckForNull
  public String website;

  @Column(name = "spam_p", nullable = true)
  @CheckForNull
  public Boolean spamP;

  @Column(name = "submitter_ip", nullable = false, length = -1)
  public String submitterIp;

  @Column(name = "submitter_user_agent", nullable = false, length = -1)
  public String submitterUserAgent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "entry_id", referencedColumnName = "id", nullable = false)
  public LegacyJournalEntry journalEntry;
}
