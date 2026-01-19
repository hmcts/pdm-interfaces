package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import java.util.Optional;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class CookieUtils.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CookieUtilsTest extends AbstractJUnit {

    static {
        // Set PDDA_COOKIE_SECRET_KEY for test if not already set
        if (System.getProperty("PDDA_COOKIE_SECERET_KEY") == null
            && System.getenv("PDDA_COOKIE_SECRET_KEY") == null) {
            System.getProperty("PDDA_COOKIE_SECRET_KEY",
                    "test-cookie-secret-key-for-testing-only-t42312st");
        }
    }

    private static final String NAME = "NAME";
    private static final String INVALID = "INVALID";
    private static final String VALUE = "Value";
    private static final String NOTNULL = "Result is Null";
    private static final String TRUE = "Result is False";

    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Mock
    private HttpServletResponse mockHttpServletResponse;

    @Mock
    private Cookie mockCookie;

    /**
     * Teardown.
     */
    @AfterEach
    public void teardown() {
        new CookieUtils();
    }
    
    @Test
    void testGetCookie() {
        Cookie[] cookies = {mockCookie};
        Mockito.when(mockHttpServletRequest.getCookies()).thenReturn(cookies);
        Mockito.when(mockCookie.getName()).thenReturn(NAME);
        Optional<Cookie> result = CookieUtils.getCookie(mockHttpServletRequest, NAME);
        assertNotNull(result, NOTNULL);
        result = CookieUtils.getCookie(mockHttpServletRequest, INVALID);
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testNullList() {
        Mockito.when(mockHttpServletRequest.getCookies()).thenReturn(null);
        Optional<Cookie> result = CookieUtils.getCookie(mockHttpServletRequest, INVALID);
        assertNotNull(result, NOTNULL);
        CookieUtils.deleteCookie(mockHttpServletRequest, mockHttpServletResponse, INVALID);
    }
    
    @Test
    void testEmptyList() {
        Cookie[] cookies = {};
        Mockito.when(mockHttpServletRequest.getCookies()).thenReturn(cookies);
        Optional<Cookie> result = CookieUtils.getCookie(mockHttpServletRequest, NAME);
        assertNotNull(result, NOTNULL);
        CookieUtils.deleteCookie(mockHttpServletRequest, mockHttpServletResponse, INVALID);
    }

    @Test
    void testAddCookie() {
        boolean result = false;
        try {
            CookieUtils.addCookie(mockHttpServletResponse, NAME, VALUE, 600);
            result = true;
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertTrue(result, TRUE);
    }

    @Test
    void testDeleteCookie() {
        boolean result = false;
        try {
            Cookie[] cookies = {mockCookie};
            Mockito.when(mockHttpServletRequest.getCookies()).thenReturn(cookies);
            Mockito.when(mockCookie.getName()).thenReturn(NAME);
            CookieUtils.deleteCookie(mockHttpServletRequest, mockHttpServletResponse, NAME);
            CookieUtils.deleteCookie(mockHttpServletRequest, mockHttpServletResponse, INVALID);
            result = true;
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertTrue(result, TRUE);
    }
    
    @Test
    void testSerializer() {
        String result = CookieUtils.serialize(VALUE);
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testDeserializer() {
        String serialized = CookieUtils.serialize(VALUE);
        Mockito.when(mockCookie.getValue()).thenReturn(serialized);
        String result = CookieUtils.deserialize(mockCookie, VALUE.getClass());
        assertNotNull(result, NOTNULL);
    }
}
