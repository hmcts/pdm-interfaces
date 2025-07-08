/*
 * Copyrights and Licenses
 * 
 * Copyright (c) 2015-2016 by the Ministry of Justice. All rights reserved. Redistribution and use
 * in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer. - Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution. - Products derived
 * from this software may not be called "XHIBIT Public Display Manager" nor may
 * "XHIBIT Public Display Manager" appear in their names without prior written permission of the
 * Ministry of Justice. - Redistributions of any form whatsoever must retain the following
 * acknowledgment: "This product includes XHIBIT Public Display Manager." This software is provided
 * "as is" and any expressed or implied warranties, including, but not limited to, the implied
 * warranties of merchantability and fitness for a particular purpose are disclaimed. In no event
 * shall the Ministry of Justice or its contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of
 * substitute goods or services; loss of use, data, or profits; or business interruption). However
 * caused any on any theory of liability, whether in contract, strict liability, or tort (including
 * negligence or otherwise) arising in any way out of the use of this software, even if advised of
 * the possibility of such damage.
 */

package uk.gov.hmcts.pdm.publicdisplay.manager.web.software;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.NoHandlerFoundException;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.SoftwareUpdateService;

import java.io.File;
import javax.servlet.http.HttpServletResponse;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class SoftwareControllerTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
class SoftwareControllerTest extends AbstractJUnit {

    /** The Constant FILENAME. */
    private static final String FILENAME = "FILENAME";

    private static final String NOT_EQUAL = "Not equal";

    private static final String NULL = "Null";

    /** The mock software update service. */
    private SoftwareUpdateService mockSoftwareUpdateService;

    /** The class under test. */
    private SoftwareController classUnderTest;

    /** The test http response. */
    private HttpServletResponse testHttpResponse;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new SoftwareController();

        // Setup the mock version of the called classes
        mockSoftwareUpdateService = createMock(SoftwareUpdateService.class);
        testHttpResponse = createMock(HttpServletResponse.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "softwareUpdateService",
            mockSoftwareUpdateService);
    }

    /**
     * Test checksum for a file that exists.
     *
     * @throws Exception the exception
     */
    @Test
    void testChecksum() throws Exception {
        // Local variables
        final String testCheckSum = "CHECKSUM";

        // Add the mock calls to child classes
        expect(mockSoftwareUpdateService.getChecksum(FILENAME)).andReturn(testCheckSum);
        replay(mockSoftwareUpdateService);

        // Perform the test
        final String result = classUnderTest.checksum(FILENAME, testHttpResponse);

        // Assert that the objects are as expected
        assertNotNull(result, NULL);
        assertEquals(result, testCheckSum, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockSoftwareUpdateService);
    }

    /**
     * Test checksum for a file that does not exist in the file structure.
     *
     * @throws Exception the exception
     */
    @Test
    void testChecksumNullError() throws Exception {
        // Add the mock calls to child classes
        expect(mockSoftwareUpdateService.getChecksum(FILENAME)).andReturn(null);
        replay(mockSoftwareUpdateService);

        try {
            // Perform the test
            classUnderTest.checksum(FILENAME, testHttpResponse);
        } catch (Exception e) {
            // TODO Fix this NoSuchRequestHandlingMethodException below
            // assertTrue(e instanceof NoSuchRequestHandlingMethodException);
            assertEquals(NoHandlerFoundException.class, e.getClass(), NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockSoftwareUpdateService);
        }
    }

    /**
     * Test download.
     *
     * @throws Exception the exception
     */
    @Test
    void testDownload() throws Exception {
        // Local variables
        final File testFile = new File(FILENAME);

        // Add the mock calls to child classes
        expect(mockSoftwareUpdateService.getFile(FILENAME, false)).andReturn(testFile);
        replay(mockSoftwareUpdateService);

        // Perform the test
        final ResponseEntity<FileSystemResource> result = classUnderTest.download(FILENAME);

        assertNotNull(result, NULL);
        assertEquals(testFile, result.getBody().getFile(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockSoftwareUpdateService);
    }

    /**
     * Test download with a no file error.
     *
     * @throws Exception the exception
     */
    @Test
    void testDownloadNoFileError() throws Exception {
        // Add the mock calls to child classes
        expect(mockSoftwareUpdateService.getFile(FILENAME, false)).andReturn(null);
        replay(mockSoftwareUpdateService);

        try {
            // Perform the test
            classUnderTest.download(FILENAME);
        } catch (Exception e) {
            // TODO Fix this NoSuchRequestHandlingMethodException below
            // assertTrue(e instanceof NoSuchRequestHandlingMethodException);
            assertEquals(NoHandlerFoundException.class, e.getClass(), NOT_EQUAL);
        } finally {
            // Verify the expected mocks were called
            verify(mockSoftwareUpdateService);
        }
    }
}
