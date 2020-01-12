package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "journal_trackback", schema = "public", catalog = "mulkcms")
public class LegacyJournalTrackback extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "uuid", nullable = false, length = 36)
  public String uuid;

  @Column(name = "date", nullable = false)
  public long date;

  @Column(name = "excerpt", nullable = false, length = -1)
  public String excerpt;

  @Column(name = "title", nullable = true, length = -1)
  public String title;

  @Column(name = "blog_name", nullable = true, length = -1)
  public String blogName;

  @Column(name = "url", nullable = true, length = -1)
  public String url;

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
