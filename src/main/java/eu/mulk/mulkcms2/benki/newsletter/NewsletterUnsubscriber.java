package eu.mulk.mulkcms2.benki.newsletter;

import eu.mulk.mulkcms2.common.logging.Messages;
import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.qute.CheckedTemplate;
import jakarta.enterprise.context.Dependent;
import jakarta.mail.internet.InternetAddress;
import jakarta.transaction.Transactional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mail.MailMessage;

@Dependent
public class NewsletterUnsubscriber implements Processor {

  @CheckedTemplate
  static class Templates {
    public static native MailTemplateInstance unsubscribedMail();
  }

  @Override
  @Transactional
  public void process(Exchange exchange) throws Exception {
    var message = exchange.getMessage(MailMessage.class);
    var mail = message.getMessage();

    for (var sender : mail.getFrom()) {
      if (!(sender instanceof InternetAddress)) {
        Messages.log.unsubscribeBadInternetAddress(sender);
        continue;
      }

      var address = ((InternetAddress) sender).getAddress();
      var subscription =
          NewsletterSubscription.<NewsletterSubscription>find("email = ?1", address)
              .singleResultOptional();
      subscription.ifPresentOrElse(
          s -> {
            try {
              var sendJob =
                  Templates.unsubscribedMail()
                      .subject("Unsubscribed from MulkCMS newsletter")
                      .to(address)
                      .send();
              sendJob.subscribeAsCompletionStage().get(60, TimeUnit.SECONDS);

              s.delete();

              Messages.log.unsubscribed(s.email, s.id);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
              throw new RuntimeException(e);
            }
          },
          () -> Messages.log.unsubscribeSubscriptionNotFound(sender));
    }
  }
}
