package uk.gov.hmcts.pdm.publicdisplay.common.rest;

import org.apache.http.HttpStatus;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CourtSiteJson;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class JsonRequestSuccessTest.
 *
 */
@SuppressWarnings({"PMD"})
@ExtendWith(EasyMockExtension.class)
class JsonRequestSuccessTest extends JsonRequestErrorTest {

    /**
     * Test get string success.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetStringSuccess() throws Exception {
        // Setup mock response
        mockJsonEndpoint.setResponseBody(TEST_RESPONSE_BODY);
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_OK);

        // Perform test
        final String responseBody = classUnderTest.sendRequest(String.class);

        // Validate json request
        assertNotNull(classUnderTest.getMessageId(), NULL);
        assertEquals(JsonRequest.STATUS_SUCCESS, classUnderTest.getStatus(), NOT_EQUAL);
        assertNull(classUnderTest.getRequestText(), NOT_NULL);
        assertEquals(TEST_RESPONSE_BODY, classUnderTest.getResponseText(), NOT_EQUAL);

        // Validate request sent
        assertNull(mockJsonEndpoint.getRequestBody(String.class), NOT_NULL);

        // Validate response received
        assertEquals(TEST_RESPONSE_BODY, responseBody, NOT_EQUAL);
    }

    /**
     * Test get json success.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetJsonSuccess() throws Exception {
        // Setup mock response
        final CourtSiteJson responseJson = getTestResponseJson();
        mockJsonEndpoint.setResponseBody(responseJson);
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_OK);

        // Perform test
        final CourtSiteJson responseBody = classUnderTest.sendRequest(CourtSiteJson.class);

        // Validate json request
        assertNotNull(classUnderTest.getMessageId(), NULL);
        assertEquals(JsonRequest.STATUS_SUCCESS, classUnderTest.getStatus(), NOT_EQUAL);
        assertNull(classUnderTest.getRequestText(), NOT_NULL);
        assertEquals(mockJsonEndpoint.getResponseBody(), classUnderTest.getResponseText(),
            NOT_EQUAL);

        // Validate request sent
        assertNull(mockJsonEndpoint.getRequestBody(String.class), NOT_NULL);

        // Validate response received
        assertEquals(responseJson.getGeneratedBy(), responseBody.getGeneratedBy(), NOT_EQUAL);
        assertEquals(responseJson.getSiteId(), responseBody.getSiteId(), NOT_EQUAL);
    }

    /**
     * Test get string success.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetBytesSuccess() throws Exception {
        // Generate random byte response
        final byte[] responseBytes = new byte[100];
        new Random().nextBytes(responseBytes);

        // Setup mock response
        mockJsonEndpoint.setResponseBody(responseBytes);
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_OK);

        // Perform test
        final byte[] responseBody = classUnderTest.sendRequest(byte[].class);

        // Validate json request
        assertNotNull(classUnderTest.getMessageId(), NULL);
        assertEquals(JsonRequest.STATUS_SUCCESS, classUnderTest.getStatus(), NOT_EQUAL);
        assertNull(classUnderTest.getRequestText(), NOT_NULL);
        assertNull(classUnderTest.getResponseText(), NOT_NULL);

        // Validate request sent
        assertNull(mockJsonEndpoint.getRequestBody(String.class), NOT_NULL);

        // Validate response received
        assertTrue(Arrays.equals(responseBytes, responseBody), FALSE);
    }

    /**
     * Test post and get string success.
     *
     * @throws Exception the exception
     */
    @Test
    void testPostStringGetStringSuccess() throws Exception {
        // Setup mock response
        mockJsonEndpoint.setResponseBody(TEST_RESPONSE_BODY);
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_OK);

        // Perform test
        final String responseBody = classUnderTest.sendRequest(TEST_REQUEST_BODY, String.class);

        // Validate json request
        assertNotNull(classUnderTest.getMessageId(), NULL);
        assertEquals(JsonRequest.STATUS_SUCCESS, classUnderTest.getStatus(), NOT_EQUAL);
        assertEquals(TEST_REQUEST_BODY, classUnderTest.getRequestText(), NOT_EQUAL);
        assertEquals(TEST_RESPONSE_BODY, classUnderTest.getResponseText(), NOT_EQUAL);

