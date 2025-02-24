package uk.gov.hmcts.pdm.publicdisplay.manager.web.judge;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefJudgeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRefJudgeService;

import java.util.List;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
abstract class JudgeControllerBaseTest extends AbstractJUnit {
    protected JudgeSelectedValidator mockJudgeSelectedValidator;
    protected JudgeCreateValidator mockJudgeCreateValidator;
    protected JudgeAmendValidator mockJudgeAmendValidator;
    protected JudgeDeleteValidator mockJudgeDeleteValidator;
    protected JudgePageStateHolder mockJudgePageStateHolder;
    protected IRefJudgeService mockRefJudgeService;
    protected String viewNameViewJudge;
    protected String viewNameCreateJudge;
    protected String viewNameAmendJudge;
    protected String viewNameDeleteJudge;
    protected String mappingNameViewJudgeUrl;
    protected String mappingNameCreateJudgeUrl;
    protected String mappingNameAmendJudgeUrl;
    protected String mappingNameDeleteJudgeUrl;
    protected MockMvc mockMvc;
    protected static final String NOT_NULL = "Not null";
    protected static final String NULL = "Null";
    protected static final String NOT_EQUAL = "Not equal";
    protected static final String FALSE = "false";
    protected static final String ADD = "add";
    protected static final String NOT_FALSE = "Not false";
    protected static final String COMMAND = "command";
    protected static final String NOT_AN_INSTANCE = "Not an instance";
    protected static final String MOCK_ERROR_MESSAGE = "mock error message";
    protected static final String COURTSITE_LIST = "courtSiteList";
    protected static final String XHIBIT_COURTSITE_ID = "xhibitCourtSiteId";
    protected static final String JUDGE_TYPE_LIST = "judgeTypeList";
    protected static final String JUDGE_LIST = "judgeList";
    protected static final String COURTSITE = "courtSite";
    protected static final String REQUEST_MAPPING = "/judge";
    protected static final String ONE = "1";
    protected static final String REF_JUDGE_ID = "refJudgeId";
    protected static final String SURNAME = "surname";
    protected static final String FIRST_NAME = "firstName";
    protected static final String MIDDLE_NAME = "middleName";
    protected static final String TITLE = "title";
    protected static final String FULL_LIST_TITLE_1 = "fullListTitle1";
    protected static final String JUDGE_TYPE = "judgeType";
    protected static final String BTN_UPDATE_CONFIRM = "btnUpdateConfirm";
    protected static final String A_SURNAME = "A surname";
    protected static final String A_FIRST_NAME = "A firstName";
    protected static final String A_MIDDLE_NAME = "A middleName";
    protected static final String A_TITLE = "A title";
    protected static final String A_FULL_LIST_TITLE_1 = "A fullListTitle1";
    protected static final String A_JUDGE_TYPE = "A judgeType";

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        JudgeController classUnderTest = new JudgeController();
        // Setup the mock version of the called classes
        mockObjects();
        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "judgeSelectedValidator",
            mockJudgeSelectedValidator);
        ReflectionTestUtils.setField(classUnderTest, "judgeCreateValidator",
            mockJudgeCreateValidator);
        ReflectionTestUtils.setField(classUnderTest, "judgeAmendValidator",
            mockJudgeAmendValidator);
        ReflectionTestUtils.setField(classUnderTest, "judgeDeleteValidator",
            mockJudgeDeleteValidator);
        ReflectionTestUtils.setField(classUnderTest, "judgePageStateHolder",
            mockJudgePageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "refJudgeService", mockRefJudgeService);

        // Get the static variables from the class under test
        viewNameViewJudge =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_VIEW_JUDGE");
        mappingNameViewJudgeUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_VIEW_JUDGE");
        viewNameCreateJudge =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_CREATE_JUDGE");
        mappingNameCreateJudgeUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_CREATE_JUDGE");
        viewNameAmendJudge =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_AMEND_JUDGE");
        mappingNameAmendJudgeUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_AMEND_JUDGE");
        viewNameDeleteJudge =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_DELETE_JUDGE");
        mappingNameDeleteJudgeUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_DELETE_JUDGE");

        // Stop circular view path error
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view");
        viewResolver.setSuffix(".jsp");

        // Setup the mock version of the modelMvc
        mockMvc =
            MockMvcBuilders.standaloneSetup(classUnderTest).setViewResolvers(viewResolver).build();
    }

    protected void mockObjects() {
        mockJudgeSelectedValidator = createMock(JudgeSelectedValidator.class);
        mockJudgeCreateValidator = createMock(JudgeCreateValidator.class);
        mockJudgeAmendValidator = createMock(JudgeAmendValidator.class);
        mockJudgeDeleteValidator = createMock(JudgeDeleteValidator.class);
        mockJudgePageStateHolder = createMock(JudgePageStateHolder.class);
        mockRefJudgeService = createMock(IRefJudgeService.class);
    }

    protected List<XhibitCourtSiteDto> createCourtSiteDtoList() {
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        xhibitCourtSiteDto.setId(8L);
        xhibitCourtSiteDto.setCourtId(10);
        return List.of(xhibitCourtSiteDto);
    }

    protected List<RefSystemCodeDto> createRefSystemCodeDto() {
        RefSystemCodeDto refSystemCodeDto = new RefSystemCodeDto();
        refSystemCodeDto.setCode("A Code");
        refSystemCodeDto.setDeCode("A DeCode");
        return List.of(refSystemCodeDto);
    }

    protected List<RefJudgeDto> createRefJudgeDto() {
        RefJudgeDto refJudgeDto = new RefJudgeDto();
        refJudgeDto.setJudgeType("A JudgeType");
        refJudgeDto.setRefJudgeId(9);
        refJudgeDto.setCourtId(2);
        return List.of(refJudgeDto);
    }

    @Test
    void loadJudgeTest() throws Exception {
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();
        final List<RefSystemCodeDto> refSystemCodeDtos = createRefSystemCodeDto();

        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        expect(mockJudgePageStateHolder.getJudges()).andReturn(refJudgeDtos).anyTimes();
        expect(mockJudgePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtos).anyTimes();
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameAmendJudgeUrl + "/9")).andReturn();
        String response = results.getResponse().getContentAsString();
        RefJudgeDto returnedRefJudgeDto = new ObjectMapper().readValue(response, RefJudgeDto.class);

        assertEquals(refJudgeDtos.get(0).getJudgeTypeDeCode(),
            returnedRefJudgeDto.getJudgeTypeDeCode(), NOT_EQUAL);
        verify(mockJudgePageStateHolder);
    }

    @Test
    void loadJudgeErrorTest() throws Exception {
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<RefJudgeDto> refJudgeDtos = createRefJudgeDto();
        final List<RefSystemCodeDto> refSystemCodeDtos = createRefSystemCodeDto();

        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        expect(mockJudgePageStateHolder.getJudges()).andReturn(refJudgeDtos).anyTimes();
        expect(mockJudgePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtos).anyTimes();
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameAmendJudgeUrl + "/9")).andReturn();
        String response = results.getResponse().getContentAsString();
        RefJudgeDto returnedRefJudgeDto = new ObjectMapper().readValue(response, RefJudgeDto.class);

        assertNull(returnedRefJudgeDto.getJudgeTypeDeCode(), NOT_NULL);
        verify(mockJudgePageStateHolder);
    }

    @Test
    void viewJudgeTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgePageStateHolder.reset();
        expectLastCall();
        expect(mockRefJudgeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        replay(mockRefJudgeService);
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(get(mappingNameViewJudgeUrl).param(XHIBIT_COURTSITE_ID, "1")).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewJudge, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);

        verify(mockRefJudgeService);
        verify(mockJudgePageStateHolder);
    }

    @Test
    void viewJudgeResetFalseTest() throws Exception {
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        JudgeSearchCommand judgeSearchCommand = new JudgeSearchCommand();

        expect(mockJudgePageStateHolder.getJudgeSearchCommand()).andReturn(judgeSearchCommand)
            .times(2);
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            get(mappingNameViewJudgeUrl).param(XHIBIT_COURTSITE_ID, "1").param("reset", "false"))
            .andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewJudge, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        verify(mockJudgePageStateHolder);
    }

    @Test
    void viewJudgeNullTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        expect(mockJudgePageStateHolder.getJudgeSearchCommand()).andReturn(null);
        mockJudgePageStateHolder.reset();
        expectLastCall();
        expect(mockRefJudgeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockJudgePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        replay(mockRefJudgeService);
        expect(mockJudgePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            get(mappingNameViewJudgeUrl).param(XHIBIT_COURTSITE_ID, "1").param("reset", "false"))
            .andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewJudge, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);

        verify(mockRefJudgeService);
        verify(mockJudgePageStateHolder);
    }
}
