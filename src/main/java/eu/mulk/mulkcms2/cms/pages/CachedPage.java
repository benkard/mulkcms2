package eu.mulk.mulkcms2.cms.pages;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.sql.Timestamp;

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
