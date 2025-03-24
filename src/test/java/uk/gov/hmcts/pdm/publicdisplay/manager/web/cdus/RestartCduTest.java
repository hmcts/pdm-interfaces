package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class RestartCduTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
abstract class RestartCduTest extends CduRedirectToUrlPage {

    /**
     * Test restart cdu valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRestartCduValid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Local variables
        final List<CduDto> selectedCdus = new ArrayList<>();
        selectedCdus.add(cdu);

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduService.getCduByMacAddressWithLike(EasyMock.isA(String.class))).andReturn(cdus);
        expectSetModelCduList();
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, true);
        mockCduService.restartCdu(selectedCdus);
        expectLastCall();
        
        replay(mockCduPageStateHolder);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCdusUrl).param(BTN_RESTART_CDU_CONFIRM, BTN_RESTART_CDU_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress()))
            .andReturn();

        // Assert that the objects are as expected
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertNotNull(results, NULL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
        verify(mockCduService);
    }

    /**
     * Test restart cdu invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRestartCduInvalid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Local variables
        final List<CduDto> selectedCdus = new ArrayList<>();
        selectedCdus.add(cdu);

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, false);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCdusUrl).param(BTN_RESTART_CDU_CONFIRM, BTN_RESTART_CDU_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress()))
            .andReturn();

        // Assert that the objects are as expected
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertNotNull(results, NULL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
    }

    /**
     * Test restart cdu data access error.
     *
     * @throws Exception the exception
     */
    @Test
    void testRestartCduError() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Local variables
        final List<CduDto> selectedCdus = new ArrayList<>();
        selectedCdus.add(cdu);

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduService.getCduByMacAddressWithLike(EasyMock.isA(String.class))).andReturn(cdus);
        expectSetModelCduList();
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, true);
        mockCduService.restartCdu(selectedCdus);
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        expectLastCall().andThrow(dataRetrievalFailureException);
        
        replay(mockCduPageStateHolder);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCdusUrl).param(BTN_RESTART_CDU_CONFIRM, BTN_RESTART_CDU_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress()))
            .andReturn();

        // Assert that the objects are as expected
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertNotNull(results, NULL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
        verify(mockCduService);
    }

    /**
     * Test restart cdu runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testRestartCduRuntimeError() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Local variables
        final List<CduDto> selectedCdus = new ArrayList<>();
        selectedCdus.add(cdu);

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduService.getCduByMacAddressWithLike(EasyMock.isA(String.class))).andReturn(cdus);
        expectSetModelCduList();
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, true);
        mockCduService.restartCdu(selectedCdus);
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        expectLastCall().andThrow(xpdmException);
        
        replay(mockCduPageStateHolder);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCdusUrl).param(BTN_RESTART_CDU_CONFIRM, BTN_RESTART_CDU_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress()))
            .andReturn();

        // Assert that the objects are as expected
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertNotNull(results, NULL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
        verify(mockCduService);
    }

    /**
     * Test restart all cdus valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRestartAllCdusValid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
        expectSetModelCduList();
        replay(mockCduPageStateHolder);
        expectCduRestartAllValidator(capturedCommand, capturedErrors, true);
        mockCduService.restartCdu(cdus);
        expectLastCall();
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameCdusUrl).param(BTN_RESTART_ALL_CDU_CONFIRM, BTN_RESTART_ALL_CDU_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress()))
            .andReturn();

        // Assert that the objects are as expected
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertNotNull(results, NULL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduRestartAllValidator);
        verify(mockCduService);
    }

    /**
     * Test restart all cdus invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRestartAllCdusInvalid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);
        expectCduRestartAllValidator(capturedCommand, capturedErrors, false);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameCdusUrl).param(BTN_RESTART_ALL_CDU_CONFIRM, BTN_RESTART_ALL_CDU_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress()))
            .andReturn();

        // Assert that the objects are as expected
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertNotNull(results, NULL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduRestartAllValidator);
    }

    /**
     * Test restart all cdus data access error.
     *
     * @throws Exception the exception
     */
    @Test
    void testRestartAllCdusError() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);
        expectCduRestartAllValidator(capturedCommand, capturedErrors, true);
        mockCduService.restartCdu(cdus);
        
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        
        expectLastCall().andThrow(dataRetrievalFailureException);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameCdusUrl).param(BTN_RESTART_ALL_CDU_CONFIRM, BTN_RESTART_ALL_CDU_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress()))
            .andReturn();

        // Assert that the objects are as expected
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertNotNull(results, NULL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduRestartAllValidator);
        verify(mockCduService);
    }

    /**
     * Test restart all cdus runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testRestartAllCdusRuntimeError() throws Exception {

        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);
        expectCduRestartAllValidator(capturedCommand, capturedErrors, true);
        mockCduService.restartCdu(cdus);
        
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        
        expectLastCall().andThrow(xpdmException);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameCdusUrl).param(BTN_RESTART_ALL_CDU_CONFIRM, BTN_RESTART_ALL_CDU_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress()))
            .andReturn();

        // Assert that the objects are as expected
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertNotNull(results, NULL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduRestartAllValidator);
        verify(mockCduService);
    }

    /**
     * Test get cdu screenshot valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetCduScreenshotValid() throws Exception {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();
        final byte[] cduScreenshot = getTestByteArray();

        // Add the mock calls to child classes
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(2);
        replay(mockCduPageStateHolder);
        expect(mockCduService.getCduScreenshot(cdu)).andReturn(cduScreenshot);
        replay(mockCduService);
        expect(mockCduSearchSelectedValidator.isValid(cduSearchCommand)).andReturn(true);
        replay(mockCduSearchSelectedValidator);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(viewNameCduScreenshot)).andReturn();

        // Assert that the objects are as expected
        assertTrue(Arrays.equals(cduScreenshot, results.getResponse().getContentAsByteArray()),
            FALSE);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduService);
        verify(mockCduSearchSelectedValidator);
    }

}
