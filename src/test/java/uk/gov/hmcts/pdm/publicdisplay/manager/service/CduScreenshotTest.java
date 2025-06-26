package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class CduScreenshotTest.
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
abstract class CduScreenshotTest extends CduNumberTest {

    /** The cdu dtos. */
    protected final List<CduDto> cduDtos = getTestCduDtos();


    /**
     * Test get cdu screenshot.
     */
    @Test
    void testGetCduScreenshot() {
        // Local variables
        final byte[] cduScreenshot = getTestByteArray();
        final CduDto cduDto = cduDtos.get(0);
        final ICourtSite courtSite = cdus.get(0).getCourtSite();

        // Add the mock calls to child classes
        expect(mockDispMgrCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockDispMgrCourtSiteRepo.findByCourtSiteId(courtSite.getId().intValue()))
            .andReturn(courtSite);
        expect(mockLocalProxyRestClient.getCduScreenshot(courtSite.getLocalProxy(),
            cduDto.getIpAddress())).andReturn(cduScreenshot);
        
        replay(mockDispMgrCourtSiteRepo);
        replay(mockEntityManager);
        replay(mockLocalProxyRestClient);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        final byte[] result = classUnderTest.getCduScreenshot(cduDto);

        // Assert that the objects are as expected
        assertTrue(Arrays.equals(cduScreenshot, result), FALSE);

        // Verify the expected mocks were called
        verify(mockDispMgrCourtSiteRepo);
        verify(mockEntityManager);
        verify(mockLocalProxyRestClient);
    }

    @Test
    void testGerUsername() {
        LocalProxyRestCduFinder localClassUnderTest = new LocalProxyRestCduFinder() {
            @Override
            public String getUsername() {
                return super.getUsername();
            }
        };
        
        String result = localClassUnderTest.getUsername();
        assertNotNull(result, NULL);
    }
    
    /**
     * Gets the test byte array.
     *
     * @return the test byte array
     */
    private byte[] getTestByteArray() {
        final byte[] bytes = new byte[100];
        new Random().nextBytes(bytes);
        return bytes;
    }

}
