package eu.mulk.mulkcms2.benki.wiki;

import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wiki_page_revisions", schema = "benki")
public class WikiPageRevision extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "date", nullable = true)
  public OffsetDateTime date;

  @Column(name = "title", nullable = true, length = -1)
  public String title;

  @Column(name = "content", nullable = true, length = -1)
  public String content;

  @Column(name = "format", nullable = true, length = -1)
  public String format;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "page", referencedColumnName = "id", nullable = false)
  public WikiPage page;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author", referencedColumnName = "id")
  public User author;
}
