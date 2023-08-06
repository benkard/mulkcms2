package eu.mulk.mulkcms2.cms.comments;

import eu.mulk.mulkcms2.cms.pages.Article;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "comments", schema = "public")
public class Comment extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "global_id", nullable = true, length = -1)
  @CheckForNull
  public String globalId;

  @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
  public Collection<CommentRevision> revisions;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
  public Article article;
}
