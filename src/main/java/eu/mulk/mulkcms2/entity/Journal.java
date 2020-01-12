package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "journals", schema = "public", catalog = "mulkcms")
public class Journal extends PanacheEntityBase {

  private int id;
  private String pathPrefix;
  private Collection<JournalEntry> entries;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "path_prefix", nullable = true, length = -1)
  public String getPathPrefix() {
    return pathPrefix;
  }

  public void setPathPrefix(String pathPrefix) {
    this.pathPrefix = pathPrefix;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Journal journal = (Journal) o;
    return id == journal.id &&
        Objects.equals(pathPrefix, journal.pathPrefix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, pathPrefix);
  }

  @OneToMany(mappedBy = "journal", fetch = FetchType.LAZY)
  public Collection<JournalEntry> getEntries() {
    return entries;
  }

  public void setEntries(Collection<JournalEntry> entries) {
    this.entries = entries;
  }
}
