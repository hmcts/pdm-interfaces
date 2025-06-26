package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtService;

import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class CourtCreateValidatorTest extends AbstractJUnit {
    private static final String NOT_EQUAL = "Not equal";
    private static final String FALSE = "False";
    private static final String COURT_CREATE_VALIDATOR = "courtCreateValidator";
    private CourtCreateValidator classUnderTest;
    private CourtPageStateHolder mockcourtPageStateHolder;
    private ICourtService mockCourtService;
    private MessageSource mockMessageSource;

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new CourtCreateValidator();

        // Setup the mock version of the called classes
        mockcourtPageStateHolder = createMock(CourtPageStateHolder.class);
        mockCourtService = createMock(ICourtService.class);
        mockMessageSource = createMock(MessageSource.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "courtPageStateHolder", mockcourtPageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "messageSource", mockMessageSource);
    }

    @Test
    void supportsTest() {
        final boolean result = classUnderTest.supports(CourtCreateCommand.class);
        assertTrue(result, FALSE);
    }

    @Test
    void validateSuccessTest() {
        final CourtCreateCommand courtCreateCommand = new CourtCreateCommand();
        courtCreateCommand.setCourtSiteName("courtSiteName");
        courtCreateCommand.setCourtSiteCode("A courtSiteCode");
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        xhibitCourtSiteDto.setCourtId(2);
        xhibitCourtSiteDto.setCourtSiteCode("A different courtSiteCode");
        List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(xhibitCourtSiteDto);
        CourtSearchCommand courtSearchCommand = new CourtSearchCommand();
        courtSearchCommand.setCourtId(3);
        final BindingResult errors = new BeanPropertyBindingResult(courtCreateCommand, COURT_CREATE_VALIDATOR);

        expect(mockCourtService.getCourtSites(eq(2))).andReturn(xhibitCourtSiteDtos);
        replay(mockCourtService);
        expect(mockcourtPageStateHolder.getCourtSearchCommand()).andReturn(courtSearchCommand);
        replay(mockcourtPageStateHolder);

        classUnderTest.validate(courtCreateCommand, errors, mockCourtService, 2);

        assertFalse(errors.hasErrors(), NOT_EQUAL);
        verify(mockCourtService);
    }

    @Test
    void validateCourtSiteNameTest() {
        final CourtCreateCommand courtCreateCommand = new CourtCreateCommand();
        courtCreateCommand.setCourtSiteName(null);
        final BindingResult errors = new BeanPropertyBindingResult(courtCreateCommand, COURT_CREATE_VALIDATOR);

        classUnderTest.validate(courtCreateCommand, errors, mockCourtService, 2);
        replay(mockCourtService);

        assertEquals("courtAmendCommand.courtSiteName.notBlank",
                errors.getGlobalErrors().get(0).getCode(), NOT_EQUAL);
        verify(mockCourtService);
    }

    @Test
    void validateCourtSiteCodeTest() {
        final CourtCreateCommand courtCreateCommand = new CourtCreateCommand();
        courtCreateCommand.setCourtSiteName("courtSiteName");
        final BindingResult errors = new BeanPropertyBindingResult(courtCreateCommand, COURT_CREATE_VALIDATOR);

        classUnderTest.validate(courtCreateCommand, errors, mockCourtService, 2);
        replay(mockCourtService);

        assertEquals("courtAmendCommand.courtSiteCode.notBlank",
                errors.getGlobalErrors().get(0).getCode(), NOT_EQUAL);
        verify(mockCourtService);
    }

    @Test()
    void validateExceptionTest() {
        final CourtCreateCommand courtCreateCommand = new CourtCreateCommand();
        final BindingResult errors = new BeanPropertyBindingResult(courtCreateCommand, COURT_CREATE_VALIDATOR);

        IllegalArgumentException thrownException = assertThrows(
                IllegalArgumentException.class,
                () -> classUnderTest.validate(courtCreateCommand, errors),
                "Exception not Thrown"
        );

        assertEquals("Use other method definition", thrownException.getMessage(), NOT_EQUAL);
    }

    @Test
    void validateCourtSiteCodeAlreadyExistsTest() {
        final CourtCreateCommand courtCreateCommand = new CourtCreateCommand();
        courtCreateCommand.setCourtSiteName("courtSiteName");
        courtCreateCommand.setCourtSiteCode("A courtSiteCode");
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        xhibitCourtSiteDto.setCourtId(2);
        xhibitCourtSiteDto.setCourtSiteCode("A courtSiteCode");
        List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(xhibitCourtSiteDto);
        final BindingResult errors = new BeanPropertyBindingResult(courtCreateCommand, COURT_CREATE_VALIDATOR);

        expect(mockCourtService.getCourtSites(eq(2))).andReturn(xhibitCourtSiteDtos);
        replay(mockCourtService);

        expect(mockMessageSource.getMessage("courtCreateCommand.courtSiteCode.exists",
                null, Locale.getDefault())).andReturn("Court Site Code already exists.");
        replay(mockMessageSource);

        classUnderTest.validate(courtCreateCommand, errors, mockCourtService, 2);

        assertEquals("Court Site Code already exists.",
                errors.getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);

        verify(mockCourtService);
        verify(mockMessageSource);
    }
}
