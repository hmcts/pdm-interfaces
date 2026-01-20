package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test class for CookieSerializationException.
 */
class CookieSerializationExceptionTest {

    // PMD MESSAGE FIXES
    private static final String TEST_MESSAGE = "Test exception message";
    private static final String CAUSE_MESSAGE = "Cause exception message";
    private static final String EXCEPTION_NOT_NULL = "Exception should not be null";
    private static final String CAUSE_NOT_NULL = "Cause should not be null";
    private static final String CAUSE_NULL = "Cause should be null";
    private static final String MESSAGE_SHOULD_MATCH = "Message should match";
    private static final String MESSAGE_SHOULD_BE_NULL = "Message should be null";

    @Test
    void testConstructorWithMessage() {
        CookieSerializationException exception = new CookieSerializationException(TEST_MESSAGE);
        
        assertNotNull(exception, EXCEPTION_NOT_NULL);
        assertEquals(TEST_MESSAGE, exception.getMessage(), MESSAGE_SHOULD_MATCH);
        assertNull(exception.getCause(), CAUSE_NULL);
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException(CAUSE_MESSAGE);
        CookieSerializationException exception = new CookieSerializationException(TEST_MESSAGE, cause);
        
        assertNotNull(exception, EXCEPTION_NOT_NULL);
        assertEquals(TEST_MESSAGE, exception.getMessage(), MESSAGE_SHOULD_MATCH);
        assertNotNull(exception.getCause(), CAUSE_NOT_NULL);
        assertSame(cause, exception.getCause(), "Cause should be the same instance");
        assertEquals(CAUSE_MESSAGE, exception.getCause().getMessage(), "Cause message should match");
    }

    @Test
    void testConstructorWithNullMessage() {
        CookieSerializationException exception = new CookieSerializationException((String) null);
        
        assertNotNull(exception, EXCEPTION_NOT_NULL);
        assertNull(exception.getMessage(), MESSAGE_SHOULD_BE_NULL);
    }

    @Test
    void testConstructorWithNullCause() {
        CookieSerializationException exception = new CookieSerializationException(TEST_MESSAGE, null);
        
        assertNotNull(exception, EXCEPTION_NOT_NULL);
        assertEquals(TEST_MESSAGE, exception.getMessage(), MESSAGE_SHOULD_MATCH);
        assertNull(exception.getCause(), CAUSE_NULL);
    }
}
