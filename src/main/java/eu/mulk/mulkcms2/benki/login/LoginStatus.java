package eu.mulk.mulkcms2.benki.login;

import io.quarkus.qute.TemplateData;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("LoginStatus")
@RequestScoped
@TemplateData
public class LoginStatus {

  @Inject SecurityIdentity identity;

  public boolean loggedIn() {
    return !identity.isAnonymous();
  }

  public boolean isEditor() {
    return identity.hasRole(LoginRoles.EDITOR);
  }

  public String getUserName() {
    return identity.getPrincipal().getName();
  }
}
