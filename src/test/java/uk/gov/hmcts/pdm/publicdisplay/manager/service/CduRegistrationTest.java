package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.ServiceException;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduRegisterCommand;

import java.math.BigDecimal;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class CduRegistrationTest.
 */
@ExtendWith(EasyMockExtension.class)
abstract class CduRegistrationTest extends CduServiceTestBase {

    /**
     * Test register cdu valid.
     */
    @Test
    void testRegisterCduValid() {
        // Local variables
        final CduDto cduDto = cduDtos.get(0);
        final CduRegisterCommand cduCommand = getTestCduRegisterCommand(cduDto.getId());

        // Capture the cdu
        final Capture<ICduModel> capturedCdu = newCapture();

        // Add the mock calls to child classes
        expect(mockCduRepo.findByMacAddress(cduDto.getMacAddress())).andReturn(cdus.get(0));
        expect(mockCduRepo.isCduWithCduNumber(cduCommand.getCduNumber())).andReturn(true);
        mockCduRepo.saveDaoFromBasicValue(capture(capturedCdu));
        expectLastCall();
        replay(mockCduRepo);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.registerCdu(cduDto, cduCommand);

        // Assert that the objects are as expected
        assertEquals(capturedCdu.getValue().getMacAddress(), cduDto.getMacAddress(), NOT_EQUAL);
        assertEquals(capturedCdu.getValue().getCduNumber(), cduCommand.getCduNumber(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
    }

    /**
     * Test register cdu null host.
     */
    @Test
    void testRegisterCduNullHost() {
        // Local variables
        final CduDto cduDto = cduDtos.get(0);
        final CduRegisterCommand cduCommand = getTestCduRegisterCommand(cduDto.getId());

        // Add the mock calls to child classes
        expect(mockCduRepo.findByMacAddress(cduDto.getMacAddress())).andReturn(null);
        expect(mockCduRepo.getNextIpHost(cduDto.getCourtSiteId().intValue(), CDU_IP_HOST_MIN,
            CDU_IP_HOST_MAX)).andReturn(null);
        expect(mockCduRepo.hostExists(cduDto.getCourtSiteId().intValue())).andReturn(true);
        replay(mockCduRepo);

        try {
            // Perform the test
            classUnderTest.registerCdu(cduDto, cduCommand);
        } catch (Exception e) {
            assertEquals(e.getClass(), ServiceException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockCduRepo);
        }
    }

    /**
     * Test register cdu invalid cdu number.
     */
    @Test
    void testRegisterCduInvalidCduNumber() {
        // Local variables
        final CduDto cduDto = cduDtos.get(0);
        final CduRegisterCommand cduCommand = getTestCduRegisterCommand(cduDto.getId());
        cduCommand.setCduNumber("Invalid");

        // Add the mock calls to child classes
        expect(mockCduRepo.findByMacAddress(cduDto.getMacAddress())).andReturn(cdus.get(0));
        expect(mockCduRepo.isCduWithCduNumber(cduCommand.getCduNumber())).andReturn(true);
        replay(mockCduRepo);

        try {
            // Perform the test
            classUnderTest.registerCdu(cduDto, cduCommand);
        } catch (Exception e) {
            assertEquals(e.getClass(), ServiceException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockCduRepo);
        }
    }

    /**
     * Test register cdu new valid.
     */
    @Test
    void testRegisterCduNewValid() {
        // Local variables
        final ICourtSite courtSite = cdus.get(0).getCourtSite();
        final CduDto cduDto = cduDtos.get(0);
        final CduRegisterCommand cduCommand = getTestCduRegisterCommand(cduDto.getId());

        // Capture the cdu
        final Capture<ICduModel> capturedCdu = newCapture();

        // Add the mock calls to child classes
        expect(mockCduRepo.findByMacAddress(cduDto.getMacAddress())).andReturn(null);
        expect(mockCduRepo.getNextIpHost(cduDto.getCourtSiteId().intValue(), CDU_IP_HOST_MIN,
            CDU_IP_HOST_MAX)).andReturn(BigDecimal.valueOf(1));
        mockCduRepo.saveDaoFromBasicValue(capture(capturedCdu));
        expectLastCall();
        replay(mockCduRepo);
        expect(mockDispMgrCourtSiteRepo.findByCourtSiteId(courtSite.getId().intValue()))
            .andReturn(courtSite);
        replay(mockDispMgrCourtSiteRepo);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.registerCdu(cduDto, cduCommand);

        // Assert that the objects are as expected
        assertEquals(capturedCdu.getValue().getMacAddress(), cduDto.getMacAddress(), NOT_EQUAL);
        assertEquals(capturedCdu.getValue().getCduNumber(), cduCommand.getCduNumber(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockDispMgrCourtSiteRepo);
    }

    /**
     * Test unregister cdu valid.
     */
    @Test
    void testUnregisterCduValid() {
        // Capture the cdu
        final Capture<ICduModel> capturedCdu = newCapture();

        // Add the mock calls to child classes
        expect(mockCduRepo.findByCduId(CDU_IDS[0].intValue())).andReturn(cdus.get(0));
        mockCduRepo.deleteDaoFromBasicValue(cdus.get(0));
        expectLastCall();
        replay(mockCduRepo);
        mockLocalProxyRestClient.deleteCdu(capture(capturedCdu));
        expectLastCall();
        replay(mockLocalProxyRestClient);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.unregisterCdu(CDU_IDS[0]);

        // Assert that the objects are as expected
        assertEquals(CDU_IDS[0], capturedCdu.getValue().getId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockLocalProxyRestClient);
    }

    /**
     * Test unregister cdu data error.
     */
    @Test
    void testUnregisterCduDataError() {
        // Add the mock calls to child classes
        expect(mockCduRepo.findByCduId(1)).andReturn(null);
        replay(mockCduRepo);

        try {
            // Perform the test
            classUnderTest.unregisterCdu(CDU_IDS[0]);
        } catch (Exception e) {
            assertEquals(e.getClass(), ServiceException.class, NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockCduRepo);
        }
    }

    /**
     * Gets the test cdu register command.
     *
     * @param cduId the cdu id
     * @return the test cdu command
     */
    private CduRegisterCommand getTestCduRegisterCommand(final Long cduId) {
        final CduRegisterCommand cduCommand = new CduRegisterCommand();
        cduCommand.setCduNumber(CDUNUMBER + cduId.toString());
        return cduCommand;
    }

}
