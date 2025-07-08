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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class LocalProxySelectedValidator.
 * 
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class LocalProxySelectedValidatorTest extends AbstractLocalProxyValidatorTest {

    private static final String NOT_EQUAL = "Not equal";

    private static final String NULL = "Null";

    private static final String TRUE = "True";

    private static final String FALSE = "False";

    /** The class under test. */
    private LocalProxySelectedValidator classUnderTest;

    /** The mock local proxy page state holder. */
    private LocalProxyPageStateHolder mockLocalProxyPageStateHolder;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new LocalProxySelectedValidator();

        // Setup the mock version of the called classes
        mockLocalProxyPageStateHolder = createMock(LocalProxyPageStateHolder.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "localProxyPageStateHolder",
            mockLocalProxyPageStateHolder);
    }

    /**
     * Test supports.
     */
    @Test
    void testSupports() {
        final boolean result = classUnderTest.supports(LocalProxySearchCommand.class);
        assertTrue(result, FALSE);
    }

    /**
     * Test validate court site invalid.
     */
    @Test
    void testValidateCourtSiteInvalid() {
        final LocalProxySearchCommand localProxySearchCommand = getLocalProxySearchCommand(1L);
        final BindingResult errors =
            new BeanPropertyBindingResult(localProxySearchCommand, "localProxySearchCommand");
        final List<XhibitCourtSiteDto> courtSiteList = getTestSites(AppConstants.NO_CHAR);

        // Define a mock version of the called methods
        expect(mockLocalProxyPageStateHolder.getSites()).andReturn(courtSiteList);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        classUnderTest.validate(localProxySearchCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test validate court site invalid.
     */
    @Test
    void testValidateCourtsiteValid() {
        final LocalProxySearchCommand localProxySearchCommand = getLocalProxySearchCommand(1L);
        final BindingResult errors =
            new BeanPropertyBindingResult(localProxySearchCommand, "localProxySearchCommand");
        final List<XhibitCourtSiteDto> courtSiteList = getTestSites(AppConstants.YES_CHAR);

        // Define a mock version of the called methods
        expect(mockLocalProxyPageStateHolder.getSites()).andReturn(courtSiteList);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        classUnderTest.validate(localProxySearchCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertFalse(errors.hasErrors(), TRUE);

        // Verify the mocks used in this method were called
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Gets the test localProxySearchCommand.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @return the test localProxySearchCommand
     */
    private LocalProxySearchCommand getLocalProxySearchCommand(final long xhibitCourtSiteId) {
        final LocalProxySearchCommand localProxySearchCommand = new LocalProxySearchCommand();
        localProxySearchCommand.setXhibitCourtSiteId(xhibitCourtSiteId);
        return localProxySearchCommand;
    }
}
