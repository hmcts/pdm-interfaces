package uk.gov.hmcts.pdm.publicdisplay.manager.web.display;

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
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayLocationDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RotationSetsDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IDisplayService;

import java.util.List;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(EasyMockExtension.class)
@SuppressWarnings({"PMD"})
abstract class DisplayControllerBaseTest extends AbstractJUnit {
    protected DisplaySelectedValidator mockDisplaySelectedValidator;
    protected DisplayCreateValidator mockDisplayCreateValidator;
    protected DisplayAmendValidator mockDisplayAmendValidator;
    protected DisplayPageStateHolder mockDisplayPageStateHolder;
    protected DisplayDeleteValidator mockDisplayDeleteValidator;
    protected IDisplayService mockDisplayService;
    protected String viewNameViewDisplay;
    protected String mappingNameViewDisplayUrl;
    protected String viewNameCreateDisplay;
    protected String mappingNameCreateDisplayUrl;
    protected String viewNameAmendDisplay;
    protected String mappingNameAmendDisplayUrl;
    protected String viewNameDeleteDisplay;
    protected String mappingNameDeleteDisplayUrl;
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
    protected static final String COURTSITE = "courtSite";
    protected static final String A_LOCALE = "A Locale";
    protected static final String DISPLAY_ID = "displayId";
    protected static final String DESCRIPTION_CODE = "descriptionCode";
    protected static final String ROTATION_SET_ID = "rotationSetId";
    protected static final String DISPLAY_TYPE_ID = "displayTypeId";
    protected static final String DISPLAY_LIST = "displayList";
    protected static final String DISPLAY_TYPE_LIST = "displayTypeList";
    protected static final String ROTATION_SET_LIST = "rotationSetList";
    protected static final String REQUEST_MAPPING = "/display";

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        DisplayController classUnderTest = new DisplayController();
        // Setup the mock version of the called classes
        mockObjects();
        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "displaySelectedValidator",
            mockDisplaySelectedValidator);
        ReflectionTestUtils.setField(classUnderTest, "displayCreateValidator",
            mockDisplayCreateValidator);
        ReflectionTestUtils.setField(classUnderTest, "displayAmendValidator",
            mockDisplayAmendValidator);
        ReflectionTestUtils.setField(classUnderTest, "displayDeleteValidator",
            mockDisplayDeleteValidator);
        ReflectionTestUtils.setField(classUnderTest, "displayPageStateHolder",
            mockDisplayPageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "displayService", mockDisplayService);

        // Get the static variables from the class under test
        viewNameViewDisplay =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_VIEW_DISPLAY");
        mappingNameViewDisplayUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_VIEW_DISPLAY");
        viewNameCreateDisplay =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_CREATE_DISPLAY");
        mappingNameCreateDisplayUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_CREATE_DISPLAY");
        viewNameAmendDisplay =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_AMEND_DISPLAY");
        mappingNameAmendDisplayUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_AMEND_DISPLAY");
        viewNameDeleteDisplay =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_DELETE_DISPLAY");
        mappingNameDeleteDisplayUrl = REQUEST_MAPPING
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_DELETE_DISPLAY");


        // Stop circular view path error
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view");
        viewResolver.setSuffix(".jsp");

        // Setup the mock version of the modelMvc
        mockMvc =
            MockMvcBuilders.standaloneSetup(classUnderTest).setViewResolvers(viewResolver).build();
    }

    protected void mockObjects() {
        mockDisplaySelectedValidator = createMock(DisplaySelectedValidator.class);
        mockDisplayCreateValidator = createMock(DisplayCreateValidator.class);
        mockDisplayAmendValidator = createMock(DisplayAmendValidator.class);
        mockDisplayPageStateHolder = createMock(DisplayPageStateHolder.class);
        mockDisplayDeleteValidator = createMock(DisplayDeleteValidator.class);
        mockDisplayService = createMock(IDisplayService.class);
    }

    protected List<XhibitCourtSiteDto> createCourtSiteDtoList() {
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        xhibitCourtSiteDto.setId(8L);
        xhibitCourtSiteDto.setCourtId(10);
        return List.of(xhibitCourtSiteDto);
    }

    protected List<DisplayDto> createDisplayDtoList() {
        DisplayDto displayDto = new DisplayDto();
        displayDto.setDisplayId(2);
        displayDto.setLocale(A_LOCALE);
        displayDto.setDisplayLocationId(1);
        displayDto.setDisplayTypeId(12);
        displayDto.setRotationSetId(12);
        return List.of(displayDto);
    }

    protected List<DisplayTypeDto> createDisplayTypeDtoList() {
        DisplayTypeDto displayTypeDto = new DisplayTypeDto();
        displayTypeDto.setDisplayTypeId(12);
        return List.of(displayTypeDto);
    }

    protected List<RotationSetsDto> createRotationSets() {
        RotationSetsDto rotationSetsDto = new RotationSetsDto();
        rotationSetsDto.setDefaultYn("yn");
        rotationSetsDto.setRotationSetId(12);
        return List.of(rotationSetsDto);
    }
    
    protected DisplayLocationDto createDisplayLocationDto() {
        DisplayLocationDto displayLocationDto = new DisplayLocationDto();
        displayLocationDto.setCourtSiteId(1);
        displayLocationDto.setDescriptionCode(DESCRIPTION_CODE);
        displayLocationDto.setDisplayLocationId(1);
        return displayLocationDto;
    }

    @Test
    void viewDisplayTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockDisplayPageStateHolder.reset();
        expectLastCall();
        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockDisplayPageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockDisplayService);
        replay(mockDisplayPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(get(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "1")).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertInstanceOf(DisplaySearchCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameViewDisplay, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);
        verify(mockDisplayService);
        verify(mockDisplayPageStateHolder);
    }

    @Test
    void viewDisplayNullTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        expect(mockDisplayPageStateHolder.getDisplaySearchCommand()).andReturn(null);
        mockDisplayPageStateHolder.reset();
        expectLastCall();
        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockDisplayPageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockDisplayService);
        replay(mockDisplayPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            get(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "1").param("reset", "false"))
            .andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertInstanceOf(DisplaySearchCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameViewDisplay, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);
        verify(mockDisplayService);
        verify(mockDisplayPageStateHolder);
    }

    @Test
    void viewDisplayResetFalseTest() throws Exception {
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        DisplaySearchCommand displaySearchCommand = new DisplaySearchCommand();

        expect(mockDisplayPageStateHolder.getDisplaySearchCommand()).andReturn(displaySearchCommand)
            .times(2);
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockDisplayPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            get(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "1").param("reset", "false"))
            .andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewDisplay, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertInstanceOf(DisplaySearchCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        verify(mockDisplayPageStateHolder);
    }
}
