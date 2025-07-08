package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduJson;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;

import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class LocalProxyCduCrudTest.
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class LocalProxyCduCrudTest extends LocalProxyCrudTest {

    /**
     * Test cdu save.
     */
    @Test
    void testSaveCdu() {
        // Capture the Json request
        final Capture<CduJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithCapture(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_CDU_PATH"),
            capturedJsonRequest);
        mockServiceAudit((String) ReflectionTestUtils.getField(classUnderTest, "SAVE_CDU_SERVICE"));

        // Perform the test
        classUnderTest.saveCdu(testCdu);

        // Assert that the objects are as expected
        assertCduJson(capturedJsonRequest.getValue(), testCdu);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test save cdu runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testSaveCduRuntimeError() throws Exception {
        // Capture the Json request
        final Capture<CduJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithException(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_CDU_PATH"),
            capturedJsonRequest);
        mockServiceAudit((String) ReflectionTestUtils.getField(classUnderTest, "SAVE_CDU_SERVICE"));

        try {
            // Perform the test
            classUnderTest.saveCdu(testCdu);
        } catch (Exception e) {
            assertEquals(e.getClass(), RestException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockJsonRequestFactory);
            verify(mockJsonRequest);
            verify(mockServiceAuditService);
        }
    }

    /**
     * Test delete cdu.
     */
    @Test
    void testDeleteCdu() {
        // Capture the Json request
        final Capture<CduJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithCapture(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_CDU_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_CDU_SERVICE"));

        // Perform the test
        classUnderTest.deleteCdu(testCdu);

        // Assert that the objects are as expected
        assertEquals(capturedJsonRequest.getValue().getMacAddress(), testCdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test delete cdu runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testDeleteCduRuntimeError() throws Exception {
        // Capture the Json request
        final Capture<CduJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithException(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_CDU_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_CDU_SERVICE"));

        try {
            // Perform the test
            classUnderTest.deleteCdu(testCdu);
        } catch (Exception e) {
            assertEquals(e.getClass(), RestException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockJsonRequestFactory);
            verify(mockJsonRequest);
            verify(mockServiceAuditService);
        }
    }

    /**
     * Assert cdu json.
     *
     * @param cduJson the cdu json
     * @param testCdu the test cdu
     */
    private void assertCduJson(final CduJson cduJson, final ICduModel testCdu) {
        // assertEquals (cduJson.getSiteId (), testCdu.getCourtSite ().getId ());
        assertEquals(cduJson.getCduNumber(), testCdu.getCduNumber(), NOT_EQUAL);
        assertEquals(cduJson.getTitle(), testCdu.getTitle(), NOT_EQUAL);
        assertEquals(cduJson.getDescription(), testCdu.getDescription(), NOT_EQUAL);
        assertEquals(cduJson.getLocation(), testCdu.getLocation(), NOT_EQUAL);
        assertEquals(cduJson.getRefresh(), testCdu.getRefresh(), NOT_EQUAL);
        assertEquals(cduJson.getIpAddress(), testCdu.getIpAddress(), NOT_EQUAL);
        assertEquals(cduJson.getMacAddress(), testCdu.getMacAddress(), NOT_EQUAL);
        assertEquals(cduJson.getNotification(), testCdu.getNotification(), NOT_EQUAL);
        assertEquals(cduJson.getOfflineIndicator(), testCdu.getOfflineIndicator(), NOT_EQUAL);
    }

}
