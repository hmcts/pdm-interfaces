package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

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
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand).times(2);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        replay(mockCduPageStateHolder);
        expect(mockCduSearchSelectedValidator.isValid(cduSearchCommand)).andReturn(false);
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
        // Add the mock calls to child classes
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(null);
        expect(mockCduPageStateHolder.getCdu()).andReturn(null);
        expectLastCall();
        replay(mockCduPageStateHolder);

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
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand).times(2);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(2);
        replay(mockCduPageStateHolder);
        
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException(MOCK_DATA_EXCEPTION);
        
        expect(mockCduService.getCduScreenshot(cdu)).andThrow(dataRetrievalFailureException);
        replay(mockCduService);
        expect(mockCduSearchSelectedValidator.isValid(cduSearchCommand)).andReturn(true);
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
        expect(mockCduPageStateHolder.getCduSearchCommand()).andReturn(cduSearchCommand).times(2);
        expect(mockCduPageStateHolder.getCdu()).andReturn(cdu);
        expectLastCall().times(2);
        replay(mockCduPageStateHolder);
        
        XpdmException xpdmException = new XpdmException(MOCK_RUNTIME_EXCEPTION);
        
        expect(mockCduService.getCduScreenshot(cdu)).andThrow(xpdmException);
        replay(mockCduService);
        expect(mockCduSearchSelectedValidator.isValid(cduSearchCommand)).andReturn(true);
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
