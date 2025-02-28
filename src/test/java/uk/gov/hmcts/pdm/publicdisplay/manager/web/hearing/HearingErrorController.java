package uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.easymock.IAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IHearingTypeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
abstract class HearingErrorController extends AbstractJUnit {
    protected HearingTypeSelectedValidator mockHearingTypeSelectedValidator;
    protected HearingTypeCreateValidator mockHearingTypeCreateValidator;
    protected HearingTypeAmendValidator mockHearingTypeAmendValidator;
    protected HearingTypePageStateHolder mockHearingTypePageStateHolder;
    protected IHearingTypeService mockHearingTypeService;
    protected String viewNameViewHearing;
    protected String viewNameCreateHearing;
    protected String viewNameAmendHearing;
    protected String mappingNameViewHearingUrl;
    protected String mappingNameCreateHearingUrl;
    protected String mappingNameAmendHearingUrl;
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
    protected static final String HEARING_TYPE_LIST = "hearingTypeList";
    protected static final String COURTSITE = "courtSite";
    protected static final String CATEGORIES_LIST = "categoriesList";
    protected static final String CATEGORY = "category";
    protected static final String A_CATEGORY = "A category";
    protected static final String HEARING_TYPE_DESC = "hearingTypeDesc";
    protected static final String A_HEARING_TYPE_DESC = "A hearingTypeDesc";
    protected static final String A_HEARING_TYPE_CODE = "A hearingTypeCode";
    protected static final String REQUEST_MAPPING = "/hearing";

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        HearingTypeController classUnderTest = new HearingTypeController();
        // Setup the mock version of the called classes
        mockObjects();
        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "hearingTypeSelectedValidator",
            mockHearingTypeSelectedValidator);
        ReflectionTestUtils.setField(classUnderTest, "hearingTypeCreateValidator",
            mockHearingTypeCreateValidator);
        ReflectionTestUtils.setField(classUnderTest, "hearingTypeAmendValidator",
            mockHearingTypeAmendValidator);
        ReflectionTestUtils.setField(classUnderTest, "hearingTypePageStateHolder",
            mockHearingTypePageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "hearingTypeService", mockHearingTypeService);

        // Get the static variables from the class under test
        viewNameViewHearing =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_VIEW_HEARING");
        mappingNameViewHearingUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_VIEW_HEARING");
        viewNameCreateHearing =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_CREATE_HEARING");
        mappingNameCreateHearingUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_CREATE_HEARING");
        viewNameAmendHearing =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_AMEND_HEARING");
        mappingNameAmendHearingUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_AMEND_HEARING");

        // Stop circular view path error
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view");
        viewResolver.setSuffix(".jsp");

        // Setup the mock version of the modelMvc
        mockMvc =
            MockMvcBuilders.standaloneSetup(classUnderTest).setViewResolvers(viewResolver).build();
    }

    protected void mockObjects() {
        mockHearingTypeSelectedValidator = createMock(HearingTypeSelectedValidator.class);
        mockHearingTypeCreateValidator = createMock(HearingTypeCreateValidator.class);
        mockHearingTypeAmendValidator = createMock(HearingTypeAmendValidator.class);
        mockHearingTypePageStateHolder = createMock(HearingTypePageStateHolder.class);
        mockHearingTypeService = createMock(IHearingTypeService.class);
    }

    protected List<XhibitCourtSiteDto> createCourtSiteDtoList() {
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        xhibitCourtSiteDto.setId(8L);
        xhibitCourtSiteDto.setCourtId(10);
        return List.of(xhibitCourtSiteDto);
    }

    protected List<HearingTypeDto> createHearingTypeDtoList() {
        HearingTypeDto hearingTypeDto = new HearingTypeDto();
        hearingTypeDto.setListSequence(4);
        hearingTypeDto.setRefHearingTypeId(2);
        hearingTypeDto.setHearingTypeCode(A_HEARING_TYPE_CODE);
        return List.of(hearingTypeDto);
    }

    protected List<String> createCategoriesList() {
        return List.of("Category A", "Category B");
    }

    @Test
    void createHearingTypeErrorTest() throws Exception {
        final Capture<HearingTypeCreateCommand> hearingTypeCreateCommandCapture = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<HearingTypeDto>> capturedHearingTypeList = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<HearingTypeDto> refHearingTypeDtos = createHearingTypeDtoList();
        final List<String> categories = createCategoriesList();

        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(refHearingTypeDtos)
            .times(2);
        mockHearingTypeCreateValidator.validate(capture(hearingTypeCreateCommandCapture),
            capture(capturedErrors), capture(capturedHearingTypeList));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockHearingTypePageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0));
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockHearingTypeService.getAllCategories()).andReturn(categories);
        replay(mockHearingTypeCreateValidator);
        replay(mockHearingTypePageStateHolder);
        replay(mockHearingTypeService);

        // Perform the test
        final MvcResult results =
            mockMvc
                .perform(post(mappingNameCreateHearingUrl)
                    .param("hearingTypeCode", A_HEARING_TYPE_CODE).param("btnCreateConfirm", ADD)
                    .param(HEARING_TYPE_DESC, A_HEARING_TYPE_DESC).param(CATEGORY, A_CATEGORY))
                .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(HearingTypeCreateCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(viewNameCreateHearing, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(A_HEARING_TYPE_CODE,
            hearingTypeCreateCommandCapture.getValue().getHearingTypeCode(), NOT_EQUAL);
        assertEquals(4, capturedHearingTypeList.getValue().get(0).getListSequence(), NOT_EQUAL);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeCreateValidator);
        verify(mockHearingTypeService);
    }

    @Test
    void showCreateHearingErrorTest() throws Exception {
        final Capture<HearingTypeSearchCommand> capturedHearingTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockHearingTypePageStateHolder
            .setHearingSearchCommand(capture(capturedHearingTypeSearchCommand));
        expectLastCall();
        mockHearingTypeSelectedValidator.validate(capture(capturedHearingTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockHearingTypeSelectedValidator);
        replay(mockHearingTypeService);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewHearingUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAdd", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedHearingTypeSearchCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(viewNameViewHearing, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockHearingTypeService);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeSelectedValidator);
    }

    @Test
    void updateHearingTypeErrorTest() throws Exception {
        final Capture<HearingTypeAmendCommand> hearingTypeAmendCommandCapture = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<HearingTypeDto> refHearingTypeDtos = createHearingTypeDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<String> categories = createCategoriesList();

        mockHearingTypeAmendValidator.validate(capture(hearingTypeAmendCommandCapture),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockHearingTypePageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0));
        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(refHearingTypeDtos);
        expect(mockHearingTypeService.getAllCategories()).andReturn(categories);
        replay(mockHearingTypeAmendValidator);
        replay(mockHearingTypeService);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendHearingUrl)
            .param("refHearingTypeId", "11").param("btnUpdateConfirm", ADD)
            .param(HEARING_TYPE_DESC, A_HEARING_TYPE_DESC).param(CATEGORY, A_CATEGORY)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(HearingTypeAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(refHearingTypeDtos, model.get(HEARING_TYPE_LIST), NOT_EQUAL);
        assertEquals(categories, model.get(CATEGORIES_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get(COURTSITE), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(11, hearingTypeAmendCommandCapture.getValue().getRefHearingTypeId(),
            NOT_EQUAL);
        assertEquals(viewNameAmendHearing, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockHearingTypeService);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeAmendValidator);
    }

    @Test
    void showAmendHearingErrorsTest() throws Exception {
        final Capture<HearingTypeSearchCommand> capturedHearingTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockHearingTypePageStateHolder
            .setHearingSearchCommand(capture(capturedHearingTypeSearchCommand));
        expectLastCall();
        mockHearingTypeSelectedValidator.validate(capture(capturedHearingTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockHearingTypeSelectedValidator);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewHearingUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD))
            .andReturn();

        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedHearingTypeSearchCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(viewNameViewHearing, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeSelectedValidator);
    }

    @Test
    void loadHearingTypeNullTest() throws Exception {
        final List<HearingTypeDto> refHearingTypeDtos = createHearingTypeDtoList();
        final List<XhibitCourtSiteDto> courtSites = new ArrayList<>();

        expect(mockHearingTypeService.getCourtSites()).andReturn(courtSites).anyTimes();
        mockHearingTypePageStateHolder.setSites(courtSites);
        expect(mockHearingTypeService.getHearingType(EasyMock.isA(Integer.class))).andReturn(null)
            .anyTimes();
        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(refHearingTypeDtos)
            .anyTimes();
        replay(mockHearingTypePageStateHolder);
        replay(mockHearingTypeService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameAmendHearingUrl + "/3")).andReturn();
        String response = results.getResponse().getContentAsString();

        assertEquals("", response, NOT_EQUAL);
        verify(mockHearingTypePageStateHolder);
    }
}
