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
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService;

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
 * The Class CduRestartAllValidatorTest.
 *
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class CduRestartAllValidatorTest extends AbstractJUnit {
    /** The no of sites. */
    private static final int NO_OF_SITES = 2;

    /** The Constant INVALID_SITE_ID. */
    private static final Long INVALID_SITE_ID = Long.valueOf(NO_OF_SITES + 1);

    private static final String NOT_EQUAL = "Not equal";

    private static final String FALSE = "False";

    private static final String CDU_SEARCH_COMMAND = "cduSearchCommand";

    /** The class under test. */
    private CduRestartAllValidator classUnderTest;

    /** The mock local proxy service. */
    private ILocalProxyService mockLocalProxyService;

    /** The sites. */
    private final List<XhibitCourtSiteDto> sites = getTestSites();

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new CduRestartAllValidator();

        // Setup the mock version of the called classes
        mockLocalProxyService = createMock(ILocalProxyService.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "localProxyService", mockLocalProxyService);
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
     * Test validate restart all cdus valid.
     */
    @Test
    void testValidateRestartAllCdusValid() {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand(sites.get(0).getId());
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);

        // Define a mock version of the called methods
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        
        replay(mockLocalProxyService);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Assert that the objects are as expected
        assertFalse(errors.hasErrors(), "True");

        // Verify the mocks used in this method were called
        verify(mockLocalProxyService);
    }

    /**
     * Test validate restart all cdus invalid site id.
     */
    @Test
    void testValidateRestartAllCdusInvalidSiteId() {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand(INVALID_SITE_ID);
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);

        // Define a mock version of the called methods
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        
        replay(mockLocalProxyService);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Assert that the objects are as expected
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockLocalProxyService);
    }

    /**
     * Test validate restart all cdus null site id.
     */
    @Test
    void testValidateRestartAllCdusNullSiteId() {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand(null);
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Assert that the objects are as expected
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);
    }

    /**
     * Test validate restart all cdus not null mac address.
     */
    @Test
    void testValidateRestartAllCdusNotNullMacAddress() {
        final CduSearchCommand cduSearchCommand = getTestCduSearchCommand(sites.get(0).getId());
        cduSearchCommand.setMacAddress("MACADDRESS");
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);

        // Define a mock version of the called methods
        expect(mockLocalProxyService.getXhibitCourtSitesWithLocalProxy()).andReturn(sites);
        
        replay(mockLocalProxyService);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Assert that the objects are as expected
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);
    }

    /**
     * Gets the test cdu search command.
     *
     * @param xhibitCourtSiteId the xhibit court sited id
     * @return the test cdu search command
     */
    private CduSearchCommand getTestCduSearchCommand(final Long xhibitCourtSiteId) {
        final CduSearchCommand cduSearchCommand = new CduSearchCommand();
        cduSearchCommand.setXhibitCourtSiteId(xhibitCourtSiteId);
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
        for (int id = 0; id < NO_OF_SITES; id++) {
            sites.add(getTestSite(Long.valueOf(id)));
        }
        return sites;
    }
}
