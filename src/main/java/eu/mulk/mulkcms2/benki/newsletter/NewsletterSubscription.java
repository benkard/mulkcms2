package eu.mulk.mulkcms2.benki.newsletter;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "newsletter_subscriptions", schema = "benki")
public class NewsletterSubscription extends PanacheEntityBase {

  private static final int registrationKeyBytes = 32;

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer id;

  @Column(name = "start_date", nullable = false)
  public OffsetDateTime startDate = OffsetDateTime.now();

  @NaturalId
  @Column(name = "email", nullable = false)
  @Email
  public String email;

  @Column(name = "registration_key", nullable = true)
  public String registrationKey = generateRegistrationKey();

  private static String generateRegistrationKey() {
    var secureRandom = new SecureRandom();
    byte[] keyBytes = new byte[registrationKeyBytes];
    secureRandom.nextBytes(keyBytes);
    return new BigInteger(keyBytes).abs().toString(36);
  }
}
