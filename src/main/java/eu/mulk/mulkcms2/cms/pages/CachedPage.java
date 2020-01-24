package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "cached_pages", schema = "public")
@IdClass(CachedPagePK.class)
public class CachedPage extends PanacheEntityBase {

  @Id
  @Column(name = "alias", nullable = false, length = -1)
  public String alias;

  @Id
  @Column(name = "characteristic_hash", nullable = false)
  public int characteristicHash;

  @Column(name = "date", nullable = false)
  public Timestamp date;

  @Column(name = "content", nullable = false, length = -1)
  public String content;
}
