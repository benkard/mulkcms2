package eu.mulk.mulkcms2.benki.posts;

import static org.hibernate.generator.EventType.INSERT;
import static org.hibernate.generator.EventType.UPDATE;

import eu.mulk.mulkcms2.benki.posts.Post.Scope;
import eu.mulk.mulkcms2.common.markdown.MarkdownConverter;
import eu.mulk.mulkcms2.common.markdown.MarkdownConverter.Mode;
import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import javax.annotation.CheckForNull;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "post_texts", schema = "benki")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(PostTextPK.class)
public abstract class PostText extends PanacheEntityBase {

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

  @Column(name = "search_terms", columnDefinition = "tsvector")
  @Generated(event = {INSERT, UPDATE})
  @Type(value = PostgreSQLTSVectorType.class)
  public String searchTerms;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post", referencedColumnName = "id", nullable = false)
  @JsonbTransient
  public Post post;

  public Post getPost() {
    return post;
  }

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
