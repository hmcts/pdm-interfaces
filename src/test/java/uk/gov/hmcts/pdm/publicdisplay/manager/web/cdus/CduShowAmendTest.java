package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class CduShowAmendTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
class CduShowAmendTest extends UpdateCduTest {

    /**
     * Test show amend cdu.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowAmendCduValid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(2);
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        replay(mockCduPageStateHolder);
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, true);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameCdusUrl).param(BTN_SHOW_AMEND_CDU, BTN_SHOW_AMEND_CDU)
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameAmendCdu);
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertEquals(cdu.getMacAddress(), capturedCommand.getValue().getSelectedMacAddress(),
            NOT_EQUAL);
        assertCduAmendModel(results.getModelAndView().getModelMap());

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
    }

    /**
     * Test show amend cdu invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowAmendCduInvalid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduPageStateHolder.getSites()).andReturn(sites);
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
        replay(mockCduPageStateHolder);
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, false);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameCdusUrl).param(BTN_SHOW_AMEND_CDU, BTN_SHOW_AMEND_CDU)
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
    }

    /**
     * Test show cdu details invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowCduDetailsInvalid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, false);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCdusUrl)
            .param(BTN_SHOW_CDU, BTN_SHOW_CDU).param(MAC_ADDRESS, cdu.getMacAddress())
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
    }


}
