package uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
class JudgeTypeControllerTest extends JudgeTypeErrorControllerTest {

    @Test
    void viewJudgeTypeTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgeTypePageStateHolder.reset();
        expectLastCall();
        expect(mockRefJudgeTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgeTypePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        replay(mockRefJudgeTypeService);
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(get(mappingNameViewJudgeTypeUrl).param(XHIBIT_COURTSITE_ID, "1")).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewJudgeType, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);

        verify(mockRefJudgeTypeService);
        verify(mockJudgeTypePageStateHolder);
    }

    @Test
    void viewJudgeTypeResetFalseTest() throws Exception {
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        JudgeTypeSearchCommand judgeTypeSearchCommand = new JudgeTypeSearchCommand();

        expect(mockJudgeTypePageStateHolder.getJudgeTypeSearchCommand())
            .andReturn(judgeTypeSearchCommand).times(2);
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameViewJudgeTypeUrl)
            .param(XHIBIT_COURTSITE_ID, "1").param("reset", "false")).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewJudgeType, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        verify(mockJudgeTypePageStateHolder);
    }

    @Test
    void viewJudgeTypeNullTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        expect(mockJudgeTypePageStateHolder.getJudgeTypeSearchCommand()).andReturn(null);
        mockJudgeTypePageStateHolder.reset();
        expectLastCall();
        expect(mockRefJudgeTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgeTypePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        replay(mockRefJudgeTypeService);
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameViewJudgeTypeUrl)
            .param(XHIBIT_COURTSITE_ID, "1").param("reset", "false")).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewJudgeType, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);

        verify(mockRefJudgeTypeService);
        verify(mockJudgeTypePageStateHolder);
    }

    @Test
    void showAmendJudgeTypeTest() throws Exception {
        final Capture<JudgeTypeSearchCommand> capturedJudgeTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<RefSystemCodeDto>> capturedRefSysCodeDtos = newCapture();
        final List<RefSystemCodeDto> refSystemCodeDtoList = List.of(new RefSystemCodeDto());
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();

        mockJudgeTypePageStateHolder
            .setJudgeTypeSearchCommand(capture(capturedJudgeTypeSearchCommand));
        mockJudgeTypeSelectedValidator.validate(capture(capturedJudgeTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockRefJudgeTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgeTypePageStateHolder.setSites(xhibitCourtSiteDtos);
        expectLastCall();
        expect(mockRefJudgeTypeService.getJudgeTypes(eq(8L))).andReturn(refSystemCodeDtoList);
        mockJudgeTypePageStateHolder.setJudgeTypes(capture(capturedRefSysCodeDtos));
        expectLastCall();
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockJudgeTypePageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();
        expect(mockJudgeTypePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtoList).anyTimes();
        replay(mockJudgeTypeSelectedValidator);
        replay(mockRefJudgeTypeService);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameViewJudgeTypeUrl)
            .param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeTypeAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(refSystemCodeDtoList, model.get(JUDGE_TYPE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get("courtSite"), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(refSystemCodeDtoList, capturedRefSysCodeDtos.getValue(), NOT_EQUAL);
        assertEquals(8, capturedJudgeTypeSearchCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(viewNameAmendJudgeType, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockRefJudgeTypeService);
        verify(mockJudgeTypePageStateHolder);
        verify(mockJudgeTypeSelectedValidator);
    }

    @Test
    void updateJudgeTypeTest() throws Exception {
        final Capture<JudgeTypeAmendCommand> capturedJudgeTypeAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();

        mockJudgeTypeAmendValidator.validate(capture(capturedJudgeTypeAmendCommand),
            capture(capturedErrors));
        expectLastCall();
        mockRefJudgeTypeService.updateJudgeType(capture(capturedJudgeTypeAmendCommand));
        expectLastCall();
        mockJudgeTypePageStateHolder.reset();
        expectLastCall();
        expect(mockRefJudgeTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgeTypePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        replay(mockRefJudgeTypeService);
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeTypeAmendValidator);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendJudgeTypeUrl)
            .param(XHIBIT_COURTSITE_ID, "8").param("btnUpdateConfirm", ADD)
            .param("refSystemCodeId", "2").param(DESCRIPTION, JUDGE_TYPE_DESCRIPTION)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeTypeSearchCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(JUDGE_TYPE_DESCRIPTION,
            capturedJudgeTypeAmendCommand.getValue().getDescription(), NOT_EQUAL);
        assertEquals(viewNameViewJudgeType, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockRefJudgeTypeService);
        verify(mockJudgeTypePageStateHolder);
        verify(mockJudgeTypeAmendValidator);
    }

    @Test
    void showCreateJudgeTypeTest() throws Exception {
        final Capture<JudgeTypeSearchCommand> capturedJudgeTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<RefSystemCodeDto>> capturedRefSysCodeDtos = newCapture();
        final List<RefSystemCodeDto> refSystemCodeDtoList = List.of(new RefSystemCodeDto());
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();

        mockJudgeTypePageStateHolder
            .setJudgeTypeSearchCommand(capture(capturedJudgeTypeSearchCommand));
        mockJudgeTypeSelectedValidator.validate(capture(capturedJudgeTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockRefJudgeTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgeTypePageStateHolder.setSites(xhibitCourtSiteDtos);
        expectLastCall();
        expect(mockRefJudgeTypeService.getJudgeTypes(eq(8L))).andReturn(refSystemCodeDtoList);
        mockJudgeTypePageStateHolder.setJudgeTypes(capture(capturedRefSysCodeDtos));
        expectLastCall();
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockJudgeTypePageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();
        expect(mockJudgeTypePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtoList).anyTimes();
        replay(mockJudgeTypeSelectedValidator);
        replay(mockRefJudgeTypeService);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewJudgeTypeUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAdd", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeTypeCreateCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(refSystemCodeDtoList, model.get(JUDGE_TYPE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get("courtSite"), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(refSystemCodeDtoList, capturedRefSysCodeDtos.getValue(), NOT_EQUAL);
        assertEquals(8, capturedJudgeTypeSearchCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(viewNameCreateJudgeType, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(8, capturedCourtSite.getValue().getId(), NOT_EQUAL);
        verify(mockRefJudgeTypeService);
        verify(mockJudgeTypePageStateHolder);
        verify(mockJudgeTypeSelectedValidator);
    }

    @Test
    void createJudgeTypeTest() throws Exception {
        final Capture<JudgeTypeCreateCommand> capturedJudgeTypeCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<RefSystemCodeDto>> capturedRefSysCodeDtos = newCapture();
        final List<RefSystemCodeDto> refSystemCodeDtoList = List.of(new RefSystemCodeDto());
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();

        expect(mockJudgeTypePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtoList);
        mockJudgeTypeCreateValidator.validate(capture(capturedJudgeTypeCreateCommand),
            capture(capturedErrors), capture(capturedRefSysCodeDtos));
        expectLastCall();
        mockRefJudgeTypeService.createJudgeType(capture(capturedJudgeTypeCreateCommand), eq(0));
        expectLastCall();
        mockJudgeTypePageStateHolder.reset();
        expectLastCall();
        expect(mockRefJudgeTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgeTypePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeTypeCreateValidator);
        replay(mockRefJudgeTypeService);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCreateJudgeTypeUrl).param("code", CREATE_COMMAND_CODE)
                .param(DESCRIPTION, "CreateCommandDescription").param("btnCreateConfirm", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeTypeSearchCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals("Judge Type has been created successfully.", model.get("successMessage"),
            NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(refSystemCodeDtoList, capturedRefSysCodeDtos.getValue(), NOT_EQUAL);
        assertEquals(CREATE_COMMAND_CODE, capturedJudgeTypeCreateCommand.getValue().getCode(),
            NOT_EQUAL);
        assertEquals(viewNameViewJudgeType, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, capturedCourtSites.getValue(), NOT_EQUAL);
        verify(mockRefJudgeTypeService);
        verify(mockJudgeTypePageStateHolder);
        verify(mockJudgeTypeCreateValidator);
    }

    @Test
    void loadJudgeTypeTest() throws Exception {
        RefSystemCodeDto refSystemCodeDto = new RefSystemCodeDto();
        refSystemCodeDto.setRefSystemCodeId(7);
        refSystemCodeDto.setCodeType("A Code Type");
        refSystemCodeDto.setCourtId(1);
        final List<RefSystemCodeDto> refSystemCodeDtoList = List.of(refSystemCodeDto);
        final List<XhibitCourtSiteDto> courtSites = new ArrayList<>();
        
        expect(mockRefJudgeTypeService.getCourtSites()).andReturn(courtSites);
        mockJudgeTypePageStateHolder.setSites(courtSites);
        expectLastCall();
        expect(mockRefJudgeTypeService.getJudgeType(EasyMock.isA(Integer.class)))
            .andReturn(refSystemCodeDto);
        expect(mockRefJudgeTypeService.getJudgeTypesByCourtId(refSystemCodeDto.getCourtId()))
            .andReturn(refSystemCodeDtoList);
        mockJudgeTypePageStateHolder.setJudgeTypes(refSystemCodeDtoList);
        expectLastCall();
        
        replay(mockRefJudgeTypeService);
        replay(mockJudgeTypePageStateHolder);
        
        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameAmendJudgeTypeUrl + "/7")).andReturn();
        String response = results.getResponse().getContentAsString();
        RefSystemCodeDto returnedRefSysCodeDto =
            new ObjectMapper().readValue(response, RefSystemCodeDto.class);

        assertEquals(7, returnedRefSysCodeDto.getRefSystemCodeId(), NOT_EQUAL);
        assertEquals("A Code Type", returnedRefSysCodeDto.getCodeType(), NOT_EQUAL);
        assertEquals(1, returnedRefSysCodeDto.getCourtId(), NOT_EQUAL);
        verify(mockRefJudgeTypeService);
        verify(mockJudgeTypePageStateHolder);
    }
}
