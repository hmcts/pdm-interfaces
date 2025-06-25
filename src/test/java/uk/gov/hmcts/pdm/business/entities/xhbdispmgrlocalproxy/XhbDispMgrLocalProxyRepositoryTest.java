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

package uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.CourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.LocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.XhibitCourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit test for XhbDispMgrLocalProxyRepository.
 *
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class XhbDispMgrLocalProxyRepositoryTest extends AbstractJUnit {

    private static final String TRUE = "Result is not True";

    @Mock
    private EntityManagerFactory mockEntityManagerFactory;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private EntityTransaction mockTransaction;

    @Mock
    private XhbDispMgrCourtSiteRepository mockXhbDispMgrCourtSiteRepository;

    @InjectMocks
    private final XhbDispMgrLocalProxyRepository classUnderTest =
        new XhbDispMgrLocalProxyRepository(mockEntityManager, mockXhbDispMgrCourtSiteRepository);

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(classUnderTest, "entityManager",
            mockEntityManager);
    }

    /**
     * Teardown.
     */
    @AfterEach
    public void teardown() {
        Mockito.clearAllCaches();
    }

    /**
     * Test saveDaoFromBasicValue.
     */
    @Test
    void testSaveDaoFromBasicValue() {
        // Setup
        Mockito.when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);
        ILocalProxy localProxy = getDummyLocalProxy();
        XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao = new XhbDispMgrCourtSiteDao();
        xhbDispMgrCourtSiteDao.setId(2);

        // Expects
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito
            .when(mockXhbDispMgrCourtSiteRepository.findDaoByXhibitCourtSiteId(
                localProxy.getCourtSite().getXhibitCourtSite().getId().intValue()))
            .thenReturn(xhbDispMgrCourtSiteDao);

        // Perform the test
        boolean result = false;
        try {
            classUnderTest.saveDaoFromBasicValue(localProxy);
            result = true;
        } catch (Exception exception) {
            fail(exception);
        }

        // Verify
        assertTrue(result, TRUE);
    }

    private ILocalProxy getDummyLocalProxy() {
        IXhibitCourtSite xhibitCourtSite = new XhibitCourtSite();
        xhibitCourtSite.setId(Long.valueOf(1));

        ICourtSite courtSite = new CourtSite();
        courtSite.setXhibitCourtSite(xhibitCourtSite);

        ILocalProxy localProxy = new LocalProxy();
        localProxy.setCourtSite(courtSite);
        localProxy.setRagStatusDate(LocalDateTime.now());
        return localProxy;
    }
}
