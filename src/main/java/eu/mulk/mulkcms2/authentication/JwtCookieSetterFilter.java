package eu.mulk.mulkcms2.authentication;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Duration;
import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adds a JWT cookie to every authenticated request.
 *
 * <p>JWT cookies are used to authenticate further requests based on initial authentication. This
 * way, there is no need to route the user through an OpenID Connect IdP on each request, for
 * example.
 *
 * @see JwtCookieLoginFilter
 */
@Provider
@Priority(1100)
public class JwtCookieSetterFilter implements ContainerResponseFilter {

  @ConfigProperty(name = "mulkcms.jwt.signing-key")
  String signingKeyAlias;

  @ConfigProperty(name = "mulkcms.jwt.keystore.file")
  String signingKeyFile;

  @ConfigProperty(name = "mulkcms.jwt.keystore.passphrase")
  String signingKeyPassphrase;

  @ConfigProperty(name = "mulkcms.jwt.issuer")
  String issuer;

  @ConfigProperty(name = "mulkcms.jwt.validity")
  Duration validity;

  @Inject SecurityIdentity identity;

  private static final Logger log = LoggerFactory.getLogger(JwtCookieSetterFilter.class);

  private PrivateKey signingKey;

  @PostConstruct
  public void postCostruct()
      throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException,
          UnrecoverableKeyException {
    try (var is = new FileInputStream(signingKeyFile)) {
      var keystore = KeyStore.getInstance(KeyStore.getDefaultType());
      keystore.load(is, signingKeyPassphrase.toCharArray());
      signingKey =
          (PrivateKey) keystore.getKey(signingKeyAlias, signingKeyPassphrase.toCharArray());
    }
  }

  @Override
  public void filter(
      ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {

    if (identity.isAnonymous()) {
      return;
    }

    var currentTimeSeconds = System.currentTimeMillis() / 1000;

    if (identity instanceof JsonWebToken
        && ((JsonWebToken) identity).getExpirationTime() < currentTimeSeconds) {
      return;
    }

    var claims = Jwt.claims();

    claims.issuedAt(currentTimeSeconds);
    claims.claim(Claims.auth_time.name(), currentTimeSeconds);
    claims.expiresAt(currentTimeSeconds + validity.toSeconds());
    claims.issuer(issuer);
    claims.preferredUserName(identity.getPrincipal().getName());
    claims.subject(identity.getPrincipal().getName());

    var token = claims.jws().signatureKeyId(signingKeyAlias).sign(signingKey);
    responseContext
        .getHeaders()
        .add(
            "Set-Cookie",
            new NewCookie(
                        "Bearer",
                        token,
                        null,
                        null,
                        1,
                        null,
                        (int) validity.toSeconds(),
                        null,
                        false,
                        true)
                    .toString()
                + ";SameSite=Strict");
  }
}
