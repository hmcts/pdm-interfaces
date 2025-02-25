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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype;

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
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.EncryptedFormat;

/**
 * The Class JudgeTypeController.
 *
 * @author toftn
 */

@Controller
@RequestMapping("/judgetype")
public class JudgeTypeController extends JudgeTypePageStateSetter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgeTypeController.class);

    private static final String ADDING_COURTSITE_TO_MODEL = "{}{} adding court site data to model";
    private static final String COURTSITE = "courtSite";
    private static final String COURTSITE_LIST = "courtSiteList";
    private static final String JUDGE_TYPE_LIST = "judgeTypeList";
    private static final String COMMAND = "command";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final int MAX_NUM_OF_RETRIES = 5;

    /** The Constant for the JSP Folder. */
    private static final String FOLDER_JUDGETYPE = "judgetype";
    
    /**
     * View Judge Type.
     */
    private static final String MAPPING_VIEW_JUDGE_TYPE = "/view_judgetype";

    /**
     * View Judge Type View.
     */
    private static final String VIEW_NAME_VIEW_JUDGE_TYPE =
        FOLDER_JUDGETYPE + MAPPING_VIEW_JUDGE_TYPE;

    /**
     * Amend Judge Type.
     */
    private static final String MAPPING_AMEND_JUDGE_TYPE = "/amend_judgetype";

    /**
     * Amend Judge Type View.
     */
    private static final String VIEW_NAME_AMEND_JUDGE_TYPE =
        FOLDER_JUDGETYPE + MAPPING_AMEND_JUDGE_TYPE;

    /**
     * Create Judge Type.
     */
    private static final String MAPPING_CREATE_JUDGE_TYPE = "/create_judgetype";

    /**
     * Create Judge View.
     */
    private static final String VIEW_NAME_CREATE_JUDGE_TYPE =
        FOLDER_JUDGETYPE + MAPPING_CREATE_JUDGE_TYPE;

    /**
     * The judge type selected validator.
     */
    @Autowired
    private JudgeTypeSelectedValidator judgeTypeSelectedValidator;

    /**
     * The judge type amend validator.
     */
    @Autowired
    private JudgeTypeAmendValidator judgeTypeAmendValidator;

    /**
     * The judge type create validator.
     */
    @Autowired
    private JudgeTypeCreateValidator judgeTypeCreateValidator;

    /**
     * View Judge Type.
     *
     * @param model the model
     * @param reset the reset
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_JUDGE_TYPE, method = RequestMethod.GET)
    public ModelAndView viewJudgeType(final ModelAndView model,
        @RequestParam(value = "reset", defaultValue = "true") final boolean reset) {
        final String methodName = "viewJudgeType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        JudgeTypeSearchCommand judgeTypeSearchCommand = new JudgeTypeSearchCommand();
        if (reset || judgeTypePageStateHolder.getJudgeTypeSearchCommand() == null) {
            LOGGER.debug("{}{} - reset judge type search results", METHOD, methodName);
            judgeTypePageStateHolder.reset();

            // retrieve and add the selection lists to the pageStateHolder
            setViewPageStateSelectionLists();
        } else {
            judgeTypeSearchCommand = judgeTypePageStateHolder.getJudgeTypeSearchCommand();
        }

        // Add the judgeTypeSearchCommand to the model
        LOGGER.debug("{}{} adding judgeTypeSearchCommand to model", METHOD, methodName);
        model.addObject(COMMAND, judgeTypeSearchCommand);

        // Add the court site data to model
        LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
        model.addObject(COURTSITE_LIST, judgeTypePageStateHolder.getSites());

        // Return the model
        LOGGER.debug("{}{} returning model", METHOD, methodName);
        model.setViewName(VIEW_NAME_VIEW_JUDGE_TYPE);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show amend judge type.
     *
     * @param judgeTypeSearchCommand the judge type search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_JUDGE_TYPE, method = RequestMethod.POST,
        params = "btnAmend")
    public ModelAndView showAmendJudgeType(
        @Valid final JudgeTypeSearchCommand judgeTypeSearchCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "showAmendJudgeType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        judgeTypePageStateHolder.setJudgeTypeSearchCommand(judgeTypeSearchCommand);

        judgeTypeSelectedValidator.validate(judgeTypeSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, judgeTypePageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_JUDGE_TYPE);

        } else {
            // Populate the amend lists
            for (int i = 0; i < MAX_NUM_OF_RETRIES; i++) {
                LOGGER.info("Attempt {}{}", i + 1, ", populating the AmendPageStateSelectionLists");
                setAmendPageStateSelectionLists(judgeTypeSearchCommand.getXhibitCourtSiteId());
                if (!judgeTypePageStateHolder.getSites().isEmpty()
                    && !judgeTypePageStateHolder.getJudgeTypes().isEmpty()) {
                    LOGGER.info("All AmendPageStateSelectionLists populated");
                    break;
                }
            }

            // Get the selected CourtSite
            final XhibitCourtSiteDto courtSite = populateSelectedCourtSiteInPageStateHolder(
                judgeTypeSearchCommand.getXhibitCourtSiteId());

            // Populate the relevant fields
            final JudgeTypeAmendCommand judgeTypeCommand = new JudgeTypeAmendCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, judgeTypePageStateHolder.getSites());
            model.addObject(JUDGE_TYPE_LIST, judgeTypePageStateHolder.getJudgeTypes());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, judgeTypeCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_AMEND_JUDGE_TYPE);
        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Load Judge Type.
     *
     * @param refSystemCodeId the judge type id
     * @return the selected RefSystemCodeDto
     */
    @RequestMapping(value = MAPPING_AMEND_JUDGE_TYPE + "/{refSystemCodeId}",
        method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public RefSystemCodeDto loadJudgeType(
        @PathVariable("refSystemCodeId") @EncryptedFormat final Integer refSystemCodeId) {
        final String methodName = "loadJudgeType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        RefSystemCodeDto result = null;
        for (RefSystemCodeDto dto : judgeTypePageStateHolder.getJudgeTypes()) {
            if (dto.getRefSystemCodeId().equals(refSystemCodeId)) {
                LOGGER.info("Found JudgeType");
                result = dto;
                break;
            }
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    /**
     * Update judge type.
     *
     * @param judgeTypeAmendCommand the judge type amend command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_AMEND_JUDGE_TYPE, method = RequestMethod.POST,
        params = "btnUpdateConfirm")
    public ModelAndView updateJudgeType(@Valid final JudgeTypeAmendCommand judgeTypeAmendCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "updateJudgeType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        judgeTypeAmendValidator.validate(judgeTypeAmendCommand, result);
        if (result.hasErrors()) {
            // Default is to return to the amend judge type page to display errors
            model.setViewName(VIEW_NAME_AMEND_JUDGE_TYPE);

            model.addObject(COURTSITE_LIST, judgeTypePageStateHolder.getSites());
            model.addObject(JUDGE_TYPE_LIST, judgeTypePageStateHolder.getJudgeTypes());
        } else {
            try {
                LOGGER.debug("{}{} - updating Judge Type", METHOD, methodName);

                refJudgeTypeService.updateJudgeType(judgeTypeAmendCommand);

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Judge Type has been updated successfully.");

                // Successfully registered so redirect to the judge type page which needs a new
                // model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewJudgeType(model, true);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to update Judge Type ", METHOD, methodName, ex);
                // Reject
                result.reject("judgeTypeErrors", "Unable to update Judge Type: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, judgeTypeAmendCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show create judge type.
     *
     * @param judgeTypeSearchCommand the judge type search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_JUDGE_TYPE, method = RequestMethod.POST, params = "btnAdd")
    public ModelAndView showCreateJudgeType(
        @Valid final JudgeTypeSearchCommand judgeTypeSearchCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "showCreateJudgeType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        judgeTypePageStateHolder.setJudgeTypeSearchCommand(judgeTypeSearchCommand);

        judgeTypeSelectedValidator.validate(judgeTypeSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, judgeTypePageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_JUDGE_TYPE);

        } else {
            // Populate the create lists
            for (int i = 0; i < MAX_NUM_OF_RETRIES; i++) {
                LOGGER.info("Attempt {}{}", i + 1, ", populating the AmendPageStateSelectionLists");
                setAmendPageStateSelectionLists(judgeTypeSearchCommand.getXhibitCourtSiteId());
                if (!judgeTypePageStateHolder.getSites().isEmpty()
                    && !judgeTypePageStateHolder.getJudgeTypes().isEmpty()) {
                    LOGGER.info("All AmendPageStateSelectionLists populated");
                    break;
                }
            }
            
            // Get the selected CourtSite
            final XhibitCourtSiteDto courtSite = populateSelectedCourtSiteInPageStateHolder(
                judgeTypeSearchCommand.getXhibitCourtSiteId());

            // Populate the relevant fields
            final JudgeTypeCreateCommand judgeTypeCommand = new JudgeTypeCreateCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, judgeTypePageStateHolder.getSites());
            model.addObject(JUDGE_TYPE_LIST, judgeTypePageStateHolder.getJudgeTypes());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, judgeTypeCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_CREATE_JUDGE_TYPE);

        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Create judge type.
     *
     * @param judgeTypeCreateCommand the judge type create command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_CREATE_JUDGE_TYPE, method = RequestMethod.POST,
        params = "btnCreateConfirm")
    public ModelAndView createJudgeType(@Valid final JudgeTypeCreateCommand judgeTypeCreateCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "createJudgeType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        judgeTypeCreateValidator.validate(judgeTypeCreateCommand, result,
            judgeTypePageStateHolder.getJudgeTypes());
        if (result.hasErrors()) {
            // Default is to return to the create judge page to display errors
            model.setViewName(VIEW_NAME_CREATE_JUDGE_TYPE);
            model.addObject(COURTSITE_LIST, judgeTypePageStateHolder.getSites());
            model.addObject(JUDGE_TYPE_LIST, judgeTypePageStateHolder.getJudgeTypes());
        } else {
            try {
                LOGGER.debug("{}{} - creating Judge type", METHOD, methodName);

                refJudgeTypeService.createJudgeType(judgeTypeCreateCommand,
                    judgeTypePageStateHolder.getCourtSite().getCourtId());

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Judge Type has been created successfully.");

                // Successfully registered so redirect to the judge type page which needs a new
                // model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewJudgeType(model, true);
            } catch (final DataAccessException | XpdmException ex) {
                model.addObject(COURTSITE_LIST, judgeTypePageStateHolder.getSites());
                model.addObject(JUDGE_TYPE_LIST, judgeTypePageStateHolder.getJudgeTypes());
                // Log the error
                LOGGER.error("{}{} Unable to create Judge Type ", METHOD, methodName, ex);
                // Reject
                result.reject("judgeTypeErrors", "Unable to create Judge Type: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, judgeTypeCreateCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }
}
