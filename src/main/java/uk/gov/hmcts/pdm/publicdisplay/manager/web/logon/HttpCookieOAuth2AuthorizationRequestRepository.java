package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
@SuppressWarnings("PMD")
public class HttpCookieOAuth2AuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final Logger LOG =
        LoggerFactory.getLogger(HttpCookieOAuth2AuthorizationRequestRepository.class);

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String OAUTH2_AUTHORIZATION_TOKEN_COOKIE_NAME = "oauth2_auth_token";
    public static final String USERNAME_COOKIE_NAME = "username";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIEEXPIRESECONDS = 600;

    /**
     * Load Authorization Request.
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            .map(cookie -> {
                OAuth2AuthorizationRequestDto dto = CookieUtils.deserialize(cookie,
                        OAuth2AuthorizationRequestDto.class);
                return dto != null ? convertDtoToOAuth2AuthorizationRequest(dto) : null;
            })
            .orElse(null);
    }

    /**
     * Save Authorization Request.
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
        HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        OAuth2AuthorizationRequestDto dto = convertOAuth2AuthorizationRequestToDto(authorizationRequest);
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtils.serialize(dto), COOKIEEXPIRESECONDS);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin,
                COOKIEEXPIRESECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
        HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    /**
     * Remove Authorization Request.
     */
    public static void removeAuthorizationRequestCookies(HttpServletRequest request,
        HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }

    /**
     * Load Authorization Token.
     */
    public OidcIdToken loadAuthorizationToken(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_TOKEN_COOKIE_NAME)
            .map(cookie -> {
                OidcIdTokenDto dto = CookieUtils.deserialize(cookie, OidcIdTokenDto.class);
                return dto != null ? convertDtoToOidcIdToken(dto) : null;
            })
            .orElse(null);
    }

    /**
     * Save Authorization Token.
     */
    public void saveAuthorizationToken(OidcIdToken authorizationToken, HttpServletRequest request,
        HttpServletResponse response) {
        if (authorizationToken == null) {
            removeAuthorizationToken(request, response);
            return;
        }
        OidcIdTokenDto dto = convertOidcIdTokenToDto(authorizationToken);
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_TOKEN_COOKIE_NAME,
            CookieUtils.serialize(dto), COOKIEEXPIRESECONDS);
    }

    /**
     * Remove Authorization Token.
     */
    public static void removeAuthorizationToken(HttpServletRequest request,
        HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_TOKEN_COOKIE_NAME);
    }

    /**
     * Load Username.
     */
    public String loadUsername(HttpServletRequest request) {
        return CookieUtils.getCookie(request, USERNAME_COOKIE_NAME)
            .map(cookie -> deserialize(cookie, String.class)).orElse(null);
    }

    /**
     * Save Username.
     */
    public void saveUsername(String username, HttpServletRequest request,
        HttpServletResponse response) {
        if (username == null) {
            removeUsername(request, response);
            return;
        }
        CookieUtils.addCookie(response, USERNAME_COOKIE_NAME, CookieUtils.serialize(username),
            COOKIEEXPIRESECONDS);
    }

    /**
     * Remove Username.
     */
    public static void removeUsername(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, USERNAME_COOKIE_NAME);
    }

    /**
     * Remove All Cookies.
     */
    public static void removeAllCookies(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("removeAllCookies()");
        removeAuthorizationToken(request, response);
        removeUsername(request, response);
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        try {
            return CookieUtils.deserialize(cookie, cls);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Convert OAuth2AuthorizationRequest to DTO.
     */
    private OAuth2AuthorizationRequestDto convertOAuth2AuthorizationRequestToDto(
        OAuth2AuthorizationRequest request) {
        LOG.info("Converting OAuth2AuthorizationRequest to DTO");
        long expiresAt = Instant.now().plusSeconds(COOKIEEXPIRESECONDS).toEpochMilli();
        
        return new OAuth2AuthorizationRequestDto(
            request.getAuthorizationUri() != null ? request.getAuthorizationUri().toString() : null,
            request.getClientId(),
            request.getRedirectUri() != null ? request.getRedirectUri().toString() : null,
            request.getScopes(),
            request.getState(),
            request.getAdditionalParameters(),
            request.getAttributes(),
            expiresAt
        );
    }

    /**
     * Convert DTO to OAuth2AuthorizationRequest.
     */
    private OAuth2AuthorizationRequest convertDtoToOAuth2AuthorizationRequest(
        OAuth2AuthorizationRequestDto dto) {
        LOG.info("Converting DTO to OAuth2AuthorizationRequest");
        
        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode()
            .clientId(dto.clientId)
            .state(dto.state);
        
        if (dto.authorizationUri != null) {
            builder.authorizationUri(dto.authorizationUri);
        }
        if (dto.redirectUri != null) {
            builder.redirectUri(dto.redirectUri);
        }
        if (dto.scopes != null && !dto.scopes.isEmpty()) {
            builder.scopes(dto.scopes);
        }
        if (dto.additionalParameters != null && !dto.additionalParameters.isEmpty()) {
            builder.additionalParameters(dto.additionalParameters);
        }
        if (dto.attributes != null && !dto.attributes.isEmpty()) {
            builder.attributes(dto.attributes);
        }
        
        return builder.build();
    }

    /**
     * Convert OidcIdToken to DTO.
     */
    private OidcIdTokenDto convertOidcIdTokenToDto(OidcIdToken token) {
        LOG.info("Converting OidcIdToken to DTO");
        
        return new OidcIdTokenDto(
            token.getTokenValue(),
            token.getIssuedAt() != null ? token.getIssuedAt().toEpochMilli() : 0,
            token.getExpiresAt() != null ? token.getExpiresAt().toEpochMilli() : 0,
            token.getClaims()
        );
    }

    /**
     * Convert DTO to OidcIdToken.
     */
    private OidcIdToken convertDtoToOidcIdToken(OidcIdTokenDto dto) {
        LOG.info("Converting DTO to OidcIdToken");
        
        Instant issuedAt = dto.issuedAt > 0 
            ? Instant.ofEpochMilli(dto.issuedAt) 
            : Instant.now();
        Instant expiresAt = dto.expiresAt > 0 
            ? Instant.ofEpochMilli(dto.expiresAt) 
            : Instant.now().plusSeconds(3600);
        
        return new OidcIdToken(
            dto.tokenValue,
            issuedAt,
            expiresAt,
            dto.claims != null ? dto.claims : java.util.Collections.emptyMap()
        );
    }
}
