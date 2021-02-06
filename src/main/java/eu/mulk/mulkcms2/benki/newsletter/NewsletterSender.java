package eu.mulk.mulkcms2.benki.newsletter;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

import eu.mulk.mulkcms2.benki.bookmarks.Bookmark;
import eu.mulk.mulkcms2.benki.lazychat.LazychatMessage;
import eu.mulk.mulkcms2.benki.posts.Post;
import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.panache.common.Sort;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.api.CheckedTemplate;
import io.quarkus.scheduler.Scheduled;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.CheckForNull;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.Session;

@Dependent
public class NewsletterSender {

  private static final DateTimeFormatter humanDateFormatter =
      DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

  @ConfigProperty(name = "mulkcms.newsletter.time-zone")
  ZoneId newsletterTimeZone;

  @ConfigProperty(name = "quarkus.mailer.from")
  String senderAddress;

  @PersistenceContext EntityManager em;

  @CheckedTemplate
  static class Templates {
    public static native MailTemplateInstance newsletter(
        Newsletter newsletter, List<Bookmark> bookmarks, List<LazychatMessage> lazychatMessages);
  }

  @Scheduled(cron = "0 0 0 ? * Mon")
  @Transactional
  void run() throws InterruptedException, TimeoutException, ExecutionException {
    var session = em.unwrap(Session.class);

    List<Post<?>> posts =
        Post.list(
            ""
                + "SELECT p FROM Post p"
                + "  JOIN p.targets r"
                + "  JOIN r.tags tag"
                + " WHERE newsletter IS NULL"
                + "   AND tag = 'world'",
            Sort.ascending("date"));
    Post.fetchTexts(posts);

    if (posts.isEmpty()) {
      return;
    }

    var postsByClass = posts.stream().collect(partitioningBy(Post::isBookmark));
    var bookmarks =
        postsByClass.getOrDefault(Boolean.TRUE, List.of()).stream()
            .map(x -> (Bookmark) x)
            .collect(toList());
    var lazychatMessages =
        postsByClass.getOrDefault(Boolean.FALSE, List.of()).stream()
            .map(x -> (LazychatMessage) x)
            .collect(toList());

    var date = OffsetDateTime.now(newsletterTimeZone);
    var newsletterNumber =
        (int)
            session
                .createQuery("SELECT max(id) FROM Newsletter", Integer.class)
                .uniqueResultOptional()
                .map(x -> x + 1)
                .orElse(1);

    var newsletter = new Newsletter();
    newsletter.id = newsletterNumber;
    newsletter.date = date;
    newsletter.persist();

    posts.forEach(post -> post.newsletter = newsletter);

    var subscriberEmails =
        NewsletterSubscription.<NewsletterSubscription>stream("registrationKey IS NULL")
            .map(x -> x.email)
            .toArray(String[]::new);

    var mailText = Templates.newsletter(newsletter, bookmarks, lazychatMessages);
    var sendJob =
        mailText
            .subject(String.format("MulkCMS newsletter #%d", newsletterNumber))
            .to(senderAddress)
            .bcc(subscriberEmails)
            .send();
    sendJob.toCompletableFuture().get(10000, TimeUnit.SECONDS);
  }

  @TemplateExtension
  @CheckForNull
  static String humanDate(@CheckForNull LocalDate x) {
    if (x == null) {
      return null;
    }
    return humanDateFormatter.format(x);
  }

  @TemplateExtension
  @CheckForNull
  static String humanDate(@CheckForNull OffsetDateTime x) {
    if (x == null) {
      return null;
    }
    return humanDateFormatter.format(x);
  }
}
