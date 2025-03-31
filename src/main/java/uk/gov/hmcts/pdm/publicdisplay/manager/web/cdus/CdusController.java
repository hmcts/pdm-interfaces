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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;

/**
 * The Class CdusController.
 *
 * @author uphillj
 */

@Controller
@RequestMapping("/cdus")
public class CdusController extends CduRegistrationController {
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CdusController.class);

    /**
     * Search for cdu.
     *
     * @param cduSearchCommand the cdu search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.GET, params = "dashboardSearch")
    public ModelAndView searchForCduFromDashboard(final CduSearchCommand cduSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "searchForCduFromDashboard";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the model and view for the cdu search
        setModelForCduSearch(cduSearchCommand, result, model, false);
        model.addObject(COMMAND, cduSearchCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Search for cdu.
     *
     * @param cduSearchCommand the cdu search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.POST, params = "btnSearchSites")
    public ModelAndView searchForCdu(final CduSearchCommand cduSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "searchForCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        
        // Set the model and view for the search CDU
        setModelForCduSearch(cduSearchCommand, result, model, true);
        model.addObject(COMMAND, cduSearchCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Search for cdu.
     *
     * @param cduSearchCommand the cdu search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.POST, params = "btnShowCdu")
    public ModelAndView showCduDetails(final CduSearchCommand cduSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showCduDetails";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        setCduSearchCommand(cduSearchCommand);
        
        // Refresh the list of CDU's
        cduPageStateHolder.setCdus(getCduList(cduSearchCommand));

        // Business Specific validation for cdu search
        cduSearchSelectedValidator.validate(cduSearchCommand, result);
        if (!result.hasErrors()) {
            LOGGER.info("{}{} - no binding errors, starting search process", METHOD, methodName);

            // Set the page holder selected cdu
            final CduDto cdu =
                populateSelectedCduInPageStateHolder(cduSearchCommand.getSelectedMacAddress());

            LOGGER.info("{}{} - Adding cdu to model", METHOD, methodName);
            model.addObject(CDU, cdu);
            model.addObject(COMMAND, cduSearchCommand);
        }

        setModelCduList(model);
        model.setViewName(VIEW_NAME_CDUS);
        return model;
    }

    /**
     * Gets the cdu screenshot.
     *
     * @return the cdu screen shot
     * @throws NoSuchRequestHandlingMethodException the no such request handling method exception
     */
    @RequestMapping(value = MAPPING_CDU_SCREENSHOT, method = RequestMethod.GET,
        produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<ByteArrayResource> getCduScreenshot() throws NoHandlerFoundException {
        final String methodName = "getCduScreenShot";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        LOGGER.info(THREE_PARAMS, methodName, " CduSearchCommand state: ", cduPageStateHolder.getCduSearchCommand());
        
        // Throw 404 exception if cdu is invalid
        if (cduPageStateHolder.getCdu() == null
            || !cduSearchSelectedValidator.isValid(cduPageStateHolder.getCduSearchCommand())) {
            LOGGER.error("Screenshot requested for non-existent cdu");
            throw new NoHandlerFoundException(methodName, MAPPING_CDU_SCREENSHOT,
                new HttpHeaders());
        }

        ResponseEntity<ByteArrayResource> response;
        try {
            // Get the screenshot in bytes
            final byte[] cduScreenshot = cduService.getCduScreenshot(cduPageStateHolder.getCdu());

            // Get the response for the png media type
            response = getMediaResource(cduScreenshot, MediaType.IMAGE_PNG);
        } catch (final DataAccessException | XpdmException ex) {
            // Log the error
            LOGGER.error("{}{} Unable to get cdu screenshot", METHOD, methodName, ex);
            // Throw 404 exception if unable to get screenshot
            NoHandlerFoundException noHandlerFoundException =
                new NoHandlerFoundException(methodName, MAPPING_CDU_SCREENSHOT, new HttpHeaders());
            noHandlerFoundException.initCause(ex);
            throw noHandlerFoundException;
        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return response;
    }

}
