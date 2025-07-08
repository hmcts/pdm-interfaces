/*
 * Copyrights and Licenses
 * 
 * Copyright (c) 2015-2016 by the Ministry of Justice. All rights reserved. Redistribution and use
 * in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer. - Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution. - Products derived
 * from this software may not be called "XHIBIT Public Display Manager" nor may
 * "XHIBIT Public Display Manager" appear in their names without prior written permission of the
 * Ministry of Justice. - Redistributions of any form whatsoever must retain the following
 * acknowledgment: "This product includes XHIBIT Public Display Manager." This software is provided
 * "as is" and any expressed or implied warranties, including, but not limited to, the implied
 * warranties of merchantability and fitness for a particular purpose are disclaimed. In no event
 * shall the Ministry of Justice or its contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of
 * substitute goods or services; loss of use, data, or profits; or business interruption). However
 * caused any on any theory of liability, whether in contract, strict liability, or tort (including
 * negligence or otherwise) arising in any way out of the use of this software, even if advised of
 * the possibility of such damage.
 */

package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CourtSiteJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CourtSiteStatusJson;
import uk.gov.hmcts.pdm.publicdisplay.common.rest.JsonRequest;
import uk.gov.hmcts.pdm.publicdisplay.common.rest.JsonRequestFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class LocalProxyRestClientTest.
 *
 * @author boparaij
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class LocalProxyRestClientTest extends LocalProxyCourtSiteStatus {

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new LocalProxyRestClient();

        // Setup the mock version of the called classes
        mockJsonRequestFactory = createMock(JsonRequestFactory.class);
        mockJsonRequest = createMock(JsonRequest.class);
        mockServiceAuditService = createMock(ServiceAuditService.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "timeout", TIMEOUT);
        ReflectionTestUtils.setField(classUnderTest, "expiry", EXPIRY);
        ReflectionTestUtils.setField(classUnderTest, "jsonRequestFactory", mockJsonRequestFactory);
        ReflectionTestUtils.setField(classUnderTest, "serviceAuditService",
            mockServiceAuditService);
    }

    /**
     * Test court site status.
     */
    @Test
    void testCourtSiteStatus() {
        // Capture the Json request
        final Capture<CourtSiteJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        expect(mockJsonRequestFactory.getJsonRequest()).andReturn(mockJsonRequest);
        replay(mockJsonRequestFactory);
        mockCreateJsonRequest(
            (String) ReflectionTestUtils.getField(classUnderTest, "RAG_STATUS_PATH"));
        expect(mockJsonRequest.sendRequest(capture(capturedJsonRequest),
            eq(CourtSiteStatusJson.class))).andReturn(testCourtSiteStatusJsons);
        replay(mockJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "RAG_STATUS_SERVICE"));

        // Perform the test
        classUnderTest.getCourtSiteStatus(testLocalProxyWithCourtSite);

        // Assert that the objects are as expected
        assertEquals(capturedJsonRequest.getValue().getSiteId(),
            testLocalProxyWithCourtSite.getCourtSite().getId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test court site status runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testCourtSiteStatusRuntimeError() throws Exception {
        // Capture the Json request
        final Capture<CourtSiteJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        expect(mockJsonRequestFactory.getJsonRequest()).andReturn(mockJsonRequest);
        replay(mockJsonRequestFactory);
        mockCreateJsonRequest(
            (String) ReflectionTestUtils.getField(classUnderTest, "RAG_STATUS_PATH"));
        expect(mockJsonRequest.sendRequest(capture(capturedJsonRequest),
            eq(CourtSiteStatusJson.class))).andThrow(new RestException(MOCK_REST_EXCEPTION));
        replay(mockJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "RAG_STATUS_SERVICE"));

        try {
            // Perform the test
            classUnderTest.getCourtSiteStatus(testLocalProxyWithCourtSite);
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
     * Test restart cdu.
     */
    @Test
    void testRestartCdu() {
        // Capture the Json request
        final Capture<List<CduJson>> capturedJsonRequest = newCapture();

        final List<String> ipAddresses = new ArrayList<>();
        for (CduJson cduJson : testCduJsons) {
            ipAddresses.add(cduJson.getIpAddress());
        }

        // Add the mock calls to child classes
        mockJsonRequestWithCapture(
            (String) ReflectionTestUtils.getField(classUnderTest, "RESTART_CDU_PATH"),
            capturedJsonRequest);
        mockServiceAudit(
            (String) ReflectionTestUtils.getField(classUnderTest, "RESTART_CDU_SERVICE"));

        // Perform the test
        classUnderTest.restartCdu(testLocalProxyWithCourtSite, ipAddresses);

        // Assert that the objects are as expected
        assertEquals(capturedJsonRequest.getValue().size(), ipAddresses.size(), NOT_EQUAL);
        for (CduJson cduJson : capturedJsonRequest.getValue()) {
            assertTrue(ipAddresses.contains(cduJson.getIpAddress()), FALSE);
        }

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Test get cdu screenshot.
     */
    @Test
    void testGetCduScreenshot() {
        final byte[] cduScreenshot = getTestByteArray();

        // Capture the Json request
        final Capture<CduJson> capturedJsonRequest = newCapture();

        // Add the mock calls to child classes
        expect(mockJsonRequestFactory.getJsonRequest()).andReturn(mockJsonRequest);
        replay(mockJsonRequestFactory);
        mockCreateJsonRequest(
            (String) ReflectionTestUtils.getField(classUnderTest, "SCREENSHOT_CDU_PATH"));
        expect(mockJsonRequest.sendRequest(capture(capturedJsonRequest), eq(byte[].class)))
            .andReturn(cduScreenshot);
        replay(mockJsonRequest);
        mockServiceAuditException(
            (String) ReflectionTestUtils.getField(classUnderTest, "SCREENSHOT_CDU_SERVICE"));

        // Perform the test
        final byte[] result =
            classUnderTest.getCduScreenshot(testLocalProxyWithCourtSite, IPADDRESS_PREFIX + 1);

        // Assert that the objects are as expected
        assertTrue(Arrays.equals(cduScreenshot, result), FALSE);

        // Verify the expected mocks were called
        verify(mockJsonRequestFactory);
        verify(mockJsonRequest);
        verify(mockServiceAuditService);
    }

    /**
     * Mock service audit.
     *
     * @param service the service
     */
    protected void mockServiceAudit(final String service) {
        mockServiceAuditService.auditService(service, mockJsonRequest);
        expectLastCall();
        replay(mockServiceAuditService);
    }

    /**
     * Mock service audit throwing exception.
     *
     * @param service the service
     */
    private void mockServiceAuditException(final String service) {
        mockServiceAuditService.auditService(service, mockJsonRequest);
        expectLastCall().andThrow(new DataRetrievalFailureException("Mock data access exception"));
        replay(mockServiceAuditService);
    }

    /**
     * Gets the test byte array.
     *
     * @return the test byte array
     */
    private byte[] getTestByteArray() {
        final byte[] bytes = new byte[100];
        new Random().nextBytes(bytes);
        return bytes;
    }
}
