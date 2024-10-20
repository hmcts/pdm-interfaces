package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class CduRedirectToUrlPage.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
abstract class CduRedirectToUrlPage extends CduUrlMappingTest {

    /**
     * Test redirect to url page add.
     *
     * @throws Exception the exception
     */
    @Test
    void testRedirectToUrlPageAddValid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(3);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
        mockCduPageStateHolder.setAvailableUrls(urls);
        expectLastCall();
        expect(mockCduPageStateHolder.getAvailableUrls()).andReturn(urls);
        replay(mockCduPageStateHolder);
        expect(mockUrlService.getUrlsByXhibitCourtSiteId(cdu.getXhibitCourtSiteId()))
            .andReturn(urls);
        replay(mockUrlService);
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, true);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCdusUrl).param(BTN_MANAGE_URL, ADD)
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
            .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(cdu.getMacAddress(), capturedCommand.getValue().getSelectedMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
        verify(mockUrlService);
    }

    /**
     * Test redirect to url page add invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRedirectToUrlPageAddInvalid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        replay(mockCduPageStateHolder);
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, false);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCdusUrl).param(BTN_MANAGE_URL, ADD)
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
            .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(cdu.getMacAddress(), capturedCommand.getValue().getSelectedMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
    }

    /**
     * Test redirect to url page add data fetch failure.
     *
     * @throws Exception the exception
     */
    @Test
    void testRedirectToUrlPageAddGetError() throws Exception {
        
        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
        replay(mockCduPageStateHolder);
        
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        
        expect(mockUrlService.getUrlsByXhibitCourtSiteId(cdu.getXhibitCourtSiteId()))
            .andThrow(dataRetrievalFailureException);
        replay(mockUrlService);
        mockCduSearchSelectedValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        replay(mockCduSearchSelectedValidator);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCdusUrl).param(BTN_MANAGE_URL, ADD)
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
            .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
        verify(mockUrlService);
    }

    /**
     * Test redirect to url page add runtime failure.
     *
     * @throws Exception the exception
     */
    @Test
    void testRedirectToUrlPageAddRuntimeError() throws Exception {

        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
        replay(mockCduPageStateHolder);
        
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        
        expect(mockUrlService.getUrlsByXhibitCourtSiteId(cdu.getXhibitCourtSiteId()))
            .andThrow(xpdmException);
        replay(mockUrlService);
        mockCduSearchSelectedValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        replay(mockCduSearchSelectedValidator);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCdusUrl).param(BTN_MANAGE_URL, ADD)
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
            .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
        verify(mockUrlService);
    }

    /**
     * Test redirect to url page remove valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRedirectToUrlPageRemoveValid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
        replay(mockCduPageStateHolder);
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, true);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCdusUrl).param(BTN_MANAGE_URL, "remove")
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())
            .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
    }

}
