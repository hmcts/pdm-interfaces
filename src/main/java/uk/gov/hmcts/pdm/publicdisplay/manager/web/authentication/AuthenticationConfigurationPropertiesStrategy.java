package uk.gov.hmcts.pdm.publicdisplay.manager.web.authentication;

import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.authentication.client.OAuthClientImpl;

import java.net.URI;
import java.net.URISyntaxException;

public interface AuthenticationConfigurationPropertiesStrategy extends RequestMatcher {
    AuthConfigurationProperties getConfiguration();

    AuthProviderConfigurationProperties getProviderConfiguration();

    @SneakyThrows(URISyntaxException.class)
    default URI getLoginUri(String redirectUri) {
        return buildCommonAuthUri(getProviderConfiguration().getAuthorizationUri(), redirectUri)
            .addParameter("response_mode", getConfiguration().getResponseMode())
            .addParameter("response_type", getConfiguration().getResponseType())
            .build();
    }

    default URI getLandingPageUri() {
        return URI.create("home");
    }

    @SneakyThrows(URISyntaxException.class)
    default URI getLogoutUri(String accessToken, String redirectUriOverride) {
        String redirectUri = getConfiguration().getLogoutRedirectUri();
        if (redirectUriOverride != null) {
            redirectUri = redirectUriOverride;
        }
        return new URIBuilder(
            getProviderConfiguration().getLogoutUri())
            .addParameter("id_token_hint", accessToken)
            .addParameter("post_logout_redirect_uri", redirectUri)
            .build();
    }

    @SneakyThrows(URISyntaxException.class)
    private URIBuilder buildCommonAuthUri(String uri, String redirectUriOverride) {
        String redirectUri = getConfiguration().getRedirectUri();
        if (redirectUriOverride != null) {
            redirectUri = redirectUriOverride;
        }
        return new URIBuilder(uri)
            .addParameter("client_id", getConfiguration().getClientId())
            .addParameter("redirect_uri", redirectUri)
            .addParameter("scope", OAuthClientImpl.getAuthorisationScope().toString())
            .addParameter("prompt", getConfiguration().getPrompt());
    }
}