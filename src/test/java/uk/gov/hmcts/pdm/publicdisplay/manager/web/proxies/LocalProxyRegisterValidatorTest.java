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
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.LocalProxyService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService;

import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class LocalProxyRegisterValidator. This validates that the court site is valid and the IP
 * address is unique.
 * 
 * @author pattersone
 *
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class LocalProxyRegisterValidatorTest extends AbstractLocalProxyValidatorTest {
    /** The test param. */
    private static final String IP_ADDRESS = "ipaddress";

    private static final String NOT_EQUAL = "Not equal";

    private static final String FALSE = "False";

    private static final String NULL = "Null";

    private static final String TRUE = "True";

    /** The class under test. */
    private LocalProxyRegisterValidator classUnderTest;

    /** The mock local proxy service. */
    private ILocalProxyService mockLocalProxyService;

    /** The mock local proxy page state holder. */
    private LocalProxyPageStateHolder mockLocalProxyPageStateHolder;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new LocalProxyRegisterValidator();

        // Setup the mock version of the called classes
        mockLocalProxyService = createMock(LocalProxyService.class);
        mockLocalProxyPageStateHolder = createMock(LocalProxyPageStateHolder.class);
        MessageSource mockMessageSource = new ReloadableResourceBundleMessageSource() {
            @Override
            public String getMessageInternal(String code, Object[] args, Locale locale) {
                return "Test";
            }
        };

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "localProxyService", mockLocalProxyService);
        ReflectionTestUtils.setField(classUnderTest, "localProxyPageStateHolder",
            mockLocalProxyPageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "messageSource", mockMessageSource);
    }

    /**
     * Test supports.
     */
    @Test
    void testSupports() {
        final boolean result = classUnderTest.supports(LocalProxyRegisterCommand.class);
        assertTrue(result, FALSE);
    }

    /**
     * Test ip address invalid.
     */
    @Test
    void testIpAddressInvalid() {
        final LocalProxyRegisterCommand localProxyRegisterCommand =
            getTestLocalProxyCommand(1L, IP_ADDRESS);
        final BindingResult errors =
            new BeanPropertyBindingResult(localProxyRegisterCommand, "localProxyRegisterCommand");

        // Define a mock version of the called methods
        expect(mockLocalProxyService.isLocalProxyWithIpAddress(IP_ADDRESS)).andReturn(true);
        replay(mockLocalProxyService);

        // Perform the test
        classUnderTest.validate(localProxyRegisterCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockLocalProxyService);

    }

    /**
     * Test validate courtsite invalid.
     */
    @Test
    void testValidateCourtsiteInvalid() {
        final LocalProxyRegisterCommand localProxyRegisterCommand =
            getTestLocalProxyCommand(1L, null);
        final BindingResult errors =
            new BeanPropertyBindingResult(localProxyRegisterCommand, "localProxyRegisterCommand");
        final List<XhibitCourtSiteDto> sites = getTestSites(AppConstants.YES_CHAR);

        // Define a mock version of the called methods
        expect(mockLocalProxyPageStateHolder.getSites()).andReturn(sites);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        classUnderTest.validate(localProxyRegisterCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Test validate all.
     */
    @Test
    void testValidateAll() {
        final LocalProxyRegisterCommand localProxyRegisterCommand =
            getTestLocalProxyCommand(1L, IP_ADDRESS);
        final BindingResult errors =
            new BeanPropertyBindingResult(localProxyRegisterCommand, "localProxyRegisterCommand");
        final List<XhibitCourtSiteDto> sites = getTestSites(AppConstants.NO_CHAR);

        // Define a mock version of the called methods
        expect(mockLocalProxyPageStateHolder.getSites()).andReturn(sites);
        replay(mockLocalProxyPageStateHolder);

        // Perform the test
        classUnderTest.validate(localProxyRegisterCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertFalse(errors.hasErrors(), TRUE);

        // Verify the mocks used in this method were called
        verify(mockLocalProxyPageStateHolder);
    }

    /**
     * Gets the test local proxy command.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @param ipAddress the ip address
     * @return the test local proxy command
     */
    private LocalProxyRegisterCommand getTestLocalProxyCommand(final long xhibitCourtSiteId,
        final String ipAddress) {
        final LocalProxyRegisterCommand localProxyRegisterCommand = new LocalProxyRegisterCommand();
        localProxyRegisterCommand.setIpAddress(ipAddress);
        localProxyRegisterCommand.setXhibitCourtSiteId(xhibitCourtSiteId);
        return localProxyRegisterCommand;
    }
}
