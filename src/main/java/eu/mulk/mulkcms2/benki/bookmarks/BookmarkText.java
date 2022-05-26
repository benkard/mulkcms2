package eu.mulk.mulkcms2.benki.bookmarks;

import eu.mulk.mulkcms2.benki.posts.PostText;
import javax.annotation.CheckForNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "bookmark_texts", schema = "benki")
public class BookmarkText extends PostText<Bookmark> {

  @Column(name = "title", nullable = true, length = -1)
  @CheckForNull
  public String title;

  @Column(name = "description", nullable = true, length = -1)
  @CheckForNull
  public String description;

  @Transient
  @CheckForNull
  protected String getDescriptionMarkup() {
    return description;
  }
}
