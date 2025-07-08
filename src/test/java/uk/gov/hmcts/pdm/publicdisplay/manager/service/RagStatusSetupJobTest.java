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

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRagStatusService;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * The Class DashboardController.
 *
 * @author boparaij
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class RagStatusSetupJobTest extends AbstractJUnit {
    /** The class under test. */
    private RagStatusSetupJob classUnderTest;

    /** The mock local proxy service. */
    private ILocalProxyService mockLocalProxyService;

    /** The mock rag status service. */
    private IRagStatusService mockRagStatusService;

    /** The mock job execution context. */
    private JobExecutionContext mockJobExecutionContext;

    /** The mock scheduler. */
    private Scheduler mockScheduler;

    /** The test xhibit court sites. */
    private final List<XhibitCourtSiteDto> testXhibitCourtSites = getTestXhibitCourtSites();

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new RagStatusSetupJob();

        // Setup the mock version of the called classes
        mockLocalProxyService = createMock(LocalProxyService.class);
        mockRagStatusService = createMock(RagStatusService.class);
        mockJobExecutionContext = createMock(JobExecutionContext.class);
        mockScheduler = createMock(Scheduler.class);

        // Map the mock to the class under tests called class
        classUnderTest.setLocalProxyService(mockLocalProxyService);
        classUnderTest.setRagStatusService(mockRagStatusService);
    }

    /**
     * Test schedule jobs.
     *
     * @throws Exception the exception
     */
    @Test
    void testScheduleJobs() throws Exception {
        // Mock getting court sites
        expect(mockLocalProxyService.getXhibitCourtSitesOrderedByRagStatus())
            .andReturn(testXhibitCourtSites);
        replay(mockLocalProxyService);

        // Mock getting scheduler
        expect(mockJobExecutionContext.getScheduler()).andReturn(mockScheduler);
        expectLastCall().times(testXhibitCourtSites.size());
        replay(mockJobExecutionContext);

        // Mock schedule refresh rag status
        for (XhibitCourtSiteDto xhibitCourtSite : testXhibitCourtSites) {
            mockRagStatusService.scheduleRagStatusJob(mockScheduler, xhibitCourtSite);
            expectLastCall();
        }
        replay(mockRagStatusService);

        // Perform the test
        classUnderTest.execute(mockJobExecutionContext);

        // Verify the mocks used in this method were called
        verify(mockLocalProxyService);
        verify(mockJobExecutionContext);
        verify(mockRagStatusService);
    }

    /**
     * Gets the test xhibit court site.
     *
     * @param id the id
     * @param name the name
     * @return the test xhibit court site
     */
    private XhibitCourtSiteDto getTestXhibitCourtSite(final Long id, final String name) {
        final XhibitCourtSiteDto xhibitCourtSite = new XhibitCourtSiteDto();
        xhibitCourtSite.setId(id);
        xhibitCourtSite.setCourtSiteName(name);
        return xhibitCourtSite;
    }

    /**
     * Gets the test xhibit court sites.
     *
     * @return the test xhibit court sites
     */
    private List<XhibitCourtSiteDto> getTestXhibitCourtSites() {
        final List<XhibitCourtSiteDto> courtSites = new ArrayList<>();
        courtSites.add(getTestXhibitCourtSite(1L, "Court 1"));
        courtSites.add(getTestXhibitCourtSite(2L, "Court 2"));
        return courtSites;
    }
}
