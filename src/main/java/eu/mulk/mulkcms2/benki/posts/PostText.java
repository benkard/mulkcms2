package eu.mulk.mulkcms2.benki.posts;

import javax.annotation.CheckForNull;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "post_texts", schema = "benki")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(PostTextPK.class)
public abstract class PostText<OwningPost extends Post<?>> {

  private static final int DESCRIPTION_CACHE_VERSION = 1;

  @Id
  @Column(name = "post", nullable = false, insertable = false, updatable = false)
  public int postId;

  @Id
  @Column(name = "language", nullable = false, length = -1)
  public String language;

  @Column(name = "cached_description_version", nullable = true)
  @CheckForNull
  public Integer cachedDescriptionVersion;

  @Column(name = "cached_description_html", nullable = true)
  @CheckForNull
  public String cachedDescriptionHtml;

  @ManyToOne(fetch = FetchType.LAZY, targetEntity = Post.class)
  @JoinColumn(name = "post", referencedColumnName = "id", nullable = false)
  @JsonbTransient
  public OwningPost post;

  @CheckForNull
  public final String getDescriptionHtml() {
    if (cachedDescriptionHtml != null
        && cachedDescriptionVersion != null
        && cachedDescriptionVersion >= DESCRIPTION_CACHE_VERSION) {
      return cachedDescriptionHtml;
    } else {
      @CheckForNull var descriptionHtml = computeDescriptionHtml();
      cachedDescriptionHtml = descriptionHtml;
      cachedDescriptionVersion = DESCRIPTION_CACHE_VERSION;
      return descriptionHtml;
    }
  }

  @CheckForNull
  protected abstract String computeDescriptionHtml();
}
