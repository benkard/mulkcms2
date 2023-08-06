package eu.mulk.mulkcms2.cms.comments;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "used_transaction_keys", schema = "public")
public class UsedTransactionKey extends PanacheEntityBase {

  @Id
  @Column(name = "key", nullable = false)
  public long key;
}
