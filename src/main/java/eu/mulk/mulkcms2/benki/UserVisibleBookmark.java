package eu.mulk.mulkcms2.benki;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_visible_bookmarks", schema = "public", catalog = "benki")
public class UserVisibleBookmark extends PanacheEntityBase {

  @Column(name = "user", nullable = true)
  public Integer userId;

  @Column(name = "message", nullable = true)
  public Integer messageId;
}
