package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CourtSiteJson;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * The Class LocalProxyCrudTest.
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class LocalProxyCrudTest extends LocalProxyRestClientTest {

    /**
     * Test save local proxy.
     */
    @Test
    void testSaveLocalProxy() {
        // update notification field
        final boolean updateNotification = true;
        // CourtSiteJson returning from local proxy
        final CourtSiteJson testCourtSiteJson = getCourtSiteJsonTest();
        // Capture the Json request
        final Capture<CourtSiteJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        expect(mockJsonRequestFactory.getJsonRequest()).andReturn(mockJsonRequest);
        replay(mockJsonRequestFactory);
        mockCreateJsonRequest(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_SITE_PATH"));
        expect(mockJsonRequest.sendRequest(capture(capturedJsonRequest), eq(CourtSiteJson.class)))
            .andReturn(testCourtSiteJson);
        replay(mockJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_SITE_SERVICE"));

        // Perform the test
        final String hostname =
            classUnderTest.saveLocalProxy(testLocalProxyWithCourtSite, updateNotification);

        // Assert that the objects are as expected
        assertEquals(capturedJsonRequest.getValue().getSiteId(),
            testLocalProxyWithCourtSite.getCourtSite().getId(), NOT_EQUAL);
        assertEquals(capturedJsonRequest.getValue().getTitle(),
            testLocalProxyWithCourtSite.getCourtSite().getTitle(), NOT_EQUAL);
        assertEquals(capturedJsonRequest.getValue().getPageUrl(),
            testLocalProxyWithCourtSite.getCourtSite().getPageUrl(), NOT_EQUAL);
        assertEquals(capturedJsonRequest.getValue().getNotification(),
            testLocalProxyWithCourtSite.getCourtSite().getNotification(), NOT_EQUAL);
        assertEquals(capturedJsonRequest.getValue().getPowersaveSchedule(),
            testLocalProxyWithCourtSite.getCourtSite().getSchedule().getDetail(), NOT_EQUAL);
        assertEquals(testCourtSiteJson.getHostName(), hostname, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test save local proxy amend no notification update.
     */
    @Test
    void testSaveLocalProxyAmendNoNotificationUpdate() {
        // update navigation field
        final boolean updateNavigation = false;
        // CourtSiteJson returning from local proxy
        final CourtSiteJson testCourtSiteJson = getCourtSiteJsonTest();
        // Capture the Json request
        final Capture<CourtSiteJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        expect(mockJsonRequestFactory.getJsonRequest()).andReturn(mockJsonRequest);
        replay(mockJsonRequestFactory);
        mockCreateJsonRequest(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_SITE_PATH"));
        expect(mockJsonRequest.sendRequest(capture(capturedJsonRequest), eq(CourtSiteJson.class)))
            .andReturn(testCourtSiteJson);
        replay(mockJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_SITE_SERVICE"));

        // Perform the test
        final String hostname =
            classUnderTest.saveLocalProxy(testLocalProxyWithCourtSite, updateNavigation);

        // Assert that the objects are as expected
        assertEquals(capturedJsonRequest.getValue().getSiteId(),
            testLocalProxyWithCourtSite.getCourtSite().getId(), NOT_EQUAL);
        assertEquals(capturedJsonRequest.getValue().getTitle(),
            testLocalProxyWithCourtSite.getCourtSite().getTitle(), NOT_EQUAL);
        assertEquals(capturedJsonRequest.getValue().getPageUrl(),
            testLocalProxyWithCourtSite.getCourtSite().getPageUrl(), NOT_EQUAL);
        assertEquals(null, capturedJsonRequest.getValue().getNotification(), NOT_EQUAL);
        assertEquals(capturedJsonRequest.getValue().getPowersaveSchedule(),
            testLocalProxyWithCourtSite.getCourtSite().getSchedule().getDetail(), NOT_EQUAL);
        assertEquals(testCourtSiteJson.getHostName(), hostname, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test save local proxy runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testSaveLocalProxyRuntimeError() throws Exception {
        // Capture the Json request
        final Capture<CourtSiteJson> capturedJsonRequest = newCapture();

        // Local Variables
        // update notification field
        final boolean updateNotification = true;

        // Add the mock calls to child classes
        expect(mockJsonRequestFactory.getJsonRequest()).andReturn(mockJsonRequest);
        replay(mockJsonRequestFactory);
        mockCreateJsonRequest(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_SITE_PATH"));
        expect(mockJsonRequest.sendRequest(capture(capturedJsonRequest), eq(CourtSiteJson.class)))
            .andThrow(new RestException(MOCK_REST_EXCEPTION));
        replay(mockJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "SAVE_SITE_SERVICE"));

        try {
            // Perform the test
            classUnderTest.saveLocalProxy(testLocalProxyWithCourtSite, updateNotification);
            fail("RestException was not thrown");
        } catch (Exception e) {
            assertEquals(e.getClass(), RestException.class, NOT_EQUAL);
        } finally {
            // Assert that the objects are as expected
            assertEquals(capturedJsonRequest.getValue().getSiteId(),
                testLocalProxyWithCourtSite.getCourtSite().getId(), NOT_EQUAL);
            assertEquals(capturedJsonRequest.getValue().getTitle(),
                testLocalProxyWithCourtSite.getCourtSite().getTitle(), NOT_EQUAL);
            assertEquals(capturedJsonRequest.getValue().getPageUrl(),
                testLocalProxyWithCourtSite.getCourtSite().getPageUrl(), NOT_EQUAL);
            assertEquals(capturedJsonRequest.getValue().getNotification(),
                testLocalProxyWithCourtSite.getCourtSite().getNotification(), NOT_EQUAL);
            assertEquals(capturedJsonRequest.getValue().getPowersaveSchedule(),
                testLocalProxyWithCourtSite.getCourtSite().getSchedule().getDetail(), NOT_EQUAL);

            // Verify the expected mocks were called
            verify(mockJsonRequestFactory);
            verify(mockJsonRequest);
            verify(mockServiceAuditService);
        }
    }

    /**
     * Test delete local proxy.
     */
    @Test
    void testDeleteLocalProxy() {
        // Capture the Json request
        final Capture<CourtSiteJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithCapture(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_SITE_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_SITE_SERVICE"));

        // Perform the test
        classUnderTest.deleteLocalProxy(testLocalProxyWithCourtSite);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test delete local proxy runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testDeleteLocalProxyRuntimeError() throws Exception {
        // Capture the Json request
        final Capture<CourtSiteJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestWithException(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_SITE_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "DELETE_SITE_SERVICE"));

        try {
            // Perform the test
            classUnderTest.deleteLocalProxy(testLocalProxyWithCourtSite);
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
     * Gets the court site json test.
     *
     * @return the court site json test
     */
    protected CourtSiteJson getCourtSiteJsonTest() {
        final CourtSiteJson testCourtSiteJson = new CourtSiteJson();
        testCourtSiteJson.setGeneratedBy("GENBYJSON");
        testCourtSiteJson.setHostName("HOSTNAMEJSON");
        testCourtSiteJson.setPageUrl("PAGEURLJSON");
        testCourtSiteJson.setPowersaveSchedule("SCHEDULEJSON");
        testCourtSiteJson.setSiteId(100L);
        testCourtSiteJson.setTitle("TITLEJSON");
        testCourtSiteJson.setWelshTitle("WELSHJSON");
        testCourtSiteJson.setNotification("NOTIFICATION");

        return testCourtSiteJson;
    }


}
