package eu.mulk.mulkcms2.cms.comments;

import eu.mulk.mulkcms2.cms.pages.Article;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "comments", schema = "public")
public class Comment extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "global_id", nullable = true, length = -1)
  public String globalId;

  @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
  public Collection<CommentRevision> revisions;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
  public Article article;
}
