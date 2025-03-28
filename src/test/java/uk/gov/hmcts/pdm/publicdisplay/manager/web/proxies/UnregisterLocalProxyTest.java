package uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.easymock.IAnswer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class UnregisterLocalProxyTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
abstract class UnregisterLocalProxyTest extends ViewLocalProxyTest {

    /**
     * Test unregister local proxy valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testUnregisterLocalProxyValid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxySearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy())
            .andReturn(xhibitCourtSites).times(2);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        mockProxyUnregisterValidator.validate(capture(capturedCommand),
            capture(capturedBindingResult));
        expectLastCall();
        mockLocalProxyPageStateHolder.setLocalProxySearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockLocalProxyService.getCourtSiteByXhibitCourtSiteId(isA(Long.class)))
            .andReturn(courtSite);
        mockLocalProxyService.unregisterLocalProxy(courtSite.getId());
        expectLastCall();
        expectPageStateHolderSetSites();
        
        replay(mockProxyUnregisterValidator);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameLocalProxyUrl).param(UNREGISTER_CONFIRM, UNREGISTER_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString()))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertFalse(capturedBindingResult.getValue().hasErrors(), TRUE);
        assertEquals(xhibitCourtSites, results.getModelAndView().getModelMap().get(COURTSITE_LIST),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyUnregisterValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test unregister local proxy params invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testUnregisterLocalProxyParamsInvalid() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxySearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy())
            .andReturn(xhibitCourtSites).times(2);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        mockProxyUnregisterValidator.validate(capture(capturedCommand),
            capture(capturedBindingResult));
        expectLastCall().andAnswer(new IAnswer<Void>() {
            @Override
            public Void answer() {
                ((BindingResult) getCurrentArguments()[1]).reject("mock error message");
                return null;
            }
        });
        mockLocalProxyPageStateHolder.setLocalProxySearchCommand(capture(capturedCommand));
        expectLastCall();
        expectPageStateHolderSetSites();
        
        replay(mockProxyUnregisterValidator);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameLocalProxyUrl).param(UNREGISTER_CONFIRM, UNREGISTER_CONFIRM))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertEquals(2, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(results.getModelAndView().getModelMap().get(COURTSITE_LIST), xhibitCourtSites,
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyUnregisterValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test unregister local proxy save error.
     *
     * @throws Exception the exception
     */
    @Test
    void testUnregisterLocalProxySaveError() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxySearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy())
            .andReturn(xhibitCourtSites).times(2);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        mockProxyUnregisterValidator.validate(capture(capturedCommand),
            capture(capturedBindingResult));
        expectLastCall();
        mockLocalProxyPageStateHolder.setLocalProxySearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockLocalProxyService.getCourtSiteByXhibitCourtSiteId(isA(Long.class)))
            .andReturn(courtSite);
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException("Mock data access exception");
        mockLocalProxyService.unregisterLocalProxy(courtSite.getId());
        expectLastCall().andThrow(dataRetrievalFailureException);
        expectPageStateHolderSetSites();
        
        replay(mockProxyUnregisterValidator);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameLocalProxyUrl).param(UNREGISTER_CONFIRM, UNREGISTER_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString()))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(results.getModelAndView().getModelMap().get(COURTSITE_LIST), xhibitCourtSites,
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyUnregisterValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test unregister local proxy runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testUnregisterLocalProxyRuntimeError() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<LocalProxySearchCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy())
            .andReturn(xhibitCourtSites).times(2);
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expect(mockLocalProxyService.getPowerSaveSchedules()).andReturn(schedules);
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        mockProxyUnregisterValidator.validate(capture(capturedCommand),
            capture(capturedBindingResult));
        expectLastCall();
        mockLocalProxyPageStateHolder.setLocalProxySearchCommand(capture(capturedCommand));
        expectLastCall();
        expect(mockLocalProxyService.getCourtSiteByXhibitCourtSiteId(isA(Long.class)))
            .andReturn(courtSite);
        XpdmException xpdmException = new XpdmException("Mock runtime exception");
        mockLocalProxyService.unregisterLocalProxy(courtSite.getId());
        expectLastCall().andThrow(xpdmException);
        expectPageStateHolderSetSites();
        
        replay(mockProxyUnregisterValidator);
        replay(mockLocalProxyService);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameLocalProxyUrl).param(UNREGISTER_CONFIRM, UNREGISTER_CONFIRM)
                .param(XHIBIT_COURTSITE_ID, XHIBIT_COURT_SITE_ID.toString()))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameLocalProxy);
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(results.getModelAndView().getModelMap().get(COURTSITE_LIST), xhibitCourtSites,
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockProxyUnregisterValidator);
        verify(mockLocalProxyService);
        verify(mockLocalProxyPageStateHolder);
    }

}
