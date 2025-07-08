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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.dashboard;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DashboardCduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DashboardCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.LocalProxyService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.RagStatusService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduSearchCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class DashboardControllerTest.
 *
 * @author boparaij
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class DashboardControllerTest extends AbstractJUnit {
    /** The Constant XHIBIT_COURT_SITE_ID. */
    protected static final Long XHIBIT_COURT_SITE_ID = 1L;

    /** The Constant MACADDRESS. */
    protected static final String MACADDRESS = "MACADDRESS";

    protected static final String NOT_EQUAL = "Not equal";

    protected static final String FALSE = "False";

    protected static final String NULL = "Null";

    /** The constant for the red character. */
    public static final Character RED_CHAR = 'R';

    /** The mapping name dashboard. */
    protected String mappingNameDashboard;

    /** The view name dashboard. */
    protected String viewNameDashboard;

    /** The mapping name search. */
    protected String mappingNameSearch;

    /** The view name cdus. */
    protected String viewNameCdus;

    /** The class under test. */
    protected DashboardController classUnderTest;

    /** The mock local proxy service. */
    protected LocalProxyService mockLocalProxyService;

    /** The mock rag status service. */
    protected RagStatusService mockRagStatusService;

    /** The mock mvc. */
    protected MockMvc mockMvc;

    /** The test xhibit court sites. */
    protected final List<XhibitCourtSiteDto> testXhibitCourtSites = getTestXhibitCourtSites();

    /** The test xhibit court sites dashboard. */
    protected final DashboardCourtSiteDto testXhibitCourtSitesDashboard = getTestXhibitCourtSite();

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new DashboardController();

        // Setup the mock version of the called classes
        mockLocalProxyService = createMock(LocalProxyService.class);
        mockRagStatusService = createMock(RagStatusService.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "localProxyService", mockLocalProxyService);
        ReflectionTestUtils.setField(classUnderTest, "ragStatusService", mockRagStatusService);

        // Get the static variables from the class under test
        mappingNameDashboard =
            (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_VIEW_DASHBOARD");
        viewNameDashboard =
            (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_DASHBOARD");
        mappingNameSearch = mappingNameDashboard
            + (String) ReflectionTestUtils.getField(classUnderTest, "MAPPING_VIEW_SEARCH");
        viewNameCdus = (String) ReflectionTestUtils.getField(classUnderTest, "VIEW_NAME_CDUS");

        // Stop circular view path error
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view");
        viewResolver.setSuffix(".jsp");

        // Setup the mock version of the modelMvc
        mockMvc =
            MockMvcBuilders.standaloneSetup(classUnderTest).setViewResolvers(viewResolver).build();
    }

    /**
     * Test dashboard view.
     *
     * @throws Exception the exception
     */
    @Test
    void testDashboardView() throws Exception {

        expect(mockLocalProxyService.getXhibitCourtSitesOrderedByRagStatus())
            .andReturn(testXhibitCourtSites);
        replay(mockLocalProxyService);
        expect(mockRagStatusService.getRagStatusOverall()).andReturn(RED_CHAR.toString());
        replay(mockRagStatusService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameDashboard + mappingNameDashboard)).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameDashboard);

        verify(mockLocalProxyService);
        verify(mockRagStatusService);
    }

    /**
     * Test search cdu.
     *
     * @throws Exception the exception
     */
    @Test
    void testSearchCdu() throws Exception {
        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameSearch).param("xhibitCourtSiteId", XHIBIT_COURT_SITE_ID.toString())
                .param("selectedMacAddress", MACADDRESS))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameCdus);
        assertEquals("true", results.getModelAndView().getModelMap().get("dashboardSearch"),
            NOT_EQUAL);
        assertCduSearchCommand(results.getFlashMap(), XHIBIT_COURT_SITE_ID, MACADDRESS);
    }

    /**
     * Assert view name.
     *
     * @param results the results
     * @param viewName the view name
     */
    protected void assertViewName(final MvcResult results, final String viewName) {
        assertNotNull(results, NULL);
        final String actualViewName = results.getModelAndView().getViewName();
        if (actualViewName.startsWith("forward:") || actualViewName.startsWith("redirect:")) {
            assertTrue(actualViewName.contains(viewName), FALSE);
        } else {
            assertEquals(actualViewName, viewName, NOT_EQUAL);
        }
    }

    /**
     * Gets the test xhibit court sites.
     *
     * @param id the id
     * 
     * @return the test xhibit court sites
     */
    protected XhibitCourtSiteDto getTestXhibitCourtSite(final Long id, Character ragStatus) {
        final XhibitCourtSiteDto courtSite = new XhibitCourtSiteDto();
        courtSite.setId(id);
        courtSite.setRagStatus(ragStatus.toString());
        return courtSite;
    }

    /**
     * Gets the test xhibit court site.
     *
     * @return the test xhibit court site
     */
    protected DashboardCourtSiteDto getTestXhibitCourtSite() {
        final DashboardCourtSiteDto xhibitCourtSite = new DashboardCourtSiteDto();
        xhibitCourtSite.setCdus(getTestCdus());
        return xhibitCourtSite;
    }

    /**
     * Gets the test xhibit court sites.
     *
     * @return the test xhibit court sites
     */
    protected List<XhibitCourtSiteDto> getTestXhibitCourtSites() {
        final List<XhibitCourtSiteDto> courtSites = new ArrayList<>();
        courtSites.add(getTestXhibitCourtSite(1L, AppConstants.RED_CHAR));
        courtSites.add(getTestXhibitCourtSite(2L, AppConstants.AMBER_CHAR));
        courtSites.add(getTestXhibitCourtSite(3L, AppConstants.GREEN_CHAR));
        return courtSites;
    }

    /**
     * Gets the test cdu.
     *
     * @param id the id
     * @return the test cdu
     */
    protected DashboardCduDto getTestCdu(final Long id) {
        final DashboardCduDto cdu = new DashboardCduDto();
        cdu.setCduNumber("CDU_" + id);
        cdu.setIpAddress("IPADDRESS_" + id);
        return cdu;
    }

    /**
     * Gets the test cdus.
     *
     * @return the test cdus
     */
    protected List<DashboardCduDto> getTestCdus() {
        final List<DashboardCduDto> cdus = new ArrayList<>();

        cdus.add(getTestCdu(1L));
        cdus.add(getTestCdu(2L));

        return cdus;
    }

    /**
     * Assert cdu search command.
     *
     * @param flashMap the flash map
     * @param xhibitCourtSiteId the xhibit court site id
     * @param macAddress the mac address
     */
    protected void assertCduSearchCommand(final Map flashMap, final Long xhibitCourtSiteId,
        final String macAddress) {
        final Object searchCommand = flashMap.get("cduSearchCommand");
        assertTrue(searchCommand instanceof CduSearchCommand, FALSE);
        assertEquals(((CduSearchCommand) searchCommand).getXhibitCourtSiteId(), xhibitCourtSiteId,
            NOT_EQUAL);
        assertEquals(((CduSearchCommand) searchCommand).getSelectedMacAddress(), macAddress,
            NOT_EQUAL);
    }
}
