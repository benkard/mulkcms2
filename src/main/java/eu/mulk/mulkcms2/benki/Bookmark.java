package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "bookmarks", schema = "public", catalog = "benki")
public class Bookmark extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "date", nullable = true)
  public Object date;

  @Column(name = "uri", nullable = false, length = -1)
  public String uri;

  @Column(name = "title", nullable = true, length = -1)
  public String title;

  @Column(name = "description", nullable = true, length = -1)
  public String description;

  @OneToMany(mappedBy = "bookmark")
  public Collection<BookmarkTag> tags;

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id")
  public User owner;
}
