package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

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
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.EncryptedFormat;

@Controller
@RequestMapping("/court")
public class CourtController extends CourtPageStateSetter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CourtController.class);

    private static final String ADDING_COURT_TO_MODEL = "{}{} adding court data to model";
    private static final String ADDING_COURTSITE_TO_MODEL = "{}{} adding court site data to model";
    private static final String COURT_LIST = "courtList";
    private static final String COURTSITE_LIST = "courtSiteList";
    private static final String COMMAND = "command";
    private static final String COURT = "court";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String ATTEMPT = "Attempt {}{}";
    private static final String POPULATING_PAGESTATE_LISTS = ", populating the PageStateSelectionLists";
    private static final String PAGESTATE_LISTS_POPULATED = "All PageStateSelectionLists populated";
    private static final int MAX_NUM_OF_RETRIES = 5;

    /** The Constant for the JSP Folder. */
    protected static final String FOLDER_COURT = "court";

    /**
     * Court Court.
     */
    private static final String MAPPING_VIEW_COURT = "/view_court";

    /**
     * Create Court.
     */
    private static final String MAPPING_CREATE_COURT = "/create_court";

    /**
     * Amend Display.
     */
    private static final String MAPPING_AMEND_COURT = "/amend_court";

    /**
     * View Court View.
     */
    private static final String VIEW_NAME_VIEW_COURT_SITE = FOLDER_COURT + MAPPING_VIEW_COURT;

    /**
     * Create Court View.
     */
    private static final String VIEW_NAME_CREATE_COURT = FOLDER_COURT + MAPPING_CREATE_COURT;

    /**
     * Amend court View.
     */
    private static final String VIEW_NAME_AMEND_COURT = FOLDER_COURT + MAPPING_AMEND_COURT;

    /**
     * The display selected validator.
     */
    @Autowired
    private CourtSelectedValidator courtSelectedValidator;

    /**
     * The court create validator.
     */
    @Autowired
    private CourtCreateValidator courtCreateValidator;

    /**
     * The court amend validator.
     */
    @Autowired
    private CourtAmendValidator courtAmendValidator;

    /**
     * View Court.
     *
     * @param model the model
     * @param reset the reset
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_COURT, method = RequestMethod.GET)
    public ModelAndView viewCourt(final ModelAndView model,
        @RequestParam(value = "reset", defaultValue = "true") final boolean reset) {
        final String methodName = "viewCourt";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        CourtSearchCommand courtSearchCommand = new CourtSearchCommand();
        if (reset || courtPageStateHolder.getCourtSearchCommand() == null) {
            LOGGER.debug("{}{} - reset court search results", METHOD, methodName);
            courtPageStateHolder.reset();

            // retrieve and add the selection lists to the pageStateHolder
            setViewPageStateSelectionLists();
        } else {
            courtSearchCommand = courtPageStateHolder.getCourtSearchCommand();
        }

        // Add the courtSearchCommand to the model
        LOGGER.debug("{}{} adding courtSearchCommand to model", METHOD, methodName);
        model.addObject(COMMAND, courtSearchCommand);

        // Add the court data to model
        LOGGER.debug(ADDING_COURT_TO_MODEL, METHOD, methodName);
        model.addObject(COURT_LIST, courtPageStateHolder.getCourts());

        // Return the model
        LOGGER.debug("{}{} returning model", METHOD, methodName);
        model.setViewName(VIEW_NAME_VIEW_COURT_SITE);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show create court.
     *
     * @param courtSearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_COURT, method = RequestMethod.POST, params = "btnAdd")
    public ModelAndView showCreateCourt(@Valid final CourtSearchCommand courtSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showCreateCourt";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        courtPageStateHolder.setCourtSearchCommand(courtSearchCommand);

        courtSelectedValidator.validate(courtSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURTSITE_LIST, courtPageStateHolder.getSites());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_COURT_SITE);

        } else {
            // Populate the CourtSites list
            for (int i = 0; i < MAX_NUM_OF_RETRIES; i++) {
                LOGGER.info(ATTEMPT, i + 1, POPULATING_PAGESTATE_LISTS);
                setViewPageStateSelectionLists();
                if (!courtPageStateHolder.getSites().isEmpty()
                    && !courtPageStateHolder.getCourts().isEmpty()) {
                    LOGGER.info(PAGESTATE_LISTS_POPULATED);
                    break;
                }
            }

            // Get the selected Court
            final CourtDto court =
                populateSelectedCourtInPageStateHolder(courtSearchCommand.getCourtId());
            courtPageStateHolder.setCourt(court);

            // Populate the relevant fields
            final CourtCreateCommand courtCreateCommand = new CourtCreateCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, courtPageStateHolder.getSites());
            model.addObject(COURT, courtPageStateHolder.getCourt());
            model.addObject(COMMAND, courtCreateCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_CREATE_COURT);

        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Create court site.
     *
     * @param courtCreateCommand the court create command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_CREATE_COURT, method = RequestMethod.POST,
        params = "btnCreateConfirm")
    public ModelAndView createCourtSite(@Valid final CourtCreateCommand courtCreateCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "createCourtSite";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Default is to return to the create court page to display errors
        model.setViewName(VIEW_NAME_CREATE_COURT);

        courtCreateValidator.validate(courtCreateCommand, result, courtService,
            courtPageStateHolder.getCourt().getId());
        if (result.hasErrors()) {
            model.addObject(COURTSITE_LIST, courtPageStateHolder.getSites());
        } else {
            try {
                LOGGER.debug("{}{} - creating Court", METHOD, methodName);

                courtService.createCourt(courtCreateCommand,
                    courtPageStateHolder.getCourt().getId(),
                    courtPageStateHolder.getCourt().getAddressId());

                // Add successMessage to model for court on page
                model.addObject(SUCCESS_MESSAGE, "Court has been created successfully.");

                // Successfully registered so redirect to the court page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewCourt(model, true);
            } catch (final Exception ex) {
                // Log the error
                LOGGER.error("{}{} Unable to create Court ", METHOD, methodName, ex);
                // Reject
                result.reject("courtErrors", "Unable to create Court: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, courtCreateCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show amend court.
     *
     * @param courtSearchCommand the xhibit court site command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_COURT, method = RequestMethod.POST, params = "btnAmend")
    public ModelAndView showAmendCourt(@Valid final CourtSearchCommand courtSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showAmendCourt";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        courtPageStateHolder.setCourtSearchCommand(courtSearchCommand);

        courtSelectedValidator.validate(courtSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURT_LIST, courtPageStateHolder.getCourts());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_COURT_SITE);

        } else {
            // Populate the amend lists
            for (int i = 0; i < MAX_NUM_OF_RETRIES; i++) {
                LOGGER.info(ATTEMPT, i + 1, POPULATING_PAGESTATE_LISTS);
                setAmendPageStateSelectionLists(courtSearchCommand.getCourtId());
                if (!courtPageStateHolder.getSites().isEmpty()
                    && !courtPageStateHolder.getCourts().isEmpty()) {
                    LOGGER.info(PAGESTATE_LISTS_POPULATED);
                    break;
                }
            }

            // Get the selected Court
            final CourtDto court =
                populateSelectedCourtInPageStateHolder(courtSearchCommand.getCourtId());
            courtPageStateHolder.setCourt(court);

            // Populate the relevant fields
            final CourtAmendCommand courtAmendCommand = new CourtAmendCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, courtPageStateHolder.getSites());
            model.addObject(COURT, courtPageStateHolder.getCourt());
            model.addObject(COMMAND, courtAmendCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_AMEND_COURT);
        }
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Load court site.
     *
     * @param xhibitCourtSiteId the court site id
     * @return the selected XhibitCourtSiteDto
     */
    @RequestMapping(value = MAPPING_AMEND_COURT + "/{xhibitCourtSiteId}",
        method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public XhibitCourtSiteDto loadCourtSite(
        @PathVariable("xhibitCourtSiteId") @EncryptedFormat final Long xhibitCourtSiteId) {
        final String methodName = "loadCourtSite";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        XhibitCourtSiteDto result = null;
        for (XhibitCourtSiteDto dto : courtPageStateHolder.getSites()) {
            if (dto.getId().equals(xhibitCourtSiteId)) {
                LOGGER.info("Found Court");
                result = dto;
                break;
            }
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    /**
     * Update court.
     *
     * @param courtAmendCommand the court amend command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_AMEND_COURT, method = RequestMethod.POST,
        params = "btnUpdateConfirm")
    public ModelAndView updateCourt(@Valid final CourtAmendCommand courtAmendCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "updateCourt";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        courtAmendValidator.validate(courtAmendCommand, result);
        if (result.hasErrors()) {
            // Default is to return to the amend court page to display errors
            model.setViewName(VIEW_NAME_AMEND_COURT);

            model.addObject(COURTSITE_LIST, courtPageStateHolder.getSites());
        } else {
            try {
                LOGGER.debug("{}{} - updating Court", METHOD, methodName);

                // Update the court
                courtService.updateCourt(courtAmendCommand);

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Court has been updated successfully.");

                // Successfully registered so redirect to the court page which needs a new
                // model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewCourt(model, true);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to update Court ", METHOD, methodName, ex);
                // Reject
                result.reject("courtErrors", "Unable to update Court: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, courtAmendCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

}
