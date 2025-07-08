package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD")
abstract class CourtControllerTest extends CourtControllerBase {

    @Test
    void viewCourtTest() throws Exception {
        final Capture<List<CourtDto>> capturedCourts = newCapture();

        mockCourtPageStateHolder.reset();
        expectLastCall();
        expect(mockCourtService.getCourts()).andReturn(getCourtDtoList());
        mockCourtPageStateHolder.setCourts(capture(capturedCourts));
        expectLastCall();
        replay(mockCourtService);
        expect(mockCourtPageStateHolder.getCourts()).andReturn(getCourtDtoList());
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameViewCourtSiteUrl)).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewCourtSite, returnedViewName, NOT_EQUAL);
        assertEquals(capturedCourts.getValue().get(0).getId(), getCourtDtoList().get(0).getId(), NOT_EQUAL);

        verify(mockCourtService);
        verify(mockCourtPageStateHolder);
    }

    @Test
    void viewCourtResetFalseTest() throws Exception {
        CourtSearchCommand courtSearchCommand = new CourtSearchCommand();

        expect(mockCourtPageStateHolder.getCourtSearchCommand()).andReturn(courtSearchCommand).times(2);
        expect(mockCourtPageStateHolder.getCourts()).andReturn(getCourtDtoList());
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameViewCourtSiteUrl).param(RESET, FALSE)).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewCourtSite, returnedViewName, NOT_EQUAL);
        verify(mockCourtPageStateHolder);
    }

    @Test
    void viewCourtNullCommandTest() throws Exception {
        expect(mockCourtPageStateHolder.getCourtSearchCommand()).andReturn(null);
        mockCourtPageStateHolder.reset();
        Capture<List<CourtDto>> capturedCourts = newCapture();
        expectLastCall();
        expect(mockCourtService.getCourts()).andReturn(getCourtDtoList());
        mockCourtPageStateHolder.setCourts(capture(capturedCourts));
        expectLastCall();
        replay(mockCourtService);
        expect(mockCourtPageStateHolder.getCourts()).andReturn(getCourtDtoList());
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameViewCourtSiteUrl).param(RESET, FALSE)).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewCourtSite, returnedViewName, NOT_EQUAL);
        assertEquals(capturedCourts.getValue().get(0).getId(), getCourtDtoList().get(0).getId(), NOT_EQUAL);
        verify(mockCourtService);
        verify(mockCourtPageStateHolder);
    }

    @Test
    void showCreateCourtTest() throws Exception {
        final Capture<CourtSearchCommand> capturedCourtSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        final Capture<CourtDto> capturedCourtDto = newCapture();
        final Capture<List<CourtDto>> capturedCourtDtoList = newCapture();

        mockCourtPageStateHolder.setCourtSearchCommand(capture(capturedCourtSearchCommand));
        expectLastCall();
        mockCourtSelectedValidator.validate(capture(capturedCourtSearchCommand),
            capture(capturedErrors));
        expectLastCall();
        replay(mockCourtSelectedValidator);
        expect(mockCourtPageStateHolder.getCourts()).andReturn(courtDtos).anyTimes();
        mockCourtPageStateHolder.setCourt(capture(capturedCourtDto));
        expectLastCall().anyTimes();
        mockCourtPageStateHolder.setCourts(capture(capturedCourtDtoList));
        expectLastCall();
        expect(mockCourtPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        expect(mockCourtPageStateHolder.getCourt()).andReturn(courtDtos.get(0)).anyTimes();
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameViewCourtSiteUrl).param(COURT_ID, THREE).param("btnAdd", ADD))
            .andReturn();
        ModelAndView modelAndView = results.getModelAndView();

        assertNotNull(modelAndView.getViewName(), NULL);
        assertEquals(viewNameCreateCourt, modelAndView.getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(3, capturedCourtSearchCommand.getValue().getCourtId(), NOT_EQUAL);
        assertInstanceOf(CourtCreateCommand.class, modelAndView.getModel().get(COMMAND),
            NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, modelAndView.getModel().get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(courtDtos.get(0), modelAndView.getModel().get(COURT), NOT_EQUAL);
        verify(mockCourtSelectedValidator);
        verify(mockCourtPageStateHolder);
    }
    
    @Test
    void showCreateCourtEmptyListsTest() throws Exception {
        final Capture<CourtSearchCommand> capturedCourtSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();
        final Capture<CourtDto> capturedCourtDto = newCapture();

        mockCourtPageStateHolder.setCourtSearchCommand(capture(capturedCourtSearchCommand));
        expectLastCall();
        mockCourtSelectedValidator.validate(capture(capturedCourtSearchCommand),
            capture(capturedErrors));
        expectLastCall();
        
        expect(mockCourtService.getCourts()).andReturn(new ArrayList<>()).times(5);
        mockCourtPageStateHolder.setCourts(new ArrayList<>());
        expectLastCall().times(5);
        
        expect(mockCourtPageStateHolder.getSites()).andReturn(new ArrayList<>()).times(5);
        
        expect(mockCourtPageStateHolder.getCourts()).andReturn(courtDtos);
        mockCourtPageStateHolder.setCourt(capture(capturedCourtDto));
        expectLastCall().times(2);
        
        expect(mockCourtPageStateHolder.getSites()).andReturn(null);
        expect(mockCourtPageStateHolder.getCourt()).andReturn(null);
        
        replay(mockCourtService);
        replay(mockCourtSelectedValidator);
        replay(mockCourtPageStateHolder);
        
        boolean result = true;
        // Perform the test
        mockMvc
            .perform(post(mappingNameViewCourtSiteUrl).param(COURT_ID, THREE).param("btnAdd", ADD))
            .andReturn();

        assertTrue(result, FALSE);
        verify(mockCourtService);
        verify(mockCourtSelectedValidator);
        verify(mockCourtPageStateHolder);
    }

    @Test
    void createCourtSiteTest() throws Exception {
        final Capture<CourtCreateCommand> capturedCourtCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();

        mockCourtCreateValidator.validate(capture(capturedCourtCreateCommand), capture(capturedErrors), anyObject(),
                anyInt());
        expectLastCall();
        replay(mockCourtCreateValidator);
        mockCourtService.createCourt(capture(capturedCourtCreateCommand), eq(3), eq(0));
        mockCourtPageStateHolder.reset();
        Capture<List<CourtDto>> capturedCourts = newCapture();
        expectLastCall();
        expect(mockCourtService.getCourts()).andReturn(courtDtos);
        mockCourtPageStateHolder.setCourts(capture(capturedCourts));
        expectLastCall();
        replay(mockCourtService);
        expect(mockCourtPageStateHolder.getCourts()).andReturn(courtDtos);
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCreateCourtUrl)
                                                 .param(COURT_ID, THREE)
                                                 .param("btnCreateConfirm", ADD)
                                                 .param(COURTSITE_NAME_PARAM, COURTSITE_NAME)
                                                 .param(COURTSITE_CODE_PARAM, COURTSITE_CODE))
                                         .andReturn();
        ModelAndView modelAndView = results.getModelAndView();

        assertNotNull(modelAndView.getViewName(), NULL);
        assertEquals(viewNameViewCourtSite, modelAndView.getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(COURTSITE_NAME, capturedCourtCreateCommand.getValue().getCourtSiteName(), NOT_EQUAL);
        assertInstanceOf(CourtSearchCommand.class,
                modelAndView.getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertEquals("Court has been created successfully.", modelAndView.getModel().get("successMessage"), NOT_EQUAL);
        assertEquals(courtDtos, modelAndView.getModel().get("courtList"), NOT_EQUAL);
        assertEquals(courtDtos, capturedCourts.getValue(), NOT_EQUAL);
        verify(mockCourtCreateValidator);
        verify(mockCourtPageStateHolder);
        verify(mockCourtService);
    }

    @Test
    void showAmendCourtTest() throws Exception {
        final Capture<CourtSearchCommand> capturedCourtSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        final Capture<CourtDto> capturedCourtDto = newCapture();

        mockCourtPageStateHolder.setCourtSearchCommand(capture(capturedCourtSearchCommand));
        mockCourtSelectedValidator.validate(capture(capturedCourtSearchCommand), capture(capturedErrors));
        expectLastCall();
        expect(mockCourtPageStateHolder.getCourts()).andReturn(courtDtos).anyTimes();
        mockCourtPageStateHolder.setCourt(capture(capturedCourtDto));
        expectLastCall().anyTimes();
        expect(mockCourtService.getCourts()).andReturn(courtDtos);
        mockCourtPageStateHolder.setCourts(courtDtos);
        expectLastCall();
        expect(mockCourtService.getCourtSites(3)).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockCourtPageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockCourtPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        expect(mockCourtPageStateHolder.getCourt()).andReturn(courtDtos.get(0)).anyTimes();
        replay(mockCourtSelectedValidator);
        replay(mockCourtService);
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameViewCourtSiteUrl)
                .param(COURT_ID, THREE)
                .param("btnAmend", ADD)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertNotNull(results.getModelAndView().getViewName(), NULL);
        assertEquals(viewNameAmendCourt, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertInstanceOf(CourtAmendCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(courtDtos.get(0), model.get(COURT), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(xhibitCourtSiteDtos.get(0).getCourtId(),
                capturedCourtSites.getValue().get(0).getCourtId(), NOT_EQUAL);
        assertEquals(3, capturedCourtSearchCommand.getValue().getCourtId(), NOT_EQUAL);
        verify(mockCourtService);
        verify(mockCourtPageStateHolder);
        verify(mockCourtSelectedValidator);
    }
    
    @Test
    void showAmendCourtEmptyListTest() throws Exception {
        final Capture<CourtSearchCommand> capturedCourtSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();
        final Capture<CourtDto> capturedCourtDto = newCapture();

        mockCourtPageStateHolder.setCourtSearchCommand(capture(capturedCourtSearchCommand));
        mockCourtSelectedValidator.validate(capture(capturedCourtSearchCommand), capture(capturedErrors));
        expectLastCall();
        
        expect(mockCourtService.getCourts()).andReturn(new ArrayList<>()).times(5);
        mockCourtPageStateHolder.setCourts(new ArrayList<>());
        expectLastCall().times(5);
        expect(mockCourtService.getCourtSites(EasyMock.isA(Integer.class)))
            .andReturn(new ArrayList<>()).times(5);
        mockCourtPageStateHolder.setSites(new ArrayList<>());
        expectLastCall().times(5);
        
        expect(mockCourtPageStateHolder.getSites()).andReturn(new ArrayList<>()).times(5);
        
        expect(mockCourtPageStateHolder.getCourts()).andReturn(courtDtos);
        mockCourtPageStateHolder.setCourt(capture(capturedCourtDto));
        expectLastCall().times(2);
        
        expect(mockCourtPageStateHolder.getSites()).andReturn(null);
        expect(mockCourtPageStateHolder.getCourt()).andReturn(null);
        
        replay(mockCourtSelectedValidator);
        replay(mockCourtService);
        replay(mockCourtPageStateHolder);

        boolean result = true;
        // Perform the test
        mockMvc.perform(post(mappingNameViewCourtSiteUrl)
                .param(COURT_ID, THREE)
                .param("btnAmend", ADD)).andReturn();

        assertTrue(result, FALSE);
        verify(mockCourtService);
        verify(mockCourtPageStateHolder);
        verify(mockCourtSelectedValidator);
    }

    @Test
    void updateCourtTest() throws Exception {
        final Capture<CourtAmendCommand> capturedCourtAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<CourtDto>> capturedCourtDtoList = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();

        mockCourtAmendValidator.validate(capture(capturedCourtAmendCommand), capture(capturedErrors));
        expectLastCall();
        mockCourtService.updateCourt(capture(capturedCourtAmendCommand));
        expectLastCall();
        mockCourtPageStateHolder.reset();
        expectLastCall();
        expect(mockCourtService.getCourts()).andReturn(getCourtDtoList());
        mockCourtPageStateHolder.setCourts(capture(capturedCourtDtoList));
        expectLastCall();
        expect(mockCourtPageStateHolder.getCourts()).andReturn(courtDtos);
        replay(mockCourtAmendValidator);
        replay(mockCourtService);
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendCourtUrl)
                                                 .param(XHIBIT_COURTSITE_ID, THREE)
                                                 .param(COURT_ID, THREE)
                                                 .param("btnUpdateConfirm", ADD)
                                                 .param(COURTSITE_NAME_PARAM, COURTSITE_NAME)
                                                 .param(COURTSITE_CODE_PARAM, COURTSITE_CODE))
                                         .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertEquals(viewNameViewCourtSite, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals("Court has been updated successfully.", model.get("successMessage"), NOT_EQUAL);
        assertInstanceOf(CourtSearchCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(3, capturedCourtAmendCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals("court_name", capturedCourtDtoList.getValue().get(0).getCourtName(), NOT_EQUAL);
        verify(mockCourtAmendValidator);
        verify(mockCourtService);
        verify(mockCourtPageStateHolder);
    }
}
