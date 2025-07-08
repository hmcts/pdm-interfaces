package uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.easymock.IAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataAccessException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRefJudgeTypeService;

import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class JudgeTypeErrorControllerTest extends AbstractJUnit {

    protected JudgeTypeSelectedValidator mockJudgeTypeSelectedValidator;
    protected JudgeTypeCreateValidator mockJudgeTypeCreateValidator;
    protected JudgeTypeAmendValidator mockJudgeTypeAmendValidator;
    protected JudgeTypePageStateHolder mockJudgeTypePageStateHolder;
    protected IRefJudgeTypeService mockRefJudgeTypeService;
    protected String viewNameViewJudgeType;
    protected String viewNameCreateJudgeType;
    protected String viewNameAmendJudgeType;
    protected String mappingNameViewJudgeTypeUrl;
    protected String mappingNameCreateJudgeTypeUrl;
    protected String mappingNameAmendJudgeTypeUrl;
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
    protected static final String JUDGE_TYPE_DESCRIPTION = "JudgeTypeDescription";
    protected static final String REQUEST_MAPPING = "/judgetype";
    protected static final String DESCRIPTION = "description";
    protected static final String CREATE_COMMAND_CODE = "CreateCommandCode";

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        JudgeTypeController classUnderTest = new JudgeTypeController();
        // Setup the mock version of the called classes
        mockObjects();
        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "judgeTypeSelectedValidator",
            mockJudgeTypeSelectedValidator);
        ReflectionTestUtils.setField(classUnderTest, "judgeTypeCreateValidator",
            mockJudgeTypeCreateValidator);
        ReflectionTestUtils.setField(classUnderTest, "judgeTypeAmendValidator",
            mockJudgeTypeAmendValidator);
        ReflectionTestUtils.setField(classUnderTest, "judgeTypePageStateHolder",
            mockJudgeTypePageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "refJudgeTypeService",
            mockRefJudgeTypeService);

        // Get the static variables from the class under test
        viewNameViewJudgeType =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_VIEW_JUDGE_TYPE");
        mappingNameViewJudgeTypeUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_VIEW_JUDGE_TYPE");
        viewNameCreateJudgeType =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_CREATE_JUDGE_TYPE");
        mappingNameCreateJudgeTypeUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_CREATE_JUDGE_TYPE");
        viewNameAmendJudgeType =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_AMEND_JUDGE_TYPE");
        mappingNameAmendJudgeTypeUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_AMEND_JUDGE_TYPE");

        // Stop circular view path error
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view");
        viewResolver.setSuffix(".jsp");

        // Setup the mock version of the modelMvc
        mockMvc =
            MockMvcBuilders.standaloneSetup(classUnderTest).setViewResolvers(viewResolver).build();
    }

    protected void mockObjects() {
        mockJudgeTypeSelectedValidator = createMock(JudgeTypeSelectedValidator.class);
        mockJudgeTypeCreateValidator = createMock(JudgeTypeCreateValidator.class);
        mockJudgeTypeAmendValidator = createMock(JudgeTypeAmendValidator.class);
        mockJudgeTypePageStateHolder = createMock(JudgeTypePageStateHolder.class);
        mockRefJudgeTypeService = createMock(IRefJudgeTypeService.class);
    }

    protected List<XhibitCourtSiteDto> createCourtSiteDtoList() {
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        xhibitCourtSiteDto.setId(8L);
        xhibitCourtSiteDto.setCourtId(10);
        return List.of(xhibitCourtSiteDto);
    }

    @Test
    void createJudgeTypeExceptionTest() throws Exception {
        final Capture<JudgeTypeCreateCommand> capturedJudgeTypeCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<RefSystemCodeDto>> capturedRefSysCodeDtos = newCapture();
        final List<RefSystemCodeDto> refSystemCodeDtoList = List.of(new RefSystemCodeDto());
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        expect(mockJudgeTypePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtoList)
            .times(2);
        mockJudgeTypeCreateValidator.validate(capture(capturedJudgeTypeCreateCommand),
            capture(capturedErrors), capture(capturedRefSysCodeDtos));
        expectLastCall();
        mockRefJudgeTypeService.createJudgeType(capture(capturedJudgeTypeCreateCommand), eq(0));
        expectLastCall().andThrow(new DataAccessException("Create Judge Type Access Exception") {});
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeTypeCreateValidator);
        replay(mockJudgeTypePageStateHolder);
        replay(mockRefJudgeTypeService);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCreateJudgeTypeUrl).param("code", CREATE_COMMAND_CODE)
                .param(DESCRIPTION, "CreateCommandDescription").param("btnCreateConfirm", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeTypeCreateCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals("Unable to create Judge Type: Create Judge Type Access Exception",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_FALSE);
        assertEquals(refSystemCodeDtoList, capturedRefSysCodeDtos.getValue(), NOT_EQUAL);
        assertEquals(CREATE_COMMAND_CODE, capturedJudgeTypeCreateCommand.getValue().getCode(),
            NOT_EQUAL);
        assertEquals(refSystemCodeDtoList, model.get(JUDGE_TYPE_LIST), NOT_EQUAL);
        verify(mockJudgeTypePageStateHolder);
        verify(mockJudgeTypeCreateValidator);
        verify(mockRefJudgeTypeService);
    }

    @Test
    void showAmendJudgeTypeErrorTest() throws Exception {
        final Capture<JudgeTypeSearchCommand> capturedJudgeTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgeTypePageStateHolder
            .setJudgeTypeSearchCommand(capture(capturedJudgeTypeSearchCommand));
        mockJudgeTypeSelectedValidator.validate(capture(capturedJudgeTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeTypeSelectedValidator);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameViewJudgeTypeUrl)
            .param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD)).andReturn();

        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedJudgeTypeSearchCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(viewNameViewJudgeType, results.getModelAndView().getViewName(),
            NOT_EQUAL);
        verify(mockJudgeTypePageStateHolder);
        verify(mockJudgeTypeSelectedValidator);
    }

    @Test
    void updateJudgeTypeErrorTest() throws Exception {
        final Capture<JudgeTypeAmendCommand> capturedJudgeTypeAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<RefSystemCodeDto> refSystemCodeDtoList = List.of(new RefSystemCodeDto());
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgeTypeAmendValidator.validate(capture(capturedJudgeTypeAmendCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockJudgeTypePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtoList);
        replay(mockJudgeTypeAmendValidator);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendJudgeTypeUrl)
            .param(XHIBIT_COURTSITE_ID, "8").param("btnUpdateConfirm", ADD)
            .param("refSystemCodeId", "2").param(DESCRIPTION, JUDGE_TYPE_DESCRIPTION)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeTypeAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(JUDGE_TYPE_DESCRIPTION,
            capturedJudgeTypeAmendCommand.getValue().getDescription(), NOT_EQUAL);
        assertEquals(viewNameAmendJudgeType, results.getModelAndView().getViewName(),
            NOT_EQUAL);
        verify(mockJudgeTypePageStateHolder);
        verify(mockJudgeTypeAmendValidator);
    }

    @Test
    void updateJudgeTypeExceptionTest() throws Exception {
        final Capture<JudgeTypeAmendCommand> capturedJudgeTypeAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        mockJudgeTypeAmendValidator.validate(capture(capturedJudgeTypeAmendCommand),
            capture(capturedErrors));
        expectLastCall();
        mockRefJudgeTypeService.updateJudgeType(capture(capturedJudgeTypeAmendCommand));
        expectLastCall().andThrow(new DataAccessException("Update Judge Type Exception") {});
        replay(mockJudgeTypeAmendValidator);
        replay(mockRefJudgeTypeService);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendJudgeTypeUrl)
            .param(XHIBIT_COURTSITE_ID, "8").param("btnUpdateConfirm", ADD)
            .param("refSystemCodeId", "2").param(DESCRIPTION, JUDGE_TYPE_DESCRIPTION)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeTypeAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(JUDGE_TYPE_DESCRIPTION,
            capturedJudgeTypeAmendCommand.getValue().getDescription(), NOT_EQUAL);
        assertEquals("Unable to update Judge Type: Update Judge Type Exception",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        verify(mockRefJudgeTypeService);
        verify(mockJudgeTypeAmendValidator);
    }

    @Test
    void showCreateJudgeTypeErrorTest() throws Exception {
        final Capture<JudgeTypeSearchCommand> capturedJudgeTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockJudgeTypePageStateHolder
            .setJudgeTypeSearchCommand(capture(capturedJudgeTypeSearchCommand));
        mockJudgeTypeSelectedValidator.validate(capture(capturedJudgeTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeTypeSelectedValidator);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewJudgeTypeUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAdd", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedJudgeTypeSearchCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(viewNameViewJudgeType, results.getModelAndView().getViewName(),
            NOT_EQUAL);
        verify(mockJudgeTypePageStateHolder);
        verify(mockJudgeTypeSelectedValidator);
    }

    @Test
    void createJudgeTypeErrorTest() throws Exception {
        final Capture<JudgeTypeCreateCommand> capturedJudgeTypeCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<RefSystemCodeDto>> capturedRefSysCodeDtos = newCapture();
        final List<RefSystemCodeDto> refSystemCodeDtoList = List.of(new RefSystemCodeDto());
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        expect(mockJudgeTypePageStateHolder.getJudgeTypes()).andReturn(refSystemCodeDtoList)
            .times(2);
        mockJudgeTypeCreateValidator.validate(capture(capturedJudgeTypeCreateCommand),
            capture(capturedErrors), capture(capturedRefSysCodeDtos));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockJudgeTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockJudgeTypeCreateValidator);
        replay(mockJudgeTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCreateJudgeTypeUrl).param("code", CREATE_COMMAND_CODE)
                .param(DESCRIPTION, "CreateCommandDescription").param("btnCreateConfirm", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(JudgeTypeCreateCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(refSystemCodeDtoList, capturedRefSysCodeDtos.getValue(), NOT_EQUAL);
        assertEquals(CREATE_COMMAND_CODE, capturedJudgeTypeCreateCommand.getValue().getCode(),
            NOT_EQUAL);
        assertEquals(viewNameCreateJudgeType, results.getModelAndView().getViewName(),
            NOT_EQUAL);
        assertEquals(refSystemCodeDtoList, model.get(JUDGE_TYPE_LIST), NOT_EQUAL);
        verify(mockJudgeTypePageStateHolder);
        verify(mockJudgeTypeCreateValidator);
    }
}
