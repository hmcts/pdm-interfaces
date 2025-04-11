package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class CdusControllerUtilityTest.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestMethodOrder(OrderAnnotation.class)
@SuppressWarnings("PMD.LawOfDemeter")
class CdusControllerUtilityTest extends AbstractJUnit {

    private static final String TRUE = "Result is False";
    private static final String NULL = "Result is not Null";
    
    @Mock
    private CduPageStateHolder mockCduPageStateHolder;
   
    @InjectMocks
    private CdusControllerUtility classUnderTest;

    @Test
    void testGetSelectedCduEmptyList() {
        boolean result = testGetSelectedCduEmptyList(null);
        assertTrue(result, TRUE);
        result = testGetSelectedCduEmptyList(new ArrayList<>());
        assertTrue(result, TRUE);
    }
    
    private boolean testGetSelectedCduEmptyList(List<CduDto> cduList) {
        // Expects
        Mockito.when(mockCduPageStateHolder.getCdus()).thenReturn(cduList);
        // Run
        CduDto result = classUnderTest.getSelectedCdu("");
        // Checks
        assertNull(result, NULL);
        return true;
    }
}
