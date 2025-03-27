package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;

public class CduAmendController extends CdusControllerUtility {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CduAmendController.class);

    /**
     * Restart cdu.
     *
     * @param cduSearchCommand the cdu search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.POST, params = "btnRestartCduConfirm")
    public ModelAndView restartCdu(final CduSearchCommand cduSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "restartCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        setCduSearchCommand(cduSearchCommand);
        
        // Refresh the list of CDU's
        cduPageStateHolder.setCdus(getCduList(cduSearchCommand));
        
        // Validate the selection
        cduSearchSelectedValidator.validate(cduSearchCommand, result);
        if (!result.hasErrors()) {
            try {
                final List<CduDto> cdus = new ArrayList<>();
                final CduDto cdu = getSelectedCdu(cduSearchCommand.getSelectedMacAddress());
                cdus.add(cdu);

                // restart the selected CDUs
                cduService.restartCdu(cdus);

                // Add successMessage for display on page
                model.addObject(SUCCESS_MESSAGE,
                    CDU_WITH_MACADDRESS + cduSearchCommand.getSelectedMacAddress()
                        + " has successfully been sent a request to restart.");
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to request restart of CDU with mac Address : {}", METHOD,
                    methodName, cduSearchCommand.getSelectedMacAddress(), ex);
                // Reject
                result.reject(CDU_ERRORS, "Unable to request restart of CDU with mac Address "
                    + cduSearchCommand.getSelectedMacAddress() + " : " + ex.getMessage());
            }
        }

        model.addObject(COMMAND, cduSearchCommand);

        // Set the model and view
        setModelCduList(model);
        model.setViewName(VIEW_NAME_CDUS);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Restart all cdus.
     *
     * @param cduSearchCommand the cdu search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.POST,
        params = "btnRestartAllCduConfirm")
    public ModelAndView restartAllCdus(final CduSearchCommand cduSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "restartAllCdus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        setCduSearchCommand(cduSearchCommand);
        
        // Refresh the list of CDU's
        cduPageStateHolder.setCdus(getCduList(cduSearchCommand));
        
        // Validate the selection
        cduRestartAllValidator.validate(cduSearchCommand, result);
        if (!result.hasErrors()) {
            try {
                // restart the selected CDUs
                cduService.restartCdu(cduPageStateHolder.getCdus());

                // Add successMessage for display on page
                model.addObject(SUCCESS_MESSAGE,
                    "All CDUs have successfully been sent a request to restart.");
            } catch (final DataAccessException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to request restart of all CDUs {}", METHOD, methodName,
                    ex.getMessage());
                // Reject
                result.reject(CDU_ERRORS,
                    "Unable to request restart of all CDUs : " + ex.getMessage());
            } catch (final XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to request restart of all CDUs {}", METHOD, methodName,
                    ex.getMessage());
                // Reject
                result.reject(CDU_ERRORS,
                    "Unable to request restart of all CDUs: " + ex.getMessage());
            }
        }

        model.addObject(COMMAND, cduSearchCommand);

        // Set the model and view
        setModelCduList(model);
        model.setViewName(VIEW_NAME_CDUS);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show amend cdu.
     *
     * @param cduSearchCommand the cdu search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.POST, params = "btnShowAmendCdu")
    public ModelAndView showAmendCdu(final CduSearchCommand cduSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showAmendCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        cduPageStateHolder.setCduSearchCommand(cduSearchCommand);
        
        // Refresh the list of CDU's
        cduPageStateHolder.setCdus(getCduList(cduSearchCommand));

        cduSearchSelectedValidator.validate(cduSearchCommand, result);
        if (result.hasErrors()) {
            setModelCduList(model);
            model.setViewName(VIEW_NAME_CDUS);
            LOGGER.info(THREE_PARAMS, METHOD, methodName, VIEW_SET);
        } else {
            // Set the selected Cdu
            populateSelectedCduInPageStateHolder(cduSearchCommand.getSelectedMacAddress());
            LOGGER.debug("{}{} selected macAddress : {}", METHOD, methodName,
                cduPageStateHolder.getCdu().getMacAddress());

            // Create a new CduAmendCommand object for the page
            CduAmendCommand cduAmendCommand = new CduAmendCommand();

            // Populate the relevant fields
            final CduDto cduDto = cduPageStateHolder.getCdu();
            cduAmendCommand.setCduId(cduDto.getId().intValue());
            cduAmendCommand.setLocation(cduDto.getLocation());
            cduAmendCommand.setDescription(cduDto.getDescription());
            cduAmendCommand.setNotification(cduDto.getNotification());
            cduAmendCommand.setRefresh(cduDto.getRefresh());
            cduAmendCommand.setWeighting(cduDto.getWeighting());
            cduAmendCommand.setOfflineIndicator(cduDto.getOfflineIndicator());

            // Set the model
            model.addObject(CDU, cduDto);
            model.addObject(COMMAND, cduAmendCommand);
            LOGGER.debug("{}{} cduDto and cduAmendCommand added to model", METHOD, methodName);

            // Set the view name
            model.setViewName(VIEW_NAME_AMEND_CDU);
            LOGGER.info(THREE_PARAMS, METHOD, methodName, VIEW_SET);
        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Update cdu.
     *
     * @param cduAmendCommand the cdu command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = AMEND_CDU, method = RequestMethod.POST, params = "btnUpdateCdu")
    public ModelAndView updateCdu(@Valid final CduAmendCommand cduAmendCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "updateCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Default is to return to the amend CDU page to display errors
        model.setViewName(VIEW_NAME_AMEND_CDU);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, VIEW_SET);

        // Validate this selection is valid for amendment
        cduAmendValidator.validate(cduAmendCommand, result);
        
        // Refresh the CduDto object in the PageStateHolder
        cduPageStateHolder.setCdu(cduService.getCduByCduId(cduAmendCommand.getCduId()));
        LOGGER.info("CDU to update with Id: {}", cduPageStateHolder.getCdu().getId());
        
        if (!result.hasErrors()) {
            try {
                // Update the CDU
                LOGGER.debug("{}{} - updating CDU", METHOD, methodName);
                cduService.updateCdu(cduPageStateHolder.getCdu(), cduAmendCommand);
                LOGGER.debug("{}{} - updated CDU", METHOD, methodName);

                model.addObject(SUCCESS_MESSAGE,
                    CDU_WITH_MACADDRESS + cduPageStateHolder.getCdu().getMacAddress()
                        + " has been updated successfully.");

                // Successfully registered so redirect to the cdus page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return showCduSearch(model, false);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to update CDU ", METHOD, methodName, ex);
                // Reject
                result.reject(CDU_ERRORS, "Unable to update CDU: " + ex.getMessage());
            }
        }

        model.addObject(CDU, cduPageStateHolder.getCdu());
        LOGGER.debug("{}{} cduDto added to model", METHOD, methodName);
        model.addObject(COMMAND, cduAmendCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show the cdu search page.
     *
     * @param model the model
     * @param reset as boolean
     * @return the model and view
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.GET)
    public ModelAndView showCduSearchGet(final ModelAndView model,
        @RequestParam(value = "reset", defaultValue = "true") final boolean reset) {
        final String methodName = "showCduSearchGet";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return showCduSearch(model, reset);
    }

    @RequestMapping(value = VIEW_CDU, method = RequestMethod.POST)
    public ModelAndView showCduSearchPost(final ModelAndView model,
        @RequestParam(value = "reset", defaultValue = "true") final boolean reset) {
        final String methodName = "showCduSearchPost";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return showCduSearch(model, reset);
    }

    private ModelAndView showCduSearch(final ModelAndView model, final boolean reset) {
        final String methodName = "showCduSearch";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        CduSearchCommand cduSearchCommand;
        if (reset || !cduSearchValidator.isValid(cduPageStateHolder.getCduSearchCommand())) {
            LOGGER.debug("{}{} - reset cdu search results", METHOD, methodName);
            cduPageStateHolder.reset();
            cduSearchCommand = new CduSearchCommand();
            LOGGER.debug("{}{} retrieving court site data...", METHOD, methodName);
            final List<XhibitCourtSiteDto> courtSiteList =
                localProxyService.getXhibitCourtSitesWithLocalProxy();
            cduPageStateHolder.setSites(courtSiteList);
        } else {
            LOGGER.debug("{}{} - refresh cdu search results from current criteria", METHOD,
                methodName);
            cduSearchCommand = cduPageStateHolder.getCduSearchCommand();
            final List<CduDto> cduList = getCduList(cduSearchCommand);
            cduPageStateHolder.setCdus(cduList);
        }
        // Add the cduSearchCommand to the model
        model.addObject(COMMAND, cduSearchCommand);
        LOGGER.debug("{}{} command object - cduSearchCommand added to model", METHOD, methodName);

        // Set the model lists
        setModelCduList(model);
        model.setViewName(VIEW_NAME_CDUS);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

}
