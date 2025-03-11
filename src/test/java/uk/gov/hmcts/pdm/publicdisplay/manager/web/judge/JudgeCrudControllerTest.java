package uk.gov.hmcts.pdm.publicdisplay.manager.web.judge;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.easymock.IAnswer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataAccessException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefJudgeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
class JudgeCrudControllerTest extends JudgeShowControllerTest {

    @Test
    void updateJudgeTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<JudgeAmendCommand> judgeAmendCommandCapture = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgeAmendValidator.validate(capture(judgeAmendCommandCapture), capture(capturedErrors));
        expectLastCall();
        mockRefJudgeService.updateJudge(capture(judgeAmendCommandCapture));
        expectLastCall();
        mockJudgePageStateHolder.reset();
        expectLastCall();
        expect(mockRefJudgeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        replay(mockRefJudgeService);
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgePageStateHolder);
        replay(mockJudgeAmendValidator);

        // Perform the test
        final MvcResult results =
                mockMvc.perform(post(mappingNameAmendJudgeUrl)
                        .param(REF_JUDGE_ID, ONE)
                        .param(SURNAME, A_SURNAME)
                        .param(FIRST_NAME, A_FIRST_NAME)
                        .param(MIDDLE_NAME, A_MIDDLE_NAME)
                        .param(TITLE, A_TITLE)
                        .param(FULL_LIST_TITLE_1, A_FULL_LIST_TITLE_1)
                        .param(JUDGE_TYPE, A_JUDGE_TYPE)
                        .param(BTN_UPDATE_CONFIRM, ADD))
                       .andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertInstanceOf(JudgeSearchCommand.class,
                results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(viewNameViewJudge, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);
        assertEquals("Judge has been updated successfully.",
                results.getModelAndView().getModel().get("successMessage"), NOT_EQUAL);
        assertEquals(1, judgeAmendCommandCapture.getValue().getRefJudgeId(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        verify(mockRefJudgeService);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeAmendValidator);
    }

    @Test
    void updateJudgeErrorTest() throws Exception {
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<JudgeAmendCommand> judgeAmendCommandCapture = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();
        final List<RefSystemCodeDto> refSystemCodeDtos = createRefSystemCodeDto();

        mockJudgeAmendValidator.validate(capture(judgeAmendCommandCapture), capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockJudgePageStateHolder.getJudges()).andReturn(refJudgeDtos);
        expect(mockJudgePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtos);
        replay(mockJudgePageStateHolder);
        replay(mockJudgeAmendValidator);

        // Perform the test
        final MvcResult results =
                mockMvc.perform(post(mappingNameAmendJudgeUrl)
                               .param(REF_JUDGE_ID, ONE)
                               .param(SURNAME, A_SURNAME)
                               .param(FIRST_NAME, A_FIRST_NAME)
                               .param(MIDDLE_NAME, A_MIDDLE_NAME)
                               .param(TITLE, A_TITLE)
                               .param(FULL_LIST_TITLE_1, A_FULL_LIST_TITLE_1)
                               .param(JUDGE_TYPE, A_JUDGE_TYPE)
                               .param(BTN_UPDATE_CONFIRM, ADD))
                       .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertNotNull(results, NULL);
        assertEquals(viewNameAmendJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(refSystemCodeDtos, model.get(JUDGE_TYPE_LIST), NOT_EQUAL);
        assertEquals(refJudgeDtos, model.get(JUDGE_LIST), NOT_EQUAL);
        assertEquals(A_FIRST_NAME, judgeAmendCommandCapture.getValue().getFirstName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeAmendValidator);
    }

    @Test
    void updateJudgeExceptionTest() throws Exception {
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<JudgeAmendCommand> judgeAmendCommandCapture = newCapture();

        mockJudgeAmendValidator.validate(capture(judgeAmendCommandCapture), capture(capturedErrors));
        expectLastCall();
        mockRefJudgeService.updateJudge(capture(judgeAmendCommandCapture));
        expectLastCall().andThrow(new DataAccessException("Update Judge Exception") {});
        replay(mockRefJudgeService);
        replay(mockJudgeAmendValidator);

        // Perform the test
        final MvcResult results =
                mockMvc.perform(post(mappingNameAmendJudgeUrl)
                               .param(REF_JUDGE_ID, ONE)
                               .param(SURNAME, A_SURNAME)
                               .param(FIRST_NAME, A_FIRST_NAME)
                               .param(MIDDLE_NAME, A_MIDDLE_NAME)
                               .param(TITLE, A_TITLE)
                               .param(FULL_LIST_TITLE_1, A_FULL_LIST_TITLE_1)
                               .param(JUDGE_TYPE, A_JUDGE_TYPE)
                               .param(BTN_UPDATE_CONFIRM, ADD))
                       .andReturn();

        assertNotNull(results, NULL);
        assertInstanceOf(JudgeAmendCommand.class,
                results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(A_FULL_LIST_TITLE_1, judgeAmendCommandCapture.getValue().getFullListTitle1(), NOT_EQUAL);
        assertEquals("Unable to update Judge: Update Judge Exception",
                capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        verify(mockRefJudgeService);
        verify(mockJudgeAmendValidator);
    }

    @Test
    void createJudgeTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<JudgeCreateCommand> judgeCreateCommandCapture = newCapture();
        final Capture<List<RefJudgeDto>> refJudgeDtoCapture = newCapture();
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        expect(mockJudgePageStateHolder.getJudges()).andReturn(refJudgeDtos);
        mockJudgeCreateValidator.validate(capture(judgeCreateCommandCapture),
                capture(capturedErrors), capture(refJudgeDtoCapture));
        expectLastCall();
        expect(mockJudgePageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0)).anyTimes();
        mockRefJudgeService.createJudge(capture(judgeCreateCommandCapture), eq(10));
        expectLastCall();
        expect(mockJudgePageStateHolder.getJudgeSearchCommand()).andReturn(null);
        mockJudgePageStateHolder.reset();
        expectLastCall();
        expect(mockRefJudgeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        replay(mockRefJudgeService);
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgePageStateHolder);
        replay(mockJudgeCreateValidator);

        // Perform the test
        final MvcResult results =
                mockMvc.perform(post(mappingNameCreateJudgeUrl)
                               .param(REF_JUDGE_ID, ONE)
                               .param(SURNAME, A_SURNAME)
                               .param(FIRST_NAME, A_FIRST_NAME)
                               .param(MIDDLE_NAME, A_MIDDLE_NAME)
                               .param(TITLE, A_TITLE)
                               .param(FULL_LIST_TITLE_1, A_FULL_LIST_TITLE_1)
                               .param(JUDGE_TYPE, A_JUDGE_TYPE)
                               .param("btnCreateConfirm", ADD))
                       .andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertInstanceOf(JudgeSearchCommand.class,
                results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(viewNameViewJudge, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);
        assertEquals("Judge has been created successfully.",
                results.getModelAndView().getModel().get("successMessage"), NOT_EQUAL);
        assertEquals(A_JUDGE_TYPE, judgeCreateCommandCapture.getValue().getJudgeType(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(2, refJudgeDtoCapture.getValue().get(0).getCourtId(), NOT_EQUAL);
        verify(mockRefJudgeService);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeCreateValidator);
    }

    @Test
    void createJudgeErrorTest() throws Exception {
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<JudgeCreateCommand> judgeCreateCommandCapture = newCapture();
        final Capture<List<RefJudgeDto>> refJudgeDtoCapture = newCapture();
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<RefSystemCodeDto> refSystemCodeDtos = createRefSystemCodeDto();

        expect(mockJudgePageStateHolder.getJudges()).andReturn(refJudgeDtos);
        mockJudgeCreateValidator.validate(capture(judgeCreateCommandCapture),
                capture(capturedErrors), capture(refJudgeDtoCapture));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtos);
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgePageStateHolder);
        replay(mockJudgeCreateValidator);

        // Perform the test
        final MvcResult results =
                mockMvc.perform(post(mappingNameCreateJudgeUrl)
                               .param(REF_JUDGE_ID, ONE)
                               .param(SURNAME, A_SURNAME)
                               .param(FIRST_NAME, A_FIRST_NAME)
                               .param(MIDDLE_NAME, A_MIDDLE_NAME)
                               .param(TITLE, A_TITLE)
                               .param(FULL_LIST_TITLE_1, A_FULL_LIST_TITLE_1)
                               .param(JUDGE_TYPE, A_JUDGE_TYPE)
                               .param("btnCreateConfirm", ADD))
                       .andReturn();

        assertNotNull(results, NULL);
        assertInstanceOf(JudgeCreateCommand.class,
                results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(A_JUDGE_TYPE, judgeCreateCommandCapture.getValue().getJudgeType(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(2, refJudgeDtoCapture.getValue().get(0).getCourtId(), NOT_EQUAL);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeCreateValidator);
    }

    @Test
    void createJudgeExceptionTest() throws Exception {
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<JudgeCreateCommand> judgeCreateCommandCapture = newCapture();
        final Capture<List<RefJudgeDto>> refJudgeDtoCapture = newCapture();

        expect(mockJudgePageStateHolder.getJudges()).andReturn(refJudgeDtos);
        mockJudgeCreateValidator.validate(capture(judgeCreateCommandCapture),
                capture(capturedErrors), capture(refJudgeDtoCapture));
        expectLastCall();
        expect(mockJudgePageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0)).anyTimes();
        mockRefJudgeService.createJudge(capture(judgeCreateCommandCapture), eq(10));
        expectLastCall().andThrow(new DataAccessException("Create Judge Exception") {});
        replay(mockJudgePageStateHolder);
        replay(mockJudgeCreateValidator);
        replay(mockRefJudgeService);

        // Perform the test
        final MvcResult results =
                mockMvc.perform(post(mappingNameCreateJudgeUrl)
                               .param(REF_JUDGE_ID, ONE)
                               .param(SURNAME, A_SURNAME)
                               .param(FIRST_NAME, A_FIRST_NAME)
                               .param(MIDDLE_NAME, A_MIDDLE_NAME)
                               .param(TITLE, A_TITLE)
                               .param(FULL_LIST_TITLE_1, A_FULL_LIST_TITLE_1)
                               .param(JUDGE_TYPE, A_JUDGE_TYPE)
                               .param("btnCreateConfirm", ADD))
                       .andReturn();

        assertNotNull(results, NULL);
        assertInstanceOf(JudgeCreateCommand.class,
                results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(A_JUDGE_TYPE, judgeCreateCommandCapture.getValue().getJudgeType(), NOT_EQUAL);
        assertEquals("Unable to create Judge: Create Judge Exception",
                capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertEquals(2, refJudgeDtoCapture.getValue().get(0).getCourtId(), NOT_EQUAL);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeCreateValidator);
        verify(mockRefJudgeService);
    }

    @Test
    void deleteJudgeTest() throws Exception {
        final Capture<JudgeDeleteCommand> judgeDeleteCommandCapture = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgeDeleteValidator.validate(capture(judgeDeleteCommandCapture), capture(capturedErrors));
        expectLastCall();
        mockRefJudgeService.updateJudge(capture(judgeDeleteCommandCapture));
        expectLastCall();
        mockJudgePageStateHolder.reset();
        expectLastCall();
        expect(mockJudgePageStateHolder.getJudgeSearchCommand()).andReturn(null);
        expect(mockRefJudgeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgePageStateHolder.setSites(capture(capturedCourtSites));
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeDeleteValidator);
        replay(mockRefJudgeService);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results =
                mockMvc.perform(post(mappingNameDeleteJudgeUrl)
                               .param(REF_JUDGE_ID, ONE)
                               .param(SURNAME, A_SURNAME)
                               .param(FIRST_NAME, A_FIRST_NAME)
                               .param(MIDDLE_NAME, A_MIDDLE_NAME)
                               .param(TITLE, A_TITLE)
                               .param(FULL_LIST_TITLE_1, A_FULL_LIST_TITLE_1)
                               .param(JUDGE_TYPE, A_JUDGE_TYPE)
                               .param("btnDeleteConfirm", ADD))
                       .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeSearchCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(A_FIRST_NAME, judgeDeleteCommandCapture.getValue().getFirstName(), NOT_EQUAL);
        assertEquals(viewNameViewJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockRefJudgeService);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeDeleteValidator);
    }

    @Test
    void deleteJudgeErrorTest() throws Exception {
        final Capture<JudgeDeleteCommand> judgeDeleteCommandCapture = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();

        mockJudgeDeleteValidator.validate(capture(judgeDeleteCommandCapture), capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgePageStateHolder.getJudges()).andReturn(refJudgeDtos);
        replay(mockJudgeDeleteValidator);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results =
                mockMvc.perform(post(mappingNameDeleteJudgeUrl)
                               .param(REF_JUDGE_ID, ONE)
                               .param(SURNAME, A_SURNAME)
                               .param(FIRST_NAME, A_FIRST_NAME)
                               .param(MIDDLE_NAME, A_MIDDLE_NAME)
                               .param(TITLE, A_TITLE)
                               .param(FULL_LIST_TITLE_1, A_FULL_LIST_TITLE_1)
                               .param(JUDGE_TYPE, A_JUDGE_TYPE)
                               .param("btnDeleteConfirm", ADD))
                       .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeDeleteCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(A_FIRST_NAME, judgeDeleteCommandCapture.getValue().getFirstName(), NOT_EQUAL);
        assertEquals(viewNameDeleteJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeDeleteValidator);
    }

    @Test
    void deleteJudgeExceptionTest() throws Exception {
        final Capture<JudgeDeleteCommand> judgeDeleteCommandCapture = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        mockJudgeDeleteValidator.validate(capture(judgeDeleteCommandCapture), capture(capturedErrors));
        expectLastCall();
        mockRefJudgeService.updateJudge(capture(judgeDeleteCommandCapture));
        expectLastCall().andThrow(new DataAccessException("Delete Judge Exception") {});
        replay(mockJudgeDeleteValidator);
        replay(mockRefJudgeService);

        // Perform the test
        final MvcResult results =
                mockMvc.perform(post(mappingNameDeleteJudgeUrl)
                               .param(REF_JUDGE_ID, ONE)
                               .param(SURNAME, A_SURNAME)
                               .param(FIRST_NAME, A_FIRST_NAME)
                               .param(MIDDLE_NAME, A_MIDDLE_NAME)
                               .param(TITLE, A_TITLE)
                               .param(FULL_LIST_TITLE_1, A_FULL_LIST_TITLE_1)
                               .param(JUDGE_TYPE, A_JUDGE_TYPE)
                               .param("btnDeleteConfirm", ADD))
                       .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeDeleteCommand.class,
                model.get(COMMAND), NOT_AN_INSTANCE);
        assertTrue(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals("Unable to delete Judge: Delete Judge Exception",
                capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertEquals(A_FIRST_NAME, judgeDeleteCommandCapture.getValue().getFirstName(), NOT_EQUAL);
        assertEquals(viewNameDeleteJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockRefJudgeService);
        verify(mockJudgeDeleteValidator);
    }
}
