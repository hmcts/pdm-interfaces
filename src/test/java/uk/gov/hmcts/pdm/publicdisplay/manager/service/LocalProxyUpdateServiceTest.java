package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.ServiceException;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies.LocalProxyAmendCommand;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * The Class LocalProxyUpdateServiceTest.
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
class LocalProxyUpdateServiceTest extends LocalProxyRegistrationServiceTest {

    /**
     * Test update local proxy where notifiaction field has amended.
     */
    @Test
    void testUpdateLocalProxyUpdateNotificationTrue() {
        // Local variables
        final boolean updateNotification = true;
        final ICourtSite courtSite = getTestCourtSite(1L);
        final LocalProxyAmendCommand localProxyAmendCommand =
            getTestLocalProxyAmendCommand(updateNotification);
        final CourtSiteDto courtSiteDto = getCourtSiteDto();
        final ISchedule schedule = getTestSchedule(localProxyAmendCommand.getScheduleId());
        final String testHostname = TEST_HOSTNAME;

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockScheduleRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findByCourtSiteId(courtSiteDto.getId().intValue()))
            .andReturn(courtSite);
        expect(mockScheduleRepo.findByScheduleId(localProxyAmendCommand.getScheduleId().intValue()))
            .andReturn(schedule);
        mockCourtSiteRepo.updateDaoFromBasicValue(courtSite);
        expectLastCall();
        // Capture the localProxy
        final Capture<ILocalProxy> capturedLocalProxy = newCapture();
        expect(mockLocalProxyRestClient.saveLocalProxy(capture(capturedLocalProxy), eq(true)))
            .andReturn(testHostname);
        mockCduRepo.updateDaoFromBasicValue(isA(ICduModel.class));
        expectLastCall().times(courtSite.getCdus().size());

        replay(mockCourtSiteRepo);
        replay(mockScheduleRepo);
        replay(mockLocalProxyRestClient);
        replay(mockCduRepo);
        replay(mockEntityManager);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.updateLocalProxy(courtSiteDto, localProxyAmendCommand);

        // Assert that the objects are as expected
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getId(), courtSite.getId(),
            NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getNotification(),
            courtSite.getNotification(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getTitle(), courtSite.getTitle(),
            NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getSchedule().getId(),
            schedule.getId(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getSchedule().getTitle(),
            schedule.getTitle(), NOT_EQUAL);
        for (ICduModel cdu : courtSite.getCdus()) {
            assertEquals(capturedLocalProxy.getValue().getCourtSite().getNotification(),
                cdu.getNotification(), NOT_EQUAL);
        }

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockScheduleRepo);
        verify(mockLocalProxyRestClient);
        verify(mockCduRepo);
        verify(mockEntityManager);
    }

    /**
     * Test update local proxy with where notification field not amended.
     */
    @Test
    void testUpdateLocalProxyUpdateNotificationFalse() {
        // Local variables
        final boolean updateNotification = false;
        final ICourtSite courtSite = getTestCourtSite(1L);
        final LocalProxyAmendCommand localProxyAmendCommand =
            getTestLocalProxyAmendCommand(updateNotification);
        final CourtSiteDto courtSiteDto = getCourtSiteDto();
        final ISchedule schedule = getTestSchedule(localProxyAmendCommand.getScheduleId());
        final String testHostname = TEST_HOSTNAME;

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockScheduleRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findByCourtSiteId(courtSiteDto.getId().intValue()))
            .andReturn(courtSite);
        expect(mockScheduleRepo.findByScheduleId(localProxyAmendCommand.getScheduleId().intValue()))
            .andReturn(schedule);
        mockCourtSiteRepo.updateDaoFromBasicValue(courtSite);
        expectLastCall();
        // Capture the localProxy
        final Capture<ILocalProxy> capturedLocalProxy = newCapture();
        expect(mockLocalProxyRestClient.saveLocalProxy(capture(capturedLocalProxy), eq(false)))
            .andReturn(testHostname);

        replay(mockCourtSiteRepo);
        replay(mockScheduleRepo);
        replay(mockLocalProxyRestClient);
        replay(mockEntityManager);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.updateLocalProxy(courtSiteDto, localProxyAmendCommand);

        // Assert that the objects are as expected
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getId(), courtSite.getId(),
            NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getNotification(),
            courtSite.getNotification(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getTitle(), courtSite.getTitle(),
            NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getSchedule().getId(),
            schedule.getId(), NOT_EQUAL);
        assertEquals(capturedLocalProxy.getValue().getCourtSite().getSchedule().getTitle(),
            schedule.getTitle(), NOT_EQUAL);
        for (ICduModel cdu : courtSite.getCdus()) {
            assertNotEquals(capturedLocalProxy.getValue().getCourtSite().getNotification(),
                cdu.getNotification(), NOT_EQUAL);
        }
        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockScheduleRepo);
        verify(mockLocalProxyRestClient);
        verify(mockEntityManager);
    }

    /**
     * Test update local proxy service exception schedule null.
     */
    @Test
    void testUpdateLocalProxyServiceExceptionScheduleNull() {
        // Local variables
        final boolean updateNotification = false;
        final ICourtSite courtSite = getTestCourtSite(1L);
        final LocalProxyAmendCommand localProxyAmendCommand =
            getTestLocalProxyAmendCommand(updateNotification);
        final CourtSiteDto courtSiteDto = getCourtSiteDto();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockScheduleRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findByCourtSiteId(courtSiteDto.getId().intValue()))
            .andReturn(courtSite);
        expect(mockScheduleRepo.findByScheduleId(localProxyAmendCommand.getScheduleId().intValue()))
            .andReturn(null);

        replay(mockCourtSiteRepo);
        replay(mockScheduleRepo);
        replay(mockEntityManager);

        try {
            // Perform the test
            classUnderTest.updateLocalProxy(courtSiteDto, localProxyAmendCommand);
        } catch (Exception e) {
            assertEquals(e.getClass(), ServiceException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockCourtSiteRepo);
            verify(mockScheduleRepo);
            verify(mockEntityManager);
        }
    }

    /**
     * Test update local proxy service exception court site null.
     */
    @Test
    void testUpdateLocalProxyServiceExceptionCourtSiteNull() {
        // Local variables
        final boolean updateNotification = false;
        final LocalProxyAmendCommand localProxyAmendCommand =
            getTestLocalProxyAmendCommand(updateNotification);
        final CourtSiteDto courtSiteDto = getCourtSiteDto();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findByCourtSiteId(courtSiteDto.getId().intValue()))
            .andReturn(null);

        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        try {
            // Perform the test
            classUnderTest.updateLocalProxy(courtSiteDto, localProxyAmendCommand);
        } catch (Exception e) {
            assertEquals(e.getClass(), ServiceException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockCourtSiteRepo);
            verify(mockEntityManager);
        }
    }

    /**
     * Gets the test local proxy amend command.
     *
     * @param updateNotification the update notification
     * @return the test local proxy amend command
     */
    private LocalProxyAmendCommand getTestLocalProxyAmendCommand(final boolean updateNotification) {
        final LocalProxyAmendCommand localProxyAmendCommand = new LocalProxyAmendCommand();
        if (updateNotification) {
            localProxyAmendCommand.setNotification("TEST_NOTIFICATION_AMENDED");
        } else {
            localProxyAmendCommand.setNotification("TEST_NOTIFICATION");
        }
        localProxyAmendCommand.setScheduleId(1L);
        localProxyAmendCommand.setTitle("TEST_TITLE");
        return localProxyAmendCommand;
    }

    /**
     * Gets the court site dto.
     *
     * @return the court site dto
     */
    private CourtSiteDto getCourtSiteDto() {
        final CourtSiteDto courtSiteDto = new CourtSiteDto();
        courtSiteDto.setId(1L);
        courtSiteDto.setIpAddress("TEST_IPADDRESS");
        courtSiteDto.setNotification("TEST_NOTIFICATION");
        courtSiteDto.setPageUrl("TEST_URL");
        courtSiteDto.setScheduleId(1L);
        courtSiteDto.setScheduleTitle("TEST_SCHEDULETITLE");
        courtSiteDto.setTitle("TEST_TITLE");
        return courtSiteDto;
    }

}
