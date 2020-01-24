package eu.mulk.mulkcms2.benki.bookmarx;

import eu.mulk.mulkcms2.benki.generic.Post;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "bookmarks", schema = "benki")
public class Bookmark extends Post {

  @Column(name = "uri", nullable = false, length = -1)
  public String uri;

  @Column(name = "title", nullable = true, length = -1)
  public String title;

  @Column(name = "description", nullable = true, length = -1)
  public String description;

  @OneToMany(mappedBy = "bookmark", fetch = FetchType.LAZY)
  public Collection<BookmarkTag> tags;
}
