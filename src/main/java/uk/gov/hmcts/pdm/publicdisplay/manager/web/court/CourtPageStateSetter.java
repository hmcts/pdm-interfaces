package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtService;

import java.util.List;

public class CourtPageStateSetter {
    /** The Constant LOGGER. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CourtPageStateSetter.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    /**
     * Our CourtPageStateHolder.
     */
    @Autowired
    protected CourtPageStateHolder courtPageStateHolder;

    /**
     * Our courtService class.
     */
    @Autowired
    protected ICourtService courtService;

    /**
     * Sets the view page state selection lists.
     */
    protected void setViewPageStateSelectionLists() {
        final String methodName = "setViewPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court list
        courtPageStateHolder.setCourts(courtService.getCourts());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the amend page state selection lists.
     */
    protected void setAmendPageStateSelectionLists(Integer courtId) {
        final String methodName = "setAmendPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court list
        courtPageStateHolder.setCourts(courtService.getCourts());
        // Set the court sites by courtId
        courtPageStateHolder.setSites(courtService.getCourtSites(courtId));

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }
    
    /**
     * Sets the selected court in page state holder.
     *
     * @param xhibitCourtId the court id
     * @return the court dto
     */
    protected CourtDto populateSelectedCourtInPageStateHolder(
        final Integer xhibitCourtId) {
        final String methodName = "populateSelectedCourtInPageStateHolder ";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        CourtDto selectedCourt = null;
        List<CourtDto> courtCourts = courtPageStateHolder.getCourts();
        for (CourtDto courtCourt : courtCourts) {
            if (courtCourt.getId().equals(xhibitCourtId)) {
                selectedCourt = courtCourt;
                break;
            }
        }
        courtPageStateHolder.setCourt(selectedCourt);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return selectedCourt;
    }
}
