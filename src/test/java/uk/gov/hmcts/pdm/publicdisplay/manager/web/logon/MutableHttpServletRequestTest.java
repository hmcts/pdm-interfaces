package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The Class MutableHttpServletRequest.
 *
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MutableHttpServletRequestTest extends AbstractJUnit {

    private static final String NAME = "Test";
    private static final String VALUE = "Value";
    private static final String EQUAL = "Result is not Equal";
    private static final String NOTNULL = "Result is Null";
    private static final String NULL = "Result is not Null";

    private HttpServletRequest mockHttpServletRequest;

    private MutableHttpServletRequest classUnderTest;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);

        classUnderTest = new MutableHttpServletRequest(mockHttpServletRequest);
    }

    @Test
    void testAddHeader() {
        String result = classUnderTest.getHeader(NAME);
        assertNull(result, NULL);
        classUnderTest.addHeader(NAME, VALUE);
        result = classUnderTest.getHeader(NAME);
        assertNotNull(result, NOTNULL);
        assertEquals(VALUE, result, EQUAL);
    }

    @Test
    void testGetheaderNames() {
        Set<String> dummyHeaderNames = new HashSet<>();
        dummyHeaderNames.add(NAME);
        Mockito.when(mockHttpServletRequest.getHeaderNames())
            .thenReturn(Collections.enumeration(dummyHeaderNames));
        Enumeration<String> result = classUnderTest.getHeaderNames();
        assertNotNull(result, NOTNULL);
    }
}
