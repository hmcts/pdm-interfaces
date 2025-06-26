package uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtSiteDto;

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
 * The Class UpdateLocalProxyTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class UpdateLocalProxyTest extends RegisterLocalProxyTest {

    /**
     * Test update local proxy valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testUpdateLocalProxyValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CourtSiteDto> capturedCourtSiteDto = newCapture();
        final Capture<LocalProxyAmendCommand> capturedCommand = newCapture();
        final LocalProxySearchCommand capturedSearchCommand = new LocalProxySearchCommand();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Amend the court site object
        courtSite.setTitle(NEW + courtSite.getTitle());
        courtSite.setScheduleId(courtSite.getScheduleId() + 1L);
        courtSite.setNotification(NEW + courtSite.getNotification());

        // Add the mock calls to child classes
        expectAmendValidator(capturedCommand, capturedBindingResult, true);
        expect(mockLocalProxyPageStateHolder.getCourtSite()).andReturn(courtSite);
        mockLocalProxyService.updateLocalProxy(capture(capturedCourtSiteDto),
            capture(capturedCommand));
        expectLastCall();
        
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(xhibitCourtSites);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        expectLastCall();
        
        expect(mockLocalProxyService.getCourtSiteByXhibitCourtSiteId(EasyMock.isA(Long.class))).andReturn(courtSite);
        mockLocalProxyPageStateHolder.setCourtSite(courtSite);
        expectLastCall();
        
        expect(mockLocalProxyPageStateHolder.getLocalProxySearchCommand()).andReturn(capturedSearchCommand).times(2);
        expect(mockLocalProxyPageStateHolder.getSites()).andReturn(xhibitCourtSites);
        
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendLocalProxyUrl)
            .param(BTN_UPDATE_CONFIRM, BTN_UPDATE_CONFIRM).param(TITLE, courtSite.getTitle())
            .param(SCHEDULE_ID_STRING, courtSite.getScheduleId().toString())
            .param(NOTIFICATION, courtSite.getNotification())
            .param("courtSiteId", courtSite.getId().toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertFalse(capturedBindingResult.getValue().hasErrors(), TRUE);
        assertSuccessfulMessage(results.getModelAndView().getModelMap());
        assertEquals(courtSite, capturedCourtSiteDto.getValue(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test update local proxy invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testUpdateLocalProxyInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxyAmendCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Amend the court site object
        courtSite.setTitle(NEW + courtSite.getTitle());
        courtSite.setScheduleId(courtSite.getScheduleId() + 1L);
        courtSite.setNotification(NEW + courtSite.getNotification());

        // Add the mock calls to child classes
        expectAmendValidator(capturedCommand, capturedBindingResult, false);
        
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(null);
        mockLocalProxyPageStateHolder.setSites(null);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(null);
        mockLocalProxyPageStateHolder.setSchedules(null);
        expectLastCall();
        
        expect(mockLocalProxyService.getCourtSiteByXhibitCourtSiteId(null)).andReturn(null);
        mockLocalProxyPageStateHolder.setCourtSite(null);
        expectLastCall();
        
        
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);
        expect(mockLocalProxyPageStateHolder.getCourtSite()).andReturn(courtSite);
        
        replay(mockLocalProxyPageStateHolder);
        replay(mockLocalProxyService);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendLocalProxyUrl)
            .param(BTN_UPDATE_CONFIRM, BTN_UPDATE_CONFIRM).param(TITLE, courtSite.getTitle())
            .param(SCHEDULE_ID_STRING, courtSite.getScheduleId().toString())
            .param(NOTIFICATION, courtSite.getNotification())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameAmendLocalProxy);
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(courtSite, results.getModelAndView().getModelMap().get(COURTSITE), NOT_EQUAL);
        assertEquals(schedules, results.getModelAndView().getModelMap().get("scheduleList"),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test update local proxy save error.
     *
     * @throws Exception the exception
     */
    @Test
    void testUpdateLocalProxySaveError() throws Exception {

        // Capture the cduCommand object and errors passed out
        final Capture<CourtSiteDto> capturedCourtSiteDto = newCapture();
        final Capture<LocalProxyAmendCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Amend the court site object
        courtSite.setTitle(NEW + courtSite.getTitle());
        courtSite.setScheduleId(courtSite.getScheduleId() + 1L);
        courtSite.setNotification(NEW + courtSite.getNotification());


        // Add the mock calls to child classes
        expectAmendValidator(capturedCommand, capturedBindingResult, true);
        
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(null);
        mockLocalProxyPageStateHolder.setSites(null);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(null);
        mockLocalProxyPageStateHolder.setSchedules(null);
        expectLastCall();
        
        expect(mockLocalProxyService.getCourtSiteByXhibitCourtSiteId(null)).andReturn(null);
        mockLocalProxyPageStateHolder.setCourtSite(null);
        expectLastCall();
        
        expect(mockLocalProxyPageStateHolder.getCourtSite()).andReturn(courtSite);
        expectLastCall().times(2);
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);

        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException("Mock data access exception");
        mockLocalProxyService.updateLocalProxy(capture(capturedCourtSiteDto),
            capture(capturedCommand));
        expectLastCall().andThrow(dataRetrievalFailureException);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendLocalProxyUrl)
            .param(BTN_UPDATE_CONFIRM, BTN_UPDATE_CONFIRM).param(TITLE, courtSite.getTitle())
            .param(SCHEDULE_ID_STRING, courtSite.getScheduleId().toString())
            .param(NOTIFICATION, courtSite.getNotification())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameAmendLocalProxy);
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyAmendValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test update local proxy runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testUpdateLocalProxyRuntimeError() throws Exception {

        // Capture the cduCommand object and errors passed out
        final Capture<CourtSiteDto> capturedCourtSiteDto = newCapture();
        final Capture<LocalProxyAmendCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Amend the court site object
        courtSite.setTitle(NEW + courtSite.getTitle());
        courtSite.setScheduleId(courtSite.getScheduleId() + 1L);
        courtSite.setNotification(NEW + courtSite.getNotification());


        // Add the mock calls to child classes
        expectAmendValidator(capturedCommand, capturedBindingResult, true);
        
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(null);
        mockLocalProxyPageStateHolder.setSites(null);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(null);
        mockLocalProxyPageStateHolder.setSchedules(null);
        expectLastCall();
        
        expect(mockLocalProxyService.getCourtSiteByXhibitCourtSiteId(null)).andReturn(null);
        mockLocalProxyPageStateHolder.setCourtSite(null);
        expectLastCall();
        
        expect(mockLocalProxyPageStateHolder.getCourtSite()).andReturn(courtSite);
        expectLastCall().times(2);
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);

        XpdmException xpdmException = new XpdmException("Mock runtime exception");
        mockLocalProxyService.updateLocalProxy(capture(capturedCourtSiteDto),
            capture(capturedCommand));
        expectLastCall().andThrow(xpdmException);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendLocalProxyUrl)
            .param(BTN_UPDATE_CONFIRM, BTN_UPDATE_CONFIRM).param(TITLE, courtSite.getTitle())
            .param(SCHEDULE_ID_STRING, courtSite.getScheduleId().toString())
            .param(NOTIFICATION, courtSite.getNotification())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameAmendLocalProxy);
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyAmendValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

}
