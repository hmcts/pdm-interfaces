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
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class CduAmendValidatorTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
class CduAmendValidatorTest extends AbstractJUnit {
    /** The Constant CDUNUMBER. */
    private static final String CDUNUMBER = "CDUNUMBER";

    /** The Constant MACADDRESS. */
    private static final String MACADDRESS = "MACADDRESS";

    private static final String NOT_EQUAL = "Not equal";

    private static final String FALSE = "False";

    private static final String CDU_COMMAND = "cduCommand";

    /** The class under test. */
    private CduAmendValidator classUnderTest;
    
    /** The mock cduService. */
    private ICduService mockCduService;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test (CduAmendValidator)
        classUnderTest = new CduAmendValidator();

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
        final boolean result = classUnderTest.supports(CduAmendCommand.class);
        assertTrue(result, FALSE);
    }

    /**
     * Test amend cdu online as a successful route through the tests.
     */
    @Test
    void testAmendCduOnlineSuccess() {
        // Local Variables
        final CduDto cdu = getTestCdu(AppConstants.YES_CHAR);
        final CduAmendCommand cduCommand = getTestCduAmendCommand(AppConstants.NO_CHAR);

        // Local Variables
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduService.getCduByCduId(cduCommand.getCduId())).andReturn(cdu);
        
        replay(mockCduService);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results for errors
        assertFalse(errors.hasErrors(), "True");

        // Verify the mocks used in this method were called
        verify(mockCduService);
    }

    /**
     * Test amend cdu offline as a successful route through the tests.
     */
    @Test
    void testAmendCduOfflineSuccess() {
        // Local Variables
        final CduDto cdu = getTestCdu(AppConstants.YES_CHAR);
        final CduAmendCommand cduCommand = getTestCduAmendCommand(AppConstants.YES_CHAR);

        // Local Variables
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduService.getCduByCduId(cduCommand.getCduId())).andReturn(cdu);
        
        replay(mockCduService);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results for errors
        assertFalse(errors.hasErrors(), "True");

        // Verify the mocks used in this method were called
        verify(mockCduService);
    }

    /**
     * Test amend unregistered cdu. Expect failure.
     */
    @Test
    void testAmendCduUnregisteredFailure() {
        // Local Variables
        final CduDto cdu = getTestCdu(AppConstants.NO_CHAR);
        final CduAmendCommand cduCommand = getTestCduAmendCommand(AppConstants.NO_CHAR);
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduService.getCduByCduId(cduCommand.getCduId())).andReturn(cdu);
        
        replay(mockCduService);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results for errors
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduService);
    }

    /**
     * Test amend cdu passing null. Expect failure.
     */
    @Test
    void testAmendCduNullFailure() {
        // Local Variables
        final CduAmendCommand cduCommand = getTestCduAmendCommand(AppConstants.NO_CHAR);
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduService.getCduByCduId(cduCommand.getCduId())).andReturn(null);
        
        replay(mockCduService);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results for errors
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduService);
    }

    /**
     * Gets the test cdu amend command.
     *
     * @param offlineIndicator the offline indicator
     * @return the test cdu amend command
     */
    private CduAmendCommand getTestCduAmendCommand(final Character offlineIndicator) {
        final CduAmendCommand cduCommand = new CduAmendCommand();
        cduCommand.setCduId(1);
        cduCommand.setOfflineIndicator(offlineIndicator);
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
