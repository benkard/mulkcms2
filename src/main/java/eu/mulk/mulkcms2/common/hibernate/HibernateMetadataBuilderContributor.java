package eu.mulk.mulkcms2.common.hibernate;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;

public final class HibernateMetadataBuilderContributor implements MetadataBuilderContributor {

  @Override
  public void contribute(MetadataBuilder metadataBuilder) {
    metadataBuilder.applySqlFunction("post_matches_websearch", new PostMatchesWebsearchFunction());
  }
}
