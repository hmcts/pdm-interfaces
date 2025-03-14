package uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtSiteDto;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class ShowAmendLocalProxyTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
class ShowAmendLocalProxyTest extends UpdateLocalProxyTest {

    /**
     * Test show amend local proxy valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowAmendLocalProxyValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<CourtSiteDto> capturedCourtSiteDto = newCapture();
        final Capture<LocalProxySearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expectSelectedValidator(capturedCommand, capturedBindingResult, true);
        mockLocalProxyPageStateHolder.setCourtSite(capture(capturedCourtSiteDto));
        expectLastCall();
        mockLocalProxyPageStateHolder.setLocalProxySearchCommand(capture(capturedCommand));
        expectLastCall();
        
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(xhibitCourtSites);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        expectLastCall();
        
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);
        expect(mockLocalProxyService.getCourtSiteByXhibitCourtSiteId(XHIBIT_COURT_SITE_ID))
            .andReturn(courtSite);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameLocalProxyUrl).param(BTN_AMEND, BTN_AMEND)
                .param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameAmendLocalProxy);
        assertFalse(capturedBindingResult.getValue().hasErrors(), TRUE);
        assertEquals(courtSite, capturedCourtSiteDto.getValue(), NOT_EQUAL);
        assertTrue(results.getModelAndView().getModelMap()
            .get("command") instanceof LocalProxyAmendCommand, FALSE);
        assertEquals(courtSite, results.getModelAndView().getModelMap().get(COURTSITE), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxySelectedValidator);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test show amend local proxy invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowAmendLocalProxyInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxySearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expectSelectedValidator(capturedCommand, capturedBindingResult, false);
        mockLocalProxyPageStateHolder.setLocalProxySearchCommand(capture(capturedCommand));
        expectLastCall();
      
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(null);
        mockLocalProxyPageStateHolder.setSites(null);
        expectLastCall();
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(null);
        mockLocalProxyPageStateHolder.setSchedules(null);
        expectLastCall();
        
        expect(mockLocalProxyPageStateHolder.getSites()).andReturn(xhibitCourtSites);
        expect(mockLocalProxyPageStateHolder.getSchedules()).andReturn(schedules);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameLocalProxyUrl).param(BTN_AMEND, BTN_AMEND)
                .param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString())).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

}
