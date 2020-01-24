package eu.mulk.mulkcms2.benki;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "wiki_pages", schema = "public", catalog = "benki")
public class WikiPage {

  private int id;
  private Collection<WikiPageRevision> revisions;

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

    WikiPage wikiPage = (WikiPage) o;

    if (id != wikiPage.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @OneToMany(mappedBy = "page")
  public Collection<WikiPageRevision> getRevisions() {
    return revisions;
  }

  public void setRevisions(Collection<WikiPageRevision> revisions) {
    this.revisions = revisions;
  }
}
