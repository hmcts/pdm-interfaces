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

package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.persistence.EntityManager;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrproperty.XhbDispMgrPropertyRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.Property;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IProperty;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IPropertyService;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The Class PropertyServiceTest.
 *
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class PropertyServiceTest extends AbstractJUnit {

    /** The Constant PROPERTY_NAME. */
    private static final String PROPERTY_NAME = "PROPERTY_NAME";

    /** The Constant PROPERTY_VALUE. */
    private static final String PROPERTY_VALUE = "PROPERTY_VALUE";

    private static final String NOT_EQUAL = "Not equal";

    private static final String NULL = "Null";

    /** The class under test. */
    private IPropertyService classUnderTest;

    /** The mock property repo. */
    private XhbDispMgrPropertyRepository mockPropertyRepo;

    /** The properties. */
    private final List<IProperty> properties = getTestProperties();

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new PropertyService(createMock(EntityManager.class));

        // Setup the mock version of the called classes
        mockPropertyRepo = createMock(XhbDispMgrPropertyRepository.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrPropertyRepository",
            mockPropertyRepo);
    }

    /**
     * Test get property value by name.
     */
    @Test
    void testGetPropertyValueByName() {
        // Add the mock calls to child classes
        expect(mockPropertyRepo.findPropertyValueByName(properties.get(0).getName()))
            .andReturn(properties.get(0).getValue());
        replay(mockPropertyRepo);

        // Perform the test
        final String result = classUnderTest.getPropertyValueByName(properties.get(0).getName());

        // Assert that the objects are as expected
        assertNotNull(result, NULL);
        assertEquals(result, properties.get(0).getValue(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockPropertyRepo);
    }

    @Test
    void testRepository() {
        IPropertyService localClassUnderTest = new PropertyService(null);
        assertThrows(RuntimeException.class, localClassUnderTest::getAllProperties,
            "Exception not Thrown");
    }

    /**
     * Test get all properties.
     */
    @Test
    void testGetAllProperties() {
        // Add the mock calls to child classes
        expect(mockPropertyRepo.findAllProperties()).andReturn(properties);
        replay(mockPropertyRepo);

        // Perform the test
        final List<IProperty> results = classUnderTest.getAllProperties();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(results.size(), properties.size(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockPropertyRepo);
    }

    /**
     * Gets the test property.
     *
     * @param id the id
     * @return the test property
     */
    private IProperty getTestProperty(final Long id) {
        final IProperty property = new Property();
        property.setName(PROPERTY_NAME + id.toString());
        property.setValue(PROPERTY_VALUE + id.toString());
        return property;
    }

    /**
     * Gets the test properties.
     *
     * @return the test properties
     */
    private List<IProperty> getTestProperties() {
        final List<IProperty> properties = new ArrayList<>();
        properties.add(getTestProperty(1L));
        properties.add(getTestProperty(2L));
        return properties;
    }
}
