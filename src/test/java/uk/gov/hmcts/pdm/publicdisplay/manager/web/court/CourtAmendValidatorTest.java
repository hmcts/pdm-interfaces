package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class CourtAmendValidatorTest extends AbstractJUnit {

    private static final String NOT_EQUAL = "Not equal";
    private static final String FALSE = "False";
    private CourtAmendValidator classUnderTest;
    private CourtPageStateHolder mockcourtPageStateHolder;

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test (CduAmendValidator)
        classUnderTest = new CourtAmendValidator();

        // Setup the mock version of the called classes
        mockcourtPageStateHolder = createMock(CourtPageStateHolder.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "courtPageStateHolder", mockcourtPageStateHolder);
    }

    @Test
    void supportsTest() {
        final boolean result = classUnderTest.supports(CourtAmendValidator.class);
        assertTrue(result, FALSE);
    }

    @Test
    void validateCourtSiteNameTest() {
        final CourtAmendCommand courtAmendCommand = new CourtAmendCommand();
        courtAmendCommand.setCourtSiteName(null);
        final BindingResult errors = new BeanPropertyBindingResult(courtAmendCommand, "courtAmendValidator");

        classUnderTest.validate(courtAmendCommand, errors);

        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);
    }

    @Test
    void validateCourtSiteCodeTest() {
        final CourtAmendCommand courtAmendCommand = new CourtAmendCommand();
        courtAmendCommand.setCourtSiteCode(null);
        courtAmendCommand.setCourtSiteName("courtSiteName");
        final BindingResult errors = new BeanPropertyBindingResult(courtAmendCommand, "courtAmendValidator");

        classUnderTest.validate(courtAmendCommand, errors);

        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);
    }

    @Test
    void validateCourtIdTest() {
        final CourtAmendCommand courtAmendCommand = new CourtAmendCommand();
        courtAmendCommand.setCourtSiteCode("courtSiteCode");
        courtAmendCommand.setCourtSiteName("courtSiteName");
        final BindingResult errors = new BeanPropertyBindingResult(courtAmendCommand, "courtAmendValidator");

        replay(mockcourtPageStateHolder);

        classUnderTest.validate(courtAmendCommand, errors);

        assertFalse(errors.hasErrors(), FALSE);
        verify(mockcourtPageStateHolder);
    }
}
