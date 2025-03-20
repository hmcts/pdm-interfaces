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
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
abstract class CduSearchValidatorTest extends AbstractJUnit {

    protected static final String NOT_EQUAL = "Not equal";

    protected static final String FALSE = "False";

    protected static final String TRUE = "True";

    protected static final String CDU_SEARCH_COMMAND = "cduSearchCommand";

    /** The class under test. */
    protected CduSearchValidator classUnderTest;
    
    /** The mock local proxy service. */
    private ILocalProxyService mockLocalProxyService;

    /** The sites. */
    protected final List<XhibitCourtSiteDto> sites = getTestSites();

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new CduSearchValidator();

        // Setup the mock version of the called classes
        mockLocalProxyService = createMock(ILocalProxyService.class);
        MessageSource mockMessageSource = new ReloadableResourceBundleMessageSource() {
            @Override
            public String getMessageInternal(String code, Object[] args, Locale locale) {
                return "Test";
            }
        };

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "localProxyService", mockLocalProxyService);
        ReflectionTestUtils.setField(classUnderTest, "messageSource", mockMessageSource);
    }

    /**
     * Test supports.
     */
    @Test
    void testSupports() {
        final boolean result = classUnderTest.supports(CduSearchCommand.class);
        assertTrue(result, FALSE);
    }

    /**
     * Test valid xhibit court site id.
     */
    @Test
    void testXhibitCourtSiteIdValid() {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommandXhibitCourtSiteId();
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        
        replay(mockLocalProxyService);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Checking that the search criteria is valid
        assertFalse(errors.hasErrors(), TRUE);

        // Verify the expected mocks were called
        verify(mockLocalProxyService);
    }

    /**
     * Test invalid xhibit court site id.
     */
    @Test
    void testXhibitCourtSiteIdInvalid() {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommandMacAddress();
        cduSearchCommand.setXhibitCourtSiteId(Long.valueOf(-1));
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);

        // Add the mock calls to child classes
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        
        replay(mockLocalProxyService);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Checking that the search criteria is invalid (negative)
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);
    }

    /**
     * Gets the test cdu search command mac address.
     *
     * @return the test cdu search command mac address
     */
    protected CduSearchCommand getTestCduSearchCommandMacAddress() {
        final CduSearchCommand cduSearchCommand = new CduSearchCommand();
        cduSearchCommand.setMacAddress("MACADDRESS");
        return cduSearchCommand;
    }

    /**
     * Gets the test site.
     *
     * @param id the id
     * @return the test site
     */
    private XhibitCourtSiteDto getTestSite(final Long id) {
        final XhibitCourtSiteDto site = new XhibitCourtSiteDto();
        site.setId(id);
        return site;
    }

    /**
     * Gets the test sites.
     *
     * @return the test sites
     */
    private List<XhibitCourtSiteDto> getTestSites() {
        final List<XhibitCourtSiteDto> sites = new ArrayList<>();
        sites.add(getTestSite(1L));
        sites.add(getTestSite(2L));
        return sites;
    }

    /**
     * Gets the test cdu search command xhibit court sited id.
     *
     * @return the test cdu search command xhibit court sited id
     */
    private CduSearchCommand getTestCduSearchCommandXhibitCourtSiteId() {
        final CduSearchCommand cduSearchCommand = new CduSearchCommand();
        cduSearchCommand.setXhibitCourtSiteId(sites.get(0).getId());
        return cduSearchCommand;
    }
}
