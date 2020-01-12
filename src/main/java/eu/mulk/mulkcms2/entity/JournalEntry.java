package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "journal_entries", schema = "public", catalog = "mulkcms")
@IdClass(JournalEntryPK.class)
public class JournalEntry extends PanacheEntityBase {

  private int journalId;
  private int index;
  private Journal journal;
  private Article article;

  @Id
  @Column(name = "journal", nullable = false)
  public int getJournalId() {
    return journalId;
  }

  public void setJournalId(int journalId) {
    this.journalId = journalId;
  }

  @Id
  @Column(name = "index", nullable = false)
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
    JournalEntry that = (JournalEntry) o;
    return journalId == that.journalId &&
        index == that.index;
  }

  @Override
  public int hashCode() {
    return Objects.hash(journalId, index);
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "journal", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  public Journal getJournal() {
    return journal;
  }

  public void setJournal(Journal journal) {
    this.journal = journal;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }
}
