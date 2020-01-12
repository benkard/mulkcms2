package eu.mulk.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "articles", schema = "public", catalog = "mulkcms")
public class Article extends PanacheEntityBase {

  private int id;
  private Collection<ArticleAlias> aliases;
  private Set<Category> categories;
  private Collection<ArticleRevision> revisions;
  private ArticleType type;
  private Collection<Comment> comments;
  private Collection<JournalEntry> journalEntries;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Article article = (Article) o;
    return id == article.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @OneToMany(mappedBy = "article")
  public Collection<ArticleAlias> getAliases() {
    return aliases;
  }

  public void setAliases(Collection<ArticleAlias> aliases) {
    this.aliases = aliases;
  }

  @ManyToMany
  @JoinTable(name = "article_category_memberships",
      joinColumns = @JoinColumn(name = "article"),
      inverseJoinColumns = @JoinColumn(name = "category")
  )
  public Set<Category> getCategories() {
    return categories;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

  @OneToMany(mappedBy = "article")
  public Collection<ArticleRevision> getRevisions() {
    return revisions;
  }

  public void setRevisions(Collection<ArticleRevision> revisions) {
    this.revisions = revisions;
  }

  @ManyToOne
  @JoinColumn(name = "type", referencedColumnName = "id", nullable = false)
  public ArticleType getType() {
    return type;
  }

  public void setType(ArticleType type) {
    this.type = type;
  }

  @OneToMany(mappedBy = "article")
  public Collection<Comment> getComments() {
    return comments;
  }

  public void setComments(Collection<Comment> comments) {
    this.comments = comments;
  }

  @OneToMany(mappedBy = "article")
  public Collection<JournalEntry> getJournalEntries() {
    return journalEntries;
  }

  public void setJournalEntries(Collection<JournalEntry> journalEntries) {
    this.journalEntries = journalEntries;
  }
}
