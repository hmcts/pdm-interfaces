package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class CookieUtils.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings({"PMD.TooManyMethods", "PMD.CloseResource"})
class CookieUtilsTest extends AbstractJUnit {

    private static final String TEST_SECRET_KEY = "test-cookie-secret-key-for-testing-only-t42312st";

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
        try (MockedStatic<CookieUtils> cookieUtilsMock = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            cookieUtilsMock.when(CookieUtils::getSecretKey).thenReturn(TEST_SECRET_KEY);
            String result = CookieUtils.serialize(VALUE);
            assertNotNull(result, NOTNULL);
        }
    }
    
    @Test
    void testDeserializer() {
        try (MockedStatic<CookieUtils> cookieUtilsMock = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            cookieUtilsMock.when(CookieUtils::getSecretKey).thenReturn(TEST_SECRET_KEY);
            String serialized = CookieUtils.serialize(VALUE);
            Mockito.when(mockCookie.getValue()).thenReturn(serialized);
            String result = CookieUtils.deserialize(mockCookie, VALUE.getClass());
            assertNotNull(result, NOTNULL);
        }
    }

    @Test
    void testDeserializeNullCookie() {
        try (MockedStatic<CookieUtils> ignored = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            String result = CookieUtils.deserialize(null, String.class);
            assertNull(result, "Should return null for null cookie");
        }
    }

    @Test
    void testDeserializeNullCookieValue() {
        try (MockedStatic<CookieUtils> ignored = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            Mockito.when(mockCookie.getValue()).thenReturn(null);
            String result = CookieUtils.deserialize(mockCookie, String.class);
            assertNull(result, "Should return null for null cookie value");
        }
    }

    @Test
    void testDeserializeEmptyCookieValue() {
        try (MockedStatic<CookieUtils> ignored = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            Mockito.when(mockCookie.getValue()).thenReturn("");
            String result = CookieUtils.deserialize(mockCookie, String.class);
            assertNull(result, "Should return null for empty cookie value");
        }
    }

    @Test
    void testDeserializeNoDelimiter() {
        try (MockedStatic<CookieUtils> ignored = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            // Create invalid Base64 that decodes to a string without delimiter
            String invalidValue = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("no-delimiter-here".getBytes());
            Mockito.when(mockCookie.getValue()).thenReturn(invalidValue);
            String result = CookieUtils.deserialize(mockCookie, String.class);
            assertNull(result, "Should return null when no delimiter found");
        }
    }

    @Test
    void testDeserializeInvalidBase64() {
        try (MockedStatic<CookieUtils> ignored = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            Mockito.when(mockCookie.getValue()).thenReturn("not-valid-base64!!!");
            String result = CookieUtils.deserialize(mockCookie, String.class);
            assertNull(result, "Should return null for invalid Base64");
        }
    }

    @Test
    void testDeserializeHmacMismatch() {
        try (MockedStatic<CookieUtils> cookieUtilsMock = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            cookieUtilsMock.when(CookieUtils::getSecretKey).thenReturn(TEST_SECRET_KEY);
            // Create a valid serialized value
            String serialized = CookieUtils.serialize(VALUE);
            // Tamper with it by changing the HMAC
            String tamperedValue = serialized.substring(0, serialized.length() - 5) + "XXXXX";
            Mockito.when(mockCookie.getValue()).thenReturn(tamperedValue);
            String result = CookieUtils.deserialize(mockCookie, String.class);
            assertNull(result, "Should return null when HMAC doesn't match");
        }
    }

    @Test
    void testSerializeNullObject() {
        try (MockedStatic<CookieUtils> cookieUtilsMock = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            cookieUtilsMock.when(CookieUtils::getSecretKey).thenReturn(TEST_SECRET_KEY);
            String result = CookieUtils.serialize(null);
            assertNotNull(result, NOTNULL);
        }
    }

    @Test
    void testSerializeNonStringObject() {
        try (MockedStatic<CookieUtils> cookieUtilsMock = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            cookieUtilsMock.when(CookieUtils::getSecretKey).thenReturn(TEST_SECRET_KEY);
            // Test with an Integer
            Integer testInt = 42;
            String result = CookieUtils.serialize(testInt);
            assertNotNull(result, NOTNULL);
            // Verify we can deserialize it back
            Mockito.when(mockCookie.getValue()).thenReturn(result);
            Integer deserialized = CookieUtils.deserialize(mockCookie, Integer.class);
            assertNotNull(deserialized, NOTNULL);
            assertTrue(deserialized.equals(testInt), "Deserialized value should match original");
        }
    }

    @Test
    void testGetSecretKeyThrowsWhenNotSet() {
        // Test that serialize throws when secret key is not configured
        // We mock getSecretKey to throw the exception that would occur
        // when the environment variable is not set
        try (MockedStatic<CookieUtils> cookieUtilsMock = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            cookieUtilsMock.when(CookieUtils::getSecretKey).thenThrow(
                new CookieSerializationException("Cookie secret key not configured. Set "
                    + "PDDA_COOKIE_SECRET_KEY environment variable."));
            assertThrows(CookieSerializationException.class, () -> {
                CookieUtils.serialize(VALUE);
            }, "Should throw when secret key is not configured");
        }
    }

    @Test
    void testSerializeWithComplexObject() {
        try (MockedStatic<CookieUtils> cookieUtilsMock = Mockito.mockStatic(CookieUtils.class, 
                Mockito.CALLS_REAL_METHODS)) {
            cookieUtilsMock.when(CookieUtils::getSecretKey).thenReturn(TEST_SECRET_KEY);
            // Test serialization of a complex object (Map)
            java.util.Map<String, Object> testMap = new java.util.concurrent.ConcurrentHashMap<>();
            testMap.put("key1", "value1");
            testMap.put("key2", 123);
            String result = CookieUtils.serialize(testMap);
            assertNotNull(result, NOTNULL);
            // Verify we can deserialize it back
            Mockito.when(mockCookie.getValue()).thenReturn(result);
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> deserialized = CookieUtils.deserialize(mockCookie, 
                (Class<java.util.Map<String, Object>>) (Class<?>) java.util.Map.class);
            assertNotNull(deserialized, NOTNULL);
        }
    }
}
