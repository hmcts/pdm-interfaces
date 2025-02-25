package uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DynamicDropdownList;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DynamicDropdownOption;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtRoomService;

import java.util.List;

public class CourtRoomPageStateSetter {

    /** The Constant LOGGER. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CourtRoomPageStateSetter.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    /**
     * Our CourtPageStateHolder.
     */
    @Autowired
    protected CourtRoomPageStateHolder courtRoomPageStateHolder;

    /**
     * Our courtRoomService class.
     */
    @Autowired
    protected ICourtRoomService courtRoomService;

    /**
     * Sets the view page state selection lists.
     */
    protected void setViewPageStateSelectionLists() {
        final String methodName = "setViewPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court list
        courtRoomPageStateHolder.setCourts(courtRoomService.getCourts());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the amend page state selection lists.
     */
    protected void setAmendPageStateSelectionLists(Integer courtId) {
        final String methodName = "setAmendPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court list
        courtRoomPageStateHolder.setCourts(courtRoomService.getCourts());
        // Set the court site list
        courtRoomPageStateHolder.setSites(courtRoomService.getCourtSites(courtId));

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the selected court in page state holder.
     *
     * @param courtId the court id
     * @return the court dto
     */
    protected CourtDto populateSelectedCourtInPageStateHolder(final Integer courtId) {
        final String methodName = "populateSelectedCourtInPageStateHolder ";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        CourtDto selectedCourt = null;
        List<CourtDto> courts = courtRoomPageStateHolder.getCourts();
        for (CourtDto court : courts) {
            if (court.getId().equals(courtId)) {
                selectedCourt = court;
                break;
            }
        }
        courtRoomPageStateHolder.setCourt(selectedCourt);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return selectedCourt;
    }
    
    protected DynamicDropdownList createDynamicDropdownList() {
        return new DynamicDropdownList();
    }

    protected DynamicDropdownOption createDynamicDropdownOption(Integer value, String text) {
        return new DynamicDropdownOption(value, text);
    }
}
