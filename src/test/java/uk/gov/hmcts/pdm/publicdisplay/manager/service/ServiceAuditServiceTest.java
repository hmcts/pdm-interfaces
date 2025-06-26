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

import jakarta.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrserviceaudit.XhbDispMgrServiceAuditRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.rest.JsonRequest;
import uk.gov.hmcts.pdm.publicdisplay.common.rest.JsonWebTokenType;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.ServiceAudit;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IServiceAudit;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class ServiceAuditServiceTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
class ServiceAuditServiceTest extends AbstractJUnit {

    /** The Constant TEST_SERVICE. */
    private static final String TEST_SERVICE = "SERVICE";

    /** The Constant TEST_URL. */
    private static final String TEST_URL = "URL";

    /** The Constant TEST_MESSAGE_ID. */
    private static final String TEST_MESSAGE_ID = "ID";

    /** The Constant TEST_MESSAGE_STATUS. */
    private static final String TEST_MESSAGE_STATUS = "STATUS";

    /** The Constant TEST_MESSAGE_REQUEST. */
    private static final String TEST_MESSAGE_REQUEST = "REQUEST";

    /** The Constant TEST_MESSAGE_RESPONSE. */
    private static final String TEST_MESSAGE_RESPONSE = "RESPONSE";

    private static final String NOT_EQUAL = "Not equal";
    private static final String NOT_NULL = "Result is Null";

    /** The class under test. */
    private ServiceAuditService classUnderTest;

    /** The mock disp mgr service audit repo. */
    private XhbDispMgrServiceAuditRepository mockServiceAuditRepo;

    /** The mock json request. */
    private JsonRequest mockJsonRequest;
    
    private EntityManager mockEntityManager;
    
    private XhbDispMgrServiceAuditRepository mockXhbDispMgrServiceAuditRepository;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new ServiceAuditService();

        // Setup the mock version of the called classes
        mockServiceAuditRepo = createMock(XhbDispMgrServiceAuditRepository.class);
        mockJsonRequest = createMock(JsonRequest.class);
        mockEntityManager = createMock(EntityManager.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrServiceAuditRepository",
            mockServiceAuditRepo);
    }
    
    /**
     * Test get property value by name.
     */
    @Test
    void testGetPropertyValueByName() {
        // Capture the service audit
        final Capture<ServiceAudit> capturedServiceAudit = newCapture();

        // Add the mock calls to child classes
        mockJsonRequestProperties();
        expect(mockServiceAuditRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        mockServiceAuditRepo.saveDao(capture(capturedServiceAudit));
        expectLastCall();
        
        replay(mockServiceAuditRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.auditService(TEST_SERVICE, mockJsonRequest);

        // Assert that the objects are as expected
        final IServiceAudit serviceAudit = capturedServiceAudit.getValue();
        assertEquals(JsonWebTokenType.DISPLAY_MANAGER.toString(), serviceAudit.getFromEndpoint(),
            NOT_EQUAL);
        assertEquals(JsonWebTokenType.LOCAL_PROXY.toString(), serviceAudit.getToEndpoint(),
            NOT_EQUAL);
        assertEquals(TEST_SERVICE, serviceAudit.getService(), NOT_EQUAL);
        assertEquals(TEST_URL, serviceAudit.getUrl(), NOT_EQUAL);
        assertEquals(TEST_MESSAGE_ID, serviceAudit.getMessageId(), NOT_EQUAL);
        assertEquals(TEST_MESSAGE_STATUS, serviceAudit.getMessageStatus(), NOT_EQUAL);
        assertEquals(TEST_MESSAGE_REQUEST, serviceAudit.getMessageRequest(), NOT_EQUAL);
        assertEquals(TEST_MESSAGE_RESPONSE, serviceAudit.getMessageResponse(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockServiceAuditRepo);
        verify(mockJsonRequest);
        verify(mockEntityManager);
    }
    
    @Test
    void testGetXhbDispMgrServiceAuditRepository() {
        ReflectionTestUtils.setField(classUnderTest, "entityManager", mockEntityManager);
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        replay(mockEntityManager);
        XhbDispMgrServiceAuditRepository result = classUnderTest.getXhbDispMgrServiceAuditRepository();
        assertNotNull(result, NOT_NULL);

        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrServiceAuditRepository",
            mockXhbDispMgrServiceAuditRepository);
        result = classUnderTest.getXhbDispMgrServiceAuditRepository();
        assertNotNull(result, NOT_NULL);
        
        // Verify the expected mocks were called
        verify(mockEntityManager);
    }

    /**
     * Mock getting the json request properties.
     */
    private void mockJsonRequestProperties() {
        expect(mockJsonRequest.getUrl()).andReturn(TEST_URL);
        expect(mockJsonRequest.getMessageId()).andReturn(TEST_MESSAGE_ID);
        expect(mockJsonRequest.getStatus()).andReturn(TEST_MESSAGE_STATUS);
        expect(mockJsonRequest.getRequestText()).andReturn(TEST_MESSAGE_REQUEST);
        expect(mockJsonRequest.getResponseText()).andReturn(TEST_MESSAGE_RESPONSE);
        replay(mockJsonRequest);
    }
}
