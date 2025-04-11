package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMockExtension;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.CduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.CourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.LocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;

import java.util.Date;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class RagStatusUpdateServiceTest.
 *
 * @author boparaij
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
class RagStatusUpdateServiceTest extends RagStatusOverallTest {

    /**
     * Test update status Red.
     */
    @Test
    void testUpdateStatusRed() {
        // Populate the details of the localProxy object
        localProxy.setCourtSite(courtSite);
        localProxy.getCourtSite().setCdus(cdus);

        // Capture the courtSite and localProxy
        final Capture<CourtSite> capturedCourtSite = newCapture();
        final Capture<LocalProxy> capturedLocalProxy = newCapture();

        // Add the mock calls to child classes
        expectRepositoryUtilCheck();
        replay(mockEntityManager);
        for (ICduModel cdu : cdus) {
            expect(mockCduRepo.findByMacAddress(cdu.getMacAddress())).andReturn(cdu);
            mockCduRepo.updateDaoFromBasicValue(cdu);
            expectLastCall();
        }
        replay(mockCduRepo);
        expect(mockDispMgrCourtSiteRepo.findByXhibitCourtSiteId(XHIBIT_COURT_SITE_ID.intValue()))
            .andReturn(courtSite);
        mockDispMgrCourtSiteRepo.updateDaoFromBasicValue(capture(capturedCourtSite));
        replay(mockDispMgrCourtSiteRepo);
        mockLocalProxyRepo.updateDaoFromBasicValue(capture(capturedLocalProxy));
        expectLastCall();
        replay(mockLocalProxyRepo);
        expect(mockLocalProxyRestClient.getCourtSiteStatus(localProxy))
            .andReturn(courtSiteStatusJson);
        replay(mockLocalProxyRestClient);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, "ragStatusCourtSiteAmberPercent", 80);
        ReflectionTestUtils.setField(classUnderTest, "ragStatusCourtSiteRedPercent", 60);
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COM_ENABLED, true);

        // Perform the test
        classUnderTest.refreshRagStatus(XHIBIT_COURT_SITE_ID);

        // Assert that the objects are as expected
        assertNotNull(capturedLocalProxy.getValue(), NULL);
        assertEquals(capturedLocalProxy.getValue().getRagStatus(), GREEN_CHAR.toString(),
            NOT_EQUAL);
        assertNotNull(capturedCourtSite.getValue(), NULL);
        assertEquals(capturedCourtSite.getValue().getRagStatus(), RED_CHAR.toString(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyRestClient);
        verify(mockLocalProxyRepo);
        verify(mockCduRepo);
        verify(mockDispMgrCourtSiteRepo);
    }

    /**
     * Test update status Amber.
     */
    @Test
    void testUpdateStatusAmber() {
        // Populate the details of the localProxy object
        localProxy.setCourtSite(courtSite);
        localProxy.getCourtSite().setCdus(cdus);

        // Capture the courtSite and localProxy
        final Capture<CourtSite> capturedCourtSite = newCapture();
        final Capture<LocalProxy> capturedLocalProxy = newCapture();

        // Add the mock calls to child classes
        expectRepositoryUtilCheck();
        replay(mockEntityManager);
        for (ICduModel cdu : cdus) {
            expect(mockCduRepo.findByMacAddress(cdu.getMacAddress())).andReturn(cdu);
            mockCduRepo.updateDaoFromBasicValue(cdu);
            expectLastCall();
        }
        replay(mockCduRepo);
        expect(mockDispMgrCourtSiteRepo.findByXhibitCourtSiteId(XHIBIT_COURT_SITE_ID.intValue()))
            .andReturn(courtSite);
        mockDispMgrCourtSiteRepo.updateDaoFromBasicValue(capture(capturedCourtSite));
        replay(mockDispMgrCourtSiteRepo);
        mockLocalProxyRepo.updateDaoFromBasicValue(capture(capturedLocalProxy));
        expectLastCall();
        replay(mockLocalProxyRepo);
        expect(mockLocalProxyRestClient.getCourtSiteStatus(localProxy))
            .andReturn(courtSiteStatusJson);
        replay(mockLocalProxyRestClient);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, "ragStatusCourtSiteAmberPercent", 60);
        ReflectionTestUtils.setField(classUnderTest, "ragStatusCourtSiteRedPercent", 10);
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COM_ENABLED, true);

        // Perform the test
        classUnderTest.refreshRagStatus(XHIBIT_COURT_SITE_ID);

        // Assert that the objects are as expected
        assertNotNull(capturedLocalProxy.getValue(), NULL);
        assertEquals(capturedLocalProxy.getValue().getRagStatus(), GREEN_CHAR.toString(),
            NOT_EQUAL);
        assertNotNull(capturedCourtSite.getValue(), NULL);
        assertEquals(capturedCourtSite.getValue().getRagStatus(), AMBER_CHAR.toString(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyRestClient);
        verify(mockLocalProxyRepo);
        verify(mockCduRepo);
        verify(mockDispMgrCourtSiteRepo);
    }

    /**
     * Test update status Green.
     */
    @Test
    void testUpdateStatusGreen() {
        // Populate the details of the localProxy object
        localProxy.setCourtSite(courtSite);
        localProxy.getCourtSite().setCdus(cdus);

        // Capture the courtSite and localProxy
        final Capture<CourtSite> capturedCourtSite = newCapture();
        final Capture<LocalProxy> capturedLocalProxy = newCapture();

        // Add the mock calls to child classes
        expectRepositoryUtilCheck();
        replay(mockEntityManager);
        for (ICduModel cdu : cdus) {
            expect(mockCduRepo.findByMacAddress(cdu.getMacAddress())).andReturn(cdu);
            mockCduRepo.updateDaoFromBasicValue(cdu);
            expectLastCall();
        }
        replay(mockCduRepo);
        expect(mockDispMgrCourtSiteRepo.findByXhibitCourtSiteId(XHIBIT_COURT_SITE_ID.intValue()))
            .andReturn(courtSite);
        mockDispMgrCourtSiteRepo.updateDaoFromBasicValue(capture(capturedCourtSite));
        replay(mockDispMgrCourtSiteRepo);
        mockLocalProxyRepo.updateDaoFromBasicValue(capture(capturedLocalProxy));
        expectLastCall();
        replay(mockLocalProxyRepo);
        expect(mockLocalProxyRestClient.getCourtSiteStatus(localProxy))
            .andReturn(courtSiteStatusJson);
        replay(mockLocalProxyRestClient);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, "ragStatusCourtSiteAmberPercent", 30);
        ReflectionTestUtils.setField(classUnderTest, "ragStatusCourtSiteRedPercent", 10);
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COM_ENABLED, true);

        // Perform the test
        classUnderTest.refreshRagStatus(XHIBIT_COURT_SITE_ID);

        // Assert that the objects are as expected
        assertNotNull(capturedLocalProxy.getValue(), NULL);
        assertEquals(capturedLocalProxy.getValue().getRagStatus(), GREEN_CHAR.toString(),
            NOT_EQUAL);
        assertNotNull(capturedCourtSite.getValue(), NULL);
        assertEquals(capturedCourtSite.getValue().getRagStatus(), GREEN_CHAR.toString(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyRestClient);
        verify(mockLocalProxyRepo);
        verify(mockCduRepo);
        verify(mockDispMgrCourtSiteRepo);
    }

    /**
     * Test update status error.
     */
    @Test
    void testUpdateStatusError() {
        // Capture the courtSite and localProxy
        final Capture<CourtSite> capturedCourtSite = newCapture();
        final Capture<LocalProxy> capturedLocalProxy = newCapture();
        final Capture<CduModel> capturedCdus = newCapture(CaptureType.ALL);

        // Add the mock calls to child classes
        expectRepositoryUtilCheck();
        replay(mockEntityManager);
        for (ICduModel cdu : cdus) {
            mockCduRepo.updateDaoFromBasicValue(cdu);
            expectLastCall();
        }
        replay(mockCduRepo);
        expect(mockDispMgrCourtSiteRepo.findByXhibitCourtSiteId(courtSite.getId().intValue()))
            .andReturn(courtSite);
        mockDispMgrCourtSiteRepo.updateDaoFromBasicValue(capture(capturedCourtSite));
        replay(mockDispMgrCourtSiteRepo);
        expect(mockLocalProxyRestClient.getCourtSiteStatus(localProxy))
            .andThrow(new RestException("mock Exception"));
        replay(mockLocalProxyRestClient);
        mockLocalProxyRepo.updateDaoFromBasicValue(capture(capturedLocalProxy));
        expectLastCall();
        replay(mockLocalProxyRepo);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COM_ENABLED, true);

        try {
            // Perform the test
            classUnderTest.refreshRagStatus(courtSite.getId());
        } catch (Exception e) {
            assertEquals(e.getClass(), RestException.class, NOT_EQUAL);
        } finally {
            // Assert that the objects are as expected
            assertNotNull(capturedLocalProxy.getValue(), NULL);
            assertEquals(RED_CHAR.toString(), capturedLocalProxy.getValue().getRagStatus(),
                NOT_EQUAL);
            assertNotNull(capturedCourtSite.getValue(), NULL);
            assertEquals(RED_CHAR.toString(), capturedCourtSite.getValue().getRagStatus(),
                NOT_EQUAL);
            for (CduModel cdu : capturedCdus.getValues()) {
                assertEquals(RED_CHAR, cdu.getRagStatus(), NOT_EQUAL);
            }

            // Verify the expected mocks were called
            verify(mockCduRepo);
            verify(mockDispMgrCourtSiteRepo);
            verify(mockLocalProxyRestClient);
            verify(mockLocalProxyRepo);
        }
    }

    /**
     * Test schedule new job.
     *
     * @throws Exception the exception
     */
    @Test
    void testScheduleNewJob() throws Exception {
        // Mock calling scheduler to create new job
        final Capture<JobDetail> capturedJobDetail = newCapture();
        final Capture<Trigger> capturedTrigger = newCapture();
        expect(mockScheduler.getTrigger(isA(TriggerKey.class))).andReturn(null);
        expect(mockScheduler.deleteJob(isA(JobKey.class))).andReturn(false);
        expect(mockScheduler.scheduleJob(capture(capturedJobDetail), capture(capturedTrigger)))
            .andReturn(new Date());
        replay(mockScheduler);

        // Perform the test
        classUnderTest.scheduleRagStatusJob(mockScheduler, xhibitCourtSiteDto);

        // Assert the new jobs were created
        assertScheduledJob(1L, new Date(), capturedJobDetail.getValue(),
            capturedTrigger.getValue());

        // Verify the mocks used in this method were called
        verify(mockScheduler);
    }

    /**
     * Test schedule existing job.
     *
     * @throws Exception the exception
     */
    @Test
    void testScheduleExistingJob() throws Exception {
        // Mock calling scheduler to replace existing job
        final Capture<JobDetail> capturedJobDetail = newCapture();
        final Capture<Trigger> capturedTrigger = newCapture();
        expect(mockScheduler.getTrigger(isA(TriggerKey.class))).andReturn(mockTrigger);
        expect(mockScheduler.deleteJob(isA(JobKey.class))).andReturn(true);
        expect(mockScheduler.scheduleJob(capture(capturedJobDetail), capture(capturedTrigger)))
            .andReturn(new Date());
        replay(mockScheduler);

        // Mock calling existing trigger to get next fire time
        expect(mockTrigger.getNextFireTime()).andReturn(testDate).anyTimes();
        replay(mockTrigger);

        // Perform the test
        classUnderTest.scheduleRagStatusJob(mockScheduler, xhibitCourtSiteDto);

        // Assert the existing job was replaced but still with trigger test date
        assertScheduledJob(1L, testDate, capturedJobDetail.getValue(), capturedTrigger.getValue());

        // Verify the mocks used in this method were called
        verify(mockScheduler);
        verify(mockTrigger);
    }

    /**
     * Assert scheduled job.
     *
     * @param id the id
     * @param date the date
     * @param jobDetail the job detail
     * @param trigger the trigger
     */
    private void assertScheduledJob(final Long id, final Date date, final JobDetail jobDetail,
        final Trigger trigger) {
        // Assert fixed job and trigger details
        assertEquals("ragStatusJob_" + id, jobDetail.getKey().getName(), NOT_EQUAL);
        assertEquals(id, jobDetail.getJobDataMap().get("id"), NOT_EQUAL);
        assertEquals("ragStatusTrigger_" + id, trigger.getKey().getName(), NOT_EQUAL);

        // Assert start date which can only be a range check because the start
        // date can be set by the job to be "now" and so we can only test the
        // date is near "now" in that case
        final DateTime dateTimeMax = new DateTime(date);
        final DateTime dateTimeMin = dateTimeMax.minusMinutes(1);
        assertTrue(trigger.getStartTime().compareTo(dateTimeMax.toDate()) <= 0, NOT_EQUAL);
        assertTrue(trigger.getStartTime().compareTo(dateTimeMin.toDate()) >= 0, NOT_EQUAL);
    }
}
