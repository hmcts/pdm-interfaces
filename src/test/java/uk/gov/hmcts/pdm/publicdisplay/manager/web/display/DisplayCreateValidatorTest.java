package uk.gov.hmcts.pdm.publicdisplay.manager.web.display;

import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;

import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class DisplayCreateValidatorTest extends AbstractJUnit {

    private static final String NOT_FALSE = "Not false";
    private static final String DISPLAY_CREATE_VALIDATOR = "displayCreateValidator";
    private static final String A_DIFFERENT_DESCRIPTION_CODE = "A different description code";
    private DisplayCreateValidator classUnderTest;
    private List<DisplayDto> displayDtos;
    private MessageSource mockMessageSource;

    @BeforeEach
    public void setup() {
        DisplayDto displayDto = new DisplayDto();
        displayDto.setDescriptionCode("A Description Code");
        displayDtos = List.of(displayDto);

        // Create a new version of the class under test
        classUnderTest = new DisplayCreateValidator();
        mockMessageSource = EasyMock.createMock(MessageSource.class);

        ReflectionTestUtils.setField(classUnderTest, "messageSource", mockMessageSource);
    }

    @Test
    void supportsTest() {
        final boolean result = classUnderTest.supports(DisplayCreateCommand.class);
        assertTrue(result, "Not true");
    }

    @Test
    void validateDescriptionCodeTest() {
        DisplayCreateCommand displayCreateCommand = new DisplayCreateCommand();
        displayCreateCommand.setDescriptionCode(null);

        final BindingResult errors = new BeanPropertyBindingResult(displayCreateCommand, DISPLAY_CREATE_VALIDATOR);

        classUnderTest.validate(displayCreateCommand, errors, displayDtos);

        assertEquals("displayAmendCommand.descriptionCode.notBlank", errors.getGlobalErrors().get(0).getCode(),
                NOT_FALSE);
    }

    @Test
    void validateDescriptionCodeAlreadyExistsTest() {
        DisplayCreateCommand displayCreateCommand = new DisplayCreateCommand();
        displayCreateCommand.setDescriptionCode("A Description Code");

        final BindingResult errors = new BeanPropertyBindingResult(displayCreateCommand, DISPLAY_CREATE_VALIDATOR);

        expect(mockMessageSource.getMessage("displayCreateCommand.descriptionCode.exists", null,
                Locale.getDefault())).andReturn("Location Description already exists.");
        replay(mockMessageSource);

        classUnderTest.validate(displayCreateCommand, errors, displayDtos);

        assertEquals("Location Description already exists.", errors.getAllErrors().get(0).getDefaultMessage(),
                "Not " + "equal");
        verify(mockMessageSource);
    }

    @Test
    void validateDisplayTypeIdTest() {
        DisplayCreateCommand displayCreateCommand = new DisplayCreateCommand();
        displayCreateCommand.setDescriptionCode(A_DIFFERENT_DESCRIPTION_CODE);
        displayCreateCommand.setDisplayTypeId(null);

        final BindingResult errors = new BeanPropertyBindingResult(displayCreateCommand, DISPLAY_CREATE_VALIDATOR);

        classUnderTest.validate(displayCreateCommand, errors, displayDtos);

        assertEquals("displayAmendCommand.displayTypeId.notNull", errors.getGlobalErrors().get(0).getCode(), NOT_FALSE);
    }

    @Test
    void validateCourtSiteIdTest() {
        DisplayCreateCommand displayCreateCommand = new DisplayCreateCommand();
        displayCreateCommand.setDescriptionCode(A_DIFFERENT_DESCRIPTION_CODE);
        displayCreateCommand.setDisplayTypeId(3);
        displayCreateCommand.setXhibitCourtSiteId(null);

        final BindingResult errors = new BeanPropertyBindingResult(displayCreateCommand, DISPLAY_CREATE_VALIDATOR);

        classUnderTest.validate(displayCreateCommand, errors, displayDtos);

        assertEquals("displayAmendCommand.xhibitCourtSiteId.notNull", errors.getGlobalErrors().get(0).getCode(),
                NOT_FALSE);
    }

    @Test
    void validateRotationSetIdTest() {
        DisplayCreateCommand displayCreateCommand = new DisplayCreateCommand();
        displayCreateCommand.setDescriptionCode(A_DIFFERENT_DESCRIPTION_CODE);
        displayCreateCommand.setDisplayTypeId(3);
        displayCreateCommand.setXhibitCourtSiteId(3L);
        displayCreateCommand.setRotationSetId(null);

        final BindingResult errors = new BeanPropertyBindingResult(displayCreateCommand, DISPLAY_CREATE_VALIDATOR);

        classUnderTest.validate(displayCreateCommand, errors, displayDtos);

        assertEquals("displayAmendCommand.rotationSetId.notNull", errors.getGlobalErrors().get(0).getCode(), NOT_FALSE);
    }

    @Test
    void validateSuccessTest() {
        DisplayCreateCommand displayCreateCommand = new DisplayCreateCommand();
        displayCreateCommand.setDescriptionCode(A_DIFFERENT_DESCRIPTION_CODE);
        displayCreateCommand.setDisplayTypeId(3);
        displayCreateCommand.setXhibitCourtSiteId(3L);
        displayCreateCommand.setRotationSetId(7);

        final BindingResult errors = new BeanPropertyBindingResult(displayCreateCommand, DISPLAY_CREATE_VALIDATOR);

        classUnderTest.validate(displayCreateCommand, errors, displayDtos);

        assertFalse(errors.hasErrors(), "Not false");

    }
}