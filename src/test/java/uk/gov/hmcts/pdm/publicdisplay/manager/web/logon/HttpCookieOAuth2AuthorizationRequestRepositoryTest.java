package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class HttpCookieOAuth2AuthorizationRequestRepository.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("PMD.TooManyMethods")
class HttpCookieOAuth2AuthorizationRequestRepositoryTest extends AbstractJUnit {

    private static final String NOTNULL = "Result is Null";
    private static final String TRUE = "Result is False";
    private static final String USERNAME = "Username";

    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Mock
    private HttpServletResponse mockHttpServletResponse;

    @Mock
    private Cookie mockCookie;

    @Mock
    private OAuth2AuthorizationRequest mockOAuth2AuthorizationRequest;
    
    @Mock
    private OidcIdToken mockOidcIdToken;

    @InjectMocks
    private final HttpCookieOAuth2AuthorizationRequestRepository classUnderTest =
        new HttpCookieOAuth2AuthorizationRequestRepository();

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        Mockito.mockStatic(CookieUtils.class);
    }

    /**
     * Teardown.
     */
    @AfterEach
    public void teardown() {
        Mockito.clearAllCaches();
    }

    @Test
    void testLoadAuthorizationRequest() {
        OAuth2AuthorizationRequestDto mockDto = new OAuth2AuthorizationRequestDto(
            "https://example.com/authorize",
            "test-client-id",
            "https://example.com/redirect",
            Collections.singleton("openid"),
            "test-state",
            Collections.emptyMap(),
            Collections.emptyMap(),
            Instant.now().plusSeconds(600).toEpochMilli()
        );
        
        Mockito.when(CookieUtils.getCookie(mockHttpServletRequest,
            HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME))
            .thenReturn(Optional.of(mockCookie));
        Mockito.when(CookieUtils.deserialize(mockCookie, OAuth2AuthorizationRequestDto.class))
            .thenReturn(mockDto);
        OAuth2AuthorizationRequest result = classUnderTest
            .removeAuthorizationRequest(mockHttpServletRequest, mockHttpServletResponse);
        assertNotNull(result, NOTNULL);
    }

    @Test
    void testSaveAuthorizationRequest() {
        boolean result = testSaveAuthorizationRequest("Test");
        assertTrue(result, TRUE);
    }

    private boolean testSaveAuthorizationRequest(String value) {
        Mockito
            .when(mockHttpServletRequest.getParameter(
                HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME))
            .thenReturn(value);
        // Mock serialize to return a test value for DTOs
        Mockito.when(CookieUtils.serialize(Mockito.any(OAuth2AuthorizationRequestDto.class)))
            .thenReturn("test-serialized-value");
        boolean result = false;
        try {
            classUnderTest.saveAuthorizationRequest(null, mockHttpServletRequest,
                mockHttpServletResponse);
            classUnderTest.saveAuthorizationRequest(mockOAuth2AuthorizationRequest,
                mockHttpServletRequest, mockHttpServletResponse);
            result = true;
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        return result;
    }
    
    @Test
    void testSaveAuthorizationRequestNull() {
        boolean result = testSaveAuthorizationRequest(null);
        assertTrue(result, TRUE);
    }

    @Test
    void testRemoveAuthorizationRequestCookies() {
        boolean result = false;
        try {
            classUnderTest.removeAuthorizationRequestCookies(mockHttpServletRequest,
                mockHttpServletResponse);
            result = true;
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertTrue(result, TRUE);
    }
    
    @Test
    void testLoadAuthorizationToken() {
        Map<String, Object> claims = new ConcurrentHashMap<>();
        claims.put("sub", "test-user");
        claims.put("iss", "https://example.com");
        claims.put("aud", "test-client");
        
        OidcIdTokenDto mockDto = new OidcIdTokenDto(
            "test-token-value",
            Instant.now().toEpochMilli(),
            Instant.now().plusSeconds(3600).toEpochMilli(),
            claims
        );
        
        Mockito.when(CookieUtils.getCookie(mockHttpServletRequest,
            HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTHORIZATION_TOKEN_COOKIE_NAME))
            .thenReturn(Optional.of(mockCookie));
        Mockito.when(CookieUtils.deserialize(mockCookie, OidcIdTokenDto.class))
            .thenReturn(mockDto);
        OidcIdToken result = classUnderTest
            .loadAuthorizationToken(mockHttpServletRequest);
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testSaveAuthorizationToken() {
        boolean result = testSaveAuthorizationToken("Test");
        assertTrue(result, TRUE);
    }
    
    private boolean testSaveAuthorizationToken(String value) {
        Mockito
            .when(mockHttpServletRequest.getParameter(
                HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTHORIZATION_TOKEN_COOKIE_NAME))
            .thenReturn(value);
        // Mock serialize to return a test value for DTOs
        Mockito.when(CookieUtils.serialize(Mockito.any(OidcIdTokenDto.class)))
            .thenReturn("test-serialized-value");
        boolean result = false;
        try {
            classUnderTest.saveAuthorizationToken(null, mockHttpServletRequest,
                mockHttpServletResponse);
            classUnderTest.saveAuthorizationToken(mockOidcIdToken,
                mockHttpServletRequest, mockHttpServletResponse);
            result = true;
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        return result;
    }
    
    @Test
    void testRemoveAuthorizationTokenCookies() {
        boolean result = false;
        try {
            classUnderTest.removeAuthorizationToken(mockHttpServletRequest,
                mockHttpServletResponse);
            result = true;
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertTrue(result, TRUE);
    }
    
    @Test
    void testLoadUsername() {        
        Mockito.when(CookieUtils.getCookie(mockHttpServletRequest,
            HttpCookieOAuth2AuthorizationRequestRepository.USERNAME_COOKIE_NAME))
            .thenReturn(Optional.of(mockCookie));
        Mockito.when(CookieUtils.deserialize(mockCookie, String.class))
            .thenReturn(USERNAME);
        String result = classUnderTest
            .loadUsername(mockHttpServletRequest);
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testSaveUsername() {
        boolean result = testSaveUsername("Test");
        assertTrue(result, TRUE);
    }
    
    private boolean testSaveUsername(String value) {
        Mockito
            .when(mockHttpServletRequest.getParameter(
                HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTHORIZATION_TOKEN_COOKIE_NAME))
            .thenReturn(value);
        boolean result = false;
        try {
            classUnderTest.saveUsername(null, mockHttpServletRequest,
                mockHttpServletResponse);
            classUnderTest.saveUsername(USERNAME,
                mockHttpServletRequest, mockHttpServletResponse);
            result = true;
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        return result;
    }
    
    @Test
    void testRemoveAllCookies() {
        boolean result = false;
        try {
            classUnderTest.removeAllCookies(mockHttpServletRequest, mockHttpServletResponse);
            result = true;
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertTrue(result, TRUE);
    }
}
