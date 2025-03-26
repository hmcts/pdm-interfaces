package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class MappingAddValidatorTest.
 *
 * @author pattersone
 */
@ExtendWith(EasyMockExtension.class)
class MappingAddValidatorUrlTest extends MappingAddValidatorTest {

    /**
     * Test cdu url valid.
     */
    @Test
    void testCduUrlValid() {
        final MappingCommand mappingCommand = getTestMappingCommand(VALIDCDU_ID, VALIDURL_ID);
        final BindingResult errors = new BeanPropertyBindingResult(mappingCommand, MAPPING_COMMAND);
        final CduDto cdu = getTestCdu(mappingCommand.getCduId());

        // Define a mock version of the called methods
        expectCduAndUrlServiceCalls(cdu);
        
        // Perform the test
        classUnderTest.validate(mappingCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertFalse(errors.hasErrors(), TRUE);

        // Verify the mocks used in this method were called
        verify(mockCduService);
        verify(mockUrlService);
    }

    /**
     * Test url invalid.
     */
    @Test
    void testUrlInvalid() {
        final MappingCommand mappingCommand = getTestMappingCommand(VALIDCDU_ID, INVALIDURL_ID);
        final BindingResult errors = new BeanPropertyBindingResult(mappingCommand, MAPPING_COMMAND);
        final CduDto cdu = getTestCdu(mappingCommand.getCduId());

        // Define a mock version of the called methods
        expectCduAndUrlServiceCalls(cdu);

        // Perform the test
        classUnderTest.validate(mappingCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduService);
        verify(mockUrlService);
    }

    /**
     * Test url null.
     */
    @Test
    void testUrlNull() {
        final MappingCommand mappingCommand = getTestMappingCommand(VALIDCDU_ID, null);
        final BindingResult errors = new BeanPropertyBindingResult(mappingCommand, MAPPING_COMMAND);
        final CduDto cdu = getTestCdu(VALIDCDU_ID);

        // Define a mock version of the called methods
        expectCduAndUrlServiceCalls(cdu);

        // Perform the test
        classUnderTest.validate(mappingCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduService);
        verify(mockUrlService);
    }
    
    private void expectCduAndUrlServiceCalls(CduDto cdu) {
        expect(mockCduService.getCduByCduId(cdu.getId().intValue())).andReturn(cdu);
        expect(mockUrlService.getUrlsByXhibitCourtSiteId(cdu.getCourtSiteId())).andReturn(getTestUrls());
        replay(mockCduService);
        replay(mockUrlService);
    }
}
