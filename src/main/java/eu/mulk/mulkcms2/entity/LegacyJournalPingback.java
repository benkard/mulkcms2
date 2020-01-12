package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "journal_pingback", schema = "public", catalog = "mulkcms")
public class LegacyJournalPingback extends PanacheEntityBase {

  private int id;
  private String uuid;
  private long date;
  private String url;
  private Boolean spamP;
  private String submitterIp;
  private String submitterUserAgent;
  private LegacyJournalEntry journalEntry;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "uuid", nullable = false, length = 36)
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Basic
  @Column(name = "date", nullable = false)
  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  @Basic
  @Column(name = "url", nullable = true, length = -1)
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Basic
  @Column(name = "spam_p", nullable = true)
  public Boolean getSpamP() {
    return spamP;
  }

  public void setSpamP(Boolean spamP) {
    this.spamP = spamP;
  }

  @Basic
  @Column(name = "submitter_ip", nullable = false, length = -1)
  public String getSubmitterIp() {
    return submitterIp;
  }

  public void setSubmitterIp(String submitterIp) {
    this.submitterIp = submitterIp;
  }

  @Basic
  @Column(name = "submitter_user_agent", nullable = false, length = -1)
  public String getSubmitterUserAgent() {
    return submitterUserAgent;
  }

  public void setSubmitterUserAgent(String submitterUserAgent) {
    this.submitterUserAgent = submitterUserAgent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LegacyJournalPingback that = (LegacyJournalPingback) o;
    return id == that.id &&
        date == that.date &&
        Objects.equals(uuid, that.uuid) &&
        Objects.equals(url, that.url) &&
        Objects.equals(spamP, that.spamP) &&
        Objects.equals(submitterIp, that.submitterIp) &&
        Objects.equals(submitterUserAgent, that.submitterUserAgent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uuid, date, url, spamP, submitterIp, submitterUserAgent);
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "entry_id", referencedColumnName = "id", nullable = false)
  public LegacyJournalEntry getJournalEntry() {
    return journalEntry;
  }

  public void setJournalEntry(LegacyJournalEntry journalEntry) {
    this.journalEntry = journalEntry;
  }
}