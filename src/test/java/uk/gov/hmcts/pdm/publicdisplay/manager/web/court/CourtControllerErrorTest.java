package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
class CourtControllerErrorTest extends CourtControllerTest {
    @Test
    void showCreateCourtWithErrorsTest() throws Exception {
        final Capture<CourtSearchCommand> capturedCourtSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());

        mockCourtPageStateHolder.setCourtSearchCommand(capture(capturedCourtSearchCommand));
        expectLastCall();
        mockCourtSelectedValidator.validate(capture(capturedCourtSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        replay(mockCourtSelectedValidator);
        expect(mockCourtPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameViewCourtSiteUrl).param(COURT_ID, THREE).param("btnAdd", ADD))
            .andReturn();
        ModelAndView modelAndView = results.getModelAndView();

        assertNotNull(modelAndView.getViewName(), NULL);
        assertEquals(viewNameViewCourtSite, modelAndView.getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(3, capturedCourtSearchCommand.getValue().getCourtId(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, modelAndView.getModel().get(COURTSITE_LIST), NOT_EQUAL);
        verify(mockCourtSelectedValidator);
        verify(mockCourtPageStateHolder);
    }

    @Test
    void createCourtSiteErrorTest() throws Exception {
        final Capture<CourtCreateCommand> capturedCourtCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());

        expect(mockCourtPageStateHolder.getCourt()).andReturn(courtDtos.get(0));
        mockCourtCreateValidator.validate(capture(capturedCourtCreateCommand),
            capture(capturedErrors), anyObject(), anyInt());
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        replay(mockCourtCreateValidator);
        expect(mockCourtPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameCreateCourtUrl).param(COURT_ID, THREE)
                .param("btnCreateConfirm", ADD).param(COURTSITE_NAME_PARAM, COURTSITE_NAME)
                .param(COURTSITE_CODE_PARAM, COURTSITE_CODE)).andReturn();
        ModelAndView modelAndView = results.getModelAndView();

        assertNotNull(modelAndView.getViewName(), NULL);
        assertEquals(viewNameCreateCourt, modelAndView.getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals("A court site name", capturedCourtCreateCommand.getValue().getCourtSiteName(),
            NOT_EQUAL);
        assertInstanceOf(CourtCreateCommand.class, modelAndView.getModel().get(COMMAND),
            NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, modelAndView.getModel().get("courtSiteList"), NOT_EQUAL);
        verify(mockCourtCreateValidator);
        verify(mockCourtPageStateHolder);
    }

    @Test
    void createCourtSiteExceptionTest() throws Exception {
        final Capture<CourtCreateCommand> capturedCourtCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();

        expect(mockCourtPageStateHolder.getCourt()).andReturn(courtDtos.get(0)).times(3);
        mockCourtCreateValidator.validate(capture(capturedCourtCreateCommand),
            capture(capturedErrors), anyObject(), anyInt());
        expectLastCall();
        replay(mockCourtCreateValidator);
        mockCourtService.createCourt(capture(capturedCourtCreateCommand), eq(3), eq(1));
        expectLastCall().andThrow(new RuntimeException("Create Court Exception"));
        replay(mockCourtService);
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameCreateCourtUrl).param(COURT_ID, THREE)
                .param("btnCreateConfirm", ADD).param(COURTSITE_NAME_PARAM, COURTSITE_NAME)
                .param(COURTSITE_CODE_PARAM, COURTSITE_CODE)).andReturn();
        ModelAndView modelAndView = results.getModelAndView();

        assertNotNull(modelAndView.getViewName(), NULL);
        assertEquals(viewNameCreateCourt, modelAndView.getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals("Unable to create Court: Create Court Exception",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertEquals("A court site name", capturedCourtCreateCommand.getValue().getCourtSiteName(),
            NOT_EQUAL);
        verify(mockCourtCreateValidator);
        verify(mockCourtPageStateHolder);
        verify(mockCourtService);
    }

    @Test
    void showAmendCourtWithErrorsTest() throws Exception {
        final Capture<CourtSearchCommand> capturedCourtSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<CourtDto> courtDtos = getCourtDtoList();

        mockCourtPageStateHolder.setCourtSearchCommand(capture(capturedCourtSearchCommand));
        mockCourtSelectedValidator.validate(capture(capturedCourtSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockCourtPageStateHolder.getCourts()).andReturn(courtDtos);
        replay(mockCourtSelectedValidator);
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(
                post(mappingNameViewCourtSiteUrl).param(COURT_ID, THREE).param("btnAmend", ADD))
            .andReturn();
        String viewName = results.getModelAndView().getViewName();

        assertNotNull(viewName, NULL);
        assertEquals(viewNameViewCourtSite, viewName, NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(3, capturedCourtSearchCommand.getValue().getCourtId(), NOT_EQUAL);
        verify(mockCourtPageStateHolder);
    }

    @Test
    void updateCourtErrorsTest() throws Exception {
        final Capture<CourtAmendCommand> capturedCourtAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        mockCourtAmendValidator.validate(capture(capturedCourtAmendCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        replay(mockCourtAmendValidator);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameAmendCourtUrl).param(XHIBIT_COURTSITE_ID, THREE).param(COURT_ID, THREE)
                .param("btnUpdateConfirm", ADD).param(COURTSITE_NAME_PARAM, COURTSITE_NAME)
                .param(COURTSITE_CODE_PARAM, COURTSITE_CODE))
            .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(CourtAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results.getModelAndView().getViewName(), NOT_NULL);
        assertEquals(viewNameAmendCourt, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), FALSE);
        assertEquals(3, capturedCourtAmendCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        verify(mockCourtAmendValidator);
    }

    @Test
    void updateCourtExceptionTest() throws Exception {
        final Capture<CourtAmendCommand> capturedCourtAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        mockCourtAmendValidator.validate(capture(capturedCourtAmendCommand),
            capture(capturedErrors));
        expectLastCall();
        mockCourtService.updateCourt(capture(capturedCourtAmendCommand));
        expectLastCall().andThrow(new DataAccessException("Court Update Error") {});
        replay(mockCourtAmendValidator);
        replay(mockCourtService);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameAmendCourtUrl).param(XHIBIT_COURTSITE_ID, THREE).param(COURT_ID, THREE)
                .param("btnUpdateConfirm", ADD).param(COURTSITE_NAME_PARAM, COURTSITE_NAME)
                .param(COURTSITE_CODE_PARAM, COURTSITE_CODE))
            .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertEquals("Unable to update Court: Court Update Error",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertInstanceOf(CourtAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(3, capturedCourtAmendCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        verify(mockCourtAmendValidator);
        verify(mockCourtService);
    }

    @Test
    void loadCourtSiteTest() throws Exception {
        XhbCourtSiteDao xhbCourtSiteDao = new XhbCourtSiteDao();
        xhbCourtSiteDao.setCourtId(1);
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        xhibitCourtSiteDto.setCourtId(4);
        xhibitCourtSiteDto.setId(6L);
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(xhibitCourtSiteDto);
        final List<CourtDto> courtDtos = getCourtDtoList();

        expect(mockCourtService.getXhbCourtSiteDao(EasyMock.isA(Integer.class)))
            .andReturn(Optional.of(xhbCourtSiteDao));
        expect(mockCourtService.getCourtSites(EasyMock.isA(Integer.class))).andReturn(xhibitCourtSiteDtos);
        mockCourtPageStateHolder.setSites(xhibitCourtSiteDtos);
        expectLastCall();
        expect(mockCourtService.getCourts()).andReturn(courtDtos);
        mockCourtPageStateHolder.setCourts(courtDtos);
        expectLastCall();
        expect(mockCourtService.getXhibitCourtSite(EasyMock.isA(Integer.class))).andReturn(xhibitCourtSiteDto);
        
        replay(mockCourtService);
        replay(mockCourtPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameAmendCourtUrl + "/6")).andReturn();
        String response = results.getResponse().getContentAsString();
        XhibitCourtSiteDto returnedCourtSiteDto =
            new ObjectMapper().readValue(response, XhibitCourtSiteDto.class);

        assertEquals(4, returnedCourtSiteDto.getCourtId(), NOT_EQUAL);
        assertEquals(6, returnedCourtSiteDto.getId(), NOT_EQUAL);
        verify(mockCourtService);
        verify(mockCourtPageStateHolder);
    }
}
