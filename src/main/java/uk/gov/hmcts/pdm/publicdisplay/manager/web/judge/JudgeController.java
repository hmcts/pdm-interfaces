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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.judge;

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
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefJudgeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.EncryptedFormat;

/**
 * The Class JudgeController.
 *
 * @author toftn
 */

@Controller
@RequestMapping("/judge")
public class JudgeController extends JudgePageStateSetter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JudgeController.class);

    private static final String ADDING_COURTSITE_TO_MODEL = "{}{} adding court site data to model";
    private static final String COURTSITE = "courtSite";
    private static final String COURTSITE_LIST = "courtSiteList";
    private static final String JUDGE_LIST = "judgeList";
    private static final String JUDGE_TYPE_LIST = "judgeTypeList";
    private static final String COMMAND = "command";
    private static final String SUCCESS_MESSAGE = "successMessage";

    /** The Constant for the JSP Folder. */
    private static final String FOLDER_JUDGE = "judge";
    
    /**
     * View Judge.
     */
    private static final String MAPPING_VIEW_JUDGE = "/view_judge";

    /**
     * View Judge View.
     */
    private static final String VIEW_NAME_VIEW_JUDGE = FOLDER_JUDGE + MAPPING_VIEW_JUDGE;

    /**
     * Amend Judge.
     */
    private static final String MAPPING_AMEND_JUDGE = "/amend_judge";

    /**
     * Amend Judge View.
     */
    private static final String VIEW_NAME_AMEND_JUDGE = FOLDER_JUDGE + MAPPING_AMEND_JUDGE;

    /**
     * Create Judge.
     */
    private static final String MAPPING_CREATE_JUDGE = "/create_judge";

    /**
     * Create Judge View.
     */
    private static final String VIEW_NAME_CREATE_JUDGE = FOLDER_JUDGE + MAPPING_CREATE_JUDGE;

    /**
     * Delete Judge.
     */
    private static final String MAPPING_DELETE_JUDGE = "/delete_judge";

    /**
     * Delete Judge View.
     */
    private static final String VIEW_NAME_DELETE_JUDGE = FOLDER_JUDGE + MAPPING_DELETE_JUDGE;

    /**
     * The judge selected validator.
     */
    @Autowired
    private JudgeSelectedValidator judgeSelectedValidator;

    /**
     * The judge amend validator.
     */
    @Autowired
    private JudgeAmendValidator judgeAmendValidator;

    /**
     * The judge create validator.
     */
    @Autowired
    private JudgeCreateValidator judgeCreateValidator;

    /**
     * The judge delete validator.
     */
    @Autowired
    private JudgeDeleteValidator judgeDeleteValidator;

    /**
     * View Judge.
     *
     * @param model the model
     * @param reset the reset
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_JUDGE, method = RequestMethod.GET)
    public ModelAndView viewJudge(final ModelAndView model,
        @RequestParam(value = "reset", defaultValue = "true") final boolean reset) {
        final String methodName = "viewJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        JudgeSearchCommand judgeSearchCommand = new JudgeSearchCommand();
        if (reset || judgePageStateHolder.getJudgeSearchCommand() == null) {
            LOGGER.debug("{}{} - reset local proxy search results", METHOD, methodName);
            judgePageStateHolder.reset();

            // retrieve and add the selection lists to the pageStateHolder
            setViewPageStateSelectionLists();
        } else {
            judgeSearchCommand = judgePageStateHolder.getJudgeSearchCommand();
        }

        // Add the judgeSearchCommand to the model
        LOGGER.debug("{}{} adding judgeSearchCommand to model", METHOD, methodName);
        model.addObject(COMMAND, judgeSearchCommand);

        // Add the court site data to model
        LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
        model.addObject(COURTSITE_LIST, judgePageStateHolder.getSites());

        // Return the model
        LOGGER.debug("{}{} returning model", METHOD, methodName);
        model.setViewName(VIEW_NAME_VIEW_JUDGE);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show amend judge.
     *
     * @param judgeSearchCommand the judge search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_JUDGE, method = RequestMethod.POST, params = "btnAmend")
    public ModelAndView showAmendJudge(@Valid final JudgeSearchCommand judgeSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showAmendJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        
        // Ensure the search command is the latest
        judgePageStateHolder.setJudgeSearchCommand(judgeSearchCommand);

        judgeSelectedValidator.validate(judgeSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, judgePageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_JUDGE);

        } else {
            // Populate the amend lists
            setAmendPageStateSelectionLists(judgeSearchCommand.getXhibitCourtSiteId());

            // Get the selected CourtSite
            final XhibitCourtSiteDto courtSite = populateSelectedCourtSiteInPageStateHolder(
                judgeSearchCommand.getXhibitCourtSiteId());

            // Populate the relevant fields
            final JudgeAmendCommand judgeCommand = new JudgeAmendCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, judgePageStateHolder.getSites());
            model.addObject(JUDGE_LIST, judgePageStateHolder.getJudges());
            model.addObject(JUDGE_TYPE_LIST, judgePageStateHolder.getJudgeTypes());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, judgeCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_AMEND_JUDGE);
        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Load Judge.
     *
     * @param refJudgeId the judge id
     * @return the selected RefJudgeDto
     */
    @RequestMapping(value = MAPPING_AMEND_JUDGE + "/{refJudgeId}", method = RequestMethod.GET,
        produces = "application/json")
    @ResponseBody
    public RefJudgeDto loadJudge(@PathVariable("refJudgeId") @EncryptedFormat final Integer refJudgeId) {
        final String methodName = "loadJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        RefJudgeDto result = null;
        LOGGER.debug("No of Judges = {}", judgePageStateHolder.getJudges().size());
        for (RefJudgeDto dto : judgePageStateHolder.getJudges()) {
            if (dto.getRefJudgeId().equals(refJudgeId)) {
                LOGGER.info("Found Judge");
                result = dto;
                break;
            }
        }
        // Get Judge Type description
        LOGGER.debug("No of JudgeTypes = {}", judgePageStateHolder.getJudgeTypes().size());
        for (RefSystemCodeDto dto : judgePageStateHolder.getJudgeTypes()) {
            if (dto.getCode().equals(result.getJudgeType())) {
                result.setJudgeTypeDeCode(dto.getDeCode());
                LOGGER.info("Found JudgeType");
                break;
            }
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    /**
     * Update judge.
     *
     * @param judgeAmendCommand the judge amend command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_AMEND_JUDGE, method = RequestMethod.POST,
        params = "btnUpdateConfirm")
    public ModelAndView updateJudge(@Valid final JudgeAmendCommand judgeAmendCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "updateJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        judgeAmendValidator.validate(judgeAmendCommand, result);
        if (result.hasErrors()) {
            // Default is to return to the amend judge page to judge errors
            model.setViewName(VIEW_NAME_AMEND_JUDGE);

            model.addObject(COURTSITE_LIST, judgePageStateHolder.getSites());
            model.addObject(JUDGE_LIST, judgePageStateHolder.getJudges());
            model.addObject(JUDGE_TYPE_LIST, judgePageStateHolder.getJudgeTypes());
        } else {
            try {
                LOGGER.debug("{}{} - updating Judge", METHOD, methodName);

                refJudgeService.updateJudge(judgeAmendCommand);

                // Add successMessage to model for judge on page
                model.addObject(SUCCESS_MESSAGE, "Judge has been updated successfully.");

                // Successfully registered so redirect to the judge page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewJudge(model, true);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to update Judge ", METHOD, methodName, ex);
                // Reject
                result.reject("judgeErrors", "Unable to update Judge: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, judgeAmendCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show create judge.
     *
     * @param judgeSearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_JUDGE, method = RequestMethod.POST, params = "btnAdd")
    public ModelAndView showCreateJudge(@Valid final JudgeSearchCommand judgeSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showCreateJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        judgePageStateHolder.setJudgeSearchCommand(judgeSearchCommand);

        judgeSelectedValidator.validate(judgeSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, judgePageStateHolder.getSites());
            model.addObject(JUDGE_TYPE_LIST, judgePageStateHolder.getJudgeTypes());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_JUDGE);

        } else {
            // Get the selected CourtSite
            final XhibitCourtSiteDto courtSite = populateSelectedCourtSiteInPageStateHolder(
                judgeSearchCommand.getXhibitCourtSiteId());

            // Populate the amend lists
            setAmendPageStateSelectionLists(judgeSearchCommand.getXhibitCourtSiteId());

            // Populate the relevant fields
            final JudgeCreateCommand judgeCommand = new JudgeCreateCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, judgePageStateHolder.getSites());
            model.addObject(JUDGE_TYPE_LIST, judgePageStateHolder.getJudgeTypes());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, judgeCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_CREATE_JUDGE);

        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Create judge.
     *
     * @param judgeCreateCommand the judge create command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_CREATE_JUDGE, method = RequestMethod.POST,
        params = "btnCreateConfirm")
    public ModelAndView createJudge(@Valid final JudgeCreateCommand judgeCreateCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "createJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        judgeCreateValidator.validate(judgeCreateCommand, result, 
            judgePageStateHolder.getJudges());
        if (result.hasErrors()) {
            // Default is to return to the create judge page to display errors
            model.setViewName(VIEW_NAME_CREATE_JUDGE);
            model.addObject(COURTSITE_LIST, judgePageStateHolder.getSites());
            model.addObject(JUDGE_TYPE_LIST, judgePageStateHolder.getJudgeTypes());
        } else {
            try {
                LOGGER.debug("{}{} - creating Judge", METHOD, methodName);

                refJudgeService.createJudge(judgeCreateCommand, 
                    judgePageStateHolder.getCourtSite().getCourtId());

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Judge has been created successfully.");

                // Successfully registered so redirect to the judge page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewJudge(model, false);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to create Judge ", METHOD, methodName, ex);
                // Reject
                result.reject("judgeErrors", "Unable to create Judge: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, judgeCreateCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show delete judge.
     *
     * @param judgeSearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_JUDGE, method = RequestMethod.POST, params = "btnDelete")
    public ModelAndView showDeleteJudge(@Valid final JudgeSearchCommand judgeSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showDeleteJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        judgePageStateHolder.setJudgeSearchCommand(judgeSearchCommand);

        judgeSelectedValidator.validate(judgeSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, judgePageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_JUDGE);

        } else {

            // Get the selected CourtSite
            final XhibitCourtSiteDto courtSite = populateSelectedCourtSiteInPageStateHolder(
                judgeSearchCommand.getXhibitCourtSiteId());

            // Populate the delete lists
            setDeletePageStateSelectionLists(judgeSearchCommand.getXhibitCourtSiteId());

            // Populate the relevant fields
            final JudgeDeleteCommand judgeCommand = new JudgeDeleteCommand();

            // Populate the model objects
            model.addObject(JUDGE_LIST, judgePageStateHolder.getJudges());
            model.addObject(JUDGE_TYPE_LIST, judgePageStateHolder.getJudgeTypes());
            model.addObject(COURTSITE, courtSite);
            model.addObject(COMMAND, judgeCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_DELETE_JUDGE);

        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Delete judge.
     *
     * @param judgeDeleteCommand the judge delete command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_DELETE_JUDGE, method = RequestMethod.POST,
        params = "btnDeleteConfirm")
    public ModelAndView deleteJudge(@Valid final JudgeDeleteCommand judgeDeleteCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "deleteJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Default is to return to the delete judge page to display errors
        model.setViewName(VIEW_NAME_DELETE_JUDGE);        

        judgeDeleteValidator.validate(judgeDeleteCommand, result);
        if (result.hasErrors()) {
            model.addObject(JUDGE_LIST, judgePageStateHolder.getJudges());
        } else {
            try {
                LOGGER.debug("{}{} - deleting Judge", METHOD, methodName);

                // Delete the Judge (update with obsInd=Y)
                refJudgeService.updateJudge(judgeDeleteCommand);


                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Judge has been deleted successfully.");

                // Successfully registered so redirect to the judge page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewJudge(model, false);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to delete Judge ", METHOD, methodName, ex);
                // Reject
                result.reject("judgeErrors", "Unable to delete Judge: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, judgeDeleteCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }
}