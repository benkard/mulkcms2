package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wiki_page_revisions", schema = "public", catalog = "benki")
public class WikiPageRevision extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "date", nullable = true)
  public Object date;

  @Column(name = "title", nullable = true, length = -1)
  public String title;

  @Column(name = "content", nullable = true, length = -1)
  public String content;

  @Column(name = "format", nullable = true, length = -1)
  public String format;

  @ManyToOne
  @JoinColumn(name = "page", referencedColumnName = "id", nullable = false)
  public WikiPage page;

  @ManyToOne
  @JoinColumn(name = "author", referencedColumnName = "id")
  public User author;
}
