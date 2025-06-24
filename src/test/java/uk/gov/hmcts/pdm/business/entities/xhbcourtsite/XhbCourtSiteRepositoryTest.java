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

package uk.gov.hmcts.pdm.business.entities.xhbcourtsite;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.CourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.XhibitCourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Unit test for XhbCourtSiteRepositoryTest.
 *
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class XhbCourtSiteRepositoryTest extends AbstractJUnit {

    private static final String EQUAL = "Result is not equal";
    private static final String NOT_NULL = "Result is Null";
    private static final String TRUE = "Result is not True";

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private XhbDispMgrLocalProxyRepository mockXhbDispMgrLocalProxyRepository;

    @InjectMocks
    private XhbCourtSiteRepository classUnderTest = new XhbCourtSiteRepository(mockEntityManager);

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        mockEntityManager = Mockito.mock(EntityManager.class);
        mockXhbDispMgrLocalProxyRepository = Mockito.mock(XhbDispMgrLocalProxyRepository.class);
        classUnderTest = new XhbCourtSiteRepository(mockEntityManager);
        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrLocalProxyRepository",
            mockXhbDispMgrLocalProxyRepository);
    }

    /**
     * Test getXhibitCourtSiteFromDao.
     */
    @Test
    void testGetXhibitCourtSiteFromDao() {
        // Setup
        XhbCourtSiteDao xhbCourtSiteDao = getDummyXhbCourtSiteDao();

        // Perform the test
        IXhibitCourtSite result = XhbCourtSiteRepository.getXhibitCourtSiteFromDao(xhbCourtSiteDao);

        // Verify
        assertNotNull(result, NOT_NULL);
        assertEquals(result.getId().intValue(), xhbCourtSiteDao.getId(), EQUAL);
        assertEquals(result.getCourtSiteName(), xhbCourtSiteDao.getCourtSiteName(), EQUAL);
    }

    /**
     * Test processLocalProxy.
     */
    @Test
    void testProcessLocalProxy() {
        // Setup
        IXhibitCourtSite xhibitCourtSite = new XhibitCourtSite();
        xhibitCourtSite.setId(Long.valueOf(1));

        ICourtSite courtSite = new CourtSite();
        courtSite.setId(Long.valueOf(1));
        courtSite.setXhibitCourtSite(xhibitCourtSite);

        XhbDispMgrCourtSiteDao dispMgrCourtSiteDao = new XhbDispMgrCourtSiteDao();
        dispMgrCourtSiteDao.setId(1);

        XhbDispMgrLocalProxyDao dispMgrLocalProxyDao = getDummyXhbDispMgrLocalProxyDao();

        // Expects
        Mockito
            .when(mockXhbDispMgrLocalProxyRepository.findByCourtSiteId(dispMgrCourtSiteDao.getId()))
            .thenReturn(dispMgrLocalProxyDao);
        Mockito.doNothing().when(mockEntityManager).refresh(dispMgrLocalProxyDao);

        // Perform the test
        boolean result = false;
        try {
            classUnderTest.processLocalProxy(courtSite, dispMgrCourtSiteDao);
            result = true;
        } catch (Exception exception) {
            fail(exception.getMessage());
        }

        // Verify
        assertTrue(result, TRUE);
    }


    @Test
    void testFindAllReturnsNonObsoleteCourtSites() {
        // Arrange
        XhbCourtSiteDao courtSite1 = new XhbCourtSiteDao();
        courtSite1.setId(1);
        courtSite1.setCourtSiteName("CourtSite A");
        courtSite1.setObsInd(null); // Not obsolete

        XhbCourtSiteDao courtSite2 = new XhbCourtSiteDao();
        courtSite2.setId(2);
        courtSite2.setCourtSiteName("CourtSite B");
        courtSite2.setObsInd("N");

        List<XhbCourtSiteDao> expectedList = Arrays.asList(courtSite1, courtSite2);

        String expectedHql =
            "FROM uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao e "
                + "WHERE e.obsInd IS NULL OR e.obsInd <> 'Y'";
        Query mockQuery = Mockito.mock(Query.class);

        try (MockedStatic<EntityManagerUtil> mockedStatic =
            Mockito.mockStatic(EntityManagerUtil.class)) {
            // Tell AbstractRepository that our EntityManager is not active
            mockedStatic.when(() -> EntityManagerUtil.isEntityManagerActive(mockEntityManager))
                .thenReturn(false);
            // Force fallback path to return mockEntityManager
            mockedStatic.when(EntityManagerUtil::getEntityManager).thenReturn(mockEntityManager);

            Mockito.when(mockEntityManager.createQuery(expectedHql)).thenReturn(mockQuery);
            Mockito.when(mockQuery.getResultList()).thenReturn(expectedList);

            // Act
            List<XhbCourtSiteDao> result = classUnderTest.findAll();

            // Assert
            assertNotNull(result, NOT_NULL);
            assertEquals(2, result.size(), EQUAL);
            assertEquals("CourtSite A", result.get(0).getCourtSiteName(), EQUAL);
            assertEquals("CourtSite B", result.get(1).getCourtSiteName(), EQUAL);
        }
    }


    private XhbCourtSiteDao getDummyXhbCourtSiteDao() {
        XhbCourtSiteDao xhbCourtSiteDao = new XhbCourtSiteDao();
        xhbCourtSiteDao.setId(1);
        xhbCourtSiteDao.setCourtSiteName("CourtSiteName");
        xhbCourtSiteDao.setDisplayName("DisplayName");
        xhbCourtSiteDao.setShortName("ShortName");
        return xhbCourtSiteDao;
    }
    
    private XhbDispMgrLocalProxyDao getDummyXhbDispMgrLocalProxyDao() {
        XhbDispMgrLocalProxyDao localProxy = new XhbDispMgrLocalProxyDao();
        localProxy.setId(1);
        localProxy.setIpAddress("IpAddress");
        localProxy.setHostName("HostName");
        localProxy.setRagStatus("G");
        localProxy.setCreationDate(LocalDateTime.now());
        localProxy.setLastUpdateDate(localProxy.getCreationDate());
        localProxy.setRagStatusDate(localProxy.getLastUpdateDate());
        localProxy.setLastUpdatedBy("User");
        localProxy.setCreatedBy(localProxy.getLastUpdatedBy());
        return localProxy;
    }
}
