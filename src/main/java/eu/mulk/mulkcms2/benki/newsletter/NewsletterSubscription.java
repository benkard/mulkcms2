package eu.mulk.mulkcms2.benki.newsletter;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "newsletter_subscriptions", schema = "benki")
public class NewsletterSubscription extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue
  public Integer id;

  @Column(name = "start_date", nullable = false)
  public OffsetDateTime startDate = OffsetDateTime.now();

  @NaturalId
  @Column(name = "email", nullable = false)
  @Email
  public String email;
}
