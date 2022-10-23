package eu.mulk.mulkcms2.common.template;

import io.quarkus.qute.TemplateExtension;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import javax.annotation.CheckForNull;

public class TemporalExtensions {

  private static final DateTimeFormatter htmlDateTimeFormatter =
      DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private static final DateTimeFormatter humanDateTimeFormatter =
      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

  private static final DateTimeFormatter htmlDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

  private static final DateTimeFormatter humanDateFormatter =
      DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

  @TemplateExtension
  @CheckForNull
  static String humanDateTime(@CheckForNull TemporalAccessor x) {
    if (x == null) {
      return null;
    }
    return humanDateTimeFormatter.format(x);
  }

  @TemplateExtension
  @CheckForNull
  static String htmlDateTime(@CheckForNull TemporalAccessor x) {
    if (x == null) {
      return null;
    }
    return htmlDateTimeFormatter.format(x);
  }

  @TemplateExtension
  @CheckForNull
  static String humanDate(@CheckForNull TemporalAccessor x) {
    if (x == null) {
      return null;
    }
    return humanDateFormatter.format(x);
  }

  @TemplateExtension
  @CheckForNull
  static String htmlDate(@CheckForNull TemporalAccessor x) {
    if (x == null) {
      return null;
    }
    return htmlDateFormatter.format(x);
  }

}
