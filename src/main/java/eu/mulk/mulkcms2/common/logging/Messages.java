package eu.mulk.mulkcms2.common.logging;

import javax.mail.Address;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

@MessageLogger(projectCode = "MLKCMS", length = 5)
public interface Messages extends BasicLogger {

  Messages log = Logger.getMessageLogger(Messages.class, "eu.mulk.mulkcms2");

  @Message(id = 1, value = "%d expired newsletter subscriptions deleted")
  @LogMessage(level = Level.INFO)
  void expiredSubscriptionsDeleted(long expiredSubscriptionCount);

  @Message(id = 2, value = "Tried to unsubscribe, but not an InternetAddress: %s")
  @LogMessage(level = Level.WARN)
  void unsubscribeBadInternetAddress(Address senderAddress);

  @Message(id = 3, value = "Unsubscribed: %s (#%d)")
  @LogMessage(level = Level.INFO)
  void unsubscribed(String email, int subscriptionId);

  @Message(id = 4, value = "Tried to unsubscribe, but no subscription found: %s")
  @LogMessage(level = Level.WARN)
  void unsubscribeSubscriptionNotFound(Address senderAddress);
}
