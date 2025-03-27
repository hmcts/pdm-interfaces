package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

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
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UrlDto;

import java.util.ArrayList;
import java.util.List;

public class CduUrlManagementController extends CduAmendController {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CduUrlManagementController.class);
    private static final String ADD = "add";
    
    /**
     * Redirect to one of amend_url or remove_url depending on contents of url method.
     * 
     * @param cduSearchCommand as CduSearchCommand
     * @param result as BindingResult
     * @param urlMethod as String
     * @param model as ModelAndView
     * @return String
     */
    @RequestMapping(value = VIEW_CDU, method = RequestMethod.POST, params = "btnManageUrl")
    public ModelAndView redirectToUrlPage(final CduSearchCommand cduSearchCommand,
        final BindingResult result, @RequestParam("btnManageUrl") final String urlMethod,
        final ModelAndView model) {
        final String methodName = "redirectToUrlPage ";

        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        cduPageStateHolder.setCduSearchCommand(cduSearchCommand);

        LOGGER.info("{}{} contents of urlMethod : {}", METHOD, methodName, urlMethod);

        // Set a default view
        model.setViewName(VIEW_NAME_CDUS);
        
        // Refresh the list of CDU's
        cduPageStateHolder.setCdus(getCduList(cduSearchCommand));

        cduSearchSelectedValidator.validate(cduSearchCommand, result);
        if (!result.hasErrors()) {
            try {
                // Set the page holder selected cdu
                final CduDto cdu =
                    populateSelectedCduInPageStateHolder(cduSearchCommand.getSelectedMacAddress());

                if (ADD.equalsIgnoreCase(urlMethod)) {
                    LOGGER.debug("{}{} - redirect to add url mapping page", METHOD, methodName);

                    /*
                     * Extract a list of urls that ARE NOT currently assigned to this cdu - only
                     * require this for ADD Use urlService retrieval methods to do this
                     */
                    final List<UrlDto> availableUrlList;
                    if (cdu != null) {
                        availableUrlList = getAvailableUrlList(cdu.getXhibitCourtSiteId());
                    } else {
                        availableUrlList = new ArrayList<>();
                    }

                    /* Add availableUrls to pageStateHolder */
                    cduPageStateHolder.setAvailableUrls(availableUrlList);
                    model.addObject("availableUrls", cduPageStateHolder.getAvailableUrls());
                    model.setViewName(ADD_URL);
                } else {
                    LOGGER.debug("{}{} - redirect to remove url mapping page", METHOD, methodName);
                    model.setViewName(REMOVE_URL);
                }

                LOGGER.info("{}{} - Adding cdu to model", METHOD, methodName);
                model.addObject(CDU, cduPageStateHolder.getCdu());

                LOGGER.info("{}{} - Adding mappingCommand to model", METHOD, methodName);
                final MappingCommand mappingCommand = new MappingCommand();

                mappingCommand.setCduId(cdu != null ? cdu.getId() : null);
                model.addObject(COMMAND, mappingCommand);
            } catch (final DataAccessException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to show correct url management page. {}", METHOD,
                    methodName, ex.getMessage());
                // Reject
                result.reject(CDU_ERRORS,
                    "Unable to show correct url management page: " + ex.getMessage());
            } catch (final XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to show correct url management page. {}", METHOD,
                    methodName, ex.getMessage());
                // Reject
                result.reject(CDU_ERRORS,
                    "Unable to show correct url management page:  " + ex.getMessage());
            }

        }

        LOGGER.debug("{}{} - view name set to {}", METHOD, methodName, model.getViewName());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Remove a url mapping.
     * 
     * @param mappingCommand as MappingCommand object
     * @param result as bindingresult
     * @param model as model and view
     * @return ModelAndView
     */
    @RequestMapping(value = MAPPING_REMOVE_URL, method = RequestMethod.POST,
        params = "btnRemoveMappingConfirm")
    public ModelAndView removeUrlMapping(final MappingCommand mappingCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "removeUrlMapping";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        mappingRemoveValidator.validate(mappingCommand, result);
        
        // Refresh the page state Cdu in case validation fails
        cduPageStateHolder.setCdu(cduService.getCduByCduId(mappingCommand.getCduId().intValue()));
        
        if (!result.hasErrors()) {
            try {
                LOGGER.info("CDU id : {} Url id : {} have been selected", mappingCommand.getCduId(),
                    mappingCommand.getUrlId());

                LOGGER.info("Calling removeMapping....");
                cduService.removeMapping(mappingCommand);

                // Regenerate the cdu as we have updated the mappings
                final CduDto updatedCdu =
                    cduService.getCduByMacAddress(cduPageStateHolder.getCdu().getMacAddress());
                cduPageStateHolder.setCdu(updatedCdu);

                model.addObject(SUCCESS_MESSAGE, "Mapping removed successfully.");
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to remove Url Mapping {}", METHOD, methodName,
                    ex.getMessage());
                // Reject
                result.reject(CDU_ERRORS, "Unable to remove Url Mapping : " + ex.getMessage());
            }
        }

        model.addObject(CDU, cduPageStateHolder.getCdu());
        model.addObject(COMMAND, mappingCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Add a url mapping.
     * 
     * @param mappingCommand as MappingCommand object
     * @param result as bindingresult
     * @param model as model and view
     * @return ModelAndView
     */
    @RequestMapping(value = MAPPING_ADD_URL, method = RequestMethod.POST, params = "btnAddMapping")
    public ModelAndView addUrlMapping(final MappingCommand mappingCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "addUrlMapping";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        mappingAddValidator.validate(mappingCommand, result);
        
        // Refresh the page state Cdu in case validation fails
        cduPageStateHolder.setCdu(cduService.getCduByCduId(mappingCommand.getCduId().intValue()));
        
        if (!result.hasErrors()) {
            try {
                LOGGER.info("CDU id : {} Url id : {} have been selected", mappingCommand.getCduId(),
                    mappingCommand.getUrlId());

                LOGGER.info("Calling addMapping....");
                cduService.addMapping(mappingCommand);

                // Regenerate the cdu as we have updated the mappings
                final CduDto updatedCdu =
                    cduService.getCduByMacAddress(cduPageStateHolder.getCdu().getMacAddress());
                cduPageStateHolder.setCdu(updatedCdu);

                model.addObject(SUCCESS_MESSAGE, "Mapping added successfully.");
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to add a Url Mapping {}", METHOD, methodName,
                    ex.getMessage());
                // Reject
                result.reject(CDU_ERRORS, "Unable to add a Url Mapping : " + ex.getMessage());
            }
        }

        LOGGER.info("{}{} - adding cdu & availableUrls back onto model", METHOD, methodName);

        // Refresh the list of available urls
        final List<UrlDto> availableUrlList =
            getAvailableUrlList(cduPageStateHolder.getCdu().getXhibitCourtSiteId());
        cduPageStateHolder.setAvailableUrls(availableUrlList);

        model.addObject("availableUrls", cduPageStateHolder.getAvailableUrls());
        model.addObject(CDU, cduPageStateHolder.getCdu());
        model.addObject(COMMAND, mappingCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }
}
