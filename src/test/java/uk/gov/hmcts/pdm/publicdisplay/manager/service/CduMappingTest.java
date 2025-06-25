package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.MappingCommand;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The Class CduMappingTest.
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class CduMappingTest extends CduMacAddressTest {

    /**
     * Test add mapping valid.
     */
    @Test
    void testAddMappingValid() {
        // Local Variables
        final MappingCommand mappingCommand = getTestMappingCommand(CDU_IDS[1], URL_IDS[2]);

        // Capture the cdu
        final Capture<ICduModel> capturedCdu = newCapture();
        final Capture<IUrlModel> capturedUrl = newCapture();

        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockUrlRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockDispMgrMappingRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        
        expect(mockCduRepo.findByCduId(mappingCommand.getCduId().intValue()))
            .andReturn(cdus.get(1));
        expectLastCall();
        expect(mockUrlRepo.findByUrlId(mappingCommand.getUrlId().intValue()))
            .andReturn(urls.get(2));
        mockDispMgrMappingRepo.addMappingForCdu(capture(capturedCdu), capture(capturedUrl));
        mockLocalProxyRestClient.saveUrl(capture(capturedUrl));
        expectLastCall();
        mockLocalProxyRestClient.saveMapping(capture(capturedCdu), capture(capturedUrl));
        expectLastCall();
        
        replay(mockCduRepo);
        replay(mockUrlRepo);
        replay(mockDispMgrMappingRepo);
        replay(mockEntityManager);
        replay(mockLocalProxyRestClient);
        
        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.addMapping(mappingCommand);

        // Assert that the objects are as expected
        assertEquals(capturedCdu.getValue().getId(), mappingCommand.getCduId(), NOT_EQUAL);
        assertEquals(capturedUrl.getValue().getId(), mappingCommand.getUrlId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockUrlRepo);
        verify(mockDispMgrMappingRepo);
        verify(mockEntityManager);
        verify(mockLocalProxyRestClient);
    }

    /**
     * Test remove mapping valid.
     */
    @Test
    void testRemoveMappingValid() {
        // Local Variables
        final MappingCommand mappingCommand = getTestMappingCommand(CDU_IDS[1], URL_IDS[1]);

        // Capture the cdu
        final Capture<ICduModel> capturedCdu = newCapture();
        final Capture<IUrlModel> capturedUrl = newCapture();

        // Add the mock calls to child classes
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockUrlRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockDispMgrMappingRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCduRepo.findByCduId(mappingCommand.getCduId().intValue()))
            .andReturn(cdus.get(1));
        expectLastCall();
        expect(mockUrlRepo.findByUrlId(mappingCommand.getUrlId().intValue()))
            .andReturn(urls.get(1));
        mockDispMgrMappingRepo.deleteMappingForCdu(capture(capturedCdu), capture(capturedUrl));
        expectLastCall();
        mockLocalProxyRestClient.deleteMapping(capture(capturedCdu), capture(capturedUrl));
        expectLastCall();
        
        replay(mockCduRepo);
        replay(mockUrlRepo);
        replay(mockDispMgrMappingRepo);
        replay(mockEntityManager);
        replay(mockLocalProxyRestClient);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);

        // Perform the test
        classUnderTest.removeMapping(mappingCommand);

        // Assert that the objects are as expected
        assertEquals(capturedCdu.getValue().getId(), mappingCommand.getCduId(), NOT_EQUAL);
        assertEquals(capturedUrl.getValue().getId(), mappingCommand.getUrlId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
        verify(mockUrlRepo);
        verify(mockDispMgrMappingRepo);
        verify(mockEntityManager);
        verify(mockLocalProxyRestClient);
    }

    /**
     * Gets the test mapping command.
     *
     * @param cduId the cdu id
     * @param urlId the url id
     * @return the test mapping command
     */
    private MappingCommand getTestMappingCommand(final Long cduId, final Long urlId) {
        final MappingCommand mappingCommand = new MappingCommand();
        mappingCommand.setCduId(cduId);
        mappingCommand.setUrlId(urlId);
        return mappingCommand;
    }

}
