package eu.mulk.mulkcms2.benki.login;

import io.quarkus.qute.TemplateData;
import io.quarkus.security.identity.SecurityIdentity;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("LoginStatus")
@RequestScoped
@TemplateData
public class LoginStatus {

  @Inject SecurityIdentity identity;

  public boolean loggedIn() {
    return !identity.isAnonymous();
  }

  public String getUserName() {
    return identity.getPrincipal().getName();
  }
}
