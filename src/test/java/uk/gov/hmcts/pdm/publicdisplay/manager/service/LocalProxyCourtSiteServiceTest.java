package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DashboardCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.ScheduleDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class LocalProxyCourtSiteServiceTest.
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
abstract class LocalProxyCourtSiteServiceTest extends LocalProxyServiceTestBase {

    /**
     * Test get xhibit court sites with local proxy.
     */
    @Test
    void testGetXhibitCourtSitesWithLocalProxyValid() {
        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findCourtSitesWithLocalProxy())
            .andReturn(courtSitesWithLocalProxies);
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        final List<XhibitCourtSiteDto> results = classUnderTest.getXhibitCourtSitesWithLocalProxy();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results.size(), courtSitesWithLocalProxies.size(), NOT_EQUAL);
        assertCourtSites(results, AppConstants.YES_CHAR);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    /**
     * Test get xhibit court sites with local proxy as an empty list.
     */
    @Test
    void testGetXhibitCourtSitesWithLocalProxyEmpty() {
        final List<IXhibitCourtSite> emptyCourtSiteList = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findCourtSitesWithLocalProxy()).andReturn(emptyCourtSiteList);
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        final List<XhibitCourtSiteDto> results = classUnderTest.getXhibitCourtSitesWithLocalProxy();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results.size(), emptyCourtSiteList.size(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    /**
     * Test get xhibit court sites without local proxy.
     */
    @Test
    void testGetXhibitCourtSitesWithoutLocalProxyValid() {
        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findCourtSitesWithoutLocalProxy())
            .andReturn(courtSitesWithoutLocalProxies);
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        final List<XhibitCourtSiteDto> results =
            classUnderTest.getXhibitCourtSitesWithoutLocalProxy();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results.size(), courtSitesWithLocalProxies.size(), NOT_EQUAL);
        assertCourtSites(results, AppConstants.NO_CHAR);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    /**
     * Test get xhibit court sites without local proxy as an empty list.
     */
    @Test
    void testGetXhibitCourtSitesWithoutLocalProxyEmpty() {
        final List<IXhibitCourtSite> emptyCourtSiteList = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findCourtSitesWithoutLocalProxy()).andReturn(emptyCourtSiteList);
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        final List<XhibitCourtSiteDto> results =
            classUnderTest.getXhibitCourtSitesWithoutLocalProxy();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results.size(), emptyCourtSiteList.size(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    /**
     * Test get xhibit court sites ordered by rag status valid.
     */
    @Test
    void testGetXhibitCourtSitesOrderedByRagStatusValid() {
        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findXhibitCourtSitesOrderedByRagStatus())
            .andReturn(courtSitesWithLocalProxies);
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        final List<XhibitCourtSiteDto> results =
            classUnderTest.getXhibitCourtSitesOrderedByRagStatus();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results.size(), courtSitesWithLocalProxies.size(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    /**
     * Test get court site by xhibit court site id valid.
     */
    @Test
    void testGetCourtSiteByXhibitCourtSiteIdValid() {
        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo
            .findCourtSiteByXhibitCourtSiteId(courtSitesWithLocalProxies.get(0).getId().intValue()))
                .andReturn(courtSitesWithLocalProxies.get(0).getCourtSite());
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        final CourtSiteDto results = classUnderTest
            .getCourtSiteByXhibitCourtSiteId(courtSitesWithLocalProxies.get(0).getId());

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results.getId(), courtSitesWithLocalProxies.get(0).getCourtSite().getId(),
            NOT_EQUAL);
        assertEquals(results.getPageUrl(),
            courtSitesWithLocalProxies.get(0).getCourtSite().getPageUrl(), NOT_EQUAL);
        assertEquals(results.getIpAddress(),
            courtSitesWithLocalProxies.get(0).getCourtSite().getLocalProxy().getIpAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    /**
     * Test get dashboard court site by xhibit court site id valid.
     */
    @Test
    void testGetDashboardCourtSiteByXhibitCourtSiteIdValid() {
        // Add the mock calls to child classes
        expect(mockDispMgrCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockDispMgrCourtSiteRepo
            .findByXhibitCourtSiteId(courtSitesWithLocalProxies.get(0).getId().intValue()))
                .andReturn(courtSitesWithLocalProxies.get(0).getCourtSite());
        
        replay(mockDispMgrCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        final DashboardCourtSiteDto results = classUnderTest
            .getDashboardCourtSiteByXhibitCourtSiteId(courtSitesWithLocalProxies.get(0).getId());

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(RED_CHAR.toString(), results.getRagStatus(), NOT_EQUAL);
        assertEquals(RAG_STATUS_DATE, results.getLastRefreshDate(), NOT_EQUAL);
        assertEquals(results.getCdus().size(),
            courtSitesWithLocalProxies.get(0).getCourtSite().getCdus().size(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockDispMgrCourtSiteRepo);
        verify(mockEntityManager);
    }

    /**
     * Assert court sites.
     *
     * @param courtSites the court sites
     * @param expectedRegisteredInd the expected registered ind
     */
    private void assertCourtSites(final List<XhibitCourtSiteDto> courtSites,
        final Character expectedRegisteredInd) {
        for (XhibitCourtSiteDto courtSite : courtSites) {
            assertEquals(expectedRegisteredInd, courtSite.getRegisteredIndicator(), NOT_EQUAL);
        }
    }

    /**
     * Test get power save schedules.
     */
    @Test
    void testGetPowerSaveSchedules() {
        // Add the mock calls to child classes
        expect(mockScheduleRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockScheduleRepo.findPowerSaveSchedules()).andReturn(powerSaveSchedules);
        
        replay(mockScheduleRepo);
        replay(mockEntityManager);

        // Perform the test
        final List<ScheduleDto> schedules = classUnderTest.getPowerSaveSchedules();

        // Assert that the objects are as expected
        assertNotNull(schedules, NULL);
        for (ISchedule schedule : powerSaveSchedules) {
            assertEquals("DETAIL", schedule.getDetail(), NOT_EQUAL);
            assertEquals("TITLE", schedule.getTitle(), NOT_EQUAL);
            assertEquals("TYPE", schedule.getType(), NOT_EQUAL);
        }

        // Verify the expected mocks were called
        verify(mockScheduleRepo);
        verify(mockEntityManager);
    }

}
