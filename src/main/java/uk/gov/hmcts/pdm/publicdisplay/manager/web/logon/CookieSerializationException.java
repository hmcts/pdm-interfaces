package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import java.io.Serial;

/**
 * Exception thrown when cookie serialization or deserialization fails.
 */
public class CookieSerializationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CookieSerializationException(String message) {
        super(message);
    }

    public CookieSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
