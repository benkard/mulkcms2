package eu.mulk.mulkcms2.benki.bookmarks;

import eu.mulk.mulkcms2.benki.generic.Post;
import eu.mulk.mulkcms2.common.markdown.MarkdownConverter;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "bookmarks", schema = "benki")
public class Bookmark extends Post {

  @Column(name = "uri", nullable = false, length = -1)
  public String uri;

  @Column(name = "title", nullable = true, length = -1)
  public String title;

  @Column(name = "description", nullable = true, length = -1)
  public String description;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "bookmark_tags",
      schema = "benki",
      joinColumns = @JoinColumn(name = "bookmark"))
  @Column(name = "tag")
  public Set<String> tags;

  @Transient
  public String getDescriptionHtml() {
    return new MarkdownConverter().htmlify(description);
  }
}
