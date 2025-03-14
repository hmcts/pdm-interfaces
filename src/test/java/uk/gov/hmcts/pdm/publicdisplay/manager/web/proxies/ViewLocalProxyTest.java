package uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies;

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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class ViewLocalProxyTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
abstract class ViewLocalProxyTest extends LocalProxyTestInitializer {

    /** The Constant XHIBIT_COURT_SITE_ID. */
    protected static final Long XHIBIT_COURT_SITE_ID = -123L;

    /** The Constant SCHEDULE_ID. */
    protected static final Long SCHEDULE_ID = 1L;

    protected static final String NOT_NULL = "Not null";

    protected static final String TRUE = "True";

    protected static final String COURTSITE_LIST = "courtSiteList";

    protected static final String XHIBIT_COURTSITE_ID = "XhibitCourtSiteId";

    protected static final String COURTSITE = "courtSite";

    protected static final String UNREGISTER_CONFIRM = "unregisterConfirm";

    protected static final String VIEW_LOCAL_PROXY = "viewlocalproxy";

    protected static final String REGISTER_LOCAL_PROXY = "registerlocalproxy";

    protected static final String SCHEDULE_ID_STRING = "scheduleId";

    protected static final String BTN_AMEND = "btnAmend";

    protected static final String NEW = "New";

    protected static final String BTN_UPDATE_CONFIRM = "btnUpdateConfirm";

    /**
     * Test view local proxy get valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testViewLocalProxyGetValid() throws Exception {
        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy())
            .andReturn(xhibitCourtSites);
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        mockLocalProxyPageStateHolder.reset();
        expectLastCall();
        replay(mockLocalProxyService);
        expectSetPageStateHolderSelectionLists();
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameLocalProxyUrl)).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertTrue(results.getModelAndView().getModelMap()
            .get("command") instanceof LocalProxySearchCommand, FALSE);
        assertEquals(xhibitCourtSites, results.getModelAndView().getModelMap().get(COURTSITE_LIST),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test view local proxy from dashboard get valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testViewLocalProxyFromDashboardGetValid() throws Exception {
        // Capture the xhibit courtsite id
        final Capture<LocalProxySearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();
        final Capture<Long> capturedXhibitCourtSiteId = newCapture();

        // Add the mock calls to child classes
        expectSelectedValidator(capturedCommand, capturedBindingResult, true);
        mockLocalProxyPageStateHolder.setLocalProxySearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockLocalProxyService
            .getCourtSiteByXhibitCourtSiteId(capture(capturedXhibitCourtSiteId)))
                .andReturn(courtSite);
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy())
            .andReturn(xhibitCourtSites);
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        replay(mockLocalProxyService);
        expectSetPageStateHolderSelectionLists();
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            get(mappingNameLocalProxyUrl).param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString())
                .param("dashboardSearch", "true"))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertFalse(capturedBindingResult.getValue().hasErrors(), TRUE);
        assertEquals(XHIBIT_COURT_SITE_ID, capturedXhibitCourtSiteId.getValue(), NOT_EQUAL);
        assertEquals(courtSite, results.getModelAndView().getModelMap().get(COURTSITE), NOT_EQUAL);
        assertEquals(xhibitCourtSites, results.getModelAndView().getModelMap().get(COURTSITE_LIST),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
        verify(mockProxySelectedValidator);
    }

    /**
     * Test view local proxy post valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testViewLocalProxyPostValid() throws Exception {
        // Capture the xhibit courtsite id
        final Capture<Long> capturedXhibitCourtSiteId = newCapture();
        final Capture<LocalProxySearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expectSelectedValidator(capturedCommand, capturedBindingResult, true);
        expect(mockLocalProxyService
            .getCourtSiteByXhibitCourtSiteId(capture(capturedXhibitCourtSiteId)))
                .andReturn(courtSite);
        
        mockLocalProxyPageStateHolder.setLocalProxySearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(xhibitCourtSites);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        expectLastCall();
        expect(mockLocalProxyPageStateHolder.getSites()).andReturn(xhibitCourtSites);
        
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameLocalProxyUrl).param(VIEW_LOCAL_PROXY, VIEW_LOCAL_PROXY)
                .param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertFalse(capturedBindingResult.getValue().hasErrors(), TRUE);
        assertEquals(XHIBIT_COURT_SITE_ID, capturedXhibitCourtSiteId.getValue(), NOT_EQUAL);
        assertEquals(courtSite, results.getModelAndView().getModelMap().get(COURTSITE), NOT_EQUAL);
        assertEquals(xhibitCourtSites, results.getModelAndView().getModelMap().get(COURTSITE_LIST),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
        verify(mockProxySelectedValidator);
    }

    /**
     * Test view local proxy post params invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testViewLocalProxyPostParamsInvalid() throws Exception {
        // Capture the command
        final Capture<BindingResult> capturedBindingResult = newCapture();
        final Capture<LocalProxySearchCommand> capturedCommand = newCapture();

        // Add the mock calls to child classes
        expectSelectedValidator(capturedCommand, capturedBindingResult, true);
        mockLocalProxyPageStateHolder.setLocalProxySearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(xhibitCourtSites);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        expectLastCall();
        expect(mockLocalProxyPageStateHolder.getSites()).andReturn(xhibitCourtSites);
        
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameLocalProxyUrl).param(VIEW_LOCAL_PROXY, VIEW_LOCAL_PROXY))
                .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(xhibitCourtSites, results.getModelAndView().getModelMap().get(COURTSITE_LIST),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

}
