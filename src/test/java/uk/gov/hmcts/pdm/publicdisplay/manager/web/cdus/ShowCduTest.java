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
import java.util.List;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class ShowCduTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
abstract class ShowCduTest extends TestCdus {

    protected static final String TRUE = "True";

    protected static final String SELECTED_MAC_ADDRESS = "selectedMacAddress";

    protected static final String XHIBIT_COURTSITE_ID = "xhibitCourtSiteId";

    protected static final String MOCK_DATA_EXCEPTION = "Mock data access exception";

    protected static final String MOCK_RUNTIME_EXCEPTION = "Mock runtime exception";

    protected static final String BTN_SEARCH_SITE = "btnSearchSites";

    protected static final String BTN_SHOW_CDU = "btnShowCdu";

    /**
     * Test show cdu search.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowCduSearchValid() throws Exception {
        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        replay(mockLocalProxyService);
        mockCduPageStateHolder.reset();
        expectLastCall();
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameCdusUrl)).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertCduSearchModel(results.getModelAndView().getModelMap(), false);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockCduPageStateHolder);
    }

    /**
     * Test show cdu search empty cdu list.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowCduSearchEmptyCduList() throws Exception {
        // Capture the cduCommand object and errors passed out
        final List<CduDto> emptyCdus = new ArrayList<>();
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();
        cduSearchCommand.setMacAddress(null);

        // Add the mock calls to child classes
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand);
        expectLastCall().times(2);
        mockCduPageStateHolder.setCdus(emptyCdus);
        expectLastCall();
        expect(mockCduPageStateHolder.getCdus()).andReturn(emptyCdus).anyTimes();
        expect(mockCduPageStateHolder.getSites()).andReturn(sites).anyTimes();
        replay(mockCduPageStateHolder);
        expect(mockCduSearchValidator.isValid(cduSearchCommand)).andReturn(true);
        replay(mockCduSearchValidator);
        expect(mockCduService.getCdusBySiteID(cduSearchCommand.getXhibitCourtSiteId()))
            .andReturn(emptyCdus);
        replay(mockCduService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameCdusUrl).param("reset", "false")).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertTrue(results.getModelAndView().getModelMap().get(COMMAND) instanceof CduSearchCommand,
            FALSE);
        assertEquals(emptyCdus, results.getModelAndView().getModelMap().get("cduList"), NOT_EQUAL);
        assertEquals(sites, results.getModelAndView().getModelMap().get("courtSiteList"),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduSearchValidator);
        verify(mockCduService);
        verify(mockCduPageStateHolder);
    }

    /**
     * Test show cdu search invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowCduSearchRefreshInvalid() throws Exception {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();

        // Add the mock calls to child classes
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand);
        expect(mockCduSearchValidator.isValid(cduSearchCommand)).andReturn(false);
        replay(mockCduSearchValidator);
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        replay(mockLocalProxyService);
        mockCduPageStateHolder.reset();
        expectLastCall();
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameCdusUrl).param("reset", "false")).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertCduSearchModel(results.getModelAndView().getModelMap(), false);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockCduPageStateHolder);
    }

    /**
     * Test show cdu search refresh valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowCduSearchRefreshValid() throws Exception {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();

        // Add the mock calls to child classes
        expect(mockCduSearchValidator.isValid(cduSearchCommand)).andReturn(true);
        replay(mockCduSearchValidator);
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand);
        expectLastCall().times(2);
        expectSetModelCduList();
        mockCduPageStateHolder.setCdus(cdus);
        expectLastCall();
        replay(mockCduPageStateHolder);
        expect(mockCduService.getCduByMacAddressWithLike(cduSearchCommand.getMacAddress()))
            .andReturn(cdus);
        replay(mockCduService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameCdusUrl).param("reset", "false")).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertCduSearchModel(results.getModelAndView().getModelMap(), false);

        // Verify the expected mocks were called
        verify(mockCduSearchValidator);
        verify(mockCduPageStateHolder);
        verify(mockCduService);
    }

    /**
     * Test search for cdu dashboard.
     *
     * @throws Exception the exception
     *
     */
    @Test
    void testSearchForCduDashboardValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        replay(mockLocalProxyService);
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        mockCduPageStateHolder.setCdus(cdus);
        mockCduPageStateHolder.setCdus(null);
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);
        expectCduSearchValidator(capturedCommand, capturedErrors, true);
        expect(mockCduService.getCdusBySiteID(cdu.getXhibitCourtSiteId())).andReturn(cdus);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameCdusUrl).param("dashboardSearch", "true")
            .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertEquals(capturedCommand.getValue().getXhibitCourtSiteId(), cdu.getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(capturedCommand.getValue().getSelectedMacAddress(), cdu.getMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockCduPageStateHolder);
        verify(mockCduSearchValidator);
        verify(mockCduService);
    }

    /**
     * Test search for cdu dashboard get error.
     *
     * @throws Exception the exception
     */
    @Test
    void testSearchForCduDashboardGetError() throws Exception {


        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        mockCduPageStateHolder.setCdus(null);
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        replay(mockLocalProxyService);
        expectCduSearchValidator(capturedCommand, capturedErrors, true);
        
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        
        expect(mockCduService.getCdusBySiteID(cdu.getXhibitCourtSiteId()))
            .andThrow(dataRetrievalFailureException);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameCdusUrl).param("dashboardSearch", "true")
            .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockCduPageStateHolder);
        verify(mockCduSearchValidator);
        verify(mockCduService);
    }

    /**
     * Test search for cdu dashboard runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testSearchForCduDashboardRuntimeError() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        mockCduPageStateHolder.setCdus(null);
        expectLastCall();
        expectSetModelCduList();
        replay(mockCduPageStateHolder);
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        replay(mockLocalProxyService);
        expectCduSearchValidator(capturedCommand, capturedErrors, true);
        
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        
        expect(mockCduService.getCdusBySiteID(cdu.getXhibitCourtSiteId())).andThrow(xpdmException);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameCdusUrl).param("dashboardSearch", "true")
            .param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockCduPageStateHolder);
        verify(mockCduSearchValidator);
        verify(mockCduService);
    }

    /**
     * Test search for cdu.
     *
     * @throws Exception the exception
     *
     */
    @Test
    void testSearchForCduValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        mockCduPageStateHolder.setCdus(null);
        expectLastCall();
        mockCduPageStateHolder.setCdus(cdus);
        expectSetModelCduList();
        
        expectCduSearchValidator(capturedCommand, capturedErrors, true);
        expect(mockCduService.getCduByMacAddressWithLike(cdu.getMacAddress())).andReturn(cdus);
        
        replay(mockCduPageStateHolder);
        replay(mockCduService);
        replay(mockLocalProxyService);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCdusUrl)
            .param(BTN_SEARCH_SITE, BTN_SEARCH_SITE).param(MAC_ADDRESS, cdu.getMacAddress())
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertCduSearchModel(results.getModelAndView().getModelMap(), false);
        assertEquals(cdu.getMacAddress(), capturedCommand.getValue().getMacAddress(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchValidator);
        verify(mockCduService);
        verify(mockLocalProxyService);
    }

    /**
     * Test search for cdu invalid.
     *
     * @throws Exception the exception
     *
     */
    @Test
    void testSearchForCduInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        mockCduPageStateHolder.setSites(sites);
        expectLastCall();
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall();
        mockCduPageStateHolder.setCdus(null);
        expectSetModelCduList();
        expectCduSearchValidator(capturedCommand, capturedErrors, false);
        
        replay(mockCduPageStateHolder);
        replay(mockLocalProxyService);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCdusUrl)
            .param(BTN_SEARCH_SITE, BTN_SEARCH_SITE).param(MAC_ADDRESS, cdu.getMacAddress())
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchValidator);
        verify(mockLocalProxyService);
        verify(mockCduService);
    }

    /**
     * Test show cdu details.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowCduDetailsValid() throws Exception {
        // Capture the cduCommand object
        final Capture<CduSearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(null);
        mockCduPageStateHolder.setSites(null);
        expectLastCall().anyTimes();
        mockCduPageStateHolder.setCduSearchCommand(capture(capturedCommand));
        expectLastCall().times(2);
        expect(mockCduService.getCdusBySiteID(EasyMock.isA(Long.class))).andReturn(cdus);
        mockCduPageStateHolder.setCdus(cdus);
        expectLastCall();
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall();
        expectSetModelCduList();
        expectCduSearchSelectedValidator(capturedCommand, capturedErrors, true);
        
        replay(mockLocalProxyService);
        replay(mockCduPageStateHolder);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCdusUrl)
            .param(BTN_SHOW_CDU, BTN_SHOW_CDU).param(SELECTED_MAC_ADDRESS, cdu.getMacAddress())
            .param(XHIBIT_COURTSITE_ID, cdu.getXhibitCourtSiteId().toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertFalse(capturedErrors.getValue().hasErrors(), TRUE);
        assertCduSearchModel(results.getModelAndView().getModelMap(), true);
        assertEquals(cdu.getMacAddress(), capturedCommand.getValue().getSelectedMacAddress(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
        verify(mockCduService);
    }

}
