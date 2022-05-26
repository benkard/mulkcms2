package eu.mulk.mulkcms2.benki.posts;

import com.vladmihalcea.hibernate.type.search.PostgreSQLTSVectorType;
import eu.mulk.mulkcms2.benki.posts.Post.Scope;
import eu.mulk.mulkcms2.common.markdown.MarkdownConverter;
import eu.mulk.mulkcms2.common.markdown.MarkdownConverter.Mode;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(name = "post_texts", schema = "benki")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(PostTextPK.class)
@TypeDef(name = "tsvector", typeClass = PostgreSQLTSVectorType.class)
public abstract class PostText<OwningPost extends Post<?>> extends PanacheEntityBase {

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

  @Column(name = "search_terms")
  @Generated(GenerationTime.ALWAYS)
  @Type(type = "tsvector")
  public String searchTerms;

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
  protected abstract String getDescriptionMarkup();

  @CheckForNull
  private String computeDescriptionHtml() {
    var markup = getDescriptionMarkup();
    if (markup == null) {
      return null;
    }
    return new MarkdownConverter(post.scope == Scope.top_level ? Mode.POST : Mode.COMMENT)
        .htmlify(markup);
  }
}
