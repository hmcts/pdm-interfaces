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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.display;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayLocationDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.EncryptedFormat;

/**
 * The Class DisplayController.
 *
 * @author harrism
 */

@Controller
@RequestMapping("/display")
@SuppressWarnings("PMD.TooManyMethods")
public class DisplayController extends DisplayPageStateSetter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayController.class);

    private static final String ADDING_COURTSITE_TO_MODEL = "{}{} adding court site data to model";
    private static final String COURTSITE = "courtSite";
    private static final String COURTSITE_LIST = "courtSiteList";
    private static final String DISPLAY_LIST = "displayList";
    private static final String DISPLAY_TYPE_LIST = "displayTypeList";
    private static final String ROTATION_SET_LIST = "rotationSetList";
    private static final String COMMAND = "command";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String ATTEMPT = "Attempt {}{}";
    private static final String POPULATING_COURTSITES = ", populating the CourtSites list";
    private static final String COURTSITES_POPULATED = "CourtSites list populated";
    private static final String POPULATING_PAGESTATE_LISTS =
        ", populating the PageStateSelectionLists";
    private static final String PAGESTATE_LISTS_POPULATED = "All PageStateSelectionLists populated";

    private static final int MAX_NUM_OF_RETRIES = 5;

    /** The Constant for the JSP Folder. */
    private static final String FOLDER_DISPLAY = "display";

    /**
     * View Display.
     */
    private static final String MAPPING_VIEW_DISPLAY = "/view_display";

    /**
     * View Display View.
     */
    private static final String VIEW_NAME_VIEW_DISPLAY = FOLDER_DISPLAY + MAPPING_VIEW_DISPLAY;

    /**
     * Amend Display.
     */
    private static final String MAPPING_AMEND_DISPLAY = "/amend_display";

    /**
     * Amend Display View.
     */
    private static final String VIEW_NAME_AMEND_DISPLAY = FOLDER_DISPLAY + MAPPING_AMEND_DISPLAY;

    /**
     * Create Display.
     */
    private static final String MAPPING_CREATE_DISPLAY = "/create_display";

    /**
     * Create Display View.
     */
    private static final String VIEW_NAME_CREATE_DISPLAY = FOLDER_DISPLAY + MAPPING_CREATE_DISPLAY;

    /**
     * Delete Display.
     */
    private static final String MAPPING_DELETE_DISPLAY = "/delete_display";

    /**
     * Delete Display View.
     */
    private static final String VIEW_NAME_DELETE_DISPLAY = FOLDER_DISPLAY + MAPPING_DELETE_DISPLAY;

    /**
     * The display selected validator.
     */
    @Autowired
    private DisplaySelectedValidator displaySelectedValidator;

    /**
     * The display amend validator.
     */
    @Autowired
    private DisplayAmendValidator displayAmendValidator;

    /**
     * The display create validator.
     */
    @Autowired
    private DisplayCreateValidator displayCreateValidator;

    /**
     * The display delete validator.
     */
    @Autowired
    private DisplayDeleteValidator displayDeleteValidator;

    /**
     * View display.
     *
     * @param model the model
     * @param reset the reset
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_DISPLAY, method = RequestMethod.GET)
    public ModelAndView viewDisplay(final ModelAndView model,
        @RequestParam(value = "reset", defaultValue = "true") final boolean reset) {
        final String methodName = "viewDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        DisplaySearchCommand displaySearchCommand = new DisplaySearchCommand();
        if (reset || displayPageStateHolder.getDisplaySearchCommand() == null) {
            LOGGER.debug("{}{} - reset display search results", METHOD, methodName);
            displayPageStateHolder.reset();

            // retrieve and add the selection lists to the pageStateHolder
            setViewPageStateSelectionLists();
        } else {
            displaySearchCommand = displayPageStateHolder.getDisplaySearchCommand();
        }

        // Add the displaySearchCommand to the model
        LOGGER.debug("{}{} adding displaySearchCommand to model", METHOD, methodName);
        model.addObject(COMMAND, displaySearchCommand);

        // Add the court site data to model
        LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
        model.addObject(COURTSITE_LIST, displayPageStateHolder.getSites());

        // Return the model
        LOGGER.debug("{}{} returning model", METHOD, methodName);
        model.setViewName(VIEW_NAME_VIEW_DISPLAY);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show amend display.
     *
     * @param displaySearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_DISPLAY, method = RequestMethod.POST, params = "btnAmend")
    public ModelAndView showAmendDisplay(@Valid final DisplaySearchCommand displaySearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showAmendDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        displayPageStateHolder.setDisplaySearchCommand(displaySearchCommand);

        displaySelectedValidator.validate(displaySearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, displayPageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_DISPLAY);
            model.addObject(COMMAND, displaySearchCommand);

        } else {
            // Populate the lists and set CourtSite
            final XhibitCourtSiteDto courtSite =
                populateListsAndSetCourtSite(displaySearchCommand, true);

            // Populate the relevant fields
            final DisplayAmendCommand displayCommand = new DisplayAmendCommand();
            displayCommand.setXhibitCourtSiteId(courtSite.getId());

            // Populate the model objects
            model.addObject(COURTSITE_LIST,
                displayPageStateHolder.getSitesBySelectedCourt(courtSite.getCourtId()));
            model.addObject(DISPLAY_LIST, displayPageStateHolder.getDisplays());
            model.addObject(DISPLAY_TYPE_LIST, displayPageStateHolder.getDisplayTypes());
            model.addObject(ROTATION_SET_LIST, displayPageStateHolder.getRotationSets());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, displayCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_AMEND_DISPLAY);
        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Load display.
     *
     * @param displayId the display id
     * @return the selected DisplayDto
     */
    @RequestMapping(value = MAPPING_AMEND_DISPLAY + "/{displayId}", method = RequestMethod.GET,
        produces = "application/json")
    @ResponseBody
    public DisplayDto loadDisplay(
        @PathVariable("displayId") @EncryptedFormat final Integer displayId) {
        final String methodName = "loadDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        // Reload the courtsites
        displayPageStateHolder.setSites(displayService.getCourtSites());
        // Reload the display types
        displayPageStateHolder.setDisplayTypes(displayService.getDisplayTypes());
        DisplayDto result = null;
        if (displayId != null) {
            result = displayService.getDisplay(displayId);
        }
        if (result != null) {
            // Fetch the display location
            DisplayLocationDto displayLocation =
                displayService.getDisplayLocation(result.getDisplayLocationId());
            if (displayLocation != null) {
                // Determine the court site from the display location
                Long xhibitCourtSiteId = Long.valueOf(displayLocation.getCourtSiteId());
                // Get the courtSite from the courtSite id
                XhibitCourtSiteDto courtSite = displayService
                    .getCourtSite(displayPageStateHolder.getSites(), xhibitCourtSiteId);
                if (courtSite != null) {
                    // Set the courtSite object for the returned display
                    result.setCourtSite(courtSite);
                    // Reload the rotation sets
                    displayPageStateHolder
                        .setRotationSets(displayService.getRotationSets(courtSite.getCourtId()));
                }
                // Reload the displays
                displayPageStateHolder.setDisplays(displayService.getDisplays(xhibitCourtSiteId,
                    displayPageStateHolder.getDisplayTypes(), displayPageStateHolder.getSites(),
                    displayPageStateHolder.getRotationSets()));
            }
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    /**
     * Update display.
     *
     * @param displayAmendCommand the display amend command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_AMEND_DISPLAY, method = RequestMethod.POST,
        params = "btnUpdateConfirm")
    public ModelAndView updateDisplay(@Valid final DisplayAmendCommand displayAmendCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "updateDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        displayAmendValidator.validate(displayAmendCommand, result);
        if (result.hasErrors()) {
            // Default is to return to the amend display page to display errors
            model.setViewName(VIEW_NAME_AMEND_DISPLAY);
            XhibitCourtSiteDto courtSite = displayPageStateHolder.getCourtSite();

            model.addObject(COURTSITE_LIST,
                displayPageStateHolder.getSitesBySelectedCourt(courtSite.getCourtId()));
            model.addObject(DISPLAY_LIST, displayPageStateHolder.getDisplays());
            model.addObject(DISPLAY_TYPE_LIST, displayPageStateHolder.getDisplayTypes());
            model.addObject(ROTATION_SET_LIST, displayPageStateHolder.getRotationSets());
        } else {
            try {
                LOGGER.debug("{}{} - updating Display", METHOD, methodName);


                displayService.updateDisplay(displayAmendCommand);

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Display has been updated successfully.");

                // Successfully registered so redirect to the display page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewDisplay(model, true);
            } catch (final DataAccessException | XpdmException ex) {
                XhibitCourtSiteDto courtSite = displayPageStateHolder.getCourtSite();

                model.addObject(COURTSITE_LIST,
                    displayPageStateHolder.getSitesBySelectedCourt(courtSite.getCourtId()));
                model.addObject(DISPLAY_LIST, displayPageStateHolder.getDisplays());
                model.addObject(DISPLAY_TYPE_LIST, displayPageStateHolder.getDisplayTypes());
                model.addObject(ROTATION_SET_LIST, displayPageStateHolder.getRotationSets());
                // Log the error
                LOGGER.error("{}{} Unable to update Display ", METHOD, methodName, ex);
                // Reject
                result.reject("displayErrors", "Unable to update Display: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, displayAmendCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show create display.
     *
     * @param displaySearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_DISPLAY, method = RequestMethod.POST, params = "btnAdd")
    public ModelAndView showCreateDisplay(@Valid final DisplaySearchCommand displaySearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showCreateDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        displayPageStateHolder.setDisplaySearchCommand(displaySearchCommand);

        displaySelectedValidator.validate(displaySearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, displayPageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_DISPLAY);

        } else {
            // Populate the lists and set CourtSite
            final XhibitCourtSiteDto courtSite =
                populateListsAndSetCourtSite(displaySearchCommand, true);

            // Populate the relevant fields
            final DisplayCreateCommand displayCommand = new DisplayCreateCommand();
            displayCommand.setXhibitCourtSiteId(courtSite.getId());

            // Populate the model objects
            model.addObject(COURTSITE_LIST,
                displayPageStateHolder.getSitesBySelectedCourt(courtSite.getCourtId()));
            model.addObject(DISPLAY_LIST, displayPageStateHolder.getDisplays());
            model.addObject(DISPLAY_TYPE_LIST, displayPageStateHolder.getDisplayTypes());
            model.addObject(ROTATION_SET_LIST, displayPageStateHolder.getRotationSets());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, displayCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_CREATE_DISPLAY);

        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Create display.
     *
     * @param displayCreateCommand the display create command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_CREATE_DISPLAY, method = RequestMethod.POST,
        params = "btnCreateConfirm")
    public ModelAndView createDisplay(@Valid final DisplayCreateCommand displayCreateCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "createDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Default is to return to the create display page to display errors
        model.setViewName(VIEW_NAME_CREATE_DISPLAY);

        displayCreateValidator.validate(displayCreateCommand, result,
            displayPageStateHolder.getDisplays());
        if (result.hasErrors()) {
            XhibitCourtSiteDto courtSite = displayPageStateHolder.getCourtSite();

            model.addObject(COURTSITE_LIST,
                displayPageStateHolder.getSitesBySelectedCourt(courtSite.getCourtId()));
            model.addObject(DISPLAY_LIST, displayPageStateHolder.getDisplays());
            model.addObject(DISPLAY_TYPE_LIST, displayPageStateHolder.getDisplayTypes());
            model.addObject(ROTATION_SET_LIST, displayPageStateHolder.getRotationSets());
        } else {
            try {
                LOGGER.debug("{}{} - creating Display", METHOD, methodName);

                displayService.createDisplay(displayCreateCommand);

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Display has been created successfully.");

                // Successfully registered so redirect to the display page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewDisplay(model, false);
            } catch (final Exception ex) {
                XhibitCourtSiteDto courtSite = displayPageStateHolder.getCourtSite();
                // Reset the drop down lists
                model.addObject(COURTSITE_LIST,
                    displayPageStateHolder.getSitesBySelectedCourt(courtSite.getCourtId()));
                model.addObject(DISPLAY_LIST, displayPageStateHolder.getDisplays());
                model.addObject(DISPLAY_TYPE_LIST, displayPageStateHolder.getDisplayTypes());
                model.addObject(ROTATION_SET_LIST, displayPageStateHolder.getRotationSets());
                // Log the error
                LOGGER.error("{}{} Unable to create Display ", METHOD, methodName, ex);
                // Reject
                result.reject("displayErrors", "Unable to create Display: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, displayCreateCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show delete display.
     *
     * @param displaySearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_DISPLAY, method = RequestMethod.POST, params = "btnDelete")
    public ModelAndView showDeleteDisplay(@Valid final DisplaySearchCommand displaySearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showDeleteDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        displayPageStateHolder.setDisplaySearchCommand(displaySearchCommand);

        displaySelectedValidator.validate(displaySearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, displayPageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_DISPLAY);

        } else {
            // Populate the lists and set CourtSite
            final XhibitCourtSiteDto courtSite =
                populateListsAndSetCourtSite(displaySearchCommand, false);

            // Populate the relevant fields
            final DisplayDeleteCommand displayCommand = new DisplayDeleteCommand();

            // Populate the model objects
            model.addObject(DISPLAY_LIST, displayPageStateHolder.getDisplays());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, displayCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_DELETE_DISPLAY);

        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Delete display.
     *
     * @param displayDeleteCommand the display delete command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_DELETE_DISPLAY, method = RequestMethod.POST,
        params = "btnDeleteConfirm")
    public ModelAndView deleteDisplay(@Valid final DisplayDeleteCommand displayDeleteCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "deleteDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Default is to return to the delete display page to display errors
        model.setViewName(VIEW_NAME_DELETE_DISPLAY);

        displayDeleteValidator.validate(displayDeleteCommand, result);
        if (result.hasErrors()) {
            model.addObject(DISPLAY_LIST, displayPageStateHolder.getDisplays());
        } else {
            try {
                LOGGER.debug("{}{} - deleting Display", METHOD, methodName);

                displayService.deleteDisplay(displayDeleteCommand);

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Display has been deleted successfully.");

                // Successfully registered so redirect to the display page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewDisplay(model, false);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to delete Display ", METHOD, methodName, ex);
                // Reject
                result.reject("displayErrors", "Unable to delete Display: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, displayDeleteCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Populate the court site lists.
     *
     */
    private void populateCourtSitesList() {
        for (int i = 0; i < MAX_NUM_OF_RETRIES; i++) {
            LOGGER.info(ATTEMPT, i + 1, POPULATING_COURTSITES);
            setViewPageStateSelectionLists();
            if (!displayPageStateHolder.getSites().isEmpty()) {
                LOGGER.info(COURTSITES_POPULATED);
                break;
            }
        }
    }

    /**
     * Populate the page state selection lists.
     *
     * @param displaySearchCommand the display search command
     * @param courtSite the court site
     * @param isAmend Is amend if not then delete
     */
    private void populatePageStateSelectionLists(DisplaySearchCommand displaySearchCommand,
        XhibitCourtSiteDto courtSite, Boolean isAmend) {
        for (int i = 0; i < MAX_NUM_OF_RETRIES; i++) {
            LOGGER.info(ATTEMPT, i + 1, POPULATING_PAGESTATE_LISTS);
            if (isAmend) {
                setAmendPageStateSelectionLists(displaySearchCommand.getXhibitCourtSiteId(),
                    courtSite.getCourtId());
            } else {
                setDeletePageStateSelectionLists(displaySearchCommand.getXhibitCourtSiteId(),
                    courtSite.getCourtId());
            }
            if (!displayPageStateHolder.getSites().isEmpty()
                && !displayPageStateHolder.getDisplays().isEmpty()
                && !displayPageStateHolder.getDisplayTypes().isEmpty()
                && !displayPageStateHolder.getRotationSets().isEmpty()) {
                LOGGER.info(PAGESTATE_LISTS_POPULATED);
                break;
            }
        }
    }

    private XhibitCourtSiteDto populateListsAndSetCourtSite(
        DisplaySearchCommand displaySearchCommand, boolean isAmend) {
        // Populate the CourtSites list
        populateCourtSitesList();

        // Get the selected CourtSite
        final XhibitCourtSiteDto courtSite =
            populateSelectedCourtSiteInPageStateHolder(displaySearchCommand.getXhibitCourtSiteId());

        // Populate the lists
        populatePageStateSelectionLists(displaySearchCommand, courtSite, isAmend);

        return courtSite;
    }
}
