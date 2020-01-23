package eu.mulk.mulkcms2.legacyjournal;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "journal_category", schema = "public", catalog = "mulkcms")
public class LegacyJournalCategory extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "uuid", nullable = false, length = 36)
  public String uuid;
}