package eu.mulk.mulkcms2.benki.newsletter;

import eu.mulk.mulkcms2.common.logging.Messages;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.Dependent;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;

@Dependent
public class NewsletterSubscriptionExpirer {

  @Scheduled(every = "PT1H")
  @Transactional
  void run() {
    var subscriptionsDeleted =
        NewsletterSubscription.delete(
            "registrationKey IS NOT NULL AND startDate < ?1", OffsetDateTime.now().minusWeeks(1));
    if (subscriptionsDeleted > 0) {
      Messages.log.expiredSubscriptionsDeleted(subscriptionsDeleted);
    }
  }
}
