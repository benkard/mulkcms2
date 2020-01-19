package eu.mulk.mulkcms2.journal;

import eu.mulk.mulkcms2.pages.Article;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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

  @Id
  @Column(name = "journal", nullable = false)
  public int journalId;

  @Id
  @Column(name = "index", nullable = false)
  public int index;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "journal",
      referencedColumnName = "id",
      nullable = false,
      insertable = false,
      updatable = false)
  public Journal journal;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
  public Article article;
}
