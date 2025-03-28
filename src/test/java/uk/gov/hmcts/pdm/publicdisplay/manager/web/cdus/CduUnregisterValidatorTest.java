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

import java.util.ArrayList;
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
 * The Class CduUnregisterValidatorTest.
 *
 * @author boparaij
 */
@ExtendWith(EasyMockExtension.class)
class CduUnregisterValidatorTest extends AbstractJUnit {

    private static final String NOT_EQUAL = "Not equal";

    private static final String FALSE = "False";

    private static final String NULL = "Null";

    private static final String CDU_COMMAND = "cduCommand";

    /** The class under test. */
    private CduUnregisterValidator classUnderTest;

    /** The mock cdu page state holder. */
    private CduPageStateHolder mockCduPageStateHolder;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new CduUnregisterValidator();

        // Setup the mock version of the called classes
        mockCduPageStateHolder = createMock(CduPageStateHolder.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "cduPageStateHolder", mockCduPageStateHolder);
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
     * Mac address is valid.
     */
    @Test
    void testMacAddressValid() {
        final List<CduDto> cdus = getTestCdus();
        final CduDto cdu = cdus.get(0);
        final CduSearchCommand cduCommand = getTestCduCommand(cdu.getMacAddress());
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus).times(2);
        replay(mockCduPageStateHolder);
        
        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertFalse(errors.hasErrors(), "True");

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
    }

    /**
     * Mac address is invalid as already unregistered.
     */
    @Test
    void testMacAddressInvalid() {
        final List<CduDto> cdus = getTestCdus();
        final CduDto cdu = cdus.get(1);
        final CduSearchCommand cduCommand = getTestCduCommand(cdu.getMacAddress());
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus).times(2);
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
     * Mac address does not exist.
     */
    @Test
    void testMacAddressNotExist() {
        final List<CduDto> cdus = getTestCdus();
        final CduDto cdu =
            getTestCdu(cdus.get(0).getCduNumber(), "MACADDRESS3", AppConstants.YES_CHAR);
        final CduSearchCommand cduCommand = getTestCduCommand(cdu.getMacAddress());
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
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
     * Mac address is null.
     */
    @Test
    void testMacAddressNull() {
        final CduSearchCommand cduCommand = getTestCduCommand(null);
        final BindingResult errors = new BeanPropertyBindingResult(cduCommand, CDU_COMMAND);

        // Perform the test
        classUnderTest.validate(cduCommand, errors);

        // Check the results
        assertNotNull(errors, NULL);
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);
    }

    /**
     * Gets the test cdu command.
     *
     * @param macAddress the macaddress
     * @return the test cdu command
     */
    private CduSearchCommand getTestCduCommand(final String macAddress) {
        final CduSearchCommand cduCommand = new CduSearchCommand();
        cduCommand.setSelectedMacAddress(macAddress);
        return cduCommand;
    }

    /**
     * Gets the test cdu.
     *
     * @param cduNumber the cdu number
     * @param macAddress the mac address
     * @param registeredIndicator the registered indicator
     * @return the test cdu
     */
    private CduDto getTestCdu(final String cduNumber, final String macAddress,
        final Character registeredIndicator) {
        final CduDto cdu = new CduDto();
        cdu.setCduNumber(cduNumber);
        cdu.setMacAddress(macAddress);
        cdu.setRegisteredIndicator(registeredIndicator);
        return cdu;
    }

    /**
     * Gets the test cdus.
     *
     * @return the test cdus
     */
    private List<CduDto> getTestCdus() {
        List<CduDto> cdus;
        cdus = new ArrayList<>();
        cdus.add(getTestCdu("CDUNUMBER1", "MACADDRESS1", AppConstants.YES_CHAR));
        cdus.add(getTestCdu("CDUNUMBER2", "MACADDRESS2", AppConstants.NO_CHAR));
        return cdus;
    }
}
