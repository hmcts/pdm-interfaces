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

package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for XhbDispMgrCduRepositoryTest.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class XhbDispMgrCduRepositoryTest extends AbstractJUnit {

    private static final String EQUAL = "Result is not equal";
    private static final String NOT_NULL = "Result is Null";
    private static final String NULL = "Result is not Null";

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private Query mockQuery;

    @InjectMocks
    private XhbDispMgrCduRepository classUnderTest = new XhbDispMgrCduRepository(mockEntityManager);

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        mockEntityManager = Mockito.mock(EntityManager.class);
        classUnderTest = new XhbDispMgrCduRepository(mockEntityManager);
    }

    /**
     * Test getNextIpHost.
     */
    @Test
    void testGetNextIpHost() {
        final Number[] expectedResults = {Long.valueOf(101), Double.valueOf(102), null};
        for (Number expectedResult : expectedResults) {
            // Run
            Number result = testGetNextIpHost(expectedResult);
            // Check
            if (expectedResult != null) {
                assertNotNull(result, NOT_NULL);
                assertEquals(result.getClass(), Integer.class, EQUAL);
            } else {
                assertNull(result, NULL); 
            }
        }
    }

    private Number testGetNextIpHost(Number expectedResult) {
        final Integer courtSiteId = 1;
        final Integer minHost = 100;
        final Integer maxHost = 199;
        final List<Number> expectedResults = new ArrayList<>();
        expectedResults.add(expectedResult);
        // Expects
        Mockito.when(mockEntityManager.createNamedQuery("XHB_DISP_MGR_CDU.getNextIpHost"))
            .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(expectedResults);
        // Run
        return classUnderTest.getNextIpHost(courtSiteId, minHost, maxHost);
    }
}
