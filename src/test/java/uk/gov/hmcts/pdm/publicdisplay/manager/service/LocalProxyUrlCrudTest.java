package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.json.UrlJson;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;

import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class LocalProxyUrlCrudTest.
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class LocalProxyUrlCrudTest extends LocalProxyCduCrudTest {

    /**
     * Test save url.
     */
    @Test
    void testSaveUrl() {
        // Capture the URL
        final Capture<UrlJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithCapture(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_URL_PATH"),
            capturedJsonRequest);
        mockServiceAudit((String) ReflectionTestUtils.getField(classUnderTest, "SAVE_URL_SERVICE"));

        // Perform the test
        classUnderTest.saveUrl(testUrl);

        // Assert that the objects are as expected
        assertUrlJson(capturedJsonRequest.getValue(), testUrl);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test save url runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testSaveUrlRuntimeError() throws Exception {
        // Capture the URL
        final Capture<UrlJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithException(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_URL_PATH"),
            capturedJsonRequest);
        mockServiceAudit((String) ReflectionTestUtils.getField(classUnderTest, "SAVE_URL_SERVICE"));

        try {
            // Perform the test
            classUnderTest.saveUrl(testUrl);
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
     * Test delete url.
     */
    @Test
    void testDeleteUrl() {
        // Capture the URL
        final Capture<UrlJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithCapture(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_URL_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_URL_SERVICE"));

        // Perform the test
        classUnderTest.deleteUrl(testUrl);

        // Assert that the objects are as expected
        assertEquals(capturedJsonRequest.getValue().getUniqueUrlId(), testUrl.getId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test delete url runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testDeleteUrlRuntimeError() throws Exception {
        // Capture the URL
        final Capture<UrlJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithException(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_URL_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_URL_SERVICE"));

        try {
            // Perform the test
            classUnderTest.deleteUrl(testUrl);
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
     * Assert url json.
     *
     * @param urlJson the url json
     * @param testUrl the test url
     */
    private void assertUrlJson(final UrlJson urlJson, final IUrlModel testUrl) {
        assertEquals(urlJson.getUniqueUrlId(), testUrl.getId(), NOT_EQUAL);
        assertEquals(urlJson.getUrl(), testUrl.getUrl(), NOT_EQUAL);
        assertEquals(urlJson.getDescription(), testUrl.getDescription(), NOT_EQUAL);
    }
}
