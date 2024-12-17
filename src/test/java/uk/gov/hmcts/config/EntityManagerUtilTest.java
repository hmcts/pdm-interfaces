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

package uk.gov.hmcts.config;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.initialization.InitializationService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for EntityManagerUtil.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestMethodOrder(OrderAnnotation.class)
class EntityManagerUtilTest extends AbstractJUnit {

    private static final String NOTNULL = "Result is Null";
    private static final String FALSE = "Result is True";
    private static final String TRUE = "Result is False";

    @Test
    @Order(1)
    void testEntityManager() {
        // Setup
        try (EntityManagerFactory mockEntityManagerFactory =
            Mockito.mock(EntityManagerFactory.class)) {
            InitializationService mockInitializationService =
                Mockito.mock(InitializationService.class);
            Mockito.mockStatic(InitializationService.class);
            // Expects
            Mockito.when(InitializationService.getInstance()).thenReturn(mockInitializationService);
            Mockito.when(mockInitializationService.getEntityManagerFactory())
                .thenReturn(mockEntityManagerFactory);
            Mockito.when(mockEntityManagerFactory.createEntityManager())
                .thenReturn(Mockito.mock(EntityManager.class));
            try (EntityManager mockEntityManager = Mockito.mock(EntityManager.class)) {
                Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
            }
            // Run
            try (EntityManager result = EntityManagerUtil.getEntityManager()) {
                assertNotNull(result, NOTNULL);
            }
            Mockito.clearAllCaches();
        }
    }

    @Test
    @Order(2)
    void testEntityManagerActiveNull() {
        boolean result = EntityManagerUtil.isEntityManagerActive(null);
        assertFalse(result, FALSE);
    }

    @Test
    @Order(3)
    void testEntityManagerActiveClosed() {
        try (EntityManager mockEntityManager = Mockito.mock(EntityManager.class)) {
            Mockito.when(mockEntityManager.isOpen()).thenReturn(false);
            boolean result = EntityManagerUtil.isEntityManagerActive(mockEntityManager);
            assertFalse(result, FALSE);
        }
    }

    @Test
    @Order(4)
    void testEntityManagerActiveOpen() {
        try (EntityManager mockEntityManager = Mockito.mock(EntityManager.class)) {
            Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
            boolean result = EntityManagerUtil.isEntityManagerActive(mockEntityManager);
            assertTrue(result, TRUE);
        }
    }

}
