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
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRagStatusService;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class DashboardController.
 *
 * @author boparaij
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
class RagStatusJobTest extends AbstractJUnit {
    /** The class under test. */
    private RagStatusJob classUnderTest;

    /** The mock rag status service. */
    private IRagStatusService mockRagStatusService;

    /** The mock job execution context. */
    private JobExecutionContext mockJobExecutionContext;

    /** The mock job details. */
    private JobDetail mockJobDetail;

    /** The mock job data map. */
    private JobDataMap mockJobDataMap;

    private static final String NOT_EQUAL = "Not equal";

    private static final String FALSE = "False";

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new RagStatusJob();

        // Setup the mock version of the called classes
        mockRagStatusService = createMock(RagStatusService.class);
        mockJobExecutionContext = createMock(JobExecutionContext.class);
        mockJobDetail = createMock(JobDetail.class);
        mockJobDataMap = createMock(JobDataMap.class);

        // Map the mock to the class under tests called class
        classUnderTest.setRagStatusService(mockRagStatusService);
    }

    /**
     * Test refresh rag status successfully.
     *
     * @throws Exception the exception
     */
    @Test
    void testRefreshSuccess() throws Exception {
        // Mock getting id
        expect(mockJobExecutionContext.getJobDetail()).andReturn(mockJobDetail);
        replay(mockJobExecutionContext);
        expect(mockJobDetail.getJobDataMap()).andReturn(mockJobDataMap);
        replay(mockJobDetail);
        expect(mockJobDataMap.getLong("id")).andReturn(1L);
        replay(mockJobDataMap);

        // Mock refresh rag status
        final Capture<Long> capturedId = newCapture();
        mockRagStatusService.refreshRagStatus(capture(capturedId));
        expectLastCall();
        replay(mockRagStatusService);

        // Perform the test
        classUnderTest.execute(mockJobExecutionContext);

        // Assert the court site id to refresh
        assertEquals(Long.valueOf(1), capturedId.getValue(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockJobExecutionContext);
        verify(mockJobDetail);
        verify(mockJobDataMap);
        verify(mockRagStatusService);
    }

    /**
     * Test refresh rag status failing.
     *
     * @throws Exception the exception
     */
    @Test
    void testRefreshError() throws Exception {
        // Mock getting id
        expect(mockJobExecutionContext.getJobDetail()).andReturn(mockJobDetail);
        replay(mockJobExecutionContext);
        expect(mockJobDetail.getJobDataMap()).andReturn(mockJobDataMap);
        replay(mockJobDetail);
        expect(mockJobDataMap.getLong("id")).andReturn(1L);
        replay(mockJobDataMap);

        // Mock refresh rag status
        final Capture<Long> capturedId = newCapture();
        mockRagStatusService.refreshRagStatus(capture(capturedId));
        expectLastCall().andThrow(new RestException("Mock rest exception"));
        replay(mockRagStatusService);

        try {
            // Perform the test
            classUnderTest.execute(mockJobExecutionContext);
        } catch (Exception e) {
            assertEquals(e.getClass(), JobExecutionException.class, NOT_EQUAL);
        } finally {
            // Assert the court site id to refresh
            assertEquals(Long.valueOf(1), capturedId.getValue(), NOT_EQUAL);

            // Verify the mocks used in this method were called
            verify(mockJobExecutionContext);
            verify(mockJobDetail);
            verify(mockJobDataMap);
            verify(mockRagStatusService);
        }
    }
}
