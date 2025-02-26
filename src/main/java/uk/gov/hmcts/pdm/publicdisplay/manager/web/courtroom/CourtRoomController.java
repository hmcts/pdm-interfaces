package uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom;

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
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DynamicDropdownList;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.EncryptedFormat;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/courtroom")
@SuppressWarnings("PMD.TooManyMethods")
public class CourtRoomController extends CourtRoomPageStateSetter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CourtRoomController.class);

    private static final String ADDING_COURTSITE_TO_MODEL = "{}{} adding court room data to model";
    private static final String ADDING_COURT_TO_MODEL = "{}{} adding court data to model";
    private static final String COURT_LIST = "courtList";
    private static final String COURTSITE_LIST = "courtSiteList";
    private static final String COURTROOM_LIST = "courtRoomList";
    private static final String COMMAND = "command";
    private static final String COURT = "court";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ATTEMPT = "Attempt {}{}";
    private static final String POPULATING_PAGESTATE_LISTS = ", populating the PageStateSelectionLists";
    private static final String PAGESTATE_LISTS_POPULATED = "All PageStateSelectionLists populated";
    private static final int MAX_NUM_OF_RETRIES = 5;

    /** The Constant for the JSP Folder. */
    private static final String FOLDER_COURTROOM = "courtroom";
    
    /**
     * Create Court Room.
     */
    private static final String MAPPING_CREATE_COURTROOM = "/create_courtroom";

    /**
     * View Court Room.
     */
    private static final String MAPPING_VIEW_COURTROOM = "/view_courtroom";

    /**
     * Delete Court Room.
     */
    private static final String MAPPING_DELETE_COURTROOM = "/delete_courtroom";

    /**
     * Amend Court Room.
     */
    private static final String MAPPING_AMEND_COURTROOM = "/amend_courtroom";

    /**
     * View Court Room.
     */
    private static final String VIEW_NAME_VIEW_COURTROOM = FOLDER_COURTROOM + MAPPING_VIEW_COURTROOM;

    /**
     * View Court Room.
     */
    private static final String VIEW_NAME_CREATE_COURTROOM =
        FOLDER_COURTROOM + MAPPING_CREATE_COURTROOM;

    /**
     * View Delete Court Room.
     */
    private static final String VIEW_NAME_DELETE_COURTROOM =
        FOLDER_COURTROOM + MAPPING_DELETE_COURTROOM;

    /**
     * View Amend Court Room.
     */
    private static final String VIEW_NAME_AMEND_COURTROOM =
        FOLDER_COURTROOM + MAPPING_AMEND_COURTROOM;

    /**
     * The Court Room selected validator.
     */
    @Autowired
    private CourtRoomSelectedValidator courtRoomSelectedValidator;

    /**
     * The Court Room create validator.
     */
    @Autowired
    private CourtRoomCreateValidator courtRoomCreateValidator;

    /**
     * The Court Room delete validator.
     */
    @Autowired
    private CourtRoomDeleteValidator courtRoomDeleteValidator;

    /**
     * The Court Room amend validator.
     */
    @Autowired
    private CourtRoomAmendValidator courtRoomAmendValidator;

    /**
     * View Court Room.
     *
     * @param model the model
     * @param reset the reset
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_COURTROOM, method = RequestMethod.GET)
    public ModelAndView viewCourtRoom(final ModelAndView model,
        @RequestParam(value = "reset", defaultValue = "true") final boolean reset) {
        final String methodName = "viewCourtRoom";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        CourtRoomSearchCommand courtRoomSearchCommand = new CourtRoomSearchCommand();
        if (reset || courtRoomPageStateHolder.getCourtRoomSearchCommand() == null) {
            LOGGER.debug("{}{} - reset court room search results", METHOD, methodName);
            courtRoomPageStateHolder.reset();

            // retrieve and add the selection lists to the pageStateHolder
            setViewPageStateSelectionLists();
        } else {
            courtRoomSearchCommand = courtRoomPageStateHolder.getCourtRoomSearchCommand();
        }

        // Add the courtRoomSearchCommand to the model
        LOGGER.debug("{}{} adding courtRoomSearchCommand to model", METHOD, methodName);
        model.addObject(COMMAND, courtRoomSearchCommand);

        // Add the court data to model
        LOGGER.debug(ADDING_COURT_TO_MODEL, METHOD, methodName);
        model.addObject(COURT_LIST, courtRoomPageStateHolder.getCourts());

        // Return the model
        LOGGER.debug("{}{} returning model", METHOD, methodName);
        model.setViewName(VIEW_NAME_VIEW_COURTROOM);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show Create Court Room.
     *
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_COURTROOM, method = RequestMethod.POST, params = "btnAdd")
    public ModelAndView showCreateCourtRoom(final CourtRoomSearchCommand courtRoomSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showCreateCourtRoom";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        courtRoomPageStateHolder.setCourtRoomSearchCommand(courtRoomSearchCommand);

        courtRoomSelectedValidator.validate(courtRoomSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURT_LIST, courtRoomPageStateHolder.getCourts());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_COURTROOM);

        } else {
            // Populate the create lists
            for (int i = 0; i < MAX_NUM_OF_RETRIES; i++) {
                LOGGER.info(ATTEMPT, i + 1, POPULATING_PAGESTATE_LISTS);
                setAmendPageStateSelectionLists(courtRoomSearchCommand.getCourtId());
                if (!courtRoomPageStateHolder.getSites().isEmpty()
                    && !courtRoomPageStateHolder.getCourts().isEmpty()) {
                    LOGGER.info(PAGESTATE_LISTS_POPULATED);
                    break;
                }
            }

            // Get the selected CourtSite
            final CourtDto court =
                populateSelectedCourtInPageStateHolder(courtRoomSearchCommand.getCourtId());

            // Populate the relevant fields
            final CourtRoomCreateCommand courtRoomCreateCommand = new CourtRoomCreateCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, courtRoomPageStateHolder.getSites());
            model.addObject(COURT, court);
            model.addObject(COMMAND, courtRoomCreateCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_CREATE_COURTROOM);

        }
        // Return the model
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Show amend court room.
     *
     * @param courtRoomSearchCommand the court room search command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_COURTROOM, method = RequestMethod.POST,
        params = "btnAmend")
    public ModelAndView showAmendCourtRoom(
        @Valid final CourtRoomSearchCommand courtRoomSearchCommand, final BindingResult result,
        final ModelAndView model) {
        final String methodName = "showAmendCourtRoom";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        courtRoomPageStateHolder.setCourtRoomSearchCommand(courtRoomSearchCommand);

        courtRoomSelectedValidator.validate(courtRoomSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURT_LIST, courtRoomPageStateHolder.getCourts());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_COURTROOM);

        } else {
            // Populate the amend lists
            for (int i = 0; i < MAX_NUM_OF_RETRIES; i++) {
                LOGGER.info(ATTEMPT, i + 1, POPULATING_PAGESTATE_LISTS);
                setAmendPageStateSelectionLists(courtRoomSearchCommand.getCourtId());
                if (!courtRoomPageStateHolder.getSites().isEmpty()
                    && !courtRoomPageStateHolder.getCourts().isEmpty()) {
                    LOGGER.info(PAGESTATE_LISTS_POPULATED);
                    break;
                }
            }
            
            // Get the selected court
            final CourtDto court =
                populateSelectedCourtInPageStateHolder(courtRoomSearchCommand.getCourtId());


            // Populate the relevant fields
            final CourtRoomAmendCommand courtRoomAmendCommand = new CourtRoomAmendCommand();
            final List<CourtRoomDto> courtRoomList = new ArrayList<>();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, courtRoomPageStateHolder.getSites());
            model.addObject(COURTROOM_LIST, courtRoomList);
            model.addObject(COURT, court);
            model.addObject(COMMAND, courtRoomAmendCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_AMEND_COURTROOM);
        }
        // Return the model
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Load courtrooms.
     *
     * @param xhibitCourtSiteId the court site id
     * @return the selected CourtRoomDtos
     */
    @RequestMapping(value = MAPPING_AMEND_COURTROOM + "/{xhibitCourtSiteId}",
        method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ResponseBody
    public DynamicDropdownList loadCourtRoomsForAmend(
        @PathVariable("xhibitCourtSiteId") @EncryptedFormat final Long xhibitCourtSiteId) {
        final String methodName = "loadCourtRoomsForAmend";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        DynamicDropdownList result = createDynamicDropdownList();
        courtRoomPageStateHolder
            .setCourtRoomsList(courtRoomService.getCourtRooms(xhibitCourtSiteId));
        for (CourtRoomDto courtRoom : courtRoomPageStateHolder.getCourtRoomsList()) {
            result.getOptions().add(createDynamicDropdownOption(courtRoom.getId().intValue(),
                courtRoom.getCourtRoomName()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    /**
     * Load selected courtroom.
     *
     * @param courtRoomId the court room id
     * @return the selected CourtRoomDto
     */
    @RequestMapping(value = MAPPING_AMEND_COURTROOM + "/courtRoom/{courtRoomId}",
        method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ResponseBody
    public CourtRoomDto loadSelectedCourtRoomForAmend(
        @PathVariable("courtRoomId") @EncryptedFormat final Long courtRoomId) {
        final String methodName = "loadSelectedCourtRoomForAmend";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        CourtRoomDto result = null;
        for (CourtRoomDto dto : courtRoomPageStateHolder.getCourtRoomsList()) {
            if (dto.getId().equals(courtRoomId)) {
                LOGGER.info("Found CourtRoom");
                result = dto;
                break;
            }
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    /**
     * Load courtrooms.
     *
     * @param xhibitCourtSiteId the court site id
     * @return the selected CourtRoomDtos
     */
    @RequestMapping(value = MAPPING_DELETE_COURTROOM + "/{xhibitCourtSiteId}",
        method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ResponseBody
    public DynamicDropdownList loadCourtRoomsForDelete(
        @PathVariable("xhibitCourtSiteId") @EncryptedFormat final Long xhibitCourtSiteId) {
        final String methodName = "loadCourtRoomsForDelete";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        DynamicDropdownList result = createDynamicDropdownList();
        courtRoomPageStateHolder
            .setCourtRoomsList(courtRoomService.getCourtRooms(xhibitCourtSiteId));
        for (CourtRoomDto courtRoom : courtRoomPageStateHolder.getCourtRoomsList()) {
            result.getOptions().add(createDynamicDropdownOption(courtRoom.getId().intValue(),
                courtRoom.getCourtRoomName()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    /**
     * Load selected courtroom.
     *
     * @param courtRoomId the court room id
     * @return the selected CourtRoomDto
     */
    @RequestMapping(value = MAPPING_DELETE_COURTROOM + "/courtRoom/{courtRoomId}",
        method = RequestMethod.GET, produces = APPLICATION_JSON)
    @ResponseBody
    public CourtRoomDto loadSelectedCourtRoomForDelete(
        @PathVariable("courtRoomId") @EncryptedFormat final Long courtRoomId) {
        final String methodName = "loadSelectedCourtRoomForDelete";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        CourtRoomDto result = null;
        for (CourtRoomDto dto : courtRoomPageStateHolder.getCourtRoomsList()) {
            if (dto.getId().equals(courtRoomId)) {
                result = dto;
                break;
            }
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    /**
     * Show Delete Court Room.
     *
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_VIEW_COURTROOM, method = RequestMethod.POST,
        params = "btnDelete")
    public ModelAndView showDeleteCourtRoom(final CourtRoomSearchCommand courtRoomSearchCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "showDeleteCourtRoom";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        courtRoomPageStateHolder.setCourtRoomSearchCommand(courtRoomSearchCommand);
        courtRoomPageStateHolder.setCourtRoomsList(new ArrayList<>());

        courtRoomSelectedValidator.validate(courtRoomSearchCommand, result);
        if (result.hasErrors()) {

            // Add the court site data to model
            LOGGER.debug(ADDING_COURTSITE_TO_MODEL, METHOD, methodName);
            model.addObject(COURT_LIST, courtRoomPageStateHolder.getCourts());

            // Set the view
            model.setViewName(VIEW_NAME_VIEW_COURTROOM);

        } else {
            // Populate the delete lists
            for (int i = 0; i < MAX_NUM_OF_RETRIES; i++) {
                LOGGER.info(ATTEMPT, i + 1, POPULATING_PAGESTATE_LISTS);
                setAmendPageStateSelectionLists(courtRoomSearchCommand.getCourtId());
                if (!courtRoomPageStateHolder.getSites().isEmpty()
                    && !courtRoomPageStateHolder.getCourts().isEmpty()) {
                    LOGGER.info(PAGESTATE_LISTS_POPULATED);
                    break;
                }
            }
            
            // Get the selected CourtSite
            final CourtDto court =
                populateSelectedCourtInPageStateHolder(courtRoomSearchCommand.getCourtId());

            // Populate the relevant fields
            final CourtRoomDeleteCommand courtRoomDeleteCommand = new CourtRoomDeleteCommand();

            // Populate the model objects
            model.addObject(COURTSITE_LIST, courtRoomPageStateHolder.getSites());
            model.addObject(COURTROOM_LIST, courtRoomPageStateHolder.getCourtRoomsList());
            model.addObject(COURT, court);
            model.addObject(COMMAND, courtRoomDeleteCommand);
            LOGGER.debug("{}{} - Added command object to model", METHOD, methodName);

            // Set the view
            model.setViewName(VIEW_NAME_DELETE_COURTROOM);

        }
        // Return the model
        LOGGER.debug("{}{} viewName: {}", METHOD, methodName, model.getViewName());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Create courtroom.
     *
     * @param courtRoomCreateCommand the court room create command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_CREATE_COURTROOM, method = RequestMethod.POST,
        params = "btnCreateConfirm")
    public ModelAndView createCourtRoom(@Valid final CourtRoomCreateCommand courtRoomCreateCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "createCourtRoom";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Default is to return to the create court room page to display errors
        model.setViewName(VIEW_NAME_CREATE_COURTROOM);

        courtRoomCreateValidator.validate(courtRoomCreateCommand, result, courtRoomService);
        if (result.hasErrors()) {
            model.addObject(COURTSITE_LIST, courtRoomPageStateHolder.getSites());
            model.addObject(COURT, courtRoomPageStateHolder.getCourt());
        } else {
            try {
                LOGGER.debug("{}{} - creating Court Room", METHOD, methodName);

                // Get the existing list of court rooms (for the roomNo)
                List<CourtRoomDto> courtRoomDtos =
                    courtRoomService.getCourtRooms(courtRoomCreateCommand.getXhibitCourtSiteId());

                // Create the court room
                courtRoomService.createCourtRoom(courtRoomCreateCommand, courtRoomDtos);

                // Add successMessage to model for court on page
                model.addObject(SUCCESS_MESSAGE, "Court Room has been created successfully.");

                // Successfully registered so redirect to the court page which needs a new model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewCourtRoom(model, true);
            } catch (final Exception ex) {
                model.addObject(COURTSITE_LIST, courtRoomPageStateHolder.getSites());
                model.addObject(COURT, courtRoomPageStateHolder.getCourt());
                // Log the error
                LOGGER.error("{}{} Unable to create Court Room ", METHOD, methodName, ex);
                // Reject
                result.reject("courtRoomErrors", "Unable to create Court Room: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, courtRoomCreateCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Delete court room.
     *
     * @param courtRoomDeleteCommand the court room delete command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_DELETE_COURTROOM, method = RequestMethod.POST,
        params = "btnDeleteConfirm")
    public ModelAndView deleteCourtRoom(@Valid final CourtRoomDeleteCommand courtRoomDeleteCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "deleteCourtRoom";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        courtRoomDeleteValidator.validate(courtRoomDeleteCommand, result);
        if (result.hasErrors()) {
            // Default is to return to the amend courtRoom page to display errors
            model.setViewName(VIEW_NAME_DELETE_COURTROOM);

            model.addObject(COURTSITE_LIST, courtRoomPageStateHolder.getSites());
            model.addObject(COURTROOM_LIST, courtRoomPageStateHolder.getCourtRoomsList());
        } else {
            try {
                LOGGER.debug("{}{} - deleting CourtRoom", METHOD, methodName);

                // Delete the courtRoom (update obsInd='Y')
                courtRoomService.updateCourtRoom(courtRoomDeleteCommand, null);

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Court Room has been deleted successfully.");

                // Successfully registered so redirect to the courtRoom page which needs a new
                // model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewCourtRoom(model, true);
            } catch (final DataAccessException | XpdmException ex) {
                model.addObject(COURTSITE_LIST, courtRoomPageStateHolder.getSites());
                model.addObject(COURTROOM_LIST, courtRoomPageStateHolder.getCourtRoomsList());
                // Log the error
                LOGGER.error("{}{} Unable to delete CourtRoom ", METHOD, methodName, ex);
                // Reject
                result.reject("courtRoomErrors", "Unable to delete CourtRoom: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, courtRoomDeleteCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }

    /**
     * Update court room.
     *
     * @param courtRoomAmendCommand the court room amend command
     * @param result the result
     * @param model the model
     * @return the model and view
     */
    @RequestMapping(value = MAPPING_AMEND_COURTROOM, method = RequestMethod.POST,
        params = "btnUpdateConfirm")
    public ModelAndView updateCourtRoom(@Valid final CourtRoomAmendCommand courtRoomAmendCommand,
        final BindingResult result, final ModelAndView model) {
        final String methodName = "updateCourtRoom";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        courtRoomAmendValidator.validate(courtRoomAmendCommand, result);
        if (result.hasErrors()) {
            // Default is to return to the amend courtRoom page to display errors
            model.setViewName(VIEW_NAME_AMEND_COURTROOM);

            model.addObject(COURTSITE_LIST, courtRoomPageStateHolder.getSites());
            model.addObject(COURTROOM_LIST, courtRoomPageStateHolder.getCourtRoomsList());
        } else {
            try {
                LOGGER.debug("{}{} - updating CourtRoom", METHOD, methodName);

                // Get the existing list of court rooms (for the roomNo)
                List<CourtRoomDto> courtRoomDtos =
                    courtRoomService.getCourtRooms(courtRoomAmendCommand.getXhibitCourtSiteId());

                // Update the courtRoom
                courtRoomService.updateCourtRoom(courtRoomAmendCommand, courtRoomDtos);

                // Add successMessage to model for display on page
                model.addObject(SUCCESS_MESSAGE, "Court Room has been updated successfully.");

                // Successfully registered so redirect to the courtRoom page which needs a new
                // model
                LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
                return viewCourtRoom(model, true);
            } catch (final DataAccessException | XpdmException ex) {
                model.addObject(COURTSITE_LIST, courtRoomPageStateHolder.getSites());
                model.addObject(COURTROOM_LIST, courtRoomPageStateHolder.getCourtRoomsList());
                // Log the error
                LOGGER.error("{}{} Unable to update CourtRoom ", METHOD, methodName, ex);
                // Reject
                result.reject("courtRoomErrors", "Unable to update CourtRoom: " + ex.getMessage());
            }
        }
        model.addObject(COMMAND, courtRoomAmendCommand);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return model;
    }
}
