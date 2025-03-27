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
 * The Class CduSearchSelectedValidatorTest.
 *
 * @author boparaij
 */
@ExtendWith(EasyMockExtension.class)
class CduSearchSelectedValidatorTest extends AbstractJUnit {

    private static final String NOT_EQUAL = "Not equal";

    private static final String FALSE = "False";

    private static final String CDU_SEARCH_COMMAND = "cduSearchCommand";

    /** The class under test. */
    private CduSearchSelectedValidator classUnderTest;
    
    /** The mock cdu page state holder. */
    private CduPageStateHolder mockCduPageStateHolder;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new CduSearchSelectedValidator();

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
     * Test valid mac address.
     */
    @Test
    void testSearchSelectedMacAddressValid() {
        final CduSearchCommand cduSearchCommand = getTestCduSearchSelectedMacAddress("MACADDRESS1");
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);
        final List<CduDto> cdus = getTestCdus();

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
        replay(mockCduPageStateHolder);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Checking that the search criteria is valid
        assertFalse(errors.hasErrors(), "True");

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
    }

    /**
     * Test invalid mac address.
     */
    @Test
    void testSearchSelectedMacAddressInvalid() {
        final CduSearchCommand cduSearchCommand = getTestCduSearchSelectedMacAddress("MACADDRESS3");
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);
        final List<CduDto> cdus = getTestCdus();

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus);
        replay(mockCduPageStateHolder);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Checking that the search criteria is invalid (negative)
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
    }

    /**
     * Test no search results is handled gracefully.
     */
    @Test
    void testSearchSelectedNoSearchResults() {
        final CduSearchCommand cduSearchCommand = getTestCduSearchSelectedMacAddress("MACADDRESS1");
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);

        // Define a mock version of the called methods
        expect(mockCduPageStateHolder.getCdus()).andReturn(null);
        replay(mockCduPageStateHolder);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Checking that the search criteria is invalid (negative)
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);

        // Verify the mocks used in this method were called
        verify(mockCduPageStateHolder);
    }

    /**
     * Test null mac address.
     */
    @Test
    void testSearchSelectedMacAddressNull() {
        final CduSearchCommand cduSearchCommand = new CduSearchCommand();
        final BindingResult errors =
            new BeanPropertyBindingResult(cduSearchCommand, CDU_SEARCH_COMMAND);

        // Perform the test
        classUnderTest.validate(cduSearchCommand, errors);

        // Checking that the search criteria is invalid (negative)
        assertEquals(1, errors.getErrorCount(), NOT_EQUAL);
    }

    /**
     * Gets the test cdu search selected mac address.
     *
     * @param macAddress the mac address
     * @return cduSearchCommand
     */
    private CduSearchCommand getTestCduSearchSelectedMacAddress(final String macAddress) {
        final CduSearchCommand cduSearchCommand = new CduSearchCommand();
        cduSearchCommand.setSelectedMacAddress(macAddress);
        return cduSearchCommand;
    }

    /**
     * Gets the test cdu.
     *
     * @param cduId the cdu id
     * @param macAddress the mac address
     * @return the test cdu
     */
    private CduDto getTestCdu(final Long cduId, final String macAddress) {
        CduDto cdu;
        cdu = new CduDto();
        cdu.setId(cduId);
        cdu.setMacAddress(macAddress);
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
        cdus.add(getTestCdu(1L, "MACADDRESS1"));
        cdus.add(getTestCdu(2L, "MACADDRESS2"));
        return cdus;
    }
}
