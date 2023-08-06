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
@Table(name = "journal_trackback", schema = "public")
public class LegacyJournalTrackback extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "uuid", nullable = false, length = 36)
  public String uuid;

  @Column(name = "date", nullable = false)
  public long date;

  @Column(name = "excerpt", nullable = false, length = -1)
  public String excerpt;

  @Column(name = "title", nullable = true, length = -1)
  @CheckForNull
  public String title;

  @Column(name = "blog_name", nullable = true, length = -1)
  @CheckForNull
  public String blogName;

  @Column(name = "url", nullable = true, length = -1)
  @CheckForNull
  public String url;

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
