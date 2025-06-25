package uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies;

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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class RegisterLocalProxyTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class RegisterLocalProxyTest extends UnregisterLocalProxyTest {

    /**
     * Test register local proxy get valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterLocalProxyGetValid() throws Exception {
        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithoutLocalProxy())
            .andReturn(xhibitCourtSites);
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        replay(mockLocalProxyService);
        expectSetPageStateHolderSelectionLists();
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameRegisterLocalProxyUrl)).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameRegisterLocalProxy);
        assertTrue(results.getModelAndView().getModelMap()
            .get("command") instanceof LocalProxyRegisterCommand, FALSE);
        assertEquals(xhibitCourtSites, results.getModelAndView().getModelMap().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(schedules, results.getModelAndView().getModelMap().get("scheduleList"),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test register local proxy post valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterLocalProxyPostValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxyRegisterCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithoutLocalProxy())
            .andReturn(xhibitCourtSites).times(2);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules).times(2);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        mockProxyRegisterValidator.validate(capture(capturedCommand),
            capture(capturedBindingResult));
        expectLastCall();
        mockLocalProxyService.registerLocalProxy(capture(capturedCommand));
        expectLastCall();
        expectSetPageStateHolderSelectionLists();
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);
        
        replay(mockProxyRegisterValidator);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameRegisterLocalProxyUrl).param(REGISTER_LOCAL_PROXY, REGISTER_LOCAL_PROXY)
                .param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString())
                .param(TITLE, courtSite.getTitle()).param("ipAddress", courtSite.getIpAddress())
                .param(SCHEDULE_ID_STRING, SCHEDULE_ID.toString()))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameRegisterLocalProxy);
        assertNull(capturedCommand.getValue().getXhibitCourtSiteId(), NOT_NULL);
        assertNull(capturedCommand.getValue().getTitle(), NOT_NULL);
        assertNull(capturedCommand.getValue().getIpAddress(), NOT_NULL);
        assertNull(capturedCommand.getValue().getScheduleId(), NOT_NULL);
        assertFalse(capturedBindingResult.getValue().hasErrors(), TRUE);
        assertEquals(xhibitCourtSites, results.getModelAndView().getModelMap().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(schedules, results.getModelAndView().getModelMap().get("scheduleList"),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyRegisterValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test register local proxy post params invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterLocalProxyPostParamsInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxyRegisterCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithoutLocalProxy())
            .andReturn(xhibitCourtSites).times(2);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules).times(2);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        expectLastCall();
        mockProxyRegisterValidator.validate(capture(capturedCommand),
            capture(capturedBindingResult));
        expectLastCall();
        expectSetPageStateHolderSelectionLists();
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);
        
        replay(mockProxyRegisterValidator);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameRegisterLocalProxyUrl)
            .param(REGISTER_LOCAL_PROXY, REGISTER_LOCAL_PROXY)).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameRegisterLocalProxy);
        assertEquals(4, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(results.getModelAndView().getModelMap().get(COURTSITE_LIST), xhibitCourtSites,
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyRegisterValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test register local proxy post save error.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterLocalProxyPostSaveError() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxyRegisterCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithoutLocalProxy())
            .andReturn(xhibitCourtSites).times(2);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules).times(2);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        mockProxyRegisterValidator.validate(capture(capturedCommand),
            capture(capturedBindingResult));
        expectLastCall();
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException("Mock data access exception");
        mockLocalProxyService.registerLocalProxy(capture(capturedCommand));
        expectLastCall().andThrow(dataRetrievalFailureException);
        expectSetPageStateHolderSelectionLists();
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);
        
        replay(mockProxyRegisterValidator);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameRegisterLocalProxyUrl).param(REGISTER_LOCAL_PROXY, REGISTER_LOCAL_PROXY)
                .param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString())
                .param(TITLE, courtSite.getTitle()).param("ipAddress", courtSite.getIpAddress())
                .param(SCHEDULE_ID_STRING, SCHEDULE_ID.toString()))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameRegisterLocalProxy);
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(XHIBIT_COURT_SITE_ID, capturedCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(courtSite.getTitle(), capturedCommand.getValue().getTitle(), NOT_EQUAL);
        assertEquals(courtSite.getIpAddress(), capturedCommand.getValue().getIpAddress(),
            NOT_EQUAL);
        assertEquals(SCHEDULE_ID, capturedCommand.getValue().getScheduleId(), NOT_EQUAL);
        assertEquals(xhibitCourtSites, results.getModelAndView().getModelMap().get(COURTSITE_LIST),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyRegisterValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test register local proxy post runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testRegisterLocalProxyPostRuntimeError() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxyRegisterCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithoutLocalProxy())
            .andReturn(xhibitCourtSites).times(2);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules).times(2);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        mockProxyRegisterValidator.validate(capture(capturedCommand),
            capture(capturedBindingResult));
        expectLastCall();
        XpdmException xpdmException = new XpdmException("Mock runtime exception");
        mockLocalProxyService.registerLocalProxy(capture(capturedCommand));
        expectLastCall().andThrow(xpdmException);
        expectSetPageStateHolderSelectionLists();
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);
        
        replay(mockProxyRegisterValidator);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameRegisterLocalProxyUrl).param(REGISTER_LOCAL_PROXY, REGISTER_LOCAL_PROXY)
                .param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString())
                .param(TITLE, courtSite.getTitle()).param("ipAddress", courtSite.getIpAddress())
                .param(SCHEDULE_ID_STRING, SCHEDULE_ID.toString()))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameRegisterLocalProxy);
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(XHIBIT_COURT_SITE_ID, capturedCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(courtSite.getTitle(), capturedCommand.getValue().getTitle(), NOT_EQUAL);
        assertEquals(courtSite.getIpAddress(), capturedCommand.getValue().getIpAddress(),
            NOT_EQUAL);
        assertEquals(SCHEDULE_ID, capturedCommand.getValue().getScheduleId(), NOT_EQUAL);
        assertEquals(xhibitCourtSites, results.getModelAndView().getModelMap().get(COURTSITE_LIST),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyRegisterValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

}
