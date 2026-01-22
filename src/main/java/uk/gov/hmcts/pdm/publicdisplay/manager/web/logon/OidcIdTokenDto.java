package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class OidcIdTokenDto {
    public final String tokenValue;
    public final long issuedAt; // epoch millis
    public final long expiresAt; // epoch millis
    public final Map<String, Object> claims;

    @JsonCreator
    public OidcIdTokenDto(
            @JsonProperty("tokenValue") String tokenValue,
            @JsonProperty("issuedAt") long issuedAt,
            @JsonProperty("expiresAt") long expiresAt,
            @JsonProperty("claims") Map<String, Object> claims
    ) {
        this.tokenValue = tokenValue;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.claims = claims;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }
}
