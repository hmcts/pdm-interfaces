package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;

import java.util.List;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class CduMacAddressTest.
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
abstract class CduMacAddressTest extends CduRegistrationTest {

    /**
     * Test is cdu with mac address Success.
     *
     * 
     */
    @Test
    void testIsCduWithMacAddressSuccess() {
        // Capture the macAddress
        final Capture<String> capturedMacAddress = newCapture();

        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCduRepo.isCduWithMacAddress(capture(capturedMacAddress))).andReturn(true);
        
        replay(mockCduRepo);
        replay(mockEntityManager);

        // Perform the test
        final boolean result = classUnderTest.isCduWithMacAddress(cdus.get(0).getMacAddress());

        // Assert that the objects are as expected
        assertEquals(result, true, NOT_EQUAL);
        assertEquals(capturedMacAddress.getValue(), cdus.get(0).getMacAddress(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockEntityManager);
    }

    /**
     * Test is cdu with mac address Failure.
     *
     *
     */
    @Test
    void testIsCduWithMacAddressFailure() {
        // Capture the macAddress
        final Capture<String> capturedMacAddress = newCapture();

        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCduRepo.isCduWithMacAddress(capture(capturedMacAddress))).andReturn(false);
        
        replay(mockCduRepo);
        replay(mockEntityManager);

        // Perform the test
        final boolean result = classUnderTest.isCduWithMacAddress(cdus.get(0).getMacAddress());

        // Assert that the objects are as expected
        assertEquals(result, false, NOT_EQUAL);
        assertEquals(capturedMacAddress.getValue(), cdus.get(0).getMacAddress(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockEntityManager);
    }

    /**
     * Test is cdu with cdu number.
     */
    @Test
    void testGetCduByMacAddressValid() {
        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCduRepo.findByMacAddress(cdus.get(0).getMacAddress())).andReturn(cdus.get(0));
        
        replay(mockCduRepo);
        replay(mockEntityManager);

        // Perform the test
        final CduDto result = classUnderTest.getCduByMacAddress(cdus.get(0).getMacAddress());

        // Assert that the objects are as expected
        assertNotNull(result, NULL);
        assertEquals(result.getId(), cdus.get(0).getId(), NOT_EQUAL);
        assertEquals(result.getMacAddress(), cdus.get(0).getMacAddress(), NOT_EQUAL);
        assertEquals(result.getCduNumber(), cdus.get(0).getCduNumber(), NOT_EQUAL);
        assertEquals(result.getLocation(), cdus.get(0).getLocation(), NOT_EQUAL);
        assertEquals(result.getIpAddress(), cdus.get(0).getIpAddress(), NOT_EQUAL);
        assertEquals(result.getXhibitCourtSiteId(),
            cdus.get(0).getCourtSite().getXhibitCourtSite().getId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockEntityManager);
    }

    /**
     * Test get cdu by mac address with like.
     */
    @Test
    void testGetCduByMacAddressWithLikeValid() {
        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCduRepo.findByMacAddressWithLike(MACADDRESS)).andReturn(cdus).anyTimes();
        
        replay(mockCduRepo);
        replay(mockEntityManager);

        // Perform the test
        final List<CduDto> results = classUnderTest.getCduByMacAddressWithLike(MACADDRESS);

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results.size(), CDU_IDS.length, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockEntityManager);
    }

}
