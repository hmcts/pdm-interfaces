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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UrlDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IUrlService;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class MappingAddValidatorTest.
 *
 * @author pattersone
 */
@ExtendWith(EasyMockExtension.class)
abstract class MappingAddValidatorTest extends AbstractJUnit {

    /** The Constant VALIDCDU_ID. */
    protected static final long VALIDCDU_ID = 1L;

    /** The Constant VALIDURL_ID. */
    protected static final long VALIDURL_ID = 1L;

    /** The Constant INVALIDCDU_ID. */
    protected static final long INVALIDCDU_ID = 3L;

    /** The Constant INVALIDURL_ID. */
    protected static final long INVALIDURL_ID = 3L;

    protected static final String NOT_EQUAL = "Not equal";

    protected static final String NULL = "Null";

    protected static final String TRUE = "True";

    protected static final String FALSE = "False";

    protected static final String MAPPING_COMMAND = "mappingCommand";

    /** The class under test. */
    protected MappingAddValidator classUnderTest;

    /** The cdu service. */
    protected ICduService mockCduService;
    
    /** The url service. */
    protected IUrlService mockUrlService;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new MappingAddValidator();

        // Setup the mock version of the called classes
        mockCduService = createMock(ICduService.class);
        mockUrlService = createMock(IUrlService.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "cduService", mockCduService);
        ReflectionTestUtils.setField(classUnderTest, "urlService", mockUrlService);
    }

    /**
     * Test supports.
     */
    @Test
    void testSupports() {
        final boolean result = classUnderTest.supports(MappingCommand.class);
        assertTrue(result, FALSE);
    }

    /**
     * Test cdu invalid.
     */
    @Test
    void testCduInvalid() {
        final MappingCommand mappingCommand = getTestMappingCommand(INVALIDCDU_ID, VALIDURL_ID);
        final BindingResult errors = new BeanPropertyBindingResult(mappingCommand, MAPPING_COMMAND);
        final CduDto cdu = getTestCdu(VALIDCDU_ID);
        
        // Define a mock version of the called methods
        expect(mockCduService.getCduByCduId(3)).andReturn(cdu);

        replay(mockCduService);
        
        // Perform the test
        classUnderTest.validate(mappingCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduService);
    }

    /**
     * Test cdu null.
     */
    @Test
    void testCduNull() {
        final MappingCommand mappingCommand = getTestMappingCommand(null, VALIDURL_ID);
        final BindingResult errors = new BeanPropertyBindingResult(mappingCommand, MAPPING_COMMAND);

        // Perform the test
        classUnderTest.validate(mappingCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);
    }

    /**
     * Gets the test mapping command.
     *
     * @param cduId the cdu id
     * @param urlId the url id
     * @return the test mapping command
     */
    protected MappingCommand getTestMappingCommand(final Long cduId, final Long urlId) {
        MappingCommand mappingCommand;
        mappingCommand = new MappingCommand();
        mappingCommand.setCduId(cduId);
        mappingCommand.setUrlId(urlId);
        return mappingCommand;
    }

    /**
     * Gets the test cdu.
     *
     * @param cduId the cdu id
     * @return the test cdu
     */
    protected CduDto getTestCdu(final Long cduId) {
        CduDto cdu;
        cdu = new CduDto();
        cdu.setId(cduId);
        return cdu;
    }

    /**
     * Gets the test url.
     *
     * @param urlId the url id
     * @return the test url
     */
    protected UrlDto getTestUrl(final Long urlId) {
        final UrlDto url = new UrlDto();
        url.setId(urlId);
        url.setUrl("url" + urlId.toString());
        return url;
    }

    /**
     * Gets the test urls.
     *
     * @return the test urls
     */
    protected List<UrlDto> getTestUrls() {
        List<UrlDto> urls;
        urls = new ArrayList<>();
        urls.add(getTestUrl(1L));
        urls.add(getTestUrl(2L));
        return urls;
    }

}
