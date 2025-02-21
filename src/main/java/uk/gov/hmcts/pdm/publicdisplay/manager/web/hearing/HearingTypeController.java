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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing;

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
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.EncryptedFormat;

/**
 * The Class HearingTypeController.
 *
 * @author gittinsl
 */

@Controller
@RequestMapping("/hearing")
public class HearingTypeController extends HearingTypePageStateSetter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HearingTypeController.class);

    private static final String ADDING_COURTSITE_TO_MODEL = "{}{} adding court site data to model";
    private static final String COURTSITE = "courtSite";
    private static final String COURTSITE_LIST = "courtSiteList";
    private static final String HEARINGTYPE_LIST = "hearingTypeList";
    private static final String CATEGORIES_LIST = "categoriesList";
    private static final String COMMAND = "command";
    private static final String SUCCESS_MESSAGE = "successMessage";

    /** The Constant for the JSP Folder. */
    private static final String FOLDER_HEARING = "hearing";

    /**
     * View Hearing.
     */
    private static final String MAPPING_VIEW_HEARING = "/view_hearing";

    /**
     * View Hearing View.
     */
    private static final String VIEW_NAME_VIEW_HEARING = FOLDER_HEARING + MAPPING_VIEW_HEARING;

    /**
     * Amend Hearing.
     */
    private static final String MAPPING_AMEND_HEARING = "/amend_hearing";

    /**
     * Amend Hearing View.
     */
    private static final String VIEW_NAME_AMEND_HEARING = FOLDER_HEARING + MAPPING_AMEND_HEARING;

    /**
     * Create Hearing.
     */
    private static final String MAPPING_CREATE_HEARING = "/create_hearing";

    /**
     * Create Hearing View.
     */
    private static final String VIEW_NAME_CREATE_HEARING = FOLDER_HEARING + MAPPING_CREATE_HEARING;


    /**
     * The hearing type selected validator.
     */
    @Autowired
    private HearingTypeSelectedValidator hearingTypeSelectedValidator;

    /**
     * The hearing type amend validator.
     */
    @Autowired
    private HearingTypeAmendValidator hearingTypeAmendValidator;

    /**
     * The hearing type create validator.
     */
    @Autowired
    private HearingTypeCreateValidator hearingTypeCreateValidator;

    /**
     * View Hearing.
     *
     * @param model the model
     * @param reset the reset
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_HEARING, method = RequestMethod.GET)
    public ModelAndView viewHearing(final ModelAndView model,
        @RequestParam(value = "reset", defaultValue = "true") final boolean reset) {
        final String methodName = "viewHearing";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        HearingTypeSearchCommand hearingTypeSearchCommand = new HearingTypeSearchCommand();
        if (reset || hearingTypePageStateHolder.getHearingSearchCommand() == null) {
            LOGGER.debug("{}{} - reset hearing search results", METHOD, methodName);
            hearingTypePageStateHolder.reset();

            // retrieve and add the selection lists to the pageStateHolder
            setPageStateSelectionLists();
        } else {
            hearingTypeSearchCommand = hearingTypePageStateHolder.getHearingSearchCommand();
        }

        // Add the hearingSearchCommand to the model
        LOGGER.debug("{}{} adding hearingSearchCommand to model", METHOD, methodName);
        model.addObject(COMMAND, hearingTypeSearchCommand);

        // Add the court site data to model
        LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
        model.addObject(COURTSITE_LIST, hearingTypePageStateHolder.getSites());

        // Return the model
        LOGGER.debug("{}{} returning model", METHOD, methodName);
        model.setViewName(VIEW_NAME_VIEW_HEARING);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }


    /**
     * Show amend hearing.
     *
     * @param hearingTypeSearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_HEARING, method = RequestMethod.POST, params = "btnAmend")
    public ModelAndView showAmendHearing(
        @Valid final HearingTypeSearchCommand hearingTypeSearchCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "showAmendHearing";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        hearingTypePageStateHolder.setHearingSearchCommand(hearingTypeSearchCommand);

        hearingTypeSelectedValidator.validate(hearingTypeSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, hearingTypePageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_HEARING);
            model.addObject(COMMAND, hearingTypeSearchCommand);
        } else {
            // Get the selected CourtSite
            final XhibitCourtSiteDto courtSite = populateSelectedCourtSiteInPageStateHolder(
                hearingTypeSearchCommand.getXhibitCourtSiteId());

            // Populate the amend lists
            setAmendPageStateSelectionLists(hearingTypeSearchCommand.getXhibitCourtSiteId());

            // Populate the relevant fields
            final HearingTypeAmendCommand hearingTypeCommand = new HearingTypeAmendCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, hearingTypePageStateHolder.getSites());
            model.addObject(HEARINGTYPE_LIST, hearingTypePageStateHolder.getHearingTypes());
            model.addObject(CATEGORIES_LIST, hearingTypeService.getAllCategories());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, hearingTypeCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_AMEND_HEARING);
        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Load hearing type.
     *
     * @param refHearingTypeId the ref Hearing Type Id
     * @return the selected HearingTypeDto
     */
    @RequestMapping(value = MAPPING_AMEND_HEARING + "/{refHearingTypeId}",
        method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public HearingTypeDto loadHearingType(
        @PathVariable("refHearingTypeId") @EncryptedFormat final Integer refHearingTypeId) {
        final String methodName = "loadHearingType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        HearingTypeDto result = null;
        for (HearingTypeDto dto : hearingTypePageStateHolder.getHearingTypes()) {
            if (dto.getRefHearingTypeId().equals(refHearingTypeId)) {
                LOGGER.info("Found HearingType");
                result = dto;
                break;
            }
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }


    /**
     * Update Hearing Type.
     *
     * @param hearingTypeAmendCommand the display amend command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_AMEND_HEARING, method = RequestMethod.POST,
        params = "btnUpdateConfirm")
    public ModelAndView updateHearingType(
        @Valid final HearingTypeAmendCommand hearingTypeAmendCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "updateHearingType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        hearingTypeAmendValidator.validate(hearingTypeAmendCommand, result);
        if (result.hasErrors()) {
            // Default is to return to the amend hearing type page to show errors
            model.setViewName(VIEW_NAME_AMEND_HEARING);

            XhibitCourtSiteDto courtSite = hearingTypePageStateHolder.getCourtSite();

            model.addObject(COURTSITE_LIST, hearingTypePageStateHolder.getSites());
            model.addObject(HEARINGTYPE_LIST, hearingTypePageStateHolder.getHearingTypes());
            model.addObject(CATEGORIES_LIST, hearingTypeService.getAllCategories());
            model.addObject(COURTSITE, courtSite);
        } else {
            try {
                LOGGER.debug("{}{} - updating Hearing Type", METHOD, methodName);


                hearingTypeService.updateHearingType(hearingTypeAmendCommand);

                // Add successMessage to model to show on hearing page
                model.addObject(SUCCESS_MESSAGE, "Hearing Type has been updated successfully.");

                // Successfully registered so redirect to the display page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewHearing(model, true);
            } catch (final DataAccessException | XpdmException ex) {
                XhibitCourtSiteDto courtSite = hearingTypePageStateHolder.getCourtSite();

                model.addObject(COURTSITE_LIST, hearingTypePageStateHolder.getSites());
                model.addObject(HEARINGTYPE_LIST, hearingTypePageStateHolder.getHearingTypes());
                model.addObject(CATEGORIES_LIST, hearingTypeService.getAllCategories());
                model.addObject(COURTSITE, courtSite);
                // Log the error
                LOGGER.error("{}{} Unable to update Hearing Type ", METHOD, methodName, ex);
                // Reject
                result.reject("hearingTypeErrors",
                    "Unable to update Hearing Type: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, hearingTypeAmendCommand);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show create hearing type.
     *
     * @param hearingTypeSearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_HEARING, method = RequestMethod.POST, params = "btnAdd")
    public ModelAndView showCreateHearing(
        @Valid final HearingTypeSearchCommand hearingTypeSearchCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "showCreateHearing";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        hearingTypePageStateHolder.setHearingSearchCommand(hearingTypeSearchCommand);

        hearingTypeSelectedValidator.validate(hearingTypeSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, hearingTypePageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_HEARING);
            model.addObject(COMMAND, hearingTypeSearchCommand);

        } else {
            // Get the selected CourtSite
            final XhibitCourtSiteDto courtSite = populateSelectedCourtSiteInPageStateHolder(
                hearingTypeSearchCommand.getXhibitCourtSiteId());

            // Populate the amend lists
            setAmendPageStateSelectionLists(hearingTypeSearchCommand.getXhibitCourtSiteId());

            // Populate the relevant fields
            final HearingTypeCreateCommand hearingTypeCreateCommand =
                new HearingTypeCreateCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, hearingTypePageStateHolder.getSites());
            model.addObject(HEARINGTYPE_LIST, hearingTypePageStateHolder.getHearingTypes());
            model.addObject(CATEGORIES_LIST, hearingTypeService.getAllCategories());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, hearingTypeCreateCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_CREATE_HEARING);

        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }


    /**
     * Create Hearing Type.
     *
     * @param hearingTypeCreateCommand the hearing type create command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_CREATE_HEARING, method = RequestMethod.POST,
        params = "btnCreateConfirm")
    public ModelAndView createHearingType(
        @Valid final HearingTypeCreateCommand hearingTypeCreateCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "createHearingType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Default is to return to the create display page to display errors
        model.setViewName(VIEW_NAME_CREATE_HEARING);

        hearingTypeCreateValidator.validate(hearingTypeCreateCommand, result,
            hearingTypePageStateHolder.getHearingTypes());
        if (result.hasErrors()) {
            XhibitCourtSiteDto courtSite = hearingTypePageStateHolder.getCourtSite();

            model.addObject(COURTSITE_LIST, hearingTypePageStateHolder.getSites());
            model.addObject(HEARINGTYPE_LIST, hearingTypePageStateHolder.getHearingTypes());
            model.addObject(CATEGORIES_LIST, hearingTypeService.getAllCategories());
            model.addObject(COURTSITE, courtSite);
        } else {
            try {
                LOGGER.debug("{}{} - creating Hearing Type", METHOD, methodName);

                hearingTypeService.createHearingType(hearingTypeCreateCommand,
                    hearingTypePageStateHolder.getCourtSite().getCourtId());

                // Add successMessage to model to show on page
                model.addObject(SUCCESS_MESSAGE, "Hearing Type has been created successfully.");

                // Successfully registered so redirect to the hearing type page which needs a new
                // model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewHearing(model, false);
            } catch (final Exception ex) {
                XhibitCourtSiteDto courtSite = hearingTypePageStateHolder.getCourtSite();

                model.addObject(COURTSITE_LIST, hearingTypePageStateHolder.getSites());
                model.addObject(HEARINGTYPE_LIST, hearingTypePageStateHolder.getHearingTypes());
                model.addObject(CATEGORIES_LIST, hearingTypeService.getAllCategories());
                model.addObject(COURTSITE, courtSite);
                // Log the error
                LOGGER.error("{}{} Unable to create Hearing Type ", METHOD, methodName, ex);
                // Reject
                result.reject("hearingTypeErrors",
                    "Unable to create Hearing Type: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, hearingTypeCreateCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }
}
