package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "lazychat_messages", schema = "public", catalog = "benki")
public class LazychatMessage extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "date", nullable = true)
  public Object date;

  @Column(name = "content", nullable = true, length = -1)
  public String content;

  @Column(name = "format", nullable = false, length = -1)
  public String format;

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id")
  public User owner;

  @OneToMany(mappedBy = "referrer")
  public Collection<LazychatReference> references;
}
