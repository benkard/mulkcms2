package eu.mulk.mulkcms2.cms.pages;

import eu.mulk.mulkcms2.cms.users.User;
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
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "article_revisions", schema = "public")
public class ArticleRevision extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "date", nullable = true)
  @CheckForNull
  public Timestamp date;

  @Column(name = "title", nullable = false, length = -1)
  public String title;

  @Column(name = "content", nullable = false, length = -1)
  public String content;

  @Column(name = "format", nullable = false, length = -1)
  public String format;

  @Column(name = "status", nullable = false, length = -1)
  public String status;

  @Column(name = "global_id", nullable = true, length = -1)
  @CheckForNull
  public String globalId;

  @OneToMany(mappedBy = "articleRevision", fetch = FetchType.LAZY)
  public Collection<ArticleRevisionCharacteristic> characteristics;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "article_revision_parenthood",
      joinColumns = @JoinColumn(name = "parent"),
      inverseJoinColumns = @JoinColumn(name = "child"))
  public Set<ArticleRevision> children;

  @ManyToMany(mappedBy = "children")
  public Set<ArticleRevision> parents;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
  public Article article;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author", referencedColumnName = "id")
  public User authors;
}
