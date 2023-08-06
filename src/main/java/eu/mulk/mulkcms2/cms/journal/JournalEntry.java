package eu.mulk.mulkcms2.cms.journal;

import eu.mulk.mulkcms2.cms.pages.Article;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "journal_entries", schema = "public")
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
