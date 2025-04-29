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


@Component
@SuppressWarnings("PMD.TooManyMethods")
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
            .map(cookie -> deserialize(cookie, OAuth2AuthorizationRequest.class))
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

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtils.serialize(authorizationRequest), COOKIEEXPIRESECONDS);
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
            .map(cookie -> deserialize(cookie, OidcIdToken.class)).orElse(null);
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
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_TOKEN_COOKIE_NAME,
            CookieUtils.serialize(authorizationToken), COOKIEEXPIRESECONDS);
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
}
