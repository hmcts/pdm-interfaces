package uk.gov.hmcts.pdm.publicdisplay.manager.web.judge;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.easymock.IAnswer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
abstract class JudgeShowControllerTest extends JudgeControllerBaseTest {
    @Test
    void showAmendJudgeTest() throws Exception {
        final Capture<JudgeSearchCommand> capturedJudgeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<RefSystemCodeDto>> capturedRefSysCodeDto = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final Capture<List<RefJudgeDto>> capturedRefJudgeDtos = newCapture();
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();
        final List<RefSystemCodeDto> refSystemCodeDtos = createRefSystemCodeDto();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgePageStateHolder.setJudgeSearchCommand(capture(capturedJudgeSearchCommand));
        expectLastCall();
        mockJudgeSelectedValidator.validate(capture(capturedJudgeSearchCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockRefJudgeService.getJudges(eq(8L))).andReturn(refJudgeDtos);
        expect(mockRefJudgeService.getJudgeTypes(eq(8L))).andReturn(refSystemCodeDtos);

        mockJudgePageStateHolder.setJudges(capture(capturedRefJudgeDtos));
        expectLastCall();
        mockJudgePageStateHolder.setJudgeTypes(capture(capturedRefSysCodeDto));
        expectLastCall();
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).times(3);
        mockJudgePageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();
        expect(mockJudgePageStateHolder.getJudges()).andReturn(refJudgeDtos);
        expect(mockJudgePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtos);
        replay(mockJudgeSelectedValidator);
        replay(mockRefJudgeService);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewJudgeUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(refSystemCodeDtos, model.get(JUDGE_TYPE_LIST), NOT_EQUAL);
        assertEquals(refJudgeDtos, model.get(JUDGE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get(COURTSITE), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(refJudgeDtos, capturedRefJudgeDtos.getValue(), NOT_EQUAL);
        assertEquals(8, capturedJudgeSearchCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(viewNameAmendJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockRefJudgeService);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeSelectedValidator);
    }

    @Test
    void showAmendJudgeErrorTest() throws Exception {
        final Capture<JudgeSearchCommand> capturedJudgeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgePageStateHolder.setJudgeSearchCommand(capture(capturedJudgeSearchCommand));
        expectLastCall();
        mockJudgeSelectedValidator.validate(capture(capturedJudgeSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeSelectedValidator);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewJudgeUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedJudgeSearchCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(viewNameViewJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeSelectedValidator);
    }

    @Test
    void showCreateJudgeTest() throws Exception {
        final Capture<JudgeSearchCommand> capturedJudgeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<RefSystemCodeDto>> capturedRefSysCodeDto = newCapture();
        final Capture<List<RefJudgeDto>> capturedRefJudgeDtos = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();
        final List<RefSystemCodeDto> refSystemCodeDtos = createRefSystemCodeDto();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgePageStateHolder.setJudgeSearchCommand(capture(capturedJudgeSearchCommand));
        expectLastCall();
        mockJudgeSelectedValidator.validate(capture(capturedJudgeSearchCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockRefJudgeService.getJudges(eq(8L))).andReturn(refJudgeDtos);
        expect(mockRefJudgeService.getJudgeTypes(eq(8L))).andReturn(refSystemCodeDtos);

        mockJudgePageStateHolder.setJudges(capture(capturedRefJudgeDtos));
        expectLastCall();
        mockJudgePageStateHolder.setJudgeTypes(capture(capturedRefSysCodeDto));
        expectLastCall();
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).times(3);
        mockJudgePageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();
        expect(mockJudgePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtos);
        replay(mockJudgeSelectedValidator);
        replay(mockRefJudgeService);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(
                post(mappingNameViewJudgeUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAdd", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeCreateCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(refSystemCodeDtos, model.get(JUDGE_TYPE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get(COURTSITE), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(refJudgeDtos, capturedRefJudgeDtos.getValue(), NOT_EQUAL);
        assertEquals(8, capturedJudgeSearchCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(viewNameCreateJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockRefJudgeService);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeSelectedValidator);
    }

    @Test
    void showCreateJudgeErrorTest() throws Exception {
        final Capture<JudgeSearchCommand> capturedJudgeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<RefSystemCodeDto> refSystemCodeDtos = createRefSystemCodeDto();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgePageStateHolder.setJudgeSearchCommand(capture(capturedJudgeSearchCommand));
        expectLastCall();
        mockJudgeSelectedValidator.validate(capture(capturedJudgeSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);

        expect(mockJudgePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtos);
        replay(mockJudgeSelectedValidator);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(
                post(mappingNameViewJudgeUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAdd", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(refSystemCodeDtos, model.get(JUDGE_TYPE_LIST), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedJudgeSearchCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(viewNameViewJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeSelectedValidator);
    }

    @Test
    void showDeleteJudgeTest() throws Exception {
        final Capture<JudgeSearchCommand> capturedJudgeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<RefSystemCodeDto>> capturedRefSysCodeDto = newCapture();
        final Capture<List<RefJudgeDto>> capturedRefJudgeDtos = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();
        final List<RefSystemCodeDto> refSystemCodeDtos = createRefSystemCodeDto();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgePageStateHolder.setJudgeSearchCommand(capture(capturedJudgeSearchCommand));
        expectLastCall();
        mockJudgeSelectedValidator.validate(capture(capturedJudgeSearchCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockRefJudgeService.getJudges(eq(8L))).andReturn(refJudgeDtos);
        expect(mockRefJudgeService.getJudgeTypes(eq(8L))).andReturn(refSystemCodeDtos);

        mockJudgePageStateHolder.setJudges(capture(capturedRefJudgeDtos));
        expectLastCall();
        mockJudgePageStateHolder.setJudgeTypes(capture(capturedRefSysCodeDto));
        expectLastCall();
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).times(2);
        mockJudgePageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();
        expect(mockJudgePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtos);
        expect(mockJudgePageStateHolder.getJudges()).andReturn(refJudgeDtos);
        replay(mockJudgeSelectedValidator);
        replay(mockRefJudgeService);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewJudgeUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnDelete", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeDeleteCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(refJudgeDtos, model.get(JUDGE_LIST), NOT_EQUAL);
        assertEquals(refSystemCodeDtos, model.get(JUDGE_TYPE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get(COURTSITE), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(refJudgeDtos, capturedRefJudgeDtos.getValue(), NOT_EQUAL);
        assertEquals(8, capturedJudgeSearchCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(viewNameDeleteJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(10, capturedCourtSite.getValue().getCourtId(), NOT_EQUAL);
        assertEquals("A Code", capturedRefSysCodeDto.getValue().get(0).getCode(), NOT_EQUAL);
        verify(mockRefJudgeService);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeSelectedValidator);
    }

    @Test
    void showDeleteJudgeErrorTest() throws Exception {
        final Capture<JudgeSearchCommand> capturedJudgeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgePageStateHolder.setJudgeSearchCommand(capture(capturedJudgeSearchCommand));
        expectLastCall();
        mockJudgeSelectedValidator.validate(capture(capturedJudgeSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeSelectedValidator);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewJudgeUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnDelete", ADD))
            .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedJudgeSearchCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(viewNameViewJudge, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        verify(mockJudgePageStateHolder);
        verify(mockJudgeSelectedValidator);
    }
}
