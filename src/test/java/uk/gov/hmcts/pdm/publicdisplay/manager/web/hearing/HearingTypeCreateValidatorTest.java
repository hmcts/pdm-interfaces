package uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;

import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class HearingTypeCreateValidatorTest extends AbstractJUnit {

    private static final String NOT_EQUAL = "Not equal";
    private static final String HEARING_TYPE_CREATE_VALIDATOR = "hearingTypeCreateValidator";
    private HearingTypeCreateValidator classUnderTest;
    private MessageSource mockMessageSource;
    private List<HearingTypeDto> hearingTypeDtos;

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new HearingTypeCreateValidator();
        HearingTypeDto hearingTypeDto = new HearingTypeDto();
        hearingTypeDto.setHearingTypeCode("A hearingTypeCode");
        hearingTypeDtos = List.of(hearingTypeDto);

        // Setup the mock version of the called classes
        HearingTypePageStateHolder mockHearingTypePageStateHolder = createMock(HearingTypePageStateHolder.class);
        mockMessageSource = createMock(MessageSource.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "hearingTypePageStateHolder", mockHearingTypePageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "messageSource", mockMessageSource);
    }

    @Test
    void supportsTest() {
        final boolean result = classUnderTest.supports(HearingTypeCreateCommand.class);
        assertTrue(result, "Not true");
    }

    @Test
    void validateHearingTypeCodeTest() {
        final HearingTypeCreateCommand hearingTypeCreateCommand = new HearingTypeCreateCommand();
        hearingTypeCreateCommand.setHearingTypeDesc(null);
        final BindingResult errors = new BeanPropertyBindingResult(hearingTypeCreateCommand,
                HEARING_TYPE_CREATE_VALIDATOR);

        classUnderTest.validate(hearingTypeCreateCommand, errors, hearingTypeDtos);

        assertEquals("hearingTypeCreateCommand.hearingTypeCode.notBlank", errors.getGlobalErrors().get(0).getCode(),
                NOT_EQUAL);
    }

    @Test
    void validateAlreadyExistsTest() {
        final HearingTypeCreateCommand hearingTypeCreateCommand = new HearingTypeCreateCommand();
        hearingTypeCreateCommand.setHearingTypeCode("A hearingTypeCode");
        final BindingResult errors = new BeanPropertyBindingResult(hearingTypeCreateCommand,
                HEARING_TYPE_CREATE_VALIDATOR);

        expect(mockMessageSource.getMessage("hearingTypeCreateCommand.hearingTypeCode.exists", null,
                Locale.getDefault())).andReturn("Code already exists.");
        replay(mockMessageSource);
        classUnderTest.validate(hearingTypeCreateCommand, errors, hearingTypeDtos);

        assertEquals("Code already exists.", errors.getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        verify(mockMessageSource);
    }

    @Test
    void validateHearingTypeCodeDescriptionTest() {
        final HearingTypeCreateCommand hearingTypeCreateCommand = new HearingTypeCreateCommand();
        hearingTypeCreateCommand.setHearingTypeDesc(null);
        hearingTypeCreateCommand.setHearingTypeCode("A different hearingTypeCode");
        final BindingResult errors = new BeanPropertyBindingResult(hearingTypeCreateCommand,
                HEARING_TYPE_CREATE_VALIDATOR);

        classUnderTest.validate(hearingTypeCreateCommand, errors, hearingTypeDtos);

        assertEquals("hearingTypeAmendCommand.hearingTypeDesc.notBlank", errors.getGlobalErrors().get(0).getCode(),
                NOT_EQUAL);
    }

    @Test
    void validateHearingTypeCategoryTest() {
        final HearingTypeCreateCommand hearingTypeCreateCommand = new HearingTypeCreateCommand();
        hearingTypeCreateCommand.setHearingTypeDesc("A hearingTypeDescription");
        hearingTypeCreateCommand.setHearingTypeCode("A different hearingTypeCode");
        hearingTypeCreateCommand.setCategory(null);
        final BindingResult errors = new BeanPropertyBindingResult(hearingTypeCreateCommand,
                HEARING_TYPE_CREATE_VALIDATOR);

        classUnderTest.validate(hearingTypeCreateCommand, errors, hearingTypeDtos);

        assertEquals("hearingTypeAmendCommand.category.notBlank", errors.getGlobalErrors().get(0).getCode(), NOT_EQUAL);
    }

    @Test
    void validateSuccessTest() {
        final HearingTypeCreateCommand hearingTypeCreateCommand = new HearingTypeCreateCommand();
        hearingTypeCreateCommand.setHearingTypeDesc("A hearingTypeDescription");
        hearingTypeCreateCommand.setHearingTypeCode("A different hearingTypeCode");
        hearingTypeCreateCommand.setCategory("A category");
        final BindingResult errors = new BeanPropertyBindingResult(hearingTypeCreateCommand,
                HEARING_TYPE_CREATE_VALIDATOR);

        classUnderTest.validate(hearingTypeCreateCommand, errors, hearingTypeDtos);

        assertFalse(errors.hasErrors(), "Not false");
    }

}