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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DashboardCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.EncryptedFormat;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRagStatusService;
import uk.gov.hmcts.pdm.publicdisplay.manager.util.ViewUtil;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduSearchCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies.LocalProxySearchCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class DashboardController.
 *
 * @author uphillj
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    /** The Constant for the JSP Folder. */
    private static final String FOLDER_DASHBOARD = "dashboard";
    private static final String FOLDER_CDUS = "cdus";
    private static final String FOLDER_PROXIES = "proxies";

    /** The Constant VIEW_NAME_CDUS. */
    private static final String VIEW_NAME_CDUS = FOLDER_CDUS + "/cdus";

    /** The Constant VIEW_NAME_LOCAL_PROXY. */
    private static final String VIEW_NAME_LOCAL_PROXY = FOLDER_PROXIES + "/view_localproxy";

    /** The Constant MAPPING_VIEW_DASHBOARD. */
    private static final String MAPPING_VIEW_DASHBOARD = "/dashboard";

    /** The Constant MAPPING_JSON_COURTSITE. */
    private static final String MAPPING_JSON_COURTSITE = "/courtsite";

    /** The Constant VIEW_NAME_DASHBOARD. */
    private static final String VIEW_NAME_DASHBOARD = FOLDER_DASHBOARD + MAPPING_VIEW_DASHBOARD;

    /** The Constant MAPPING_VIEW_SEARCH. */
    private static final String MAPPING_VIEW_SEARCH = "/search";

    private static final Character AMBERCHAR = AppConstants.AMBER_CHAR;

    private static final Character REDCHAR = AppConstants.RED_CHAR;


    /** The rag status reload interval. */
    @Value("#{applicationConfiguration.ragStatusReloadInterval}")
    private Integer ragStatusReloadInterval;

    /**
     * Our localProxyService class.
     */
    @Autowired
    private ILocalProxyService localProxyService;

    /** The rag status service. */
    @Autowired
    private IRagStatusService ragStatusService;

    /**
     * Dashboard.
     *
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_DASHBOARD, method = RequestMethod.GET)
    public ModelAndView dashboard(final ModelAndView model) {
        LOGGER.info("dashboard()");
        model.setViewName(VIEW_NAME_DASHBOARD);
        return getDashboardModelAndView(model);
    }

    /**
     * Get the JSON for the required court site.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @param refreshStatus the refresh status
     * @return the court site as JSON
     */
    @RequestMapping(value = MAPPING_JSON_COURTSITE + "/{xhibitCourtSiteId}",
        method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DashboardCourtSiteDto courtSite(
        @PathVariable("xhibitCourtSiteId") @EncryptedFormat final Long xhibitCourtSiteId,
        @RequestParam(value = "refresh", defaultValue = "false") final boolean refreshStatus) {
        // Refresh the rag statuses of the court site
        if (refreshStatus) {
            try {
                refreshRagStatus(xhibitCourtSiteId);
            } catch (final RestException ex) {
                LOGGER.error("Unable to update local proxy status", ex);
            }
        }

        // Get the requested court site
        return localProxyService.getDashboardCourtSiteByXhibitCourtSiteId(xhibitCourtSiteId);
    }

    /**
     * Search cdu.
     *
     * @param cduSearchCommand the cdu search command
     * @param redirectAttrs the redirect attrs
     * @return the string
     */
    @RequestMapping(value = MAPPING_VIEW_SEARCH, method = RequestMethod.POST)
    public String search(final CduSearchCommand cduSearchCommand,
        final RedirectAttributes redirectAttrs) {
        final String methodName = "search";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        String viewName;

        // Request Attributes for the request mapping in the cdus controller
        redirectAttrs.addAttribute("dashboardSearch", "true");

        // Determine which view we are redirecting to
        if (StringUtils.isBlank(cduSearchCommand.getSelectedMacAddress())) {
            final LocalProxySearchCommand localProxySearchCommand = new LocalProxySearchCommand();
            localProxySearchCommand.setXhibitCourtSiteId(cduSearchCommand.getXhibitCourtSiteId());

            // Model Attributes for the method parameters in the local proxy controller
            redirectAttrs.addFlashAttribute("localProxySearchCommand", localProxySearchCommand);

            // Search for a Local Proxy
            viewName = VIEW_NAME_LOCAL_PROXY;
        } else {
            // Model Attributes for the method parameters in the cdus controller
            redirectAttrs.addFlashAttribute("cduSearchCommand", cduSearchCommand);

            // Search for a CDU
            viewName = VIEW_NAME_CDUS;
        }

        // Return the model
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return ViewUtil.getRedirectViewName(viewName);
    }

    /**
     * Gets the dashboard model and view.
     *
     * @param model the model
     * @return the dashboard model and view
     */
    private ModelAndView getDashboardModelAndView(final ModelAndView model) {
        final String methodName = "getDashboardModelAndView";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Get all xhibit court sites with a local proxy
        final List<XhibitCourtSiteDto> xhibitCourtSites =
            localProxyService.getXhibitCourtSitesOrderedByRagStatus();
        final List<XhibitCourtSiteDto> xhibitCourtSitesRedStatus = new ArrayList<>();
        final List<XhibitCourtSiteDto> xhibitCourtSitesAmberStatus = new ArrayList<>();
        final List<XhibitCourtSiteDto> xhibitCourtSitesGreenStatus = new ArrayList<>();

        // Build the xhibit court sites arrays by RAG status
        for (XhibitCourtSiteDto xhibitCourtSite : xhibitCourtSites) {
            if (xhibitCourtSite.getRagStatus() != null
                && xhibitCourtSite.getRagStatus().equals(REDCHAR.toString())) {
                xhibitCourtSitesRedStatus.add(xhibitCourtSite);
            } else if (xhibitCourtSite.getRagStatus() != null
                && xhibitCourtSite.getRagStatus().equals(AMBERCHAR.toString())) {
                xhibitCourtSitesAmberStatus.add(xhibitCourtSite);
            } else {
                xhibitCourtSitesGreenStatus.add(xhibitCourtSite);
            }
        }

        // Add the court site data to model
        LOGGER.debug("{}{} adding court site data to model", METHOD, methodName);
        model.addObject("courtSiteList", xhibitCourtSites);
        model.addObject("courtSiteListRedStatus", xhibitCourtSitesRedStatus);
        model.addObject("courtSiteListAmberStatus", xhibitCourtSitesAmberStatus);
        model.addObject("courtSiteListGreenStatus", xhibitCourtSitesGreenStatus);
        model.addObject("noOfCourtSiteListRedStatus", xhibitCourtSitesRedStatus.size());
        model.addObject("noOfCourtSiteListAmberStatus", xhibitCourtSitesAmberStatus.size());
        model.addObject("noOfCourtSiteListGreenStatus", xhibitCourtSitesGreenStatus.size());

        // Add the overall status to the model
        model.addObject("overAllStatus", ragStatusService.getRagStatusOverall());

        // Add the countdown time for page reload to the model
        model.addObject("countdown", ragStatusReloadInterval);

        // Return the model
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Refresh the rag status of the court site.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @throws RestException the rest exception
     */
    private void refreshRagStatus(final Long xhibitCourtSiteId) {
        final String methodName = "refreshRagStatus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        ragStatusService.refreshRagStatus(xhibitCourtSiteId);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }
}
