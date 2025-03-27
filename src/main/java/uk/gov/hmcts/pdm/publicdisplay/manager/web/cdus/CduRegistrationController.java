package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtSiteDto;

import java.util.List;

public class CduRegistrationController extends CduUrlManagementController {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CduRegistrationController.class);

    /**
     * Initial display of Cdu registration page.
     *
     * @param cduSearchCommand as CduSearchCommand
     * @param result as Bindingresult
     * @param model as ModelAndView
     * @return model as ModelAndView
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.POST, params = "btnShowRegisterCdu")
    public ModelAndView showRegisterCdu(final CduSearchCommand cduSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showRegisterCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        setCduSearchCommand(cduSearchCommand);
        
        // Refresh the Cdu list in case validation fails
        cduPageStateHolder.setCdus(getCduList(cduSearchCommand));
        
        LOGGER.info("{}{} - selected mac address : {}", METHOD, methodName,
            cduSearchCommand.getSelectedMacAddress());

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

            // Create a new CduRegisterCommand object for the page
            CduRegisterCommand cduRegisterCommand = new CduRegisterCommand();
            final CourtSiteDto siteDto = localProxyService
                .getCourtSiteByXhibitCourtSiteId(cduSearchCommand.getXhibitCourtSiteId());
            
            cduRegisterCommand.setNotification(siteDto.getNotification());
            cduRegisterCommand.setCourtSiteId(cduPageStateHolder.getCdu().getCourtSiteId());
            cduRegisterCommand.setIpAddress(cduPageStateHolder.getCdu().getIpAddress());
            cduRegisterCommand.setMacAddress(cduPageStateHolder.getCdu().getMacAddress());
            
            final CduDto cduDto = cduPageStateHolder.getCdu();

            model.addObject(CDU, cduDto);
            model.addObject(COMMAND, cduRegisterCommand);
            LOGGER.debug("{}{} cduDto and cduRegisterCommand added to model", METHOD, methodName);

            // Set the view name
            model.setViewName(VIEW_NAME_REGISTER_CDU);
            LOGGER.info(THREE_PARAMS, METHOD, methodName, VIEW_SET);
        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }


    /**
     * Skeleton hook for cdu registration.
     *
     * @param cduRegisterCommand as CDUCommand
     * @param result as Bindingresult
     * @param model as ModelAndView
     * @return ModelAndView
     */
    @RequestMapping(value = REGISTER_CDU, method = RequestMethod.POST, params = "btnRegisterCdu")
    public ModelAndView registerCdu(@Valid final CduRegisterCommand cduRegisterCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "registerCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Default is to return to the register cdu page to display errors
        model.setViewName(VIEW_NAME_REGISTER_CDU);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, VIEW_SET);

        // Create a CduDto object using fields from the CduRegisterCommand
        cduPageStateHolder.setCdu(cduService.createCduDtoFromRegisterCommand(cduRegisterCommand));
        
        cduRegisterValidator.validate(cduRegisterCommand, result);
        
        if (!result.hasErrors()) {
            try {
                LOGGER.debug("{}{} - registering CDU", METHOD, methodName);
                cduService.registerCdu(cduPageStateHolder.getCdu(), cduRegisterCommand);
                LOGGER.debug("{}{} - registered CDU", METHOD, methodName);

                // Add successMessage to model for display on cdus page
                model.addObject(SUCCESS_MESSAGE,
                    CDU_WITH_MACADDRESS + cduPageStateHolder.getCdu().getMacAddress()
                        + " has been registered successfully.");

                // Successfully registered so redirect to the cdus page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return showCduSearchPost(model, false);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to register a CDU {}", METHOD, methodName,
                    ex.getMessage());
                // Reject
                result.reject(CDU_ERRORS, UNABLE_TO_REGISTER_CDU + ex.getMessage());
            }
        }

        model.addObject(CDU, cduPageStateHolder.getCdu());
        model.addObject(COMMAND, cduRegisterCommand);
        LOGGER.debug("{}{} cduDto added to model", METHOD, methodName);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }


    /**
     * Handler for the UnregisterCdu Process.
     *
     * @param cduSearchCommand as CduSearchCommand
     * @param result as BindingResult
     * @param model as ModelAndView
     * @return modelandview
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.POST,
        params = "btnUnRegisterCduConfirm")
    public ModelAndView unregisterCdu(final CduSearchCommand cduSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "unRegisterCdu";
        LOGGER.info("{}{} - starts here", METHOD, methodName);

        // Ensure the search command is the latest
        cduPageStateHolder.setCduSearchCommand(cduSearchCommand);

        cduUnregisterValidator.validate(cduSearchCommand, result);
        if (!result.hasErrors()) {
            try {
                LOGGER.debug("{}{} - unregister cdu with mac address : {}", METHOD, methodName,
                    cduSearchCommand.getSelectedMacAddress());
                final CduDto cdu = getSelectedCdu(cduSearchCommand.getSelectedMacAddress());
                if (cdu != null) {
                    cduService.unregisterCdu(cdu.getId());
                }
                LOGGER.debug("{}{} - unregister process completes here", METHOD, methodName);

                LOGGER.debug("{}{} - refresh cdu list", METHOD, methodName);
                final List<CduDto> cduList = getCduList(cduSearchCommand);
                cduPageStateHolder.setCdus(cduList);

                // Add successMessage for display on page
                model.addObject(SUCCESS_MESSAGE,
                    CDU_WITH_MACADDRESS + cduSearchCommand.getSelectedMacAddress()
                        + " has been unregistered successfully.");
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to unregister a CDU {}", METHOD, methodName,
                    ex.getMessage());
                // Reject
                result.reject(CDU_ERRORS, "Unable to unregister CDU: " + ex.getMessage());
            }
        }

        // Set the model and view
        setModelCduList(model);
        model.setViewName(VIEW_NAME_CDUS);

        model.addObject(COMMAND, cduSearchCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

}