        // Validate request sent
        assertEquals(TEST_REQUEST_BODY, mockJsonEndpoint.getRequestBody(String.class), NOT_EQUAL);

        // Validate response received
        assertEquals(TEST_RESPONSE_BODY, responseBody, NOT_EQUAL);
    }

    /**
     * Test post and get json success.
     *
     * @throws Exception the exception
     */
    @Test
    void testPostJsonGetJsonSuccess() throws Exception {
        // Setup request and mock response
        final CduJson requestJson = getTestRequestJson();
        final CourtSiteJson responseJson = getTestResponseJson();
        mockJsonEndpoint.setResponseBody(responseJson);
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_OK);

        // Perform test
        final CourtSiteJson responseBody =
            classUnderTest.sendRequest(requestJson, CourtSiteJson.class);

        // Validate json request
        assertNotNull(classUnderTest.getMessageId(), NULL);
        assertEquals(JsonRequest.STATUS_SUCCESS, classUnderTest.getStatus(), NOT_EQUAL);
        assertEquals(mockJsonEndpoint.getRequestBody(String.class), classUnderTest.getRequestText(),
            NOT_EQUAL);
        assertEquals(mockJsonEndpoint.getResponseBody(), classUnderTest.getResponseText(),
            NOT_EQUAL);

        // Validate request sent
        final CduJson requestBody = mockJsonEndpoint.getRequestBody(CduJson.class);
        assertEquals(requestJson.getCduNumber(), requestBody.getCduNumber(), NOT_EQUAL);
        assertEquals(requestJson.getRagStatus(), requestBody.getRagStatus(), NOT_EQUAL);
        assertEquals(requestJson.getRefresh(), requestBody.getRefresh(), NOT_EQUAL);

        // Validate response received
        assertEquals(responseJson.getGeneratedBy(), responseBody.getGeneratedBy(), NOT_EQUAL);
        assertEquals(responseJson.getSiteId(), responseBody.getSiteId(), NOT_EQUAL);
    }

    /**
     * Test post json and get bytes success.
     *
     * @throws Exception the exception
     */
    @Test
    void testPostJsonGetBytesSuccess() throws Exception {
        // Generate random byte response
        final byte[] responseBytes = new byte[100];
        new Random().nextBytes(responseBytes);

        // Setup request and mock response
        final CduJson requestJson = getTestRequestJson();
        mockJsonEndpoint.setResponseBody(responseBytes);
        mockJsonEndpoint.setResponseCode(HttpStatus.SC_OK);

        // Perform test
        final byte[] responseBody = classUnderTest.sendRequest(requestJson, byte[].class);

        // Validate json request
        assertNotNull(classUnderTest.getMessageId(), NULL);
        assertEquals(JsonRequest.STATUS_SUCCESS, classUnderTest.getStatus(), NOT_EQUAL);
        assertEquals(mockJsonEndpoint.getRequestBody(String.class), classUnderTest.getRequestText(),
            NOT_EQUAL);
        assertNull(classUnderTest.getResponseText(), NOT_NULL);

        // Validate request sent
        final CduJson requestBody = mockJsonEndpoint.getRequestBody(CduJson.class);
        assertEquals(requestJson.getCduNumber(), requestBody.getCduNumber(), NOT_EQUAL);
        assertEquals(requestJson.getRagStatus(), requestBody.getRagStatus(), NOT_EQUAL);
        assertEquals(requestJson.getRefresh(), requestBody.getRefresh(), NOT_EQUAL);

        // Validate response received
        assertTrue(Arrays.equals(responseBytes, responseBody), FALSE);
    }

    /**
     * Gets the test request json.
     *
     * @return the test request json
     */
    protected CduJson getTestRequestJson() {
        final CduJson json = new CduJson();
        json.setCduNumber("12345");
        json.setRagStatus('G');
        json.setRefresh(60L);
        return json;
    }

    /**
     * Gets the test response json.
     *
     * @return the test response json
     */
    protected CourtSiteJson getTestResponseJson() {
        final CourtSiteJson json = new CourtSiteJson();
        json.setGeneratedBy("user");
        json.setSiteId(1L);
        return json;
    }

}
