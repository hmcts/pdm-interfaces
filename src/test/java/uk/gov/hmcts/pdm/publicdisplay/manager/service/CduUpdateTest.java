package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.ServiceException;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduAmendCommand;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class CduUpdateTest.
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class CduUpdateTest extends CduScreenshotTest {

    /**
     * Test restart cdu.
     */
    @Test
    void testRestartCdu() {
        // Local variables
        final ICourtSite courtSite = cdus.get(0).getCourtSite();
        final List<String> ipAddresses = new ArrayList<>();
        for (CduDto cduDto : cduDtos) {
            ipAddresses.add(cduDto.getIpAddress());
        }

        // Add the mock calls to child classes
        expect(mockDispMgrCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockDispMgrCourtSiteRepo.findByCourtSiteId(courtSite.getId().intValue()))
            .andReturn(courtSite);
        mockLocalProxyRestClient.restartCdu(courtSite.getLocalProxy(), ipAddresses);
        expectLastCall();
        
        replay(mockDispMgrCourtSiteRepo);
        replay(mockEntityManager);
        replay(mockLocalProxyRestClient);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.restartCdu(cduDtos);

        // Verify the expected mocks were called
        verify(mockDispMgrCourtSiteRepo);
        verify(mockEntityManager);
        verify(mockLocalProxyRestClient);
    }

    /**
     * Test update cdu success.
     */
    @Test
    void testUpdateCduSuccess() {
        final Capture<ICduModel> capturedCdu = newCapture();

        final ICduModel cdu = cdus.get(0);
        final CduDto cduDto = cduDtos.get(0);
        final CduAmendCommand cduCommand = getTestCduAmendCommand(cduDto);
        cduCommand.setOfflineIndicator(AppConstants.YES_CHAR);

        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCduRepo.findByMacAddress(cduDto.getMacAddress())).andReturn(cdu);
        mockCduRepo.updateDaoFromBasicValue(capture(capturedCdu));
        expectLastCall();
        
        replay(mockCduRepo);
        replay(mockEntityManager);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.updateCdu(cduDto, cduCommand);

        // Assert that the objects are as expected
        assertEquals(cduCommand.getOfflineIndicator(), capturedCdu.getValue().getOfflineIndicator(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockEntityManager);
    }

    /**
     * Test update cdu failure.
     */
    @Test
    void testUpdateCduFailure() {
        final CduDto cduDto = cduDtos.get(0);
        final CduAmendCommand cduCommand = getTestCduAmendCommand(cduDto);
        cduCommand.setOfflineIndicator(AppConstants.YES_CHAR);

        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCduRepo.findByMacAddress(cduDto.getMacAddress())).andReturn(null);
        
        replay(mockCduRepo);
        replay(mockEntityManager);

        try {
            // Perform the test
            classUnderTest.updateCdu(cduDto, cduCommand);
        } catch (Exception e) {
            assertEquals(e.getClass(), ServiceException.class, NOT_EQUAL);
        } finally {

            // Verify the expected mocks were called
            verify(mockCduRepo);
            verify(mockEntityManager);
        }
    }

    /**
     * Gets the test cdu amend command.
     *
     * @param cduDto the cdu dto
     * @return the test cdu amend command
     */
    private CduAmendCommand getTestCduAmendCommand(final CduDto cduDto) {
        final CduAmendCommand cduCommand = new CduAmendCommand();
        cduCommand.setLocation(cduDto.getLocation());
        cduCommand.setDescription(cduDto.getDescription());
        cduCommand.setNotification(cduDto.getNotification());
        cduCommand.setRefresh(cduDto.getRefresh());
        cduCommand.setWeighting(cduDto.getWeighting());
        cduCommand.setOfflineIndicator(cduDto.getOfflineIndicator());

        return cduCommand;
    }

}
