package uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom;

import org.easymock.Capture;
import org.easymock.IAnswer;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtRoomService;

import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SuppressWarnings("PMD")
abstract class CourtRoomControllerErrorsTest extends LoadCourtRoomsControllerTest {

    @Test
    void showCreateCourtRoomWithErrorsTest() throws Exception {
        final Capture<CourtRoomSearchCommand> capturedCourtRoomSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();

        mockCourtRoomPageStateHolder
            .setCourtRoomSearchCommand(capture(capturedCourtRoomSearchCommand));
        mockCourtRoomSelectedValidator.validate(capture(capturedCourtRoomSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(courtDtos);
        replay(mockCourtRoomSelectedValidator);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameViewCourtRoomUrl).param(COURT_ID, THREE).param("btnAdd", ADD))
            .andReturn();
        ModelAndView modelAndView = results.getModelAndView();

        assertEquals(courtDtos, modelAndView.getModel().get("courtList"), NOT_EQUAL);
        assertNotNull(modelAndView.getViewName(), NULL);
        assertEquals(viewNameViewCourtRoom, modelAndView.getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void showAmendCourtRoomWithErrorsTest() throws Exception {
        final Capture<CourtRoomSearchCommand> capturedCourtRoomSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();

        mockCourtRoomPageStateHolder
            .setCourtRoomSearchCommand(capture(capturedCourtRoomSearchCommand));
        mockCourtRoomSelectedValidator.validate(capture(capturedCourtRoomSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(courtDtos);
        replay(mockCourtRoomSelectedValidator);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(
                post(mappingNameViewCourtRoomUrl).param(COURT_ID, THREE).param("btnAmend", ADD))
            .andReturn();
        String viewName = results.getModelAndView().getViewName();

        assertNotNull(viewName, NULL);
        assertEquals(viewNameViewCourtRoom, viewName, NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(3, capturedCourtRoomSearchCommand.getValue().getCourtId(), NOT_EQUAL);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void showDeleteCourtRoomWithErrorsTest() throws Exception {
        final Capture<CourtRoomSearchCommand> capturedCourtRoomSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();

        mockCourtRoomPageStateHolder
            .setCourtRoomSearchCommand(capture(capturedCourtRoomSearchCommand));
        mockCourtRoomPageStateHolder.setCourtRoomsList(anyObject());
        mockCourtRoomSelectedValidator.validate(capture(capturedCourtRoomSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockCourtRoomPageStateHolder.getCourts()).andReturn(courtDtos);
        replay(mockCourtRoomSelectedValidator);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(
                post(mappingNameViewCourtRoomUrl).param(COURT_ID, THREE).param("btnDelete", ADD))
            .andReturn();
        String viewName = results.getModelAndView().getViewName();

        assertNotNull(viewName, NULL);
        assertEquals(viewNameViewCourtRoom, viewName, NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(3, capturedCourtRoomSearchCommand.getValue().getCourtId(), NOT_EQUAL);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void createCourtRoomWithErrorsTest() throws Exception {
        final Capture<CourtRoomCreateCommand> capturedCourtRoomCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<ICourtRoomService> capturedCourtRoomService = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());

        mockCourtRoomCreateValidator.validate(capture(capturedCourtRoomCreateCommand),
            capture(capturedErrors), capture(capturedCourtRoomService));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockCourtRoomPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockCourtRoomPageStateHolder.getCourt()).andReturn(getCourtDtoList().get(0));
        replay(mockCourtRoomCreateValidator);
        replay(mockCourtRoomService);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCreateCourtRoomUrl).param(XHIBIT_COURTSITE_ID, THREE)
                .param(DESCRIPTION, CREATE_COMMAND_DESCRIPTION)
                .param(NAME, COURT_ROOM_CREATE_COMMAND_NAME).param("btnCreateConfirm", ADD))
            .andReturn();
        String viewName = results.getModelAndView().getViewName();

        assertNotNull(viewName, NOT_NULL);
        assertEquals(viewNameCreateCourtRoom, viewName, NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertInstanceOf(CourtRoomCreateCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        verify(mockCourtRoomCreateValidator);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void createCourtRoomExceptionTest() throws Exception {

        final CourtRoomDto courtRoomDto = new CourtRoomDto();
        courtRoomDto.setDescription(COURTROOM_DTO_DESCRIPTION);
        final List<CourtRoomDto> courtRoomDtoList = List.of(courtRoomDto);
        final Capture<CourtRoomCreateCommand> capturedCourtRoomCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<ICourtRoomService> capturedCourtRoomService = newCapture();
        final Capture<List<CourtRoomDto>> capturedCourtRoomDtoList = newCapture();
        final CourtDto courtDto = new CourtDto();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());

        mockCourtRoomCreateValidator.validate(capture(capturedCourtRoomCreateCommand),
            capture(capturedErrors), capture(capturedCourtRoomService));
        expectLastCall();
        expect(mockCourtRoomService.getCourtRooms(3L)).andReturn(courtRoomDtoList);
        mockCourtRoomService.createCourtRoom(capture(capturedCourtRoomCreateCommand),
            capture(capturedCourtRoomDtoList));
        expectLastCall().andThrow(new RuntimeException("CourtRoom Creation Error"));
        expect(mockCourtRoomPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockCourtRoomPageStateHolder.getCourt()).andReturn(courtDto);
        replay(mockCourtRoomCreateValidator);
        replay(mockCourtRoomService);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCreateCourtRoomUrl).param(XHIBIT_COURTSITE_ID, THREE)
                .param(DESCRIPTION, CREATE_COMMAND_DESCRIPTION)
                .param(NAME, COURT_ROOM_CREATE_COMMAND_NAME).param("btnCreateConfirm", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(CourtRoomCreateCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(courtDto, model.get(COURT), NOT_EQUAL);
        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertEquals(viewNameCreateCourtRoom, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals("Unable to create Court Room: CourtRoom Creation Error",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertEquals(3, capturedCourtRoomCreateCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(COURTROOM_DTO_DESCRIPTION,
            capturedCourtRoomDtoList.getValue().get(0).getDescription(), NOT_EQUAL);
        verify(mockCourtRoomCreateValidator);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void deleteCourtRoomErrorsTest() throws Exception {
        final Capture<CourtRoomDeleteCommand> capturedCourtRoomDeleteCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        final List<CourtRoomDto> courtRoomDtos = List.of(new CourtRoomDto());

        mockCourtRoomDeleteValidator.validate(capture(capturedCourtRoomDeleteCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockCourtRoomPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockCourtRoomPageStateHolder.getCourtRoomsList()).andReturn(courtRoomDtos);
        replay(mockCourtRoomDeleteValidator);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameDeleteCourtRoomUrl)
            .param(XHIBIT_COURTSITE_ID, THREE).param(DESCRIPTION, CREATE_COMMAND_DESCRIPTION)
            .param(NAME, COURT_ROOM_CREATE_COMMAND_NAME).param("btnDeleteConfirm", ADD)
            .param(COURTROOM_ID, "4")).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(CourtRoomDeleteCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertEquals(viewNameDeleteCourtRoom, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(3, capturedCourtRoomDeleteCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        verify(mockCourtRoomDeleteValidator);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void deleteCourtRoomExceptionTest() throws Exception {
        final Capture<CourtRoomDeleteCommand> capturedCourtRoomDeleteCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        final List<CourtRoomDto> courtRoomDtos = List.of(new CourtRoomDto());

        mockCourtRoomDeleteValidator.validate(capture(capturedCourtRoomDeleteCommand),
            capture(capturedErrors));
        expectLastCall();
        mockCourtRoomService.updateCourtRoom(capture(capturedCourtRoomDeleteCommand), anyObject());
        expectLastCall().andThrow(new DataAccessException("Court Room Deletion Error") {});
        expect(mockCourtRoomPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockCourtRoomPageStateHolder.getCourtRoomsList()).andReturn(courtRoomDtos);
        replay(mockCourtRoomDeleteValidator);
        replay(mockCourtRoomPageStateHolder);
        replay(mockCourtRoomService);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameDeleteCourtRoomUrl)
            .param(XHIBIT_COURTSITE_ID, THREE).param(DESCRIPTION, CREATE_COMMAND_DESCRIPTION)
            .param(NAME, COURT_ROOM_CREATE_COMMAND_NAME).param("btnDeleteConfirm", ADD)
            .param(COURTROOM_ID, "4")).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertInstanceOf(CourtRoomDeleteCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(3, capturedCourtRoomDeleteCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        verify(mockCourtRoomDeleteValidator);
        verify(mockCourtRoomPageStateHolder);
        verify(mockCourtRoomService);
    }

    @Test
    void updateCourtRoomErrorsTest() throws Exception {
        final Capture<CourtRoomAmendCommand> capturedCourtRoomAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        final List<CourtRoomDto> courtRoomDtos = List.of(new CourtRoomDto());

        mockCourtRoomAmendValidator.validate(capture(capturedCourtRoomAmendCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockCourtRoomPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockCourtRoomPageStateHolder.getCourtRoomsList()).andReturn(courtRoomDtos);
        replay(mockCourtRoomAmendValidator);
        replay(mockCourtRoomService);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendCourtRoomUrl)
            .param(XHIBIT_COURTSITE_ID, THREE).param(DESCRIPTION, CREATE_COMMAND_DESCRIPTION)
            .param(NAME, COURT_ROOM_CREATE_COMMAND_NAME).param("btnUpdateConfirm", ADD)
            .param(COURTROOM_ID, "4")).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(CourtRoomAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertEquals(viewNameAmendCourtRoom, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertTrue(capturedErrors.getValue().hasErrors(), FALSE);
        assertEquals(3, capturedCourtRoomAmendCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        verify(mockCourtRoomAmendValidator);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }

    @Test
    void updateCourtRoomExceptionTest() throws Exception {
        final Capture<CourtRoomAmendCommand> capturedCourtRoomAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        final List<CourtRoomDto> courtRoomDtos = List.of(new CourtRoomDto());

        mockCourtRoomAmendValidator.validate(capture(capturedCourtRoomAmendCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockCourtRoomService.getCourtRooms(3L)).andReturn(courtRoomDtos);
        mockCourtRoomService.updateCourtRoom(capture(capturedCourtRoomAmendCommand), anyObject());
        expectLastCall().andThrow(new DataAccessException("Court Room Update Error") {});
        expect(mockCourtRoomPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockCourtRoomPageStateHolder.getCourtRoomsList()).andReturn(courtRoomDtos);
        replay(mockCourtRoomAmendValidator);
        replay(mockCourtRoomService);
        replay(mockCourtRoomPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendCourtRoomUrl)
            .param(XHIBIT_COURTSITE_ID, THREE).param(DESCRIPTION, CREATE_COMMAND_DESCRIPTION)
            .param(NAME, COURT_ROOM_CREATE_COMMAND_NAME).param("btnUpdateConfirm", ADD)
            .param(COURTROOM_ID, "4")).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertEquals("Unable to update CourtRoom: Court Room Update Error",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertInstanceOf(CourtRoomAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(3, capturedCourtRoomAmendCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        verify(mockCourtRoomAmendValidator);
        verify(mockCourtRoomService);
        verify(mockCourtRoomPageStateHolder);
    }
}
