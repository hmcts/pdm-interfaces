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

package uk.gov.hmcts.pdm.publicdisplay.common.rest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.json.ErrorJson;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractHttpClientTest;
import uk.gov.hmcts.pdm.publicdisplay.common.test.MockJsonEndpoint;
import uk.gov.hmcts.pdm.publicdisplay.common.util.ObjectMapperUtil;

import java.io.IOException;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class JsonRequestErrorTest.
 *
 * @author uphillj
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class JsonRequestErrorTest extends AbstractHttpClientTest {

    /** The mock endpoint url. */
    protected static final String ENDPOINT_URL = "/test";

    /** The Constant TEST_RESPONSE_BODY. */
    protected static final String TEST_REQUEST_BODY = "test request";

    /** The Constant TEST_RESPONSE_BODY. */
    protected static final String TEST_RESPONSE_BODY = "test response";

    /** The Constant TEST_ERROR_MESSAGE. */
    protected static final String TEST_ERROR_MESSAGE = "test error message";

    /** The Constant TEST_ERROR_DETAIL. */
    protected static final String TEST_ERROR_DETAIL = "test error detail";

    /** The Constant INTERNAL_SERVER_ERROR_MESSAGE. */
    protected static final String INTERNAL_SERVER_ERROR_MESSAGE =
        "status code: 500, reason phrase: Internal Server Error";

    protected static final String FALSE = "False";

    protected static final String NULL = "Null";

    protected static final String NOT_EQUAL = "Not equal";

    protected static final String NOT_NULL = "Not null";

    /** The class under test. */
    protected JsonRequest classUnderTest;

    /** The mock json endpoint. */
    protected MockJsonEndpoint mockJsonEndpoint;

    /**
     * Setup.
     *
     * @throws Exception the exception
     */
    @BeforeEach
    public void setup() {
        // Get the port for the test
        int port = getPort();
        String baseUrl = "http://" + IP_ADDRESS + ":" + port;
        // Create a new version of the class under test
        classUnderTest = new JsonRequest(new ObjectMapperUtil());
        classUnderTest.setTokenType(JsonWebTokenType.DISPLAY_MANAGER);
        classUnderTest.setUrl(baseUrl + ENDPOINT_URL);

        // Start test http server which will receive test
        // requests and return back the test responses
        mockJsonEndpoint = new MockJsonEndpoint();
        try {
            startServer(port, ENDPOINT_URL, mockJsonEndpoint);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Destroy.
     *
     * @throws Exception the exception
     */
    @AfterEach
    public void teardown() {
        try {
            stopServer();
        } catch (InterruptedException | IOException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test json web token.
     *
     * @throws Exception the exception
     */
    @Test
    void testJsonWebToken() throws Exception {
        // Setup mock response
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_OK);

        // Perform test
        classUnderTest.sendRequest();

        // Validate json web token received
        final String auth =
            mockJsonEndpoint.getRequestHeader(JsonWebTokenUtility.REQUEST_HEADER_AUTHORIZATION);
        assertTrue(JsonWebTokenUtility.INSTANCE.isTokenValid(JsonWebTokenType.DISPLAY_MANAGER,
            auth.substring(JsonWebTokenUtility.REQUEST_HEADER_BEARER.length() + 1)), FALSE);
    }

    /**
     * Test get error response.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetStringErrorResponse() throws Exception {
        // Setup mock response
        mockJsonEndpoint.setResponseBody(getTestError());
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        // Perform test
        String responseBody = null;
        try {
            responseBody = classUnderTest.sendRequest(String.class);
        } catch (final RestException ex) {
            // Validate exception message
            assertEquals(TEST_ERROR_MESSAGE, ex.getMessage(), NOT_EQUAL);
        } finally {
            // Validate json request
            assertNotNull(classUnderTest.getMessageId(), NULL);
            assertEquals(JsonRequest.STATUS_FAILED, classUnderTest.getStatus(), NOT_EQUAL);
            assertNull(classUnderTest.getRequestText(), NOT_NULL);
            assertEquals(mockJsonEndpoint.getResponseBody(), classUnderTest.getResponseText(),
                NOT_EQUAL);

            // Validate request sent
            assertNull(mockJsonEndpoint.getRequestBody(String.class), NOT_NULL);

            // Validate response received
            assertNull(responseBody, NOT_NULL);
        }
    }

    /**
     * Test get error no response.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetStringErrorNoResponse() throws Exception {
        // Setup mock response
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        // Perform test
        String responseBody = null;
        try {
            responseBody = classUnderTest.sendRequest(String.class);
        } catch (final RestException ex) {
            // Validate exception message
            assertEquals(INTERNAL_SERVER_ERROR_MESSAGE, ex.getMessage(), NOT_EQUAL);
            final String responseText = classUnderTest.getResponseText();
            final String exceptionCause = responseText.substring(0, responseText.indexOf('\n'));
            assertTrue(ExceptionUtils.getStackTrace(ex).contains(exceptionCause), FALSE);
        } finally {
            // Validate json request
            assertNotNull(classUnderTest.getMessageId(), NULL);
            assertEquals(JsonRequest.STATUS_FAILED, classUnderTest.getStatus(), NOT_EQUAL);
            assertNull(classUnderTest.getRequestText(), NOT_NULL);

            // Validate request sent
            assertNull(mockJsonEndpoint.getRequestBody(String.class), NOT_NULL);

            // Validate response received
            assertNull(responseBody, NOT_NULL);
        }
    }

    /**
     * Test post and get error response.
     *
     * @throws Exception the exception
     */
    @Test
    void testPostStringGetStringErrorResponse() throws Exception {
        // Setup mock response
        mockJsonEndpoint.setResponseBody(getTestError());
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        // Perform test
        String responseBody = null;
        try {
            responseBody = classUnderTest.sendRequest(TEST_REQUEST_BODY, String.class);
        } catch (final RestException ex) {
            // Validate exception message
            assertEquals(TEST_ERROR_MESSAGE, ex.getMessage(), NOT_EQUAL);
        } finally {
            // Validate json request
            assertNotNull(classUnderTest.getMessageId(), NULL);
            assertEquals(JsonRequest.STATUS_FAILED, classUnderTest.getStatus(), NOT_EQUAL);
            assertEquals(TEST_REQUEST_BODY, classUnderTest.getRequestText(), NOT_EQUAL);
            assertEquals(mockJsonEndpoint.getResponseBody(), classUnderTest.getResponseText(),
                NOT_EQUAL);

            // Validate request sent
            assertEquals(TEST_REQUEST_BODY, mockJsonEndpoint.getRequestBody(String.class),
                NOT_EQUAL);

            // Validate response received
            assertNull(responseBody, NOT_NULL);
        }
    }

    /**
     * Test post and get error no response.
     *
     * @throws Exception the exception
     */
    @Test
    void testPostStringGetStringErrorNoResponse() throws Exception {
        // Setup mock response
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        // Perform test
        String responseBody = null;
        try {
            responseBody = classUnderTest.sendRequest(TEST_REQUEST_BODY, String.class);
        } catch (final RestException ex) {
            // Validate exception message
            assertEquals(INTERNAL_SERVER_ERROR_MESSAGE, ex.getMessage(), NOT_EQUAL);
            final String responseText = classUnderTest.getResponseText();
            final String exceptionCause = responseText.substring(0, responseText.indexOf('\n'));
            assertTrue(ExceptionUtils.getStackTrace(ex).contains(exceptionCause), FALSE);
        } finally {
            // Validate json request
            assertNotNull(classUnderTest.getMessageId(), NULL);
            assertEquals(JsonRequest.STATUS_FAILED, classUnderTest.getStatus(), NOT_EQUAL);
            assertEquals(TEST_REQUEST_BODY, classUnderTest.getRequestText(), NOT_EQUAL);

            // Validate request sent
            assertEquals(TEST_REQUEST_BODY, mockJsonEndpoint.getRequestBody(String.class),
                NOT_EQUAL);

            // Validate response received
            assertNull(responseBody, NOT_NULL);
        }
    }

    /**
     * Test http connection timeout.
     *
     * @throws Exception the exception
     */
    @Test
    void testHttpConnectionTimeout() throws Exception {
        // Create timeout by stopping server
        classUnderTest.setTimeout(1);
        stopServer();

        // Perform the test
        try {
            classUnderTest.sendRequest();
        } catch (final RestException ex) {
            // Validate exception message and stack trace is in the response text
            assertTrue(ex.getMessage().contains(IP_ADDRESS), FALSE);
            final String responseText = classUnderTest.getResponseText();
            final String exceptionCause = responseText.substring(0, responseText.indexOf('\n'));
            assertTrue(ExceptionUtils.getStackTrace(ex).contains(exceptionCause), FALSE);
        } finally {
            // Validate json request
            assertNotNull(classUnderTest.getMessageId(), NULL);
            assertEquals(JsonRequest.STATUS_FAILED, classUnderTest.getStatus(), NOT_EQUAL);
            assertNull(classUnderTest.getRequestText(), NOT_NULL);
        }
    }

    /**
     * Gets the test error.
     *
     * @return the test error
     */
    protected ErrorJson getTestError() {
        final ErrorJson error = new ErrorJson();
        error.setMessage(TEST_ERROR_MESSAGE);
        error.setDetail(TEST_ERROR_DETAIL);
        return error;
    }
}
