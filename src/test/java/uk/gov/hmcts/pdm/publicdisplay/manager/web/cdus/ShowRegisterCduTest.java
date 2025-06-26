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
 * The Class ShowRegisterCduTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class ShowRegisterCduTest extends ShowCduTest {

    /** The Constant REFRESH. */
    protected static final Long REFRESH = 30L;

    /** The Constant WEIGHTING. */
    protected static final Long WEIGHTING = 1L;

    protected static final String BTN_SHOW_REGISTER_CDU = "btnShowRegisterCdu";

    protected static final String BTN_REGISTER_CDU = "btnRegisterCdu";

    protected static final String REFRESH_STRING = "refresh";

    protected static final String WEIGHTING_STRING = "weighting";

    protected static final String BTN_UNREGISTER_CDU_CONFIRM = "btnUnRegisterCduConfirm";

    /**
     * Test show register cdu.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowRegisterCduValid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduService.getCdusBySiteID(EasyMock.isA(Long.class))).andReturn(cdus);
        mockCduPageStateHolder.setCdus(cdus);
        expectLastCall();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus).anyTimes();
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        expect(mockLocalProxyService.getCourtSiteByXhibitCourtSiteId(cdu.getXhibitCourtSiteId()))
            .andReturn(courtSiteDto);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, true);
        
        replay(mockLocalProxyService);
        replay(mockCduPageStateHolder);
        replay(mockCduService);
        
        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameCdusUrl).param(BTN_SHOW_REGISTER_CDU, BTN_SHOW_REGISTER_CDU)
                .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())
                .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameRegisterCdu);
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertCduRegisterModel(results.getModelAndView().getModelMap());
        assertEquals(cdu.getMacAddress(), capturedCommand.getValue().getSelectedMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
        verify(mockLocalProxyService);
        verify(mockCduService);
    }

    /**
     * Test show register cdu invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowRegisterCduInvalid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduService.getCdusBySiteID(null)).andReturn(cdus);
        mockCduPageStateHolder.setCdus(cdus);
        expectLastCall();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus).anyTimes();
        expect(mockCduPageStateHolder.getSites()).andReturn(sites).anyTimes();
        
        replay(mockCduPageStateHolder);
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, false);
        replay(mockLocalProxyService);
        replay(mockCduService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameCdusUrl).param(BTN_SHOW_REGISTER_CDU, BTN_SHOW_REGISTER_CDU))
                .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
        verify(mockLocalProxyService);
        verify(mockCduService);
    }

    /**
     * Test register cdu valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterCduValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduDto> capturedCdu = newCapture();
        final Capture<CduRegisterCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();

        // Add the mock calls to child classes
        expect(mockCduService.createCduDtoFromRegisterCommand(capture(capturedCommand))).andReturn(cdu);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        mockCduRegisterValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        mockCduService.registerCdu(capture(capturedCdu), capture(capturedCommand));
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

        replay(mockCduRegisterValidator);
        replay(mockCduService);
        replay(mockLocalProxyService);
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameRegisterCduUrl).param(BTN_REGISTER_CDU, BTN_REGISTER_CDU)
                .param(MAC_ADDRESS, cdu.getMacAddress()).param("cduNumber", cdu.getCduNumber())
                .param(LOCATION_STRING, cdu.getLocation()).param(REFRESH_STRING, REFRESH.toString())
                .param(WEIGHTING_STRING, WEIGHTING.toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertSuccessfulMessage(results.getModelAndView().getModelMap());
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertEquals(capturedCdu.getValue(), cdu, NOT_EQUAL);
        assertEquals(capturedCommand.getValue().getCduNumber(), cdu.getCduNumber(), NOT_EQUAL);
        assertEquals(capturedCommand.getValue().getLocation(), cdu.getLocation(), NOT_EQUAL);
        assertEquals(REFRESH, capturedCommand.getValue().getRefresh(), NOT_EQUAL);
        assertEquals(WEIGHTING, capturedCommand.getValue().getWeighting(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRegisterValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
        verify(mockLocalProxyService);
    }

    /**
     * Test register cdu params invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterCduParamsInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduRegisterCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduRegisterValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        replay(mockCduRegisterValidator);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameRegisterCduUrl).param(BTN_REGISTER_CDU, BTN_REGISTER_CDU))
                .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameRegisterCdu);
        assertEquals(4, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRegisterValidator);
    }

    /**
     * Test register cdu save error.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterCduSaveError() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduDto> capturedCdu = newCapture();
        final Capture<CduRegisterCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expect(mockCduService.createCduDtoFromRegisterCommand(capture(capturedCommand))).andReturn(cdu);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        mockCduRegisterValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        mockCduService.registerCdu(capture(capturedCdu), capture(capturedCommand));
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        expectLastCall().andThrow(dataRetrievalFailureException);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        
        replay(mockCduRegisterValidator);
        replay(mockCduService);
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameRegisterCduUrl).param(BTN_REGISTER_CDU, BTN_REGISTER_CDU)
                .param(MAC_ADDRESS, cdu.getMacAddress()).param("cduNumber", cdu.getCduNumber())
                .param(LOCATION_STRING, cdu.getLocation()).param(REFRESH_STRING, REFRESH.toString())
                .param(WEIGHTING_STRING, WEIGHTING.toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameRegisterCdu);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRegisterValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
    }

    /**
     * Test register cdu runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterCduRuntimeError() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduDto> capturedCdu = newCapture();
        final Capture<CduRegisterCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expect(mockCduService.createCduDtoFromRegisterCommand(capture(capturedCommand))).andReturn(cdu);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        mockCduRegisterValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        mockCduService.registerCdu(capture(capturedCdu), capture(capturedCommand));
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        expectLastCall().andThrow(xpdmException);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        
        replay(mockCduRegisterValidator);
        replay(mockCduService);
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameRegisterCduUrl).param(BTN_REGISTER_CDU, BTN_REGISTER_CDU)
                .param(MAC_ADDRESS, cdu.getMacAddress()).param("cduNumber", cdu.getCduNumber())
                .param(LOCATION_STRING, cdu.getLocation()).param(REFRESH_STRING, REFRESH.toString())
                .param(WEIGHTING_STRING, WEIGHTING.toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameRegisterCdu);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRegisterValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
    }

    /**
     * Test unregister cdu valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testUnregisterCduValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expectSetModelCduList();
        expect(mockCduService.getCduByMacAddressWithLike(EasyMock.isA(String.class))).andReturn(cdus).times(2);
        mockCduPageStateHolder.setCdus(cdus);
        expectLastCall().times(2);
        expectUnregisterCduValidator(capturedCommand, capturedErrors, true);
        mockCduService.unregisterCdu(cdu.getId());
        expectLastCall();
        
        replay(mockCduPageStateHolder);
        replay(mockCduService);

        // Perform the test
        final MvcResult results =
            mockMvc
                .perform(
                    post(mappingNameCdusUrl).param(BTN_UNREGISTER_CDU_CONFIRM, BTN_UNREGISTER_CDU_CONFIRM)
                        .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())
                        .param(MAC_ADDRESS, cdu.getMacAddress())
                        .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString()))
                .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertSuccessfulMessage(results.getModelAndView().getModelMap());
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);
        assertEquals(capturedCommand.getValue().getMacAddress(), cdu.getMacAddress(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduUnregisterValidator);
        verify(mockCduService);
    }

    /**
     * Test unregister cdu invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testUnregisterCduInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockCduService.getCdusBySiteID(EasyMock.isA(Long.class))).andReturn(cdus);
        mockCduPageStateHolder.setCdus(cdus);
        expectSetModelCduList();
        expectUnregisterCduValidator(capturedCommand, capturedErrors, false);

        replay(mockCduPageStateHolder);
        replay(mockCduService);
        
        // Perform the test
        final MvcResult results =
            mockMvc
                .perform(
                    post(mappingNameCdusUrl).param(BTN_UNREGISTER_CDU_CONFIRM, BTN_UNREGISTER_CDU_CONFIRM)
                        .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())
                        .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString()))
                .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduUnregisterValidator);
        verify(mockCduService);
    }

    /**
     * Test unregister cdu save error.
     *
     * @throws Exception the exception
     */
    @Test
    void testUnregisterCduSaveError() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expectSetModelCduList();
        expect(mockCduService.getCduByMacAddressWithLike(MAC_ADDRESS)).andReturn(cdus);
        mockCduPageStateHolder.setCdus(cdus);
        mockCduUnregisterValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        mockCduService.unregisterCdu(cdu.getId());
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        expectLastCall().andThrow(dataRetrievalFailureException);
        
        replay(mockCduPageStateHolder);
        replay(mockCduUnregisterValidator);
        replay(mockCduService);

        // Perform the test
        final MvcResult results =
            mockMvc
                .perform(
                    post(mappingNameCdusUrl).param(BTN_UNREGISTER_CDU_CONFIRM, BTN_UNREGISTER_CDU_CONFIRM)
                        .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())
                        .param(MAC_ADDRESS, cdu.getMacAddress())
                        .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString()))
                .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduService);
    }

    /**
     * Test unregister cdu runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testUnregisterCduRuntimeError() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        expectSetModelCduList();
        expect(mockCduService.getCduByMacAddressWithLike(MAC_ADDRESS)).andReturn(cdus);
        mockCduPageStateHolder.setCdus(cdus);
        mockCduUnregisterValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectLastCall();
        mockCduService.unregisterCdu(cdu.getId());
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        expectLastCall().andThrow(xpdmException);
        
        replay(mockCduPageStateHolder);
        replay(mockCduUnregisterValidator);
        replay(mockCduService);

        // Perform the test
        final MvcResult results =
            mockMvc
                .perform(
                    post(mappingNameCdusUrl).param(BTN_UNREGISTER_CDU_CONFIRM, BTN_UNREGISTER_CDU_CONFIRM)
                        .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())
                        .param(MAC_ADDRESS, cdu.getMacAddress())
                        .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString()))
                .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduService);
    }

}
