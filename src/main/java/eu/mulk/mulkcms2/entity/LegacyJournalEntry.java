package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "journal_entry", schema = "public", catalog = "mulkcms")
public class LegacyJournalEntry extends PanacheEntityBase {

  private int id;
  private String uuid;
  private String title;
  private long date;
  private Long lastModification;
  private String body;
  private String type;
  private Collection<LegacyJournalComment> comments;
  private Collection<LegacyJournalPingback> pingbacks;
  private Collection<LegacyJournalTrackback> trackbacks;

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
  @Column(name = "title", nullable = false, length = -1)
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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
  @Column(name = "last_modification", nullable = true)
  public Long getLastModification() {
    return lastModification;
  }

  public void setLastModification(Long lastModification) {
    this.lastModification = lastModification;
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
  @Column(name = "type", nullable = false, length = -1)
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LegacyJournalEntry that = (LegacyJournalEntry) o;
    return id == that.id &&
        date == that.date &&
        Objects.equals(uuid, that.uuid) &&
        Objects.equals(title, that.title) &&
        Objects.equals(lastModification, that.lastModification) &&
        Objects.equals(body, that.body) &&
        Objects.equals(type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uuid, title, date, lastModification, body, type);
  }

  @OneToMany(mappedBy = "journalEntry")
  public Collection<LegacyJournalComment> getComments() {
    return comments;
  }

  public void setComments(Collection<LegacyJournalComment> comments) {
    this.comments = comments;
  }

  @OneToMany(mappedBy = "journalEntry")
  public Collection<LegacyJournalPingback> getPingbacks() {
    return pingbacks;
  }

  public void setPingbacks(Collection<LegacyJournalPingback> pingbacks) {
    this.pingbacks = pingbacks;
  }

  @OneToMany(mappedBy = "journalEntry")
  public Collection<LegacyJournalTrackback> getTrackbacks() {
    return trackbacks;
  }

  public void setTrackbacks(Collection<LegacyJournalTrackback> trackbacks) {
    this.trackbacks = trackbacks;
  }
}
