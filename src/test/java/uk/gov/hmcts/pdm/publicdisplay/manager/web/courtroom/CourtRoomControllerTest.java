package uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom;

import org.easymock.Capture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtRoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
class CourtRoomControllerTest extends CourtRoomControllerErrorsTest {

    @Test
    void viewCourtRoomTest() throws Exception {
        mockCourtRoomPageStateHolder.reset();
        Capture<List<CourtDto>> capturedCourts = newCapture();
        expectLastCall();
        expect(mockCourtRoomService.getCourts()).andReturn(getCourtDtoList());
        mockCourtRoomPageStateHolder.setCourts(capture(capturedCourts));
        expectLastCall();
        replay(mockCourtRoomService);
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(getCourtDtoList());
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameViewCourtRoomUrl)).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewCourtRoom, returnedViewName, NOT_EQUAL);
        assertEquals(capturedCourts.getValue().get(0).getId(), getCourtDtoList().get(0).getId(), NOT_EQUAL);

        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void viewCourtRoomResetFalseTest() throws Exception {
        CourtRoomSearchCommand courtRoomSearchCommand = new CourtRoomSearchCommand();

        expect(mockCourtRoomPageStateHolder.getCourtRoomSearchCommand()).andReturn(courtRoomSearchCommand).times(2);
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(getCourtDtoList());
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameViewCourtRoomUrl).param(RESET, FALSE)).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewCourtRoom, returnedViewName, NOT_EQUAL);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void viewCourtRoomNullCommandTest() throws Exception {
        expect(mockCourtRoomPageStateHolder.getCourtRoomSearchCommand()).andReturn(null);
        mockCourtRoomPageStateHolder.reset();
        Capture<List<CourtDto>> capturedCourts = newCapture();
        expectLastCall();
        expect(mockCourtRoomService.getCourts()).andReturn(getCourtDtoList());
        mockCourtRoomPageStateHolder.setCourts(capture(capturedCourts));
        expectLastCall();
        replay(mockCourtRoomService);
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(getCourtDtoList());
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameViewCourtRoomUrl).param(RESET, FALSE)).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewCourtRoom, returnedViewName, NOT_EQUAL);
        assertEquals(capturedCourts.getValue().get(0).getId(), getCourtDtoList().get(0).getId(), NOT_EQUAL);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void showCreateCourtRoomTest() throws Exception {
        final Capture<CourtRoomSearchCommand> capturedCourtRoomSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());

        mockCourtRoomPageStateHolder.setCourtRoomSearchCommand(capture(capturedCourtRoomSearchCommand));
        mockCourtRoomSelectedValidator.validate(capture(capturedCourtRoomSearchCommand), capture(capturedErrors));
        expectLastCall();
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(courtDtos).anyTimes();
        
        replay(mockCourtRoomSelectedValidator);
        expect(mockCourtRoomService.getCourts()).andReturn(courtDtos);
        mockCourtRoomPageStateHolder.setCourts(courtDtos);
        expectLastCall();
        expect(mockCourtRoomService.getCourtSites(3)).andReturn(xhibitCourtSiteDtos);
        replay(mockCourtRoomService);
        mockCourtRoomPageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        mockCourtRoomPageStateHolder.setCourt(courtDtos.get(0));
        expectLastCall();
        expect(mockCourtRoomPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameViewCourtRoomUrl)
                .param(COURT_ID, THREE)
                .param("btnAdd", ADD)).andReturn();
        ModelAndView modelAndView = results.getModelAndView();

        assertNotNull(modelAndView.getViewName(), NULL);
        assertEquals(viewNameCreateCourtRoom, modelAndView.getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(xhibitCourtSiteDtos.get(0).getCourtId(),
                capturedCourtSites.getValue().get(0).getCourtId(), NOT_EQUAL);
        assertEquals(3, capturedCourtRoomSearchCommand.getValue().getCourtId(), NOT_EQUAL);
        assertInstanceOf(CourtRoomCreateCommand.class,
                modelAndView.getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, modelAndView.getModel().get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(courtDtos.get(0), modelAndView.getModel().get(COURT), NOT_EQUAL);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void showAmendCourtRoomTest() throws Exception {
        final Capture<CourtRoomSearchCommand> capturedCourtRoomSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());

        mockCourtRoomPageStateHolder.setCourtRoomSearchCommand(capture(capturedCourtRoomSearchCommand));
        mockCourtRoomSelectedValidator.validate(capture(capturedCourtRoomSearchCommand), capture(capturedErrors));
        expectLastCall();
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(courtDtos).anyTimes();
        replay(mockCourtRoomSelectedValidator);
        expect(mockCourtRoomService.getCourts()).andReturn(courtDtos);
        mockCourtRoomPageStateHolder.setCourts(courtDtos);
        expectLastCall();
        expect(mockCourtRoomService.getCourtSites(3)).andReturn(xhibitCourtSiteDtos);
        replay(mockCourtRoomService);
        mockCourtRoomPageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        mockCourtRoomPageStateHolder.setCourt(courtDtos.get(0));
        expectLastCall();
        expect(mockCourtRoomPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameViewCourtRoomUrl)
                .param(COURT_ID, THREE)
                .param("btnAmend", ADD)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertNotNull(results.getModelAndView().getViewName(), NULL);
        assertEquals(viewNameAmendCourtRoom, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertInstanceOf(CourtRoomAmendCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(courtDtos.get(0), model.get(COURT), NOT_EQUAL);
        assertEquals(new ArrayList<>(), model.get("courtRoomList"), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(xhibitCourtSiteDtos.get(0).getCourtId(),
                capturedCourtSites.getValue().get(0).getCourtId(), NOT_EQUAL);
        assertEquals(3, capturedCourtRoomSearchCommand.getValue().getCourtId(), NOT_EQUAL);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void showDeleteCourtRoomTest() throws Exception {
        final Capture<CourtRoomSearchCommand> capturedCourtRoomSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());

        mockCourtRoomPageStateHolder.setCourtRoomSearchCommand(capture(capturedCourtRoomSearchCommand));
        mockCourtRoomPageStateHolder.setCourtRoomsList(anyObject());
        expectLastCall();
        mockCourtRoomSelectedValidator.validate(capture(capturedCourtRoomSearchCommand), capture(capturedErrors));
        expectLastCall();
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(courtDtos).anyTimes();
        replay(mockCourtRoomSelectedValidator);
        expect(mockCourtRoomService.getCourts()).andReturn(courtDtos);
        mockCourtRoomPageStateHolder.setCourts(courtDtos);
        expectLastCall();
        expect(mockCourtRoomService.getCourtSites(3)).andReturn(xhibitCourtSiteDtos);
        replay(mockCourtRoomService);
        mockCourtRoomPageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        mockCourtRoomPageStateHolder.setCourt(courtDtos.get(0));
        expectLastCall();
        expect(mockCourtRoomPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        expect(mockCourtRoomPageStateHolder.getCourtRoomsList()).andReturn(new ArrayList<>());
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameViewCourtRoomUrl)
                .param(COURT_ID, THREE)
                .param("btnDelete", ADD)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(CourtRoomAmendCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(courtDtos.get(0), model.get(COURT), NOT_EQUAL);
        assertEquals(new ArrayList<>(), model.get("courtRoomList"), NOT_EQUAL);
        assertNotNull(results.getModelAndView().getViewName(), NULL);
        assertEquals(viewNameDeleteCourtRoom, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(xhibitCourtSiteDtos.get(0).getCourtId(),
                capturedCourtSites.getValue().get(0).getCourtId(), NOT_EQUAL);
        assertEquals(3, capturedCourtRoomSearchCommand.getValue().getCourtId(), NOT_EQUAL);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void createCourtRoomTest() throws Exception {
        final Capture<CourtRoomCreateCommand> capturedCourtRoomCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<ICourtRoomService> capturedCourtRoomService = newCapture();
        final Capture<List<CourtDto>> capturedCourtDtoList = newCapture();

        final CourtRoomDto courtRoomDto = new CourtRoomDto();
        courtRoomDto.setDescription(COURTROOM_DTO_DESCRIPTION);
        final List<CourtRoomDto> courtRoomDtoList = List.of(courtRoomDto);
        final List<CourtDto> courtDtos = getCourtDtoList();

        mockCourtRoomCreateValidator.validate(capture(capturedCourtRoomCreateCommand), capture(capturedErrors),
                capture(capturedCourtRoomService));
        expectLastCall();
        expect(mockCourtRoomService.getCourtRooms(3L)).andReturn(courtRoomDtoList);
        mockCourtRoomService.createCourtRoom(capture(capturedCourtRoomCreateCommand), anyObject());
        expectLastCall();
        expect(mockCourtRoomService.getCourts()).andReturn(getCourtDtoList());
        mockCourtRoomPageStateHolder.setCourts(capture(capturedCourtDtoList));
        expectLastCall();
        mockCourtRoomPageStateHolder.reset();
        expectLastCall();
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(courtDtos);
        replay(mockCourtRoomCreateValidator);
        replay(mockCourtRoomService);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameCreateCourtRoomUrl)
                                            .param(XHIBIT_COURTSITE_ID, THREE)
                                            .param(DESCRIPTION, CREATE_COMMAND_DESCRIPTION)
                                            .param("name", "courtRoomCreateCommandName")
                                            .param("btnCreateConfirm", ADD))
                                         .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertEquals("Court Room has been created successfully.", model.get("successMessage"), NOT_EQUAL);
        assertInstanceOf(CourtRoomSearchCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertEquals(viewNameViewCourtRoom, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(3, capturedCourtRoomCreateCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals("court_name", capturedCourtDtoList.getValue().get(0).getCourtName(), NOT_EQUAL);
        verify(mockCourtRoomCreateValidator);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void deleteCourtRoomTest() throws Exception {
        final Capture<CourtRoomDeleteCommand> capturedCourtRoomDeleteCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<CourtDto>> capturedCourtDtoList = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();

        mockCourtRoomDeleteValidator.validate(capture(capturedCourtRoomDeleteCommand), capture(capturedErrors));
        expectLastCall();
        mockCourtRoomService.updateCourtRoom(capture(capturedCourtRoomDeleteCommand), anyObject());
        expectLastCall();
        expect(mockCourtRoomService.getCourts()).andReturn(getCourtDtoList());
        mockCourtRoomPageStateHolder.setCourts(capture(capturedCourtDtoList));
        expectLastCall();
        mockCourtRoomPageStateHolder.reset();
        expectLastCall();
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(courtDtos);
        replay(mockCourtRoomDeleteValidator);
        replay(mockCourtRoomService);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameDeleteCourtRoomUrl)
                                            .param(XHIBIT_COURTSITE_ID, THREE)
                                            .param(DESCRIPTION, CREATE_COMMAND_DESCRIPTION)
                                            .param("name", "courtRoomCreateCommandName")
                                            .param("btnDeleteConfirm", ADD)
                                            .param("courtRoomId", "4"))
                                         .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertEquals(viewNameViewCourtRoom, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals("Court Room has been deleted successfully.", model.get("successMessage"), NOT_EQUAL);
        assertInstanceOf(CourtRoomSearchCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(3, capturedCourtRoomDeleteCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals("court_name", capturedCourtDtoList.getValue().get(0).getCourtName(), NOT_EQUAL);
        verify(mockCourtRoomDeleteValidator);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void updateCourtRoomTest() throws Exception {
        final Capture<CourtRoomAmendCommand> capturedCourtRoomAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<CourtDto>> capturedCourtDtoList = newCapture();
        final List<CourtRoomDto> courtRoomDtos = List.of(new CourtRoomDto());
        final List<CourtDto> courtDtos = getCourtDtoList();

        mockCourtRoomAmendValidator.validate(capture(capturedCourtRoomAmendCommand), capture(capturedErrors));
        expectLastCall();
        expect(mockCourtRoomService.getCourtRooms(3L)).andReturn(courtRoomDtos);
        mockCourtRoomService.updateCourtRoom(capture(capturedCourtRoomAmendCommand), anyObject());
        expectLastCall();
        expect(mockCourtRoomService.getCourts()).andReturn(getCourtDtoList());
        mockCourtRoomPageStateHolder.setCourts(capture(capturedCourtDtoList));
        expectLastCall();
        mockCourtRoomPageStateHolder.reset();
        expectLastCall();
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(courtDtos);
        replay(mockCourtRoomAmendValidator);
        replay(mockCourtRoomService);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendCourtRoomUrl)
                                            .param(XHIBIT_COURTSITE_ID, THREE)
                                            .param(DESCRIPTION, CREATE_COMMAND_DESCRIPTION)
                                            .param("name", "courtRoomCreateCommandName")
                                            .param("btnUpdateConfirm", ADD)
                                            .param("courtRoomId", "4"))
                                         .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertEquals(viewNameViewCourtRoom, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals("Court Room has been updated successfully.", model.get("successMessage"), NOT_EQUAL);
        assertInstanceOf(CourtRoomSearchCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(3, capturedCourtRoomAmendCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals("court_name", capturedCourtDtoList.getValue().get(0).getCourtName(), NOT_EQUAL);
        verify(mockCourtRoomAmendValidator);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }
}

