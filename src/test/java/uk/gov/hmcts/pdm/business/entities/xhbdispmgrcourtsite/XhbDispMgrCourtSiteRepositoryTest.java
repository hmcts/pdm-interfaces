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

package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite;


import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
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
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleDao;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.initialization.InitializationService;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.CourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.LocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.Schedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.XhibitCourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for XhbDispMgrCourtSiteRepository.
 *
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class XhbDispMgrCourtSiteRepositoryTest extends AbstractJUnit {

    private static final String EQUAL = "Result is not equal";
    private static final String NOT_NULL = "Result is Null";
    private static final String TRUE = "Result is not True";

    @Mock
    private EntityManagerFactory mockEntityManagerFactory;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private EntityTransaction mockTransaction;

    @Mock
    private Query mockQuery;

    @Mock
    private XhbDispMgrLocalProxyRepository mockXhbDispMgrLocalProxyRepository;

    @Mock
    private XhbCourtSiteRepository mockXhbCourtSiteRepository;

    @InjectMocks
    private XhbDispMgrCourtSiteRepository classUnderTest;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        InitializationService.getInstance().setEntityManagerFactory(mockEntityManagerFactory);
        mockEntityManager = Mockito.mock(EntityManager.class);
        classUnderTest = new XhbDispMgrCourtSiteRepository(mockEntityManager);
        Mockito.mockStatic(EntityManagerUtil.class);
        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrLocalProxyRepository",
            mockXhbDispMgrLocalProxyRepository);
        ReflectionTestUtils.setField(classUnderTest, "xhbCourtSiteRepository",
            mockXhbCourtSiteRepository);
    }

    /**
     * Teardown.
     */
    @AfterEach
    public void teardown() {
        Mockito.clearAllCaches();
    }

    /**
     * Test findByCourtSiteId.
     */
    @Test
    void testFindByCourtSiteId() {
        Mockito.when(EntityManagerUtil.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);

        // Setup
        XhbDispMgrLocalProxyDao xhbDispMgrLocalProxyDao = new XhbDispMgrLocalProxyDao();
        xhbDispMgrLocalProxyDao.setId(1);
        xhbDispMgrLocalProxyDao.setIpAddress("IpAddress");
        xhbDispMgrLocalProxyDao.setHostName("HostName");
        xhbDispMgrLocalProxyDao.setRagStatus("A");
        xhbDispMgrLocalProxyDao.setRagStatusDate(LocalDateTime.now());
        xhbDispMgrLocalProxyDao.setCreatedBy("User");
        XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao = getDummyXhbDispMgrCourtSiteDao();

        // Expects
        Mockito.when(
            mockEntityManager.find(classUnderTest.getDaoClass(), xhbDispMgrCourtSiteDao.getId()))
            .thenReturn(xhbDispMgrCourtSiteDao);
        Mockito
            .when(mockXhbDispMgrLocalProxyRepository
                .findByCourtSiteId(xhbDispMgrCourtSiteDao.getId()))
            .thenReturn(xhbDispMgrLocalProxyDao);

        // Perform the test
        ICourtSite result = classUnderTest.findByCourtSiteId(xhbDispMgrCourtSiteDao.getId());

        // Verify
        assertNotNull(result, NOT_NULL);
    }

    /**
     * Test findByXhibitCourtSiteId.
     */
    @Test
    void testFindByXhibitCourtSiteId() {
        Mockito.when(EntityManagerUtil.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);

        // Setup
        List<XhbDispMgrCourtSiteDao> xhbDispMgrCourtSiteDaoArray = new ArrayList<>();
        xhbDispMgrCourtSiteDaoArray.add(getDummyXhbDispMgrCourtSiteDao());
        ICourtSite courtSite = getDummyLocalProxy().getCourtSite();

        // Expects
        Mockito
            .when(mockEntityManager
                .createNamedQuery("XHB_DISP_MGR_COURT_SITE.findByXhibitCourtSiteId"))
            .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(xhbDispMgrCourtSiteDaoArray);
        Mockito.when(mockXhbCourtSiteRepository.convertDaoToCourtSiteBasicValue(
            xhbDispMgrCourtSiteDaoArray.get(0).getXhbCourtSiteDao())).thenReturn(courtSite);
        // Perform the test
        ICourtSite result = classUnderTest
            .findByXhibitCourtSiteId(courtSite.getXhibitCourtSite().getId().intValue());

        // Verify
        assertNotNull(result, NOT_NULL);
        assertEquals(result, courtSite, EQUAL);
    }

    /**
     * Test updateDaoFromBasicValue.
     */
    @Test
    void testUpdateDaoFromBasicValue() {
        Mockito.when(EntityManagerUtil.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);

        // Setup
        ILocalProxy localProxy = getDummyLocalProxy();
        List<XhbDispMgrCourtSiteDao> xhbDispMgrCourtSiteDaoArray = new ArrayList<>();
        xhbDispMgrCourtSiteDaoArray.add(getDummyXhbDispMgrCourtSiteDao());
        XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao = new XhbDispMgrCourtSiteDao();
        xhbDispMgrCourtSiteDao.setId(2);

        // Expects
        Mockito
            .when(mockEntityManager
                .createNamedQuery("XHB_DISP_MGR_COURT_SITE.findByXhibitCourtSiteId"))
            .thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(xhbDispMgrCourtSiteDaoArray);

        // Perform the test
        boolean result = false;
        try {
            classUnderTest.updateDaoFromBasicValue(localProxy.getCourtSite());
            result = true;
        } catch (Exception exception) {
            fail(exception.getMessage());
        }

        // Verify
        assertTrue(result, TRUE);
    }

    private ILocalProxy getDummyLocalProxy() {
        IXhibitCourtSite xhibitCourtSite = new XhibitCourtSite();
        xhibitCourtSite.setId(Long.valueOf(1));

        ISchedule schedule = new Schedule();
        schedule.setId(Long.valueOf(1));

        ICourtSite courtSite = new CourtSite();
        courtSite.setXhibitCourtSite(xhibitCourtSite);
        courtSite.setSchedule(schedule);

        ILocalProxy localProxy = new LocalProxy();
        localProxy.setCourtSite(courtSite);
        localProxy.setRagStatusDate(LocalDateTime.now());
        return localProxy;
    }

    private XhbDispMgrCourtSiteDao getDummyXhbDispMgrCourtSiteDao() {
        XhbCourtSiteDao xhbCourtSiteDao = new XhbCourtSiteDao();
        xhbCourtSiteDao.setId(1);
        xhbCourtSiteDao.setCourtSiteName("CourtSiteName");
        xhbCourtSiteDao.setDisplayName("DisplayName");
        xhbCourtSiteDao.setShortName("ShortName");

        XhbDispMgrCduDao xhbDispMgrCduDao = new XhbDispMgrCduDao();
        xhbDispMgrCduDao.setId(1);
        xhbDispMgrCduDao.setCduNumber("CduNumber");
        xhbDispMgrCduDao.setMacAddress("MacAddress");
        xhbDispMgrCduDao.setIpAddress("IpAddress");
        xhbDispMgrCduDao.setTitle("Title");
        xhbDispMgrCduDao.setDescription("Description");
        xhbDispMgrCduDao.setLocation("Location");
        xhbDispMgrCduDao.setRefresh(Long.valueOf(30));
        xhbDispMgrCduDao.setWeighting(Long.valueOf(20));
        xhbDispMgrCduDao.setNotification("Notification");
        xhbDispMgrCduDao.setOfflineInd('Y');
        xhbDispMgrCduDao.setRagStatus('R');
        xhbDispMgrCduDao.setRagStatusDate(LocalDateTime.now());

        List<XhbDispMgrCduDao> xhbDispMgrCduDaoArray = new ArrayList<>();
        xhbDispMgrCduDaoArray.add(xhbDispMgrCduDao);
        
        XhbDispMgrScheduleDao xhbDispMgrScheduleDao = new XhbDispMgrScheduleDao();
        xhbDispMgrScheduleDao.setId(1);
        xhbDispMgrScheduleDao.setScheduleType("Type");
        xhbDispMgrScheduleDao.setTitle("Title");
        xhbDispMgrScheduleDao.setDetail("Detail");
        
        XhbDispMgrCourtSiteDao result = new XhbDispMgrCourtSiteDao();
        result.setId(1);
        result.setTitle("Title");
        result.setNotification("Notification");
        result.setPageUrl("PageUrl");
        result.setRagStatus("G");
        result.setCreatedBy("User");
        result.setLastUpdatedBy(result.getCreatedBy());
        result.setCreationDate(LocalDateTime.now());
        result.setLastUpdateDate(result.getCreationDate());
        result.setRagStatusDate(result.getLastUpdateDate());
        result.setXhbCourtSiteDao(xhbCourtSiteDao);
        result.setXhbDispMgrCduDao(new HashSet<>(xhbDispMgrCduDaoArray));
        result.setXhbDispMgrScheduleDao(xhbDispMgrScheduleDao);
        return result;
    }
}
