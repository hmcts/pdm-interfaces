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

package uk.gov.hmcts.pdm.publicdisplay.manager.config;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.initialization.InitializationService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.PropertyService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for ApplicationConfiguration.
 *
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ApplicationConfigurationTest extends AbstractJUnit {

    private static final String EQUALS = "Result is not equal";
    private static final String NOTNULL = "Result is null";
    
    private EntityManager mockEntityManager;

    private PropertyService mockPropertyService;

    private ApplicationConfiguration classUnderTest;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        Mockito.mockStatic(EntityManagerUtil.class);
        mockEntityManager = Mockito.mock(EntityManager.class);
        mockPropertyService = Mockito.mock(PropertyService.class);
        InitializationService.getInstance().setEntityManagerFactory(null);
        classUnderTest = new ApplicationConfiguration();
        Environment mockEnvironment = Mockito.mock(Environment.class);
        ReflectionTestUtils.setField(classUnderTest, "env", mockEnvironment);
        classUnderTest.init();
        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, "entityManagerFactory",
            Mockito.mock(EntityManagerFactory.class));
        ReflectionTestUtils.setField(classUnderTest, "propertyService", mockPropertyService);
    }

    /**
     * Teardown.
     */
    @AfterEach
    public void teardown() {
        Mockito.clearAllCaches();
    }

    /**
     * Test getRestClientTimeout.
     */
    @Test
    void testGetRestClientTimeout() {
        // Setup
        String dummyTimeout = "30";

        // Expects
        Mockito.when(EntityManagerUtil.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockPropertyService.getPropertyValueByName("rest.client.timeout"))
            .thenReturn(dummyTimeout);

        // Run
        Integer result = classUnderTest.getRestClientTimeout();

        // Verify
        assertNotNull(result, NOTNULL);
        assertEquals(result, Integer.valueOf(dummyTimeout), EQUALS);
    }

    /**
     * Test getRestTokenExpiry.
     */
    @Test
    void testGetRestTokenExpiry() {
        // Setup
        String dummyTokenExpiry = "30";

        // Expects
        Mockito.when(mockPropertyService.getPropertyValueByName("rest.token.expiry"))
            .thenReturn(dummyTokenExpiry);

        // Run
        Integer result = classUnderTest.getRestTokenExpiry();

        // Verify
        assertNotNull(result, NOTNULL);
        assertEquals(result, Integer.valueOf(dummyTokenExpiry), EQUALS);
    }

    /**
     * Test getRagStatusOverallAmberPercent.
     */
    @Test
    void testGetRagStatusOverallAmberPercent() {
        // Setup
        String dummyRagPercentage = "30";

        // Expects
        Mockito.when(mockPropertyService.getPropertyValueByName("rag.status.overall.amber.percent"))
            .thenReturn(dummyRagPercentage);

        // Run
        Integer result = classUnderTest.getRagStatusOverallAmberPercent();

        // Verify
        assertNotNull(result, NOTNULL);
        assertEquals(result, Integer.valueOf(dummyRagPercentage), EQUALS);
    }
}
