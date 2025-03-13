package uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;

import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(EasyMockExtension.class)
class JudgeTypeCreateValidatorTest extends AbstractJUnit {
    private static final String JUDGE_TYPE_CREATE_VALIDATOR = "judgeTypeCreateValidator";
    private static final String NOT_FALSE = "Not false";
    private static final String A_CODE = "A Code";
    private JudgeTypeCreateValidator classUnderTest;
    private MessageSource mockMessageSource;
    private List<RefSystemCodeDto> refSystemCodeDtos;
    private JudgeTypePageStateHolder mockJudgeTypePageStateHolder;

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new JudgeTypeCreateValidator();
        RefSystemCodeDto refSystemCodeDto = new RefSystemCodeDto();
        refSystemCodeDto.setCode(A_CODE);
        refSystemCodeDtos = List.of(refSystemCodeDto);

        // Setup the mock version of the called classes
        mockJudgeTypePageStateHolder = createMock(JudgeTypePageStateHolder.class);
        mockMessageSource = createMock(MessageSource.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "judgeTypePageStateHolder",
            mockJudgeTypePageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "messageSource", mockMessageSource);
    }

    @Test
    void supportsTest() {
        final boolean result = classUnderTest.supports(JudgeTypeCreateCommand.class);
        assertTrue(result, "Not true");
    }

    @Test
    void validateCodeTest() {
        JudgeTypeCreateCommand judgeTypeCreateCommand = new JudgeTypeCreateCommand();
        judgeTypeCreateCommand.setCode(null);

        final BindingResult errors =
            new BeanPropertyBindingResult(judgeTypeCreateCommand, JUDGE_TYPE_CREATE_VALIDATOR);

        classUnderTest.validate(judgeTypeCreateCommand, errors, refSystemCodeDtos);

        assertEquals("judgeTypeCreateCommand.code.notBlank",
            errors.getGlobalErrors().get(0).getCode(), NOT_FALSE);
    }

    @Test
    void validateAlreadyExistsTest() {
        JudgeTypeCreateCommand judgeTypeCreateCommand = new JudgeTypeCreateCommand();
        judgeTypeCreateCommand.setCode("A code");

        final BindingResult errors =
            new BeanPropertyBindingResult(judgeTypeCreateCommand, JUDGE_TYPE_CREATE_VALIDATOR);
        expect(mockMessageSource.getMessage("judgeTypeCreateCommand.code.exists", null,
            Locale.getDefault())).andReturn("Code is required.");
        replay(mockMessageSource);
        classUnderTest.validate(judgeTypeCreateCommand, errors, refSystemCodeDtos);

        assertEquals("Code is required.", errors.getAllErrors().get(0).getDefaultMessage(),
            NOT_FALSE);
    }

    @Test
    void validateDescriptionTest() {
        JudgeTypeCreateCommand judgeTypeCreateCommand = new JudgeTypeCreateCommand();
        judgeTypeCreateCommand.setCode("A different Code");

        final BindingResult errors =
            new BeanPropertyBindingResult(judgeTypeCreateCommand, JUDGE_TYPE_CREATE_VALIDATOR);

        classUnderTest.validate(judgeTypeCreateCommand, errors, refSystemCodeDtos);

        assertEquals("judgeTypeAmendCommand.description.notBlank",
            errors.getGlobalErrors().get(0).getCode(), NOT_FALSE);
    }

    @Test
    void validateSuccessTest() {
        final JudgeTypeCreateCommand judgeTypeCreateCommand = new JudgeTypeCreateCommand();
        judgeTypeCreateCommand.setCode("A different Code");
        judgeTypeCreateCommand.setDescription("A description");
        final BindingResult errors =
            new BeanPropertyBindingResult(judgeTypeCreateCommand, JUDGE_TYPE_CREATE_VALIDATOR);

        replay(mockJudgeTypePageStateHolder);
        classUnderTest.validate(judgeTypeCreateCommand, errors, refSystemCodeDtos);

        assertFalse(errors.hasErrors(), "Not false");
        verify(mockJudgeTypePageStateHolder);
    }
}
