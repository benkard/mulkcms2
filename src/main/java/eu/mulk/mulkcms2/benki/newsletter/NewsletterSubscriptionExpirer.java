package eu.mulk.mulkcms2.benki.newsletter;

import io.quarkus.scheduler.Scheduled;
import java.time.OffsetDateTime;
import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;
import org.jboss.logging.Logger;

@Dependent
public class NewsletterSubscriptionExpirer {

  private static final Logger log = Logger.getLogger(NewsletterUnsubscriber.class);

  @Scheduled(every = "PT1H")
  @Transactional
  void run() {
    var subscriptionsDeleted =
        NewsletterSubscription.delete(
            "registrationKey IS NOT NULL AND startDate < ?1",
            OffsetDateTime.now().minusWeeks(1));
    if (subscriptionsDeleted > 0) {
      log.infof("%d expired newsletter subscriptions deleted.", subscriptionsDeleted);
    }
  }
}
