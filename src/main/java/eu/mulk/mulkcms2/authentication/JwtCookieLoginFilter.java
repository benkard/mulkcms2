package eu.mulk.mulkcms2.authentication;

import static javax.ws.rs.Priorities.AUTHENTICATION;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.AbstractBearerTokenExtractor;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interprets a possibly present JWT cookie and uses it to authenticate the user.
 *
 * <p>JWT cookies are used to authenticate further requests based on initial authentication. This
 * way, there is no need to route the user through an OpenID Connect IdP on each request, for
 * example.
 *
 * @see JwtCookieSetterFilter
 */
@Provider
@Priority(AUTHENTICATION)
public class JwtCookieLoginFilter implements ContainerRequestFilter {

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

  @Inject JWTAuthContextInfo authContextInfo;

  private static final Logger log = LoggerFactory.getLogger(JwtCookieLoginFilter.class);

  private PublicKey signingKey;

  @PostConstruct
  public void postCostruct()
      throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException,
          UnrecoverableKeyException {
    try (var is = new FileInputStream(signingKeyFile)) {
      var keystore = KeyStore.getInstance(KeyStore.getDefaultType());
      keystore.load(is, signingKeyPassphrase.toCharArray());
      signingKey = keystore.getCertificate(signingKeyAlias).getPublicKey();
      Objects.requireNonNull(signingKey);
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext)
      throws IOException {

    try {
      if (!identity.isAnonymous()) {
        log.debug("Already authenticated, skipping JWT check.");
        return;
      }

      AbstractBearerTokenExtractor extractor =
          new BearerTokenExtractor(requestContext, authContextInfo);
      String bearerToken = extractor.getBearerToken();

      var jwtConsumer =
          new JwtConsumerBuilder()
              .setJwsAlgorithmConstraints(
                  new AlgorithmConstraints(
                      AlgorithmConstraints.ConstraintType.WHITELIST,
                      AlgorithmIdentifiers.RSA_USING_SHA256,
                      AlgorithmIdentifiers.RSA_USING_SHA384,
                      AlgorithmIdentifiers.RSA_USING_SHA512,
                      AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256,
                      AlgorithmIdentifiers.ECDSA_USING_P384_CURVE_AND_SHA384,
                      AlgorithmIdentifiers.ECDSA_USING_P521_CURVE_AND_SHA512))
              .setVerificationKey(signingKey)
              .setRequireExpirationTime()
              .setAllowedClockSkewInSeconds(60)
              .build();

      var claims = jwtConsumer.process(bearerToken).getJwtClaims();
      claims.getSubject();

      var jwtPrincipal = new DefaultJWTCallerPrincipal(claims);
      log.debug("JWT verified: {}", jwtPrincipal);

      var securityContext =
          new JwtSecurityContext(requestContext.getSecurityContext(), jwtPrincipal);
      requestContext.setSecurityContext(securityContext);
    } catch (InvalidJwtException | MalformedClaimException e) {
      log.debug("Invalid JWT", e);
    }
  }

  private static class BearerTokenExtractor extends AbstractBearerTokenExtractor {

    private final ContainerRequestContext requestContext;

    BearerTokenExtractor(
        ContainerRequestContext requestContext, JWTAuthContextInfo authContextInfo) {
      super(authContextInfo);
      this.requestContext = requestContext;
    }

    @Override
    protected String getHeaderValue(String headerName) {
      return requestContext.getHeaderString(headerName);
    }

    @Override
    protected String getCookieValue(String cookieName) {
      var tokenCookie = requestContext.getCookies().get(cookieName);

      if (tokenCookie != null) {
        return tokenCookie.getValue();
      }
      return null;
    }
  }

  private static class JwtSecurityContext implements SecurityContext {
    private SecurityContext delegate;
    private JsonWebToken principal;

    JwtSecurityContext(SecurityContext delegate, JsonWebToken principal) {
      this.delegate = delegate;
      this.principal = principal;
    }

    @Override
    public Principal getUserPrincipal() {
      return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
      return principal.getGroups().contains(role);
    }

    @Override
    public boolean isSecure() {
      return delegate.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
      return delegate.getAuthenticationScheme();
    }
  }
}
