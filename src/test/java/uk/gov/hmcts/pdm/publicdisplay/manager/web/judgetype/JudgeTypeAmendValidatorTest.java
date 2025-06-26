package uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype;

import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class JudgeTypeAmendValidatorTest extends AbstractJUnit {

    private static final String NOT_FALSE = "Not false";
    private static final String JUDGE_TYPE_AMEND_VALIDATOR = "judgeTypeAmendValidator";
    private JudgeTypeAmendValidator classUnderTest;

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new JudgeTypeAmendValidator();
    }

    @Test
    void supportsTest() {
        final boolean result = classUnderTest.supports(JudgeTypeAmendCommand.class);
        assertTrue(result, "Not true");
    }

    @Test
    void validateRefSystemCodeIdTest() {
        JudgeTypeAmendCommand judgeTypeAmendCommand = new JudgeTypeAmendCommand();
        judgeTypeAmendCommand.setRefSystemCodeId(null);

        final BindingResult errors = new BeanPropertyBindingResult(judgeTypeAmendCommand, JUDGE_TYPE_AMEND_VALIDATOR);

        classUnderTest.validate(judgeTypeAmendCommand, errors);

        assertEquals("judgeTypeAmendCommand.refSystemCodeId.notNull", errors.getGlobalErrors().get(0).getCode(),
                NOT_FALSE);
    }

    @Test
    void validateDescriptionTest() {
        JudgeTypeAmendCommand judgeTypeAmendCommand = new JudgeTypeAmendCommand();
        judgeTypeAmendCommand.setRefSystemCodeId(2);
        judgeTypeAmendCommand.setDescription(null);

        final BindingResult errors = new BeanPropertyBindingResult(judgeTypeAmendCommand, JUDGE_TYPE_AMEND_VALIDATOR);

        classUnderTest.validate(judgeTypeAmendCommand, errors);

        assertEquals("judgeTypeAmendCommand.description.notBlank", errors.getGlobalErrors().get(0).getCode(),
                NOT_FALSE);
    }

    @Test
    void validateSuccessTest() {
        JudgeTypeAmendCommand judgeTypeAmendCommand = new JudgeTypeAmendCommand();
        judgeTypeAmendCommand.setRefSystemCodeId(2);
        judgeTypeAmendCommand.setDescription("A description");

        final JudgeTypePageStateHolder mockJudgeTypePageStateHolder =
                EasyMock.createMock(JudgeTypePageStateHolder.class);
        ReflectionTestUtils.setField(classUnderTest, "judgeTypePageStateHolder", mockJudgeTypePageStateHolder);

        final BindingResult errors = new BeanPropertyBindingResult(judgeTypeAmendCommand, JUDGE_TYPE_AMEND_VALIDATOR);

        replay(mockJudgeTypePageStateHolder);
        
        classUnderTest.validate(judgeTypeAmendCommand, errors);

        assertFalse(errors.hasErrors(), "Not false");
        verify(mockJudgeTypePageStateHolder);
    }
}