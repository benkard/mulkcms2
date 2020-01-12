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
@Table(name = "journal_comment", schema = "public", catalog = "mulkcms")
public class LegacyJournalComment extends PanacheEntityBase {

  private int id;
  private String uuid;
  private long date;
  private String body;
  private String author;
  private String email;
  private String website;
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
  @Column(name = "body", nullable = false, length = -1)
  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  @Basic
  @Column(name = "author", nullable = true, length = -1)
  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Basic
  @Column(name = "email", nullable = true, length = -1)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "website", nullable = true, length = -1)
  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
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
    LegacyJournalComment that = (LegacyJournalComment) o;
    return id == that.id &&
        date == that.date &&
        Objects.equals(uuid, that.uuid) &&
        Objects.equals(body, that.body) &&
        Objects.equals(author, that.author) &&
        Objects.equals(email, that.email) &&
        Objects.equals(website, that.website) &&
        Objects.equals(spamP, that.spamP) &&
        Objects.equals(submitterIp, that.submitterIp) &&
        Objects.equals(submitterUserAgent, that.submitterUserAgent);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, uuid, date, body, author, email, website, spamP, submitterIp, submitterUserAgent);
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
