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
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl.XhbDispMgrUrlRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.UrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UrlDto;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class UrlServiceTest.
 *
 * @author pattersone
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
class UrlServiceTest extends AbstractJUnit {

    private static final String NOTNULL = "Result is null";

    /** The class under test. */
    private UrlService classUnderTest;

    /** The mock disp mgr url repo. */
    private XhbDispMgrUrlRepository mockUrlRepo;

    private EntityManager mockEntityManager;

    /** The urls. */
    private final List<IUrlModel> urls = getTestUrls();

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test (UrlService)
        classUnderTest = new UrlService();

        // Setup the mock version of the UrlDao
        mockUrlRepo = createMock(XhbDispMgrUrlRepository.class);
        mockEntityManager = createMock(EntityManager.class);

        // Map the mock (mockUrlDao) to the class under tests called class (urlDao)
        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrUrlRepository", mockUrlRepo);

    }

    /**
     * Test get urls by xhibit court site id.
     */
    @Test
    void testGetUrlsByXhibitCourtSiteId() {
        // Local variables
        final Long xhibitCourtSiteId = 1L;

        // Add the mock calls to child classes
        expect(mockUrlRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockUrlRepo.findByCourtSiteId(xhibitCourtSiteId.intValue())).andReturn(urls);
        
        replay(mockUrlRepo);
        replay(mockEntityManager);

        // Perform the test
        final List<UrlDto> results = classUnderTest.getUrlsByXhibitCourtSiteId(xhibitCourtSiteId);

        // Assert that the objects are as expected
        assertNotNull(results, "Null");
        assertEquals(results.size(), urls.size(), "Not equal");

        // Verify the expected mocks were called
        verify(mockUrlRepo);
        verify(mockEntityManager);
    }

    @Test
    void testEntityManager() {
        UrlService localClassUnderTest = new UrlService() {

            @Override
            public EntityManager getEntityManager() {
                return super.getEntityManager();
            }
        };
        ReflectionTestUtils.setField(localClassUnderTest, "entityManager", mockEntityManager);
        expect(mockEntityManager.isOpen()).andReturn(true);
        mockEntityManager.close();
        replay(mockEntityManager);
        try (EntityManager result = localClassUnderTest.getEntityManager()) {
            assertNotNull(result, NOTNULL);
        }
    }

    @Test
    void testGetXhbDispMgrUrlRepository() {
        expect(mockUrlRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        
        replay(mockUrlRepo);
        replay(mockEntityManager);
        
        XhbDispMgrUrlRepository result = classUnderTest.getXhbDispMgrUrlRepository();
        assertNotNull(result, NOTNULL);

        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrUrlRepository", mockUrlRepo);
        result = classUnderTest.getXhbDispMgrUrlRepository();
        assertNotNull(result, NOTNULL);
        
        verify(mockUrlRepo);
        verify(mockEntityManager);
    }

    /**
     * Gets the test url.
     *
     * @param urlId the url id
     * @return the test url
     */
    private IUrlModel getTestUrl(final Long urlId) {
        final IUrlModel url = new UrlModel();
        url.setId(urlId);
        url.setUrl("url" + urlId.toString());
        url.setDescription("Description" + urlId.toString());
        return url;
    }

    /**
     * Gets the test urls.
     *
     * @return the test urls
     */
    private List<IUrlModel> getTestUrls() {
        final List<IUrlModel> urls = new ArrayList<>();
        urls.add(getTestUrl(1L));
        urls.add(getTestUrl(2L));
        return urls;
    }

}
