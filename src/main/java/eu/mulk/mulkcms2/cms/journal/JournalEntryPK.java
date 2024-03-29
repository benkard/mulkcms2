package eu.mulk.mulkcms2.cms.journal;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class JournalEntryPK implements Serializable {

  private int journalId;
  private int index;

  @Column(name = "journal", nullable = false)
  @Id
  public int getJournalId() {
    return journalId;
  }

  public void setJournalId(int journalId) {
    this.journalId = journalId;
  }

  @Column(name = "index", nullable = false)
  @Id
  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JournalEntryPK that = (JournalEntryPK) o;
    return journalId == that.journalId && index == that.index;
  }

  @Override
  public int hashCode() {
    return Objects.hash(journalId, index);
  }
}
