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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class CduUrlMappingTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
abstract class CduUrlMappingTest extends ShowRegisterCduTest {


    protected static final String BTN_ADD_MAPPING = "btnAddMapping";

    protected static final String URL_ID = "urlId";

    protected static final String BTN_REMOVE_MAPPING_CONFIRM = "btnRemoveMappingConfirm";

    protected static final String ADD = "add";

    protected static final String BTN_MANAGE_URL = "btnManageUrl";

    protected static final String BTN_RESTART_CDU_CONFIRM = "btnRestartCduConfirm";

    protected static final String BTN_RESTART_ALL_CDU_CONFIRM = "btnRestartAllCduConfirm";

    protected static final String BTN_SHOW_AMEND_CDU = "btnShowAmendCdu";

    protected static final String BTN_UPDATE_CDU = "btnUpdateCdu";

    protected static final String OFFLINE_INDICATOR = "offlineIndicator";

    /** The constant for the yes character. */
    public static final Character YES_CHAR = 'Y';

    /**
     * Test add url mapping.
     *
     * @throws Exception the exception
     */
    @Test
    void testAddUrlMappingValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<MappingCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expectMappingAddValidator(capturedCommand, capturedErrors, true);
        mockCduService.addMapping(capture(capturedCommand));
        expectLastCall();
        expect(mockCduService.getCduByMacAddress(cdu.getMacAddress())).andReturn(cdu);
        replay(mockCduService);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(5);
        mockCduPageStateHolder.setAvailableUrls(urls);
        expectLastCall();
        expect(mockCduPageStateHolder.getAvailableUrls()).andReturn(urls);
        replay(mockCduPageStateHolder);
        expect(mockUrlService.getUrlsByXhibitCourtSiteId(cdu.getXhibitCourtSiteId()))
            .andReturn(urls);
        replay(mockUrlService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(viewNameMappingAddUrl).param(BTN_ADD_MAPPING, BTN_ADD_MAPPING)
                .param(URL_ID, url.getId().toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertSuccessfulMessage(results.getModelAndView().getModelMap());
        assertEquals(capturedCommand.getValue().getUrlId(), url.getId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockMappingAddValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
        verify(mockUrlService);
    }

    /**
     * Test add url mapping invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testAddUrlMappingInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<MappingCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expectMappingAddValidator(capturedCommand, capturedErrors, false);
        mockCduService.addMapping(capture(capturedCommand));
        expectLastCall();
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(4);
        mockCduPageStateHolder.setAvailableUrls(urls);
        expectLastCall();
        expect(mockCduPageStateHolder.getAvailableUrls()).andReturn(urls);
        replay(mockCduPageStateHolder);
        expect(mockUrlService.getUrlsByXhibitCourtSiteId(cdu.getXhibitCourtSiteId()))
            .andReturn(urls);
        replay(mockUrlService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(viewNameMappingAddUrl).param(BTN_ADD_MAPPING, BTN_ADD_MAPPING)
                .param(URL_ID, url.getId().toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockMappingAddValidator);
        verify(mockCduPageStateHolder);
        verify(mockUrlService);
    }

    /**
     * Test add url mapping failure.
     *
     * @throws Exception the exception
     */
    @Test
    void testAddUrlMappingSaveError() throws Exception {


        // Capture the cduCommand object and errors passed out
        final Capture<MappingCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockMappingAddValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        replay(mockMappingAddValidator);
        mockCduService.addMapping(capture(capturedCommand));
        
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        
        expectLastCall().andThrow(dataRetrievalFailureException);
        replay(mockCduService);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(4);
        expect(mockCduPageStateHolder.getAvailableUrls()).andReturn(urls);
        mockCduPageStateHolder.setAvailableUrls(urls);
        expectLastCall();
        replay(mockCduPageStateHolder);
        expect(mockUrlService.getUrlsByXhibitCourtSiteId(cdu.getXhibitCourtSiteId()))
            .andReturn(urls);
        replay(mockUrlService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(viewNameMappingAddUrl).param(BTN_ADD_MAPPING, BTN_ADD_MAPPING)
                .param(URL_ID, url.getId().toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockMappingAddValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
        verify(mockUrlService);
    }

    /**
     * Test add url mapping runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testAddUrlMappingRuntimeError() throws Exception {

        // Capture the cduCommand object and errors passed out
        final Capture<MappingCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockMappingAddValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        replay(mockMappingAddValidator);
        mockCduService.addMapping(capture(capturedCommand));
        
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        
        expectLastCall().andThrow(xpdmException);
        replay(mockCduService);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(4);
        expect(mockCduPageStateHolder.getAvailableUrls()).andReturn(urls);
        mockCduPageStateHolder.setAvailableUrls(urls);
        expectLastCall();
        replay(mockCduPageStateHolder);
        expect(mockUrlService.getUrlsByXhibitCourtSiteId(cdu.getXhibitCourtSiteId()))
            .andReturn(urls);
        replay(mockUrlService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(viewNameMappingAddUrl).param(BTN_ADD_MAPPING, BTN_ADD_MAPPING)
                .param(URL_ID, url.getId().toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockMappingAddValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
        verify(mockUrlService);
    }

    /**
     * Test remove url mapping.
     *
     * @throws Exception the exception
     */
    @Test
    void testRemoveUrlMappingValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<MappingCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expectMappingRemoveValidator(capturedCommand, capturedErrors, true);
        mockCduService.removeMapping(capture(capturedCommand));
        expectLastCall();
        expect(mockCduService.getCduByMacAddress(cdu.getMacAddress())).andReturn(cdu);
        replay(mockCduService);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(2);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(viewNameMappingRemoveUrl)
            .param(BTN_REMOVE_MAPPING_CONFIRM, BTN_REMOVE_MAPPING_CONFIRM)
            .param(URL_ID, url.getId().toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertSuccessfulMessage(results.getModelAndView().getModelMap());
        assertEquals(capturedCommand.getValue().getUrlId(), url.getId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockMappingRemoveValidator);
        verify(mockCduService);
    }

    /**
     * Test remove url mapping invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRemoveUrlMappingInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<MappingCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expectMappingRemoveValidator(capturedCommand, capturedErrors, false);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(2);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(viewNameMappingRemoveUrl)
            .param(BTN_REMOVE_MAPPING_CONFIRM, BTN_REMOVE_MAPPING_CONFIRM)
            .param(URL_ID, url.getId().toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockMappingRemoveValidator);
    }

    /**
     * Test remove url mapping failure.
     *
     * @throws Exception the exception
     */
    @Test
    void testRemoveUrlMappingDeleteError() throws Exception {


        // Capture the cduCommand object and errors passed out
        final Capture<MappingCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockMappingRemoveValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        replay(mockMappingRemoveValidator);
        mockCduService.removeMapping(capture(capturedCommand));
        
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        
        expectLastCall().andThrow(dataRetrievalFailureException);
        replay(mockCduService);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(viewNameMappingRemoveUrl)
            .param(BTN_REMOVE_MAPPING_CONFIRM, BTN_REMOVE_MAPPING_CONFIRM)
            .param(URL_ID, url.getId().toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockMappingRemoveValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
    }

    /**
     * Test remove url mapping runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testRemoveUrlMappingRuntimeError() throws Exception {

        // Capture the cduCommand object and errors passed out
        final Capture<MappingCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockMappingRemoveValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        replay(mockMappingRemoveValidator);
        mockCduService.removeMapping(capture(capturedCommand));
        
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        
        expectLastCall().andThrow(xpdmException);
        replay(mockCduService);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(viewNameMappingRemoveUrl)
            .param(BTN_REMOVE_MAPPING_CONFIRM, BTN_REMOVE_MAPPING_CONFIRM)
            .param(URL_ID, url.getId().toString())).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockMappingRemoveValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
    }

}
