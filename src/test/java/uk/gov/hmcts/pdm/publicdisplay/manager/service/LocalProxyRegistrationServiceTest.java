package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.ServiceException;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.LocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies.LocalProxyRegisterCommand;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class LocalProxyRegistrationServiceTest.
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class LocalProxyRegistrationServiceTest extends LocalProxyCourtSiteServiceTest {
    /**
     * Test unregister local proxy.
     */
    @Test
    void testUnregisterLocalProxy() {
        // Local variables
        final ICourtSite courtSite = courtSitesWithLocalProxies.get(0).getCourtSite();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockLocalProxyRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findByCourtSiteId(courtSite.getId().intValue()))
            .andReturn(courtSite);
        mockLocalProxyRepo.deleteDaoFromBasicValue(courtSite.getLocalProxy());
        expectLastCall();
        mockCduRepo.deleteDaoFromBasicValue(isA(ICduModel.class));
        expectLastCall().times(courtSite.getCdus().size());
        
        replay(mockCourtSiteRepo);
        replay(mockLocalProxyRepo);
        replay(mockCduRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.unregisterLocalProxy(courtSite.getId());

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockLocalProxyRepo);
        verify(mockCduRepo);
        verify(mockEntityManager);
    }

    /**
     * Test unregister local proxy court site get error.
     */
    @Test
    void testUnregisterLocalProxyCourtSiteGetError() {
        // Local variables
        final ICourtSite courtSite = courtSitesWithLocalProxies.get(0).getCourtSite();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findByCourtSiteId(courtSite.getId().intValue())).andReturn(null);
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        try {
            // Perform the test
            classUnderTest.unregisterLocalProxy(courtSite.getId());
        } catch (Exception e) {
            assertEquals(e.getClass(), ServiceException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockCourtSiteRepo);
            verify(mockEntityManager);
        }
    }

    /**
     * Test unregister local proxy local proxy get error.
     */
    @Test
    void testUnregisterLocalProxyLocalProxyGetError() {
        // Local variables
        final ICourtSite courtSite = courtSitesWithLocalProxies.get(0).getCourtSite();
        courtSite.setLocalProxy(null);

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findByCourtSiteId(courtSite.getId().intValue()))
            .andReturn(courtSite);
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        try {
            // Perform the test
            classUnderTest.unregisterLocalProxy(courtSite.getId());
        } catch (Exception e) {
            assertEquals(e.getClass(), ServiceException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockCourtSiteRepo);
            verify(mockEntityManager);
        }
    }

    /**
     * Test register local proxy with no existing court site.
     */
    @Test
    void testRegisterLocalProxyCourtSiteNotExists() {
        // Local variables
        final IXhibitCourtSite xhibitCourtSite = courtSitesWithLocalProxies.get(0);
        final LocalProxyRegisterCommand localProxyRegisterCommand =
            getTestLocalProxyRegisterCommand(xhibitCourtSite.getId());
        final ISchedule schedule = getTestSchedule(localProxyRegisterCommand.getScheduleId());
        final String testHostname = TEST_HOSTNAME;

        // Capture the courtSite and localProxy
        final Capture<ICourtSite> capturedCourtSite = newCapture();
        final Capture<LocalProxy> capturedLocalProxy = newCapture();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockScheduleRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockLocalProxyRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(
            mockCourtSiteRepo.findCourtSiteByXhibitCourtSiteId(xhibitCourtSite.getId().intValue()))
                .andReturn(null);
        mockCourtSiteRepo.updateDaoFromBasicValue(capture(capturedCourtSite));
        expectLastCall();
        expect(
            mockScheduleRepo.findByScheduleId(localProxyRegisterCommand.getScheduleId().intValue()))
                .andReturn(schedule);
        expect(mockLocalProxyRestClient.saveLocalProxy(capture(capturedLocalProxy), eq(true)))
            .andReturn(testHostname);
        expectLastCall();
        mockLocalProxyRepo.saveDaoFromBasicValue(capture(capturedLocalProxy));
        expectLastCall();
        
        replay(mockCourtSiteRepo);
        replay(mockScheduleRepo);
        replay(mockLocalProxyRestClient);
        replay(mockLocalProxyRepo);
        replay(mockEntityManager);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.registerLocalProxy(localProxyRegisterCommand);

        // Assert that the objects are as expected
        assertEquals(capturedCourtSite.getValue().getXhibitCourtSite().getId(),
            localProxyRegisterCommand.getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getIpAddress(),
            localProxyRegisterCommand.getIpAddress(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getSchedule().getId(),
            localProxyRegisterCommand.getScheduleId(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getTitle(),
            localProxyRegisterCommand.getTitle(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getNotification(),
            localProxyRegisterCommand.getNotification(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockScheduleRepo);
        verify(mockLocalProxyRestClient);
        verify(mockLocalProxyRepo);
        verify(mockEntityManager);
    }

    /**
     * Test register local proxy with existing court site.
     */
    @Test
    void testRegisterLocalProxyCourtSiteExists() {
        // Local variables
        final IXhibitCourtSite xhibitCourtSite = courtSitesWithLocalProxies.get(0);
        final ICourtSite courtSite = xhibitCourtSite.getCourtSite();
        final LocalProxyRegisterCommand localProxyRegisterCommand =
            getTestLocalProxyRegisterCommand(xhibitCourtSite.getId());
        final ISchedule schedule = getTestSchedule(localProxyRegisterCommand.getScheduleId());
        final String testHostname = TEST_HOSTNAME;

        // Capture the localProxy
        final Capture<LocalProxy> capturedLocalProxy = newCapture();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockScheduleRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockLocalProxyRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(
            mockCourtSiteRepo.findCourtSiteByXhibitCourtSiteId(xhibitCourtSite.getId().intValue()))
                .andReturn(courtSite);
        mockCourtSiteRepo.updateDaoFromBasicValue(courtSite);
        expectLastCall();
        expect(
            mockScheduleRepo.findByScheduleId(localProxyRegisterCommand.getScheduleId().intValue()))
                .andReturn(schedule);
        expect(mockLocalProxyRestClient.saveLocalProxy(capture(capturedLocalProxy), eq(true)))
            .andReturn(testHostname);
        mockLocalProxyRepo.saveDaoFromBasicValue(capture(capturedLocalProxy));
        expectLastCall();
        
        
        replay(mockCourtSiteRepo);
        replay(mockScheduleRepo);
        replay(mockLocalProxyRestClient);
        replay(mockLocalProxyRepo);
        replay(mockEntityManager);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.registerLocalProxy(localProxyRegisterCommand);

        // Assert that the objects are as expected
        assertEquals(capturedLocalProxy.getValue().getIpAddress(),
            localProxyRegisterCommand.getIpAddress(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getSchedule().getId(),
            localProxyRegisterCommand.getScheduleId(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getTitle(),
            localProxyRegisterCommand.getTitle(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getNotification(),
            localProxyRegisterCommand.getNotification(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockScheduleRepo);
        verify(mockLocalProxyRepo);
        verify(mockLocalProxyRestClient);
        verify(mockEntityManager);
    }

    /**
     * Checks if is local proxy with ip address.
     */
    @Test
    void isLocalProxyWithIpAddress() {
        // Add the mock calls to child classes
        expect(mockLocalProxyRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockLocalProxyRepo.isLocalProxyWithIpAddress(IPADDRESS)).andReturn(true);
        
        replay(mockLocalProxyRepo);
        replay(mockEntityManager);

        // Perform the test
        final boolean result = classUnderTest.isLocalProxyWithIpAddress(IPADDRESS);

        // Assert that the objects are as expected
        assertEquals(true, result, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyRepo);
        verify(mockEntityManager);
    }

    /**
     * Gets the test local proxy register command.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @return the test local proxy register ommand
     */
    private LocalProxyRegisterCommand getTestLocalProxyRegisterCommand(
        final Long xhibitCourtSiteId) {
        final LocalProxyRegisterCommand localProxyRegisterCommand = new LocalProxyRegisterCommand();
        localProxyRegisterCommand.setXhibitCourtSiteId(xhibitCourtSiteId);
        localProxyRegisterCommand.setScheduleId(3L);
        localProxyRegisterCommand.setTitle("TEST_REGISTER_TITLE");
        localProxyRegisterCommand.setNotification("TEST_REGISTER_NOTIFICATION");
        return localProxyRegisterCommand;
    }
}
