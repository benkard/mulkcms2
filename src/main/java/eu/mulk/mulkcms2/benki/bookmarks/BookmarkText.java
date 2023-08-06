package eu.mulk.mulkcms2.benki.bookmarks;

import eu.mulk.mulkcms2.benki.posts.PostText;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import javax.annotation.CheckForNull;

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
