package eu.mulk.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "used_transaction_keys", schema = "public", catalog = "mulkcms")
public class UsedTransactionKey extends PanacheEntityBase {

  private long key;

  @Id
  @Column(name = "key", nullable = false)
  public long getKey() {
    return key;
  }

  public void setKey(long key) {
    this.key = key;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UsedTransactionKey that = (UsedTransactionKey) o;
    return key == that.key;
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }
}
