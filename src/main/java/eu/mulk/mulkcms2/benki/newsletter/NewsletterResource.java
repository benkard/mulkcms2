package eu.mulk.mulkcms2.benki.newsletter;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.transaction.Transactional;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/newsletter")
@Produces(TEXT_HTML)
public class NewsletterResource {

  @CheckedTemplate
  static class Templates {
    public static native MailTemplateInstance registrationMail(String registrationKey);

    public static native TemplateInstance index();

    public static native TemplateInstance completeRegistration();

    public static native TemplateInstance registered();
  }

  @GET
  public TemplateInstance getIndex() {
    return Templates.index();
  }

  @POST
  @Path("register")
  @Transactional
  public CompletionStage<String> register(@FormParam("email") String email) {
    var existingSubscription =
        NewsletterSubscription.<NewsletterSubscription>find("email = ?1", email)
            .singleResultOptional();
    if (existingSubscription.isPresent()) {
      // If a subscription already exists, act as if we had created it.  This provides better
      // privacy to users than an error message does.
      return CompletableFuture.completedStage(Templates.completeRegistration().render());
    }

    var subscription = new NewsletterSubscription();
    subscription.email = email;
    subscription.persist();

    var mailText = Templates.registrationMail(subscription.registrationKey);
    var sendJob = mailText.subject("MulkCMS newsletter registration").to(email).send();
    var page = Templates.completeRegistration().render();
    return sendJob.onItem().transform(x -> page).subscribeAsCompletionStage();
  }

  @GET
  @Path("finish-registration")
  @Transactional
  public TemplateInstance finishRegistration(@QueryParam("key") String registrationKey) {
    NewsletterSubscription.<NewsletterSubscription>find("registrationKey = ?1", registrationKey)
        .singleResultOptional()
        .ifPresent(s -> s.registrationKey = null);

    return Templates.registered();
  }
}
