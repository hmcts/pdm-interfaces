package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * The Class CduScreenshotTest.
 *
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
abstract class CduScreenshotTest extends RestartCduTest {
    /**
     * Test get cdu screenshot invalid.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetCduScreenshotInvalid() throws Exception {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();

        // Add the mock calls to child classes
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand).anyTimes();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus).anyTimes();
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall().anyTimes();
        mockCduPageStateHolder.setCdus(cdus);
        expectLastCall().anyTimes();
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        mockCduPageStateHolder.setSites(sites);
        expectLastCall().anyTimes();
        mockCduPageStateHolder.setCduSearchCommand(EasyMock.isA(CduSearchCommand.class));
        expectLastCall().anyTimes();
        expect(mockCduService.getCduByMacAddressWithLike(EasyMock.isA(String.class))).andReturn(cdus);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        expect(mockCduSearchSelectedValidator.isValid(cduSearchCommand)).andReturn(false);
        
        replay(mockCduPageStateHolder);
        replay(mockLocalProxyService);
        replay(mockCduService);
        replay(mockCduSearchSelectedValidator);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(viewNameCduScreenshot)).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results.getResolvedException(), NULL);
        // TODO Fix this NoSuchRequestHandlingMethodException below
        // assertTrue(results.getResolvedException() instanceof
        // NoSuchRequestHandlingMethodException);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduSearchSelectedValidator);
    }

    /**
     * Test get cdu screenshot null cdu error.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetCduScreenshotNullCduError() throws Exception {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();
        // Add the mock calls to child classes
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand).anyTimes();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus).anyTimes();
        expect(mockCduService.getCduByMacAddressWithLike(EasyMock.isA(String.class))).andReturn(cdus);
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall().anyTimes();
        mockCduPageStateHolder.setCdus(cdus);
        expectLastCall().anyTimes();
        expect(mockCduPageStateHolder.getCdu()).andReturn(null).anyTimes();
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        mockCduPageStateHolder.setSites(sites);
        expectLastCall().anyTimes();
        mockCduPageStateHolder.setCduSearchCommand(EasyMock.isA(CduSearchCommand.class));
        expectLastCall().anyTimes();
        
        replay(mockLocalProxyService);
        replay(mockCduPageStateHolder);
        replay(mockCduService);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(viewNameCduScreenshot)).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertNotNull(results.getResolvedException(), NULL);
        // TODO Fix this NoSuchRequestHandlingMethodException below
        // assertTrue(results.getResolvedException() instanceof
        // NoSuchRequestHandlingMethodException);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
    }

    /**
     * Test get cdu screenshot error.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetCduScreenshotError() throws Exception {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();
        // Add the mock calls to child classes
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand).anyTimes();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus).anyTimes();
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall().anyTimes();
        mockCduPageStateHolder.setCdus(cdus);
        expectLastCall().anyTimes();
        expect(mockCduService.getCduByMacAddressWithLike(EasyMock.isA(String.class))).andReturn(cdus);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        expect(mockCduService.getCduScreenshot(cdu)).andThrow(dataRetrievalFailureException);
        expect(mockCduSearchSelectedValidator.isValid(cduSearchCommand)).andReturn(true);
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        mockCduPageStateHolder.setSites(sites);
        expectLastCall().anyTimes();
        mockCduPageStateHolder.setCduSearchCommand(EasyMock.isA(CduSearchCommand.class));
        expectLastCall().anyTimes();
        
        replay(mockLocalProxyService);
        replay(mockCduPageStateHolder);
        replay(mockCduService);
        replay(mockCduSearchSelectedValidator);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(viewNameCduScreenshot)).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertNotNull(results.getResolvedException(), NULL);
        // TODO Fix this NoSuchRequestHandlingMethodException below
        // assertTrue(results.getResolvedException() instanceof
        // NoSuchRequestHandlingMethodException);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduService);
        verify(mockCduSearchSelectedValidator);
    }

    /**
     * Test get cdu screenshot runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetCduScreenshotRuntimeError() throws Exception {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand();

        // Add the mock calls to child classes
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand).anyTimes();
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus).anyTimes();
        mockCduPageStateHolder.setCdu(cdu);
        expectLastCall().anyTimes();
        mockCduPageStateHolder.setCdus(cdus);
        expectLastCall().anyTimes();
        expect(mockCduService.getCduByMacAddressWithLike(EasyMock.isA(String.class))).andReturn(cdus);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu).anyTimes();
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        expect(mockCduService.getCduScreenshot(cdu)).andThrow(xpdmException);
        expect(mockCduSearchSelectedValidator.isValid(cduSearchCommand)).andReturn(true);
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        mockCduPageStateHolder.setSites(sites);
        expectLastCall().anyTimes();
        mockCduPageStateHolder.setCduSearchCommand(EasyMock.isA(CduSearchCommand.class));
        expectLastCall().anyTimes();
        
        replay(mockLocalProxyService);
        replay(mockCduPageStateHolder);
        replay(mockCduService);
        replay(mockCduSearchSelectedValidator);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(viewNameCduScreenshot)).andReturn();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertNotNull(results.getResolvedException(), NULL);
        // TODO Fix this NoSuchRequestHandlingMethodException below
        // assertTrue(results.getResolvedException() instanceof
        // NoSuchRequestHandlingMethodException);

        // Verify the expected mocks were called
        verify(mockCduPageStateHolder);
        verify(mockCduService);
        verify(mockCduSearchSelectedValidator);
    }
}
