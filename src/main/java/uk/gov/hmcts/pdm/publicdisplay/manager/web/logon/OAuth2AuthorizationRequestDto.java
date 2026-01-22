package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public class OAuth2AuthorizationRequestDto {
    public final String authorizationUri;
    public final String clientId;
    public final String redirectUri;
    public final Set<String> scopes;
    public final String state;
    public final Map<String, Object> additionalParameters;
    public final Map<String, Object> attributes;
    public final long expiresAt; // epoch millis

    @JsonCreator
    public OAuth2AuthorizationRequestDto(
            @JsonProperty("authorizationUri") String authorizationUri,
            @JsonProperty("clientId") String clientId,
            @JsonProperty("redirectUri") String redirectUri,
            @JsonProperty("scopes") Set<String> scopes,
            @JsonProperty("state") String state,
            @JsonProperty("additionalParameters") Map<String, Object> additionalParameters,
            @JsonProperty("attributes") Map<String, Object> attributes,
            @JsonProperty("expiresAt") long expiresAt
    ) {
        this.authorizationUri = authorizationUri;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
        this.state = state;
        this.additionalParameters = additionalParameters;
        this.attributes = attributes;
        this.expiresAt = expiresAt;
    }

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public String getState() {
        return state;
    }

    public Map<String, Object> getAdditionalParameters() {
        return additionalParameters;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public long getExpiresAt() {
        return expiresAt;
    }
}
