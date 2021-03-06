package eu.mulk.mulkcms2.cms.comments;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "used_transaction_keys", schema = "public")
public class UsedTransactionKey extends PanacheEntityBase {

  @Id
  @Column(name = "key", nullable = false)
  public long key;
}
