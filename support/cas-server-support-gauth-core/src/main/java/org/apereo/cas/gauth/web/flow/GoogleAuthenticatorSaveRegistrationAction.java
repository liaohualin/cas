package org.apereo.cas.gauth.web.flow;

import org.apereo.cas.authentication.OneTimeTokenAccount;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.gauth.credential.GoogleAuthenticatorTokenCredential;
import org.apereo.cas.gauth.token.GoogleAuthenticatorToken;
import org.apereo.cas.otp.repository.credentials.OneTimeTokenCredentialRepository;
import org.apereo.cas.otp.repository.credentials.OneTimeTokenCredentialValidator;
import org.apereo.cas.otp.web.flow.OneTimeTokenAccountSaveRegistrationAction;
import org.apereo.cas.util.LoggingUtils;
import org.apereo.cas.web.support.WebUtils;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.webflow.execution.RequestContext;

/**
 * This is {@link GoogleAuthenticatorSaveRegistrationAction}.
 *
 * @author Misagh Moayyed
 * @since 6.3.0
 */
@Slf4j
public class GoogleAuthenticatorSaveRegistrationAction extends OneTimeTokenAccountSaveRegistrationAction {
    private final OneTimeTokenCredentialValidator<GoogleAuthenticatorTokenCredential, GoogleAuthenticatorToken> validator;

    public GoogleAuthenticatorSaveRegistrationAction(final OneTimeTokenCredentialRepository repository,
                                                     final CasConfigurationProperties casProperties,
                                                     final OneTimeTokenCredentialValidator<GoogleAuthenticatorTokenCredential, GoogleAuthenticatorToken> validator) {
        super(repository, casProperties);
        this.validator = validator;
    }

    @Override
    protected boolean validate(final OneTimeTokenAccount account, final RequestContext requestContext) {
        try {
            val token = requestContext.getRequestParameters().getRequiredInteger(REQUEST_PARAMETER_TOKEN);
            if (validator.isTokenAuthorizedFor(token, account)) {
                LOGGER.debug("Successfully validated token [{}]", token);
                return super.validate(account, requestContext);
            }
        } catch (final Exception e) {
            LoggingUtils.error(LOGGER, e);
        }
        return false;
    }
}
