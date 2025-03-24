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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.List;

/**
 * The Class LocalProxyController.
 *
 * @author uphillj
 */

@Controller
@RequestMapping("/proxies")
public class LocalProxyController extends LocalProxyPageStateSetter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalProxyController.class);

    private static final String ADDING_COURTSITE_TO_MODEL = "{}{} adding court site data to model";
    private static final String ADDING_SCHEDULE_TO_MODEL = "{}{} adding schedule data to model";
    private static final String COURTSITE = "courtSite";
    private static final String COURTSITE_LIST = "courtSiteList";
    private static final String LOCALPROXYERRORS = "localProxyErrors";
    private static final String SCHEDULE_LIST = "scheduleList";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String COMMAND = "command";

    /** The Constant for the JSP Folder. */
    private static final String FOLDER_PROXIES = "proxies";
    
    /**
     * View Local Proxy Url.
     */
    private static final String MAPPING_VIEW_LOCAL_PROXY = "/view_localproxy";

    /**
     * View Local Proxy View.
     */
    private static final String VIEW_NAME_VIEW_LOCAL_PROXY =
        FOLDER_PROXIES + MAPPING_VIEW_LOCAL_PROXY;

    /**
     * Amend Local Proxy Url.
     */
    private static final String MAPPING_AMEND_LOCAL_PROXY = "/amend_localproxy";

    /**
     * Amend Local Proxy View.
     */
    private static final String VIEW_NAME_AMEND_LOCAL_PROXY =
        FOLDER_PROXIES + MAPPING_AMEND_LOCAL_PROXY;

    /**
     * Register Local Proxy Url.
     */
    private static final String MAPPING_REGISTER_LOCAL_PROXY = "/register_localproxy";

    /**
     * Register Local Proxy View.
     */
    private static final String VIEW_NAME_REGISTER_LOCAL_PROXY =
        FOLDER_PROXIES + MAPPING_REGISTER_LOCAL_PROXY;

    /**
     * The local proxy register validator.
     */
    @Autowired
    private LocalProxyRegisterValidator localProxyRegisterValidator;

    /**
     * The local proxy amend validator.
     */
    @Autowired
    private LocalProxyAmendValidator localProxyAmendValidator;

    /**
     * The local proxy selected validator.
     */
    @Autowired
    private LocalProxySelectedValidator localProxySelectedValidator;

    /**
     * The local proxy unregister validator.
     */
    @Autowired
    private LocalProxyUnregisterValidator localProxyUnregisterValidator;

    /**
     * View local proxy.
     *
     * @param model the model
     * @param reset the reset
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_LOCAL_PROXY, method = RequestMethod.GET)
    public ModelAndView viewLocalProxy(final ModelAndView model,
        @RequestParam(value = "reset", defaultValue = "true") final boolean reset) {
        final String methodName = "viewLocalProxy";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        LocalProxySearchCommand localProxySearchCommand = new LocalProxySearchCommand();
        if (reset || localProxyPageStateHolder.getLocalProxySearchCommand() == null) {
            LOGGER.debug("{}{} - reset local proxy search results", METHOD, methodName);
            localProxyPageStateHolder.reset();

            // retrieve and add the selection lists to the pageStateHolder
            setPageStateSelectionLists(true);
        } else {
            localProxySearchCommand = localProxyPageStateHolder.getLocalProxySearchCommand();
        }

        // Add the localProxySearchCommand to the model
        LOGGER.debug("{}{} adding localProxySearchCommand to model", METHOD, methodName);
        model.addObject(COMMAND, localProxySearchCommand);

        // Add the court site data to model
        LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
        model.addObject(COURTSITE_LIST, localProxyPageStateHolder.getSites());

        // Return the model
        LOGGER.debug("{}{} returning model", METHOD, methodName);
        model.setViewName(VIEW_NAME_VIEW_LOCAL_PROXY);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * viewLocalProxy.
     * 
     * @param model the model
     * @param localProxySearchCommand as LocalProxySearchCommand Command object.
     * @param result the BindingResult
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_LOCAL_PROXY, method = RequestMethod.POST,
        params = "viewlocalproxy")
    public ModelAndView viewLocalProxy(@Valid final LocalProxySearchCommand localProxySearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "viewLocalProxy";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        localProxyPageStateHolder.setLocalProxySearchCommand(localProxySearchCommand);

        // Retrieve and add the court sites & schedules to the pageStateHolder
        setPageStateSelectionLists(true);
        
        // Set the model and view for the search Local Proxy
        setModelForLocalProxyView(localProxySearchCommand, result, model);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show amend local proxy.
     *
     * @param localProxySearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_LOCAL_PROXY, method = RequestMethod.POST,
        params = "btnAmend")
    public ModelAndView showAmendLocalProxy(
        @Valid final LocalProxySearchCommand localProxySearchCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "showAmendLocalProxy";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        localProxyPageStateHolder.setLocalProxySearchCommand(localProxySearchCommand);
        
        // Retrieve and add the court sites & schedules to the pageStateHolder
        setPageStateSelectionLists(true);
        
        localProxySelectedValidator.validate(localProxySearchCommand, result);
        if (result.hasErrors()) {
            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, localProxyPageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_LOCAL_PROXY);

        } else {

            // Get the selected CourtSite
            final CourtSiteDto courtSite = populateSelectedCourtSiteInPageStateHolder(
                localProxySearchCommand.getXhibitCourtSiteId());

            // Populate the relevant fields
            LocalProxyAmendCommand localProxyCommand = new LocalProxyAmendCommand();
            localProxyCommand.setCourtSiteId(localProxySearchCommand.getXhibitCourtSiteId());
            localProxyCommand.setTitle(courtSite.getTitle());
            localProxyCommand.setScheduleId(courtSite.getScheduleId());
            localProxyCommand.setNotification(courtSite.getNotification());

            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, localProxyCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_AMEND_LOCAL_PROXY);
        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());

        // Add the schedule data to model
        LOGGER.debug(ADDING_SCHEDULE_TO_MODEL, METHOD, methodName);
        model.addObject(SCHEDULE_LIST, localProxyPageStateHolder.getSchedules());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Update local proxy.
     *
     * @param localProxyAmendCommand the local proxy amend command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_AMEND_LOCAL_PROXY, method = RequestMethod.POST,
        params = "btnUpdateConfirm")
    public ModelAndView updateLocalProxy(@Valid final LocalProxyAmendCommand localProxyAmendCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "updateLocalProxy";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Default is to return to the amend local proxy page to display errors
        model.setViewName(VIEW_NAME_AMEND_LOCAL_PROXY);

        // Retrieve and add the court sites & schedules to the pageStateHolder
        setPageStateSelectionLists(true);
        
        // Refreshing the courtSiteDto
        populateSelectedCourtSiteInPageStateHolder(localProxyAmendCommand.getCourtSiteId());
        
        localProxyAmendValidator.validate(localProxyAmendCommand, result);
        if (!result.hasErrors()) {
            try {
                LOGGER.debug("{}{} - updating Local Proxy", METHOD, methodName);
                localProxyService.updateLocalProxy(localProxyPageStateHolder.getCourtSite(),
                    localProxyAmendCommand);

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Local proxy has been updated successfully.");

                // Successfully registered so redirect to the local proxy page which needs a new
                // model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewLocalProxy(model, false);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to update Local Proxy ", METHOD, methodName, ex);
                // Reject
                result.reject("cduErrors", "Unable to update Local Proxy: " + ex.getMessage());
            }

        }

        LOGGER.debug("{}{} adding selected court site to model", METHOD, methodName);
        model.addObject(COURTSITE, localProxyPageStateHolder.getCourtSite());

        // Add the schedule data to model
        LOGGER.debug(ADDING_SCHEDULE_TO_MODEL, METHOD, methodName);
        model.addObject(SCHEDULE_LIST, localProxyPageStateHolder.getSchedules());
        model.addObject(COMMAND, localProxyAmendCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * unregisterLocalProxy.
     * 
     * @param model the model
     * @param localProxySearchCommand LocalProxySearchCommand object.
     * @param result as BindingResult
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_LOCAL_PROXY, method = RequestMethod.POST,
        params = "unregisterConfirm")
    public ModelAndView unregisterLocalProxy(
        @Valid final LocalProxySearchCommand localProxySearchCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "unregisterlocalproxy";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        localProxyPageStateHolder.setLocalProxySearchCommand(localProxySearchCommand);

        // Business Specific validation for local proxy
        localProxyUnregisterValidator.validate(localProxySearchCommand, result);
        if (!result.hasErrors()) {
            // Perform unregister - get the courtSite
            LOGGER.debug("{}{} unregistering proxy for site", METHOD, methodName);

            try {
                LOGGER.debug("{}{} obtaining translated court site id via courtsite object", METHOD,
                    methodName);
                final CourtSiteDto courtSite = localProxyService.getCourtSiteByXhibitCourtSiteId(
                    localProxySearchCommand.getXhibitCourtSiteId());

                LOGGER.debug("{}{} Xhibit court site vs Other Court site ID", METHOD, methodName);

                localProxyService.unregisterLocalProxy(courtSite.getId());

                LOGGER.debug("{}{} Local proxy deleted successfully", METHOD, methodName);
                model.addObject(SUCCESS_MESSAGE, "Local proxy deleted successfully");
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to delete local proxy", METHOD, methodName, ex);
                // Reject
                result.reject(LOCALPROXYERRORS,
                    "Unable to unregister local proxy: " + ex.getMessage());
            }
        }

        // retrieve and add the court sites to the pageStateHolder
        setPageStateXhibitCourtSites(true);

        // Add the court site data to model
        LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
        model.addObject(COURTSITE_LIST, localProxyPageStateHolder.getSites());

        model.addObject(COMMAND, localProxySearchCommand);

        model.setViewName(VIEW_NAME_VIEW_LOCAL_PROXY);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * View local proxy from dashboard.
     *
     * @param localProxySearchCommand the local proxy search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_LOCAL_PROXY, method = RequestMethod.GET,
        params = "dashboardSearch")
    public ModelAndView viewLocalProxyFromDashboard(
        @Valid final LocalProxySearchCommand localProxySearchCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "viewLocalProxyFromDashboard";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        localProxyPageStateHolder.setLocalProxySearchCommand(localProxySearchCommand);

        // retrieve and add the court sites to the pageStateHolder
        setPageStateSelectionLists(true);

        // Set the model and view for the search Local Proxy
        setModelForLocalProxyView(localProxySearchCommand, result, model);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * registerLocalProxy.
     * 
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_REGISTER_LOCAL_PROXY, method = RequestMethod.GET)
    public ModelAndView registerLocalProxy(final ModelAndView model) {
        final String methodName = "registerlocalproxy";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Add a new localProxyCommand to the model
        model.addObject(COMMAND, new LocalProxyRegisterCommand());
        LOGGER.debug("{}{} command object - localProxyCommand created and added to model", METHOD,
            methodName);

        // retrieve and add the court sites to the pageStateHolder
        setPageStateSelectionLists(false);

        // Add the court site data to model
        LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
        model.addObject(COURTSITE_LIST, localProxyPageStateHolder.getSites());

        // Add the schedule data to model
        LOGGER.debug(ADDING_SCHEDULE_TO_MODEL, METHOD, methodName);
        model.addObject(SCHEDULE_LIST, localProxyPageStateHolder.getSchedules());

        model.setViewName(VIEW_NAME_REGISTER_LOCAL_PROXY);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * registerLocalProxy.
     * 
     * @param localProxyRegisterCommand as Local Proxy Command Object
     * @param result as BindingResult
     * @param model the model
     * @return model and view
     */
    @RequestMapping(value = MAPPING_REGISTER_LOCAL_PROXY, method = RequestMethod.POST,
        params = "registerlocalproxy")
    public ModelAndView registerLocalProxy(
        @Valid final LocalProxyRegisterCommand localProxyRegisterCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "registerlocalproxy (POST)";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Business Specific validation for local proxy
        localProxyRegisterValidator.validate(localProxyRegisterCommand, result);
        if (!result.hasErrors()) {
            try {
                LOGGER.info("{}{} - no binding errors, starting registration process", METHOD,
                    methodName);
                localProxyService.registerLocalProxy(localProxyRegisterCommand);

                LOGGER.debug("{}{} Local proxy registered successfully", METHOD, methodName);
                model.addObject(SUCCESS_MESSAGE, "Local proxy registered successfully");

                LOGGER.debug("{}{} zero out the command object fields to prepare for a new request",
                    METHOD, methodName);
                localProxyRegisterCommand.reset();
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to add a local proxy", METHOD, methodName, ex);
                // Reject
                result.reject(LOCALPROXYERRORS,
                    "Unable to register local proxy: " + ex.getMessage());
            }
        }

        // retrieve and add the court sites to the pageStateHolder
        setPageStateSelectionLists(false);

        // Add the court site data to model
        LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
        model.addObject(COURTSITE_LIST, localProxyPageStateHolder.getSites());

        // Add the schedule data to model
        LOGGER.debug(ADDING_SCHEDULE_TO_MODEL, METHOD, methodName);
        model.addObject(SCHEDULE_LIST, localProxyPageStateHolder.getSchedules());

        LOGGER.debug("{}{} adding localProxyRegisterCommand to model", METHOD, methodName);
        model.addObject(COMMAND, localProxyRegisterCommand);

        model.setViewName(VIEW_NAME_REGISTER_LOCAL_PROXY);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Gets the search for local proxy model.
     *
     * @param localProxySearchCommand the local proxy search command
     * @param result the result
     * @param model the model
     */
    private void setModelForLocalProxyView(final LocalProxySearchCommand localProxySearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "setModelForLocalProxyView";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        localProxySelectedValidator.validate(localProxySearchCommand, result);
        if (!result.hasErrors()) {
            // Get the details for the selected court site
            LOGGER.debug(
                "{}{} Grab the Court Site & associated local Proxy using xhibitCourtSIteId",
                METHOD, methodName);

            final CourtSiteDto courtSite = localProxyService
                .getCourtSiteByXhibitCourtSiteId(localProxySearchCommand.getXhibitCourtSiteId());

            // Add the court site details to the model
            model.addObject(COURTSITE, courtSite);
        }

        // retrieve the court sites
        final List<XhibitCourtSiteDto> courtSiteList =
            localProxyService.getXhibitCourtSitesWithLocalProxy();

        // Add the court site data to model
        LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
        model.addObject(COURTSITE_LIST, courtSiteList);

        // Hand back the view
        LOGGER.debug("{}{} returning view ", METHOD, methodName);
        model.setViewName(VIEW_NAME_VIEW_LOCAL_PROXY);

        LOGGER.debug("{}{} adding localProxySearchCommand to model", METHOD, methodName);
        model.addObject(COMMAND, localProxySearchCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

}
