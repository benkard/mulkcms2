package eu.mulk.mulkcms2.common.hibernate;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.BooleanType;

public final class PostMatchesWebsearchFunction extends SQLFunctionTemplate {

  public PostMatchesWebsearchFunction() {
    super(BooleanType.INSTANCE, "(?1 @@ websearch_to_tsquery(language_regconfig(?2), ?3))");
  }
}
