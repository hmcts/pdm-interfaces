package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.json.MappingJson;

import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class LocalProxyMappingCrudTest.
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class LocalProxyMappingCrudTest extends LocalProxyUrlCrudTest {

    /**
     * Test save mapping.
     */
    @Test
    void testSaveMapping() {
        // Capture the URL
        final Capture<MappingJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithCapture(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_MAPPING_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_MAPPING_SERVICE"));

        // Perform the test
        classUnderTest.saveMapping(testCdu, testUrl);

        // Assert that the objects are as expected
        assertEquals(capturedJsonRequest.getValue().getUniqueUrlId(), testUrl.getId(), NOT_EQUAL);
        assertEquals(capturedJsonRequest.getValue().getMacAddress(), testCdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test save mapping runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testSaveMappingRuntimeError() throws Exception {
        // Capture the URL
        final Capture<MappingJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithException(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_MAPPING_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_MAPPING_SERVICE"));

        try {
            // Perform the test
            classUnderTest.saveMapping(testCdu, testUrl);
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
     * Test delete mapping.
     */
    @Test
    void testDeleteMapping() {
        // Capture the URL
        final Capture<MappingJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithCapture(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_MAPPING_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_MAPPING_SERVICE"));

        // Perform the test
        classUnderTest.deleteMapping(testCdu, testUrl);

        // Assert that the objects are as expected
        assertEquals(capturedJsonRequest.getValue().getUniqueUrlId(), testUrl.getId(), NOT_EQUAL);
        assertEquals(capturedJsonRequest.getValue().getMacAddress(), testCdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test delete mapping runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testDeleteMappingRuntimeError() throws Exception {
        // Capture the URL
        final Capture<MappingJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithException(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_MAPPING_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_MAPPING_SERVICE"));

        try {
            // Perform the test
            classUnderTest.deleteMapping(testCdu, testUrl);
        } catch (Exception e) {
            assertEquals(e.getClass(), RestException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockJsonRequestFactory);
            verify(mockJsonRequest);
            verify(mockServiceAuditService);
        }
    }

}
