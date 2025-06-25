package uk.gov.hmcts.pdm.publicdisplay.manager.web.judge;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefJudgeDto;

import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class JudgeCreateValidatorTest extends AbstractJUnit {
    private static final String NOT_EQUAL = "Not equal";
    private static final String JUDGE_CREATE_VALIDATOR = "judgeCreateValidator";
    private static final String NOT_FALSE = "Not false";
    private static final String A_FIRST_NAME = "A firstName";
    private static final String A_SURNAME = "A surname";
    private JudgeCreateValidator classUnderTest;
    private MessageSource mockMessageSource;
    private List<RefJudgeDto> refJudgeDtos;

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new JudgeCreateValidator();
        RefJudgeDto hearingTypeDto = new RefJudgeDto();
        hearingTypeDto.setSurname(A_SURNAME);
        hearingTypeDto.setFirstName(A_FIRST_NAME);
        refJudgeDtos = List.of(hearingTypeDto);

        // Setup the mock version of the called classes
        JudgePageStateHolder mockJudgePageStateHolder = createMock(JudgePageStateHolder.class);
        mockMessageSource = createMock(MessageSource.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "judgePageStateHolder", mockJudgePageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "messageSource", mockMessageSource);
    }

    @Test
    void supportsTest() {
        final boolean result = classUnderTest.supports(JudgeCreateCommand.class);
        assertTrue(result, "Not true");
    }

    @Test
    void validateSurnameTest() {
        final JudgeCreateCommand judgeCreateCommand = new JudgeCreateCommand();
        judgeCreateCommand.setSurname(null);

        final BindingResult errors = new BeanPropertyBindingResult(judgeCreateCommand, JUDGE_CREATE_VALIDATOR);

        classUnderTest.validate(judgeCreateCommand, errors, refJudgeDtos);

        assertEquals("judgeAmendCommand.surname.notBlank", errors.getGlobalErrors().get(0).getCode(), NOT_FALSE);
    }

    @Test
    void validateFirstNameTest() {
        final JudgeCreateCommand judgeCreateCommand = new JudgeCreateCommand();
        judgeCreateCommand.setSurname(A_SURNAME);
        judgeCreateCommand.setFirstName(null);

        final BindingResult errors = new BeanPropertyBindingResult(judgeCreateCommand, JUDGE_CREATE_VALIDATOR);

        classUnderTest.validate(judgeCreateCommand, errors, refJudgeDtos);

        assertEquals("judgeAmendCommand.firstName.notBlank", errors.getGlobalErrors().get(0).getCode(), NOT_FALSE);
    }

    @Test
    void validateTitleTest() {
        final JudgeCreateCommand judgeCreateCommand = new JudgeCreateCommand();
        judgeCreateCommand.setSurname(A_SURNAME);
        judgeCreateCommand.setFirstName(A_FIRST_NAME);
        judgeCreateCommand.setTitle(null);

        final BindingResult errors = new BeanPropertyBindingResult(judgeCreateCommand, JUDGE_CREATE_VALIDATOR);

        classUnderTest.validate(judgeCreateCommand, errors, refJudgeDtos);

        assertEquals("judgeAmendCommand.title.notBlank", errors.getGlobalErrors().get(0).getCode(), NOT_FALSE);
    }

    @Test
    void validateFullListTitleTest() {
        final JudgeCreateCommand judgeCreateCommand = new JudgeCreateCommand();
        judgeCreateCommand.setSurname(A_SURNAME);
        judgeCreateCommand.setFirstName(A_FIRST_NAME);
        judgeCreateCommand.setTitle("A title");
        judgeCreateCommand.setFullListTitle1(null);

        final BindingResult errors = new BeanPropertyBindingResult(judgeCreateCommand, JUDGE_CREATE_VALIDATOR);

        classUnderTest.validate(judgeCreateCommand, errors, refJudgeDtos);

        assertEquals("judgeAmendCommand.fullListTitle1.notBlank", errors.getGlobalErrors().get(0).getCode(),
                "Not " + "false");
    }

    @Test
    void validateJudgeTypeTest() {
        final JudgeCreateCommand judgeCreateCommand = new JudgeCreateCommand();
        judgeCreateCommand.setSurname(A_SURNAME);
        judgeCreateCommand.setFirstName(A_FIRST_NAME);
        judgeCreateCommand.setTitle("A title");
        judgeCreateCommand.setFullListTitle1("A FullListTitle1");
        judgeCreateCommand.setJudgeType(null);

        final BindingResult errors = new BeanPropertyBindingResult(judgeCreateCommand, JUDGE_CREATE_VALIDATOR);

        classUnderTest.validate(judgeCreateCommand, errors, refJudgeDtos);

        assertEquals("judgeAmendCommand.judgeType.notBlank", errors.getGlobalErrors().get(0).getCode(), NOT_FALSE);
    }

    @Test
    void validateAlreadyExistsTest() {
        final JudgeCreateCommand judgeCreateCommand = new JudgeCreateCommand();
        judgeCreateCommand.setSurname(A_SURNAME);
        judgeCreateCommand.setFirstName(A_FIRST_NAME);
        judgeCreateCommand.setTitle("A title");
        judgeCreateCommand.setFullListTitle1("A FullListTitle1");
        judgeCreateCommand.setJudgeType("A judgeType");

        final BindingResult errors = new BeanPropertyBindingResult(judgeCreateCommand, JUDGE_CREATE_VALIDATOR);

        expect(mockMessageSource.getMessage("judgeCreateCommand.judge.exists", null, Locale.getDefault())).andReturn(
                "Judge " + "details" + " already exists.");
        replay(mockMessageSource);

        classUnderTest.validate(judgeCreateCommand, errors, refJudgeDtos);

        assertEquals("Judge details already exists.", errors.getAllErrors().get(0).getDefaultMessage(), NOT_FALSE);
        verify(mockMessageSource);
    }

    @Test()
    void validateExceptionTest() {
        final JudgeCreateCommand judgeCreateCommand = new JudgeCreateCommand();
        final BindingResult errors = new BeanPropertyBindingResult(judgeCreateCommand, JUDGE_CREATE_VALIDATOR);

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class,
                () -> classUnderTest.validate(judgeCreateCommand, errors), "Exception not Thrown");

        assertEquals("Use other method definition", thrownException.getMessage(), NOT_EQUAL);
    }
}
