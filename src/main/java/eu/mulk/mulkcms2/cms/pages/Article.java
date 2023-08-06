package eu.mulk.mulkcms2.cms.pages;

import eu.mulk.mulkcms2.cms.comments.Comment;
import eu.mulk.mulkcms2.cms.journal.JournalEntry;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.Set;

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
