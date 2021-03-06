package eu.mulk.mulkcms2.cms.pages;

import eu.mulk.mulkcms2.cms.comments.Comment;
import eu.mulk.mulkcms2.cms.journal.JournalEntry;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "articles", schema = "public")
public class Article extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
  public Collection<ArticleAlias> aliases;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "article_category_memberships",
      joinColumns = @JoinColumn(name = "article"),
      inverseJoinColumns = @JoinColumn(name = "category"))
  public Set<Category> categories;

  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
  public Collection<ArticleRevision> revisions;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "type", referencedColumnName = "id", nullable = false)
  public ArticleType type;

  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
  public Collection<Comment> comments;

  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
  public Collection<JournalEntry> journalEntries;
}
