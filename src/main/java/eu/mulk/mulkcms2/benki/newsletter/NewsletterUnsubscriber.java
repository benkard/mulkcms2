package eu.mulk.mulkcms2.benki.newsletter;

import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.qute.api.CheckedTemplate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.Dependent;
import javax.mail.internet.InternetAddress;
import javax.transaction.Transactional;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mail.MailMessage;
import org.jboss.logging.Logger;

@Dependent
public class NewsletterUnsubscriber implements Processor {

  private static final Logger log = Logger.getLogger(NewsletterUnsubscriber.class);

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
        log.warnf("Tried to unsubscribe, but not an InternetAddress: %s", sender);
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
              sendJob.toCompletableFuture().get(60, TimeUnit.SECONDS);

              s.delete();

              log.infof("Unsubscribed: %s (#%d)", s.email, s.id);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
              throw new RuntimeException(e);
            }
          },
          () ->
              log.warnf("Tried to unsubscribe, but no subscription found: %s", sender.toString()));
    }
  }
}
