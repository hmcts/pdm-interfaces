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

package uk.gov.hmcts.pdm.publicdisplay.manager.domain;

import com.pdm.hb.jpa.AuthorizationUtil;
import org.hibernate.type.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import uk.gov.hmcts.pdm.publicdisplay.common.domain.api.IDomainObject;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for DomainInterceptorTest.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DomainInterceptorTest extends AbstractJUnit {

    private static final String FALSE = "Result is True";
    private static final String TRUE = "Result is not True";
    private static final String UPDATED_BY = "updatedBy";
    private static final String CREATED_BY = "createdBy";

    @InjectMocks
    private final DomainInterceptor classUnderTest = new DomainInterceptor();

    /**
     * Test saveDaoFromBasicValue.
     */
    @Test
    void testsave() {
        IDomainObject entity = getDummyDomainObject();
        Serializable id = entity.getId();

        // Perform the test
        boolean result = classUnderTest.onSave(entity, id, new String[] {CREATED_BY, UPDATED_BY},
            new String[] {CREATED_BY, UPDATED_BY}, new Type[] {});

        // Verify
        assertTrue(result, TRUE);
    }

    @Test
    void testOnFlushDirty() {
        IDomainObject entity = getDummyDomainObject();
        Serializable id = entity.getId();
        
        Mockito.mockStatic(AuthorizationUtil.class);
        Mockito.when(AuthorizationUtil.getUsername()).thenReturn("user");

        // Perform the test
        boolean result = classUnderTest.onFlushDirty(entity, id,
            new String[] {CREATED_BY, UPDATED_BY}, new String[] {CREATED_BY, UPDATED_BY},
            new String[] {CREATED_BY, UPDATED_BY}, new Type[] {});

        // Verify
        assertFalse(result, FALSE);
        Mockito.clearAllCaches();
    }

    private IDomainObject getDummyDomainObject() {
        IDomainObject result = new LocalProxy();
        result.setId(Long.valueOf(1));
        return result;
    }
}
