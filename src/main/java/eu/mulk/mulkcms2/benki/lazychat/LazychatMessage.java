package eu.mulk.mulkcms2.benki.lazychat;

import eu.mulk.mulkcms2.benki.generic.Post;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "lazychat_messages", schema = "benki")
public class LazychatMessage extends Post {

  @Column(name = "content", nullable = true, length = -1)
  public String content;

  @Column(name = "format", nullable = false, length = -1)
  public String format;

  @OneToMany(mappedBy = "referrer", fetch = FetchType.LAZY)
  public Collection<LazychatReference> references;
}
