package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("PMD")
public class CookieUtils {

    private static final Logger LOG = LoggerFactory.getLogger(CookieUtils.class);
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String COOKIE_SECRET_KEY_ENV = "PDDA_COOKIE_SECRET_KEY";
    private static final String DELIMITER = ":";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected CookieUtils() {
        // Protected constructor
    }
    
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * Serialize object to JSON, sign with HMAC, and Base64 encode.
     * Step 1: Starting serialization
     * Step 2: Converting object to JSON
     * Step 3: JSON serialization
     * Step 4: HMAC signing
     * Step 5: Base64 encoding
     * Step 6: Success
     */
    public static String serialize(Object object) {
        LOG.info("Step 1: Starting serialization for object type: {}", 
            object != null ? object.getClass().getSimpleName() : "null");
        
        try {
            // Step 2: Converting object to JSON
            LOG.info("Step 2: Converting object to JSON");
            String jsonPayload;
            if (object instanceof String) {
                // For String objects, wrap in quotes to make valid JSON
                jsonPayload = OBJECT_MAPPER.writeValueAsString(object);
            } else {
                jsonPayload = OBJECT_MAPPER.writeValueAsString(object);
            }
            LOG.info("Step 3: JSON serialization completed, length: {}", jsonPayload.length());
            
            // Step 4: HMAC signing
            LOG.info("Step 4: HMAC signing");
            String hmacSignature = computeHmac(jsonPayload);
            LOG.info("Step 4: HMAC signature computed");
            
            // Step 5: Base64 encoding
            LOG.info("Step 5: Base64 encoding");
            String signedPayload = jsonPayload + DELIMITER + hmacSignature;
            String encodedValue = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(signedPayload.getBytes(StandardCharsets.UTF_8));
            LOG.info("Step 5: Base64 encoding completed");
            
            LOG.info("Step 6: Serialization successful");
            return encodedValue;
        } catch (Exception ex) {
            LOG.error("Step 6: Serialization failed", ex);
            throw new RuntimeException("Failed to serialize object to cookie", ex);
        }
    }

    /**
     * Deserialize cookie value: verify HMAC, decode Base64, and parse JSON.
     * Step 1: Starting deserialization
     * Step 2: Base64 decoding
     * Step 3: Extracting JSON and HMAC
     * Step 4: HMAC verification
     * Step 5: JSON deserialization
     * Step 6: Success/failure
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        LOG.info("Step 1: Starting deserialization for class: {}", cls.getSimpleName());
        
        if (cookie == null || cookie.getValue() == null || cookie.getValue().isEmpty()) {
            LOG.error("Step 6: Deserialization failed - cookie or value is null/empty");
            return null;
        }
        
        try {
            // Step 2: Base64 decoding
            LOG.info("Step 2: Base64 decoding");
            byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());
            String decodedValue = new String(decodedBytes, StandardCharsets.UTF_8);
            LOG.info("Step 2: Base64 decoding completed");
            
            // Step 3: Extracting JSON and HMAC
            LOG.info("Step 3: Extracting JSON and HMAC signature");
            int delimiterIndex = decodedValue.lastIndexOf(DELIMITER);
            if (delimiterIndex == -1) {
                LOG.error("Step 6: Deserialization failed - no HMAC delimiter found");
                return null;
            }
            
            String jsonPayload = decodedValue.substring(0, delimiterIndex);
            String hmacSignature = decodedValue.substring(delimiterIndex + 1);
            LOG.info("Step 3: JSON and HMAC extracted, JSON length: {}", jsonPayload.length());
            
            // Step 4: HMAC verification
            LOG.info("Step 4: HMAC verification");
            String computedHmac = computeHmac(jsonPayload);
            if (!computedHmac.equals(hmacSignature)) {
                LOG.error("Step 6: Deserialization failed - "
                        + "HMAC verification failed. Cookie may have been tampered with.");
                return null;
            }
            LOG.info("Step 4: HMAC verification successful");
            
            // Step 5: JSON deserialization
            LOG.info("Step 5: JSON deserialization");
            T result;
            if (String.class.equals(cls)) {
                // For String, unwrap JSON string quotes
                result = cls.cast(OBJECT_MAPPER.readValue(jsonPayload, String.class));
            } else {
                result = OBJECT_MAPPER.readValue(jsonPayload, cls);
            }
            LOG.info("Step 5: JSON deserialization completed");
            
            LOG.info("Step 6: Deserialization successful");
            return result;
        } catch (IllegalArgumentException ex) {
            LOG.error("Step 6: Deserialization failed - Base64 decode error", ex);
            return null;
        } catch (Exception ex) {
            LOG.error("Step 6: Deserialization failed", ex);
            return null;
        }
    }

    /**
     * Compute HMAC-SHA256 signature for the given payload.
     */
    private static String computeHmac(String payload) {
        try {
            String secretKey = System.getenv(COOKIE_SECRET_KEY_ENV);
            if (secretKey == null || secretKey.isEmpty()) {
                LOG.error("HMAC secret key not found in environment variable: {}", COOKIE_SECRET_KEY_ENV);
                throw new RuntimeException("Cookie secret key not configured. Set "
                        + COOKIE_SECRET_KEY_ENV + " environment variable.");
            }
            
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8), 
                HMAC_ALGORITHM
            );
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(hmacBytes);
        } catch (Exception ex) {
            LOG.error("Failed to compute HMAC", ex);
            throw new RuntimeException("Failed to compute HMAC signature", ex);
        }
    }

}