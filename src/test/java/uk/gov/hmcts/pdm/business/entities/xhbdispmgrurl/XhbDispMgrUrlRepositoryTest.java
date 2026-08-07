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

package uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl;


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
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping.XhbDispMgrMappingDao;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for XhbDispMgrUrlRepositoryTest.
 *
 * @author gittinsl
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("PMD.SingularField")
class XhbDispMgrUrlRepositoryTest extends AbstractJUnit {

    private static final String NULL = "Result is Null";
    private static final String IPADDRESS = "192.168.1.";


    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private Query mockQuery;

    @InjectMocks
    private XhbDispMgrUrlRepository classUnderTest = new XhbDispMgrUrlRepository(mockEntityManager);

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        mockEntityManager = Mockito.mock(EntityManager.class);
        classUnderTest = new XhbDispMgrUrlRepository(mockEntityManager);
    }

    @Test
    void testFindUrlFromMappingDao() {
        // Setup Objects
        XhbDispMgrUrlDao urlDao = new XhbDispMgrUrlDao();
        urlDao.setId(1);
        urlDao.setUrl("http://example.com");
        urlDao.setDescription("Example URL");
        
        XhbCourtSiteDao courtSiteDao = new XhbCourtSiteDao();
        courtSiteDao.setId(1);
        courtSiteDao.setCourtSiteName("Example Court Site");
        courtSiteDao.setDisplayName("Example Display Name");
        courtSiteDao.setShortName("Example Short Name");
        
        XhbDispMgrCourtSiteDao dispMgrCourtSiteDao = new XhbDispMgrCourtSiteDao();
        dispMgrCourtSiteDao.setId(1);
        dispMgrCourtSiteDao.setTitle("Example Title");
        dispMgrCourtSiteDao.setNotification("Example Note");
        dispMgrCourtSiteDao.setPageUrl("http://example.com/page");
        dispMgrCourtSiteDao.setRagStatus("Green");
        dispMgrCourtSiteDao.setRagStatusDate(LocalDateTime.now());
  
        
        XhbDispMgrLocalProxyDao localProxyDao = new XhbDispMgrLocalProxyDao();
        localProxyDao.setId(1);
        localProxyDao.setIpAddress(IPADDRESS + "1");
        localProxyDao.setHostName("example-host");
        localProxyDao.setRagStatus("Green");
        localProxyDao.setRagStatusDate(LocalDateTime.now());
        localProxyDao.setCreatedBy("test-user");
        
        // Set up relationships
        dispMgrCourtSiteDao.setXhbDispMgrLocalProxyDao(localProxyDao);
        Set<XhbDispMgrCourtSiteDao> dispMgrCourtSiteDaos = Set.of(dispMgrCourtSiteDao);
        courtSiteDao.setXhbDispMgrCourtSiteDao(dispMgrCourtSiteDaos);
        urlDao.setXhbCourtSiteDao(courtSiteDao);
        XhbDispMgrMappingDao mappingDao = new XhbDispMgrMappingDao();
        mappingDao.setXhbDispMgrUrlDao(urlDao);
        
        // Run
        IUrlModel urlModel = classUnderTest.findUrlFromMappingDao(mappingDao);
        
        // Verify
        assertNotNull(urlModel, NULL);
    }
    
    @Test
    void testGetUrlFromDao() {
        // Setup Objects
        XhbDispMgrUrlDao urlDao = new XhbDispMgrUrlDao();
        urlDao.setId(1);
        urlDao.setUrl("http://example.com");
        urlDao.setDescription("Example URL");
        urlDao.setXhbDispMgrMappingDao(new XhbDispMgrMappingDao());
        
        // Run
        IUrlModel urlModel = classUnderTest.getUrlFromDao(urlDao);
        
        // Verify
        assertNotNull(urlModel, NULL);
        assertNotNull(urlDao.getXhbDispMgrMappingDao(), NULL);
    }
}
