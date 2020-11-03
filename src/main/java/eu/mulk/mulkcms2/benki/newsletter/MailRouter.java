package eu.mulk.mulkcms2.benki.newsletter;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
public class MailRouter extends RouteBuilder {

  @ConfigProperty(name = "quarkus.mailer.host")
  String emailHost;

  @ConfigProperty(name = "mulkcms.imap.port")
  int emailPort;

  @ConfigProperty(name = "quarkus.mailer.username")
  String emailUser;

  @ConfigProperty(name = "quarkus.mailer.password")
  String emailPassword;

  @Inject NewsletterUnsubscriber newsletterUnsubscriber;

  @Override
  public void configure() {
    fromF(
            "imaps://%s:%d?password=%s&username=%s&searchTerm.to=unsubscribe",
            emailHost, emailPort, emailPassword, emailUser)
        .process(newsletterUnsubscriber);
  }
}
