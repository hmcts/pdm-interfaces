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

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class CduSearchValidatorTest.
 *
 * @author boparaij
 */
@ExtendWith(EasyMockExtension.class)
class MappingRemoveValidatorTest extends AbstractJUnit {
    /** The class under test. */
    private MappingRemoveValidator classUnderTest;

    /** The cdu service. */
    protected ICduService mockCduService;

    private static final String NOT_EQUAL = "Not equal";

    private static final String FALSE = "False";

    private static final String TRUE = "True";

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new MappingRemoveValidator();

        // Setup the mock version of the called classes
        mockCduService = createMock(ICduService.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "cduService", mockCduService);
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
     * Test valid remove mapping.
     */
    @Test
    void testRemoveMappingValid() {
        final MappingCommand mappingCommand = getTestMappingRemove(1L, 1L);
        final BindingResult errors =
            new BeanPropertyBindingResult(mappingCommand, "mappingCommand");
        final CduDto cdu = getTestCdu(1L, getTestUrls());

        // Define a mock version of the called methods
        expectCduServiceCalls(cdu);

        // Perform the test
        classUnderTest.validate(mappingCommand, errors);

        // Checking that the search criteria is valid
        assertFalse(errors.hasErrors(), TRUE);

        // Verify the mocks used in this method were called
        verify(mockCduService);
    }

    /**
     * Test cdu null.
     */
    @Test
    void testRemoveMappingCduNull() {
        final MappingCommand mappingCommand = getTestMappingRemove(null, 1L);
        final BindingResult errors =
            new BeanPropertyBindingResult(mappingCommand, "mappingCommand");

        // Perform the test
        classUnderTest.validate(mappingCommand, errors);

        // Checking that the search criteria is invalid (negative)
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);
    }

    /**
     * Test cdu null.
     */
    @Test
    void testRemoveMappingUrlNull() {
        final MappingCommand mappingCommand = getTestMappingRemove(1L, null);
        final BindingResult errors =
            new BeanPropertyBindingResult(mappingCommand, "mappingCommand");
        final CduDto cdu = getTestCdu(1L, getTestUrls());

        // Define a mock version of the called methods
        expectCduServiceCalls(cdu);

        // Perform the test
        classUnderTest.validate(mappingCommand, errors);

        // Checking that the search criteria is invalid (negative)
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduService);
    }

    
    private void expectCduServiceCalls(CduDto cdu) {
        expect(mockCduService.getCduByCduId(cdu.getId().intValue())).andReturn(cdu);
        replay(mockCduService);
    }
    
    /**
     * Gets the test mapping remove.
     *
     * @param cduId the cdu id
     * @param urlId the url id
     * @return the test mapping remove
     */
    private MappingCommand getTestMappingRemove(final Long cduId, final Long urlId) {
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
     * @param urls the urls
     * @return the test cdu
     */
    private CduDto getTestCdu(final Long cduId, final List<UrlDto> urls) {
        CduDto cdu;
        cdu = new CduDto();
        cdu.setId(cduId);
        cdu.setUrls(urls);
        return cdu;
    }

    /**
     * Gets the test cdu.
     *
     * @param urlId the url id
     * @return the test cdu
     */
    private UrlDto getTestUrl(final Long urlId) {
        UrlDto url;
        url = new UrlDto();
        url.setId(urlId);
        return url;
    }

    /**
     * Gets the test cdus.
     *
     * @return the test cdus
     */
    private List<UrlDto> getTestUrls() {
        List<UrlDto> urls;
        urls = new ArrayList<>();
        urls.add(getTestUrl(1L));
        urls.add(getTestUrl(2L));
        return urls;
    }
}
