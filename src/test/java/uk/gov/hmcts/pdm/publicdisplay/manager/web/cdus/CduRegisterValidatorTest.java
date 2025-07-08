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
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.CduService;

import java.util.Locale;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class CduRegisterValidatorTest.
 *
 * @author pattersone
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class CduRegisterValidatorTest extends AbstractJUnit {
    /** The Constant CDUNUMBER. */
    private static final String CDUNUMBER = "CDUNUMBER";

    /** The Constant MACADDRESS. */
    private static final String MACADDRESS = "MACADDRESS";

    private static final String NOT_EQUAL = "Not equal";

    private static final String NULL = "Null";

    private static final String TRUE = "True";

    private static final String FALSE = "False";

    private static final String CDU_COMMAND = "cduCommand";

    /** The class under test. */
    private CduRegisterValidator classUnderTest;

    /** The mock cdu page state holder. */
    private CduPageStateHolder mockCduPageStateHolder;

    /** The mock cdu service. */
    private CduService mockCduService;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test (CduRegisterValidator)
        classUnderTest = new CduRegisterValidator();

        // Setup the mock version of the called classes
        mockCduPageStateHolder = createMock(CduPageStateHolder.class);
        mockCduService = createMock(CduService.class);
        MessageSource mockMessageSource = new ReloadableResourceBundleMessageSource() {
            @Override
            public String getMessageInternal(String code, Object[] args, Locale locale) {
                return "Test";
            }
        };

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "cduPageStateHolder", mockCduPageStateHolder);
        ReflectionTestUtils.setField(classUnderTest, "cduService", mockCduService);
        ReflectionTestUtils.setField(classUnderTest, "messageSource", mockMessageSource);
    }

    /**
     * Test supports.
     */
    @Test
    void testSupports() {
        final boolean result = classUnderTest.supports(CduRegisterCommand.class);
        assertTrue(result, FALSE);
    }

    /**
     * Test an unregistered cdu as a successful route through the tests.
     */
    @Test
    void testUnregisteredCduSuccess() {
        // Local Variables
        final CduDto unregisteredCdu = getTestCdu(AppConstants.NO_CHAR);
        final CduRegisterCommand cduCommand =
            getTestCduRegisterCommand(unregisteredCdu.getCduNumber());
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods to unregister a cdu
        expect(mockCduPageStateHolder.getCdu()).andReturn(unregisteredCdu);
        expectLastCall().times(2);
        replay(mockCduPageStateHolder);
        expect(mockCduService.isCduWithCduNumber(cduCommand.getCduNumber())).andReturn(false);
        replay(mockCduService);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results for errors
        assertFalse(errors.hasErrors(), TRUE);

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
        verify(mockCduService);
    }

    /**
     * Test an already registered cdu.
     */
    @Test
    void testRegisteredCduFailure() {
        // Local Variables
        final CduDto registeredCdu = getTestCdu(AppConstants.YES_CHAR);
        final CduRegisterCommand cduCommand =
            getTestCduRegisterCommand(registeredCdu.getCduNumber());
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdu()).andReturn(registeredCdu);
        expectLastCall().times(2);
        replay(mockCduPageStateHolder);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
    }

    /**
     * Test an unregistered cdu with a mismatch on MacAddress.
     */
    @Test
    void testNoCduSelectedFailure() {
        // Local Variables
        final CduDto unregisteredCdu = getTestCdu(AppConstants.NO_CHAR);
        final CduRegisterCommand cduCommand =
            getTestCduRegisterCommand(unregisteredCdu.getCduNumber());
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdu()).andReturn(null);
        expectLastCall();
        replay(mockCduPageStateHolder);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
    }

    /**
     * Test unregistered cdu duplicate cdu number in use by different CDU.
     */
    @Test
    void testUnregisteredCduNonUniqueCduNumberFailure1() {
        // Local Variables
        final CduDto unregisteredCdu = getTestCdu(AppConstants.NO_CHAR);
        final CduRegisterCommand cduCommand =
            getTestCduRegisterCommand(unregisteredCdu.getCduNumber());
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdu()).andReturn(unregisteredCdu);
        expectLastCall().times(3);
        replay(mockCduPageStateHolder);
        expect(mockCduService.isCduWithCduNumber(cduCommand.getCduNumber())).andReturn(true);
        expect(mockCduService.isCduWithMacAddress(unregisteredCdu.getMacAddress()))
            .andReturn(false);
        replay(mockCduService);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
        verify(mockCduService);
    }

    /**
     * Test unregistered cdu duplicate cdu number in use by different CDU.
     */
    @Test
    void testUnregisteredCduNonUniqueCduNumberFailure2() {
        // Local Variables
        final CduDto unregisteredCdu = getTestCdu(AppConstants.NO_CHAR);
        final CduRegisterCommand cduCommand = getTestCduRegisterCommand(CDUNUMBER + "1");
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdu()).andReturn(unregisteredCdu);
        expectLastCall().times(4);
        replay(mockCduPageStateHolder);
        expect(mockCduService.isCduWithCduNumber(cduCommand.getCduNumber())).andReturn(true);
        expect(mockCduService.isCduWithMacAddress(unregisteredCdu.getMacAddress())).andReturn(true);
        expect(mockCduService.getCduByMacAddress(unregisteredCdu.getMacAddress()))
            .andReturn(unregisteredCdu);
        replay(mockCduService);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
        verify(mockCduService);
    }

    /**
     * Test unregistered cdu duplicate cdu number in use by same CDU.
     */
    @Test
    void testUnregisteredCduNonUniqueCduNumberSuccess() {
        // Local Variables
        final CduDto unregisteredCdu = getTestCdu(AppConstants.NO_CHAR);
        final CduRegisterCommand cduCommand =
            getTestCduRegisterCommand(unregisteredCdu.getCduNumber());
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdu()).andReturn(unregisteredCdu);
        expectLastCall().times(4);
        replay(mockCduPageStateHolder);
        expect(mockCduService.isCduWithCduNumber(cduCommand.getCduNumber())).andReturn(true);
        expect(mockCduService.isCduWithMacAddress(unregisteredCdu.getMacAddress())).andReturn(true);
        expect(mockCduService.getCduByMacAddress(unregisteredCdu.getMacAddress()))
            .andReturn(unregisteredCdu);
        replay(mockCduService);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results
        assertFalse(errors.hasErrors(), TRUE);

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
        verify(mockCduService);
    }

    /**
     * Gets the test cdu register command.
     *
     * @param cduNumber the cdu number
     * @return the test cdu command
     */
    private CduRegisterCommand getTestCduRegisterCommand(final String cduNumber) {
        final CduRegisterCommand cduCommand = new CduRegisterCommand();
        cduCommand.setCduNumber(cduNumber);
        return cduCommand;
    }

    /**
     * Gets the test cdu.
     *
     * @param registeredIndicator the registered indicator
     * @return the test cdu
     */
    private CduDto getTestCdu(final Character registeredIndicator) {
        final CduDto cdu = new CduDto();
        cdu.setCduNumber(CDUNUMBER);
        cdu.setMacAddress(MACADDRESS);
        cdu.setRegisteredIndicator(registeredIndicator);
        return cdu;
    }
}
