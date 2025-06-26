package uk.gov.hmcts.pdm.publicdisplay.manager.web.dashboard;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DashboardCourtSiteDto;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class DashboardControllerCourtSiteTest.
 *
 * @author boparaij
 */
@SuppressWarnings("PMD")
class DashboardControllerCourtSiteTest extends DashboardControllerTest {

    /**
     * Test court site with refresh.
     *
     * @throws Exception the exception
     */
    @Test
    void testCourtSiteWithRefresh() throws Exception {

        final Long xhibitCourtSiteId = testXhibitCourtSites.get(0).getId();

        expect(mockLocalProxyService.getDashboardCourtSiteByXhibitCourtSiteId(xhibitCourtSiteId))
            .andReturn(testXhibitCourtSitesDashboard);
        replay(mockLocalProxyService);

        mockRagStatusService.refreshRagStatus(xhibitCourtSiteId);
        expectLastCall();
        replay(mockRagStatusService);


        // Perform the test
        final DashboardCourtSiteDto results = classUnderTest.courtSite(xhibitCourtSiteId, true);

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results, testXhibitCourtSitesDashboard, NOT_EQUAL);

        verify(mockLocalProxyService);

        verify(mockRagStatusService);

    }

    /**
     * Test court site without refresh.
     *
     * @throws Exception the exception
     */
    @Test
    void testCourtSiteWithoutRefresh() throws Exception {

        final Long xhibitCourtSiteId = testXhibitCourtSites.get(0).getId();

        expect(mockLocalProxyService.getDashboardCourtSiteByXhibitCourtSiteId(xhibitCourtSiteId))
            .andReturn(testXhibitCourtSitesDashboard);
        replay(mockLocalProxyService);

        // Perform the test
        final DashboardCourtSiteDto results = classUnderTest.courtSite(xhibitCourtSiteId, false);

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results, testXhibitCourtSitesDashboard, NOT_EQUAL);

        verify(mockLocalProxyService);
    }
}
