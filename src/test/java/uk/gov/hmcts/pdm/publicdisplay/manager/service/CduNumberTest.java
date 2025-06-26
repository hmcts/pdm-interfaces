package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class CduNumberTest.
 */
@SuppressWarnings("PMD")
abstract class CduNumberTest extends CduMappingTest {

    /**
     * Test is cdu with cdu number success.
     *
     */
    @Test
    void testIsCduWithCduNumberSuccess() {
        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCduRepo.isCduWithCduNumber(cdus.get(0).getCduNumber())).andReturn(true);
        
        replay(mockCduRepo);
        replay(mockEntityManager);

        // Perform the test
        final boolean result = classUnderTest.isCduWithCduNumber(cdus.get(0).getCduNumber());

        // Assert that the objects are as expected
        assertEquals(true, result, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockEntityManager);
    }

    /**
     * Test is cdu with cdu number failure.
     *
     */
    @Test
    void testIsCduWithCduNumberFailure() {
        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCduRepo.isCduWithCduNumber(cdus.get(0).getCduNumber())).andReturn(false);
        
        replay(mockCduRepo);
        replay(mockEntityManager);

        // Perform the test
        final boolean result = classUnderTest.isCduWithCduNumber(cdus.get(0).getCduNumber());

        // Assert that the objects are as expected
        assertEquals(false, result, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockEntityManager);
    }

}
