package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CourtSiteJson;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class LocalProxyCduListTest.
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
class LocalProxyCduListTest extends LocalProxyMappingCrudTest {

    /**
     * Test list cdu.
     */
    @Test
    void testListCdu() {
        // Capture the Json request
        final Capture<CourtSiteJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        expect(mockJsonRequestFactory.getJsonRequest()).andReturn(mockJsonRequest);
        replay(mockJsonRequestFactory);
        mockCreateJsonRequest(
            (String) ReflectionTestUtils.getField(classUnderTest, "LIST_CDU_PATH"));
        expect(mockJsonRequest.sendRequest(capture(capturedJsonRequest), eq(CduJson[].class)))
            .andReturn(testCduJsons);
        replay(mockJsonRequest);
        mockServiceAudit((String) ReflectionTestUtils.getField(classUnderTest, "LIST_CDU_SERVICE"));

        // Perform the test
        classUnderTest.getCdus(testLocalProxyWithCourtSite);

        // Assert that the objects are as expected
        assertEquals(capturedJsonRequest.getValue().getSiteId(),
            testLocalProxyWithCourtSite.getCourtSite().getId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test list cdu runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testListCduRuntimeError() throws Exception {
        // Capture the Json request
        final Capture<CourtSiteJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        expect(mockJsonRequestFactory.getJsonRequest()).andReturn(mockJsonRequest);
        replay(mockJsonRequestFactory);
        mockCreateJsonRequest(
            (String) ReflectionTestUtils.getField(classUnderTest, "LIST_CDU_PATH"));
        expect(mockJsonRequest.sendRequest(capture(capturedJsonRequest), eq(CduJson[].class)))
            .andThrow(new RestException(MOCK_REST_EXCEPTION));
        replay(mockJsonRequest);
        mockServiceAudit((String) ReflectionTestUtils.getField(classUnderTest, "LIST_CDU_SERVICE"));

        try {
            // Perform the test
            classUnderTest.getCdus(testLocalProxyWithCourtSite);
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
