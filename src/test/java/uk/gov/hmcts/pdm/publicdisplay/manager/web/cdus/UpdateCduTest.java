package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


/**
 * The Class UpdateCduTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class UpdateCduTest extends CduScreenshotTest {

    /**
     * Test update cdu valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testUpdateCduValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduDto> capturedCdu = newCapture();
        final Capture<CduAmendCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();

        // Add the mock calls to child classes
        expectCduAmendValidator(capturedCommand, capturedErrors, true);
        expect(mockCduService.getCduByCduId(0)).andReturn(cdu);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        mockCduService.updateCdu(capture(capturedCdu), capture(capturedCommand));
        expectLastCall();
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        // Redirecting screen back to showCduSearch
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand);
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        mockCduPageStateHolder.reset();
        expectLastCall();
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        expectSetModelCduList();
        
        replay(mockCduService);
        replay(mockLocalProxyService);
        replay(mockCduPageStateHolder);
        
        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendCduUrl)
            .param(BTN_UPDATE_CDU, BTN_UPDATE_CDU).param(MAC_ADDRESS, cdu.getMacAddress())
            .param(LOCATION_STRING, cdu.getLocation()).param(REFRESH_STRING, REFRESH.toString())
            .param(WEIGHTING_STRING, WEIGHTING.toString())
            .param(OFFLINE_INDICATOR, YES_CHAR.toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertSuccessfulMessage(results.getModelAndView().getModelMap());
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertEquals(cdu, capturedCdu.getValue(), NOT_EQUAL);
        assertEquals(YES_CHAR, capturedCommand.getValue().getOfflineIndicator(), NOT_EQUAL);
        assertEquals(cdu.getLocation(), capturedCommand.getValue().getLocation(), NOT_EQUAL);
        assertEquals(REFRESH, capturedCommand.getValue().getRefresh(), NOT_EQUAL);
        assertEquals(WEIGHTING, capturedCommand.getValue().getWeighting(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduAmendValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
    }

    /**
     * Test update cdu invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testUpdateCduInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduAmendCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expectCduAmendValidator(capturedCommand, capturedErrors, false);
        expect(mockCduService.getCduByCduId(0)).andReturn(cdu);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        
        replay(mockCduService);
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendCduUrl)
            .param(BTN_UPDATE_CDU, BTN_UPDATE_CDU).param(MAC_ADDRESS, cdu.getMacAddress())
            .param(LOCATION_STRING, cdu.getLocation()).param(REFRESH_STRING, REFRESH.toString())
            .param(WEIGHTING_STRING, WEIGHTING.toString())
            .param(OFFLINE_INDICATOR, YES_CHAR.toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameAmendCdu);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduAmendValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
    }

    /**
     * Test update cdu save error.
     *
     * @throws Exception the exception
     */
    @Test
    void testUpdateCduSaveError() throws Exception {
        final DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);

        // Capture the cduCommand object and errors passed out
        final Capture<CduDto> capturedCdu = newCapture();
        final Capture<CduAmendCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expectCduAmendValidator(capturedCommand, capturedErrors, true);
        expect(mockCduService.getCduByCduId(0)).andReturn(cdu);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        mockCduService.updateCdu(capture(capturedCdu), capture(capturedCommand));
        expectLastCall().andThrow(dataRetrievalFailureException);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        
        replay(mockCduService);
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendCduUrl)
            .param(BTN_UPDATE_CDU, BTN_UPDATE_CDU).param(MAC_ADDRESS, cdu.getMacAddress())
            .param(LOCATION_STRING, cdu.getLocation()).param(REFRESH_STRING, REFRESH.toString())
            .param(WEIGHTING_STRING, WEIGHTING.toString())
            .param(OFFLINE_INDICATOR, YES_CHAR.toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduAmendValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
    }

    /**
     * Test update cdu runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testUpdateCduRuntimeError() throws Exception {
        final XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);

        // Capture the cduCommand object and errors passed out
        final Capture<CduDto> capturedCdu = newCapture();
        final Capture<CduAmendCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expectCduAmendValidator(capturedCommand, capturedErrors, true);
        expect(mockCduService.getCduByCduId(0)).andReturn(cdu);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        mockCduService.updateCdu(capture(capturedCdu), capture(capturedCommand));
        expectLastCall().andThrow(xpdmException);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        
        replay(mockCduService);
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendCduUrl)
            .param(BTN_UPDATE_CDU, BTN_UPDATE_CDU).param(MAC_ADDRESS, cdu.getMacAddress())
            .param(LOCATION_STRING, cdu.getLocation()).param(REFRESH_STRING, REFRESH.toString())
            .param(WEIGHTING_STRING, WEIGHTING.toString())
            .param(OFFLINE_INDICATOR, YES_CHAR.toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduAmendValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
    }

}
