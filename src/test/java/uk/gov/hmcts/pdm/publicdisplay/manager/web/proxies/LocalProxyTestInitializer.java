package uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.easymock.IAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.ScheduleDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.LocalProxyService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService;

import java.util.List;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.replay;


/**
 * The Class LocalProxyController.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
abstract class LocalProxyTestInitializer extends LocalProxyControllerTestBase {

    /** The view name local proxy. */
    protected String viewNameLocalProxy;

    /** The view name mapping local proxy. */
    protected String mappingNameLocalProxyUrl;

    /** The view name register local proxy. */
    protected String viewNameRegisterLocalProxy;

    /** The view name mapping register local proxy. */
    protected String mappingNameRegisterLocalProxyUrl;

    /** The view name amend local proxy. */
    protected String viewNameAmendLocalProxy;

    /** The view name mapping amend local proxy. */
    protected String mappingNameAmendLocalProxyUrl;

    /** The mock mvc. */
    protected MockMvc mockMvc;

    /** The mock local proxy service. */
    protected ILocalProxyService mockLocalProxyService;

    /** The mock local proxy unregister validator. */
    protected LocalProxyUnregisterValidator mockProxyUnregisterValidator;

    /** The mock local proxy selected validator. */
    protected LocalProxySelectedValidator mockProxySelectedValidator;

    /** The mock local proxy amend validator. */
    protected LocalProxyAmendValidator mockProxyAmendValidator;

    /** The mock local proxy register validator. */
    protected LocalProxyRegisterValidator mockProxyRegisterValidator;

    /** The mock local proxy page state holder. */
    protected LocalProxyPageStateHolder mockLocalProxyPageStateHolder;

    /** The xhibit court sites. */
    protected final List<XhibitCourtSiteDto> xhibitCourtSites = getTestXhibitCourtSites();

    /** The schedules. */
    protected final List<ScheduleDto> schedules = getTestSchedules();

    /** The court site. */
    protected final CourtSiteDto courtSite = getTestCourtSite(1L);


    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Setup the mock version of the called classes
        mockLocalProxyService = createMock(LocalProxyService.class);
        mockLocalProxyPageStateHolder = createMock(LocalProxyPageStateHolder.class);
        mockProxyUnregisterValidator = createMock(LocalProxyUnregisterValidator.class);
        mockProxyRegisterValidator = createMock(LocalProxyRegisterValidator.class);
        mockProxySelectedValidator = createMock(LocalProxySelectedValidator.class);
        mockProxyAmendValidator = createMock(LocalProxyAmendValidator.class);

        // Create a new version of the class under test
        LocalProxyController classUnderTest = new LocalProxyController();

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "localProxyService", mockLocalProxyService);
        ReflectionTestUtils.setField(classUnderTest, "localProxyPageStateHolder",
            mockLocalProxyPageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "localProxyUnregisterValidator",
            mockProxyUnregisterValidator);
        ReflectionTestUtils.setField(classUnderTest, "localProxyRegisterValidator",
            mockProxyRegisterValidator);
        ReflectionTestUtils.setField(classUnderTest, "localProxyAmendValidator",
            mockProxyAmendValidator);
        ReflectionTestUtils.setField(classUnderTest, "localProxySelectedValidator",
            mockProxySelectedValidator);

        // Get the static variables from the class under test
        String mappingName = "/proxies";
        viewNameLocalProxy =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_VIEW_LOCAL_PROXY");
        mappingNameLocalProxyUrl = mappingName
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_VIEW_LOCAL_PROXY");
        viewNameRegisterLocalProxy =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_REGISTER_LOCAL_PROXY");
        mappingNameRegisterLocalProxyUrl = mappingName
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_REGISTER_LOCAL_PROXY");
        viewNameAmendLocalProxy =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_AMEND_LOCAL_PROXY");
        mappingNameAmendLocalProxyUrl = mappingName
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_AMEND_LOCAL_PROXY");

        // Stop circular view path error
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view");
        viewResolver.setSuffix(".jsp");

        // Setup the mock version of the modelMvc
        mockMvc =
            MockMvcBuilders.standaloneSetup(classUnderTest).setViewResolvers(viewResolver).build();
    }

    /**
     * Expect page state holder set selection lists.
     */
    protected void expectSetPageStateHolderSelectionLists() {
        expectPageStateHolderSetSites();
        expectPageStateHolderSetSchedules();
    }

    /**
     * Expect page state holder set schedules.
     */
    protected void expectPageStateHolderSetSchedules() {
        mockLocalProxyPageStateHolder.setSchedules(schedules);
        expectLastCall();
    }

    /**
     * Expect page state holder set sites.
     */
    protected void expectPageStateHolderSetSites() {
        mockLocalProxyPageStateHolder.setSites(xhibitCourtSites);
        expectLastCall();
        expect(mockLocalProxyPageStateHolder.getSites()).andReturn(xhibitCourtSites);
    }

    /**
     * Expect amend validator.
     *
     * @param capturedCommand the captured command
     * @param capturedErrors the captured errors
     * @param isSuccess the is success
     */
    protected void expectAmendValidator(final Capture<LocalProxyAmendCommand> capturedCommand,
        final Capture<BindingResult> capturedErrors, final boolean isSuccess) {
        mockProxyAmendValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectValidatorSuccess(isSuccess);
        replay(mockProxyAmendValidator);
    }

    /**
     * Expect selected validator.
     *
     * @param capturedCommand the captured command
     * @param capturedErrors the captured errors
     * @param isSuccess the is success
     */
    protected void expectSelectedValidator(final Capture<LocalProxySearchCommand> capturedCommand,
        final Capture<BindingResult> capturedErrors, final boolean isSuccess) {
        mockProxySelectedValidator.validate(capture(capturedCommand), capture(capturedErrors));
        expectValidatorSuccess(isSuccess);
        replay(mockProxySelectedValidator);
    }

    /**
     * Expect validator success.
     *
     * @param isSuccess the is success
     */
    protected void expectValidatorSuccess(final boolean isSuccess) {
        if (isSuccess) {
            expectLastCall();
        } else {
            expectLastCall().andAnswer(new IAnswer<Void>() {
                @Override
                public Void answer() {
                    ((BindingResult) getCurrentArguments()[1]).reject("mock error message");
                    return null;
                }
            });
        }
    }

}
