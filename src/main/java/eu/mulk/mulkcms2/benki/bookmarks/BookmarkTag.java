package eu.mulk.mulkcms2.benki.bookmarks;

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
@Table(name = "bookmark_tags", schema = "benki")
@IdClass(BookmarkTagPK.class)
public class BookmarkTag extends PanacheEntityBase {

  @Id
  @Column(name = "bookmark", nullable = false)
  public int bookmarkId;

  @Id
  @Column(name = "tag", nullable = false, length = -1)
  public String tag;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bookmark", referencedColumnName = "id", nullable = false)
  public Bookmark bookmark;
}
