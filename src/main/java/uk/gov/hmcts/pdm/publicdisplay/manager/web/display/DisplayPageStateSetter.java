package uk.gov.hmcts.pdm.publicdisplay.manager.web.display;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IDisplayService;

import java.util.List;

public class DisplayPageStateSetter {

    /** The Constant LOGGER. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DisplayPageStateSetter.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    /**
     * Our DisplayPageStateHolder.
     */
    @Autowired
    protected DisplayPageStateHolder displayPageStateHolder;

    /**
     * Our displayService class.
     */
    @Autowired
    protected IDisplayService displayService;

    /**
     * Sets the view page state selection lists.
     */
    protected void setViewPageStateSelectionLists() {
        final String methodName = "setViewPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        displayPageStateHolder.setSites(displayService.getCourtSites());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the amend page state selection lists.
     */
    protected void setAmendPageStateSelectionLists(Long xhibitCourtSiteId, Integer courtId) {
        final String methodName = "setAmendPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        displayPageStateHolder.setSites(displayService.getCourtSites());
        // Set the display types
        displayPageStateHolder.setDisplayTypes(displayService.getDisplayTypes());
        // Set the rotation sets
        displayPageStateHolder.setRotationSets(displayService.getRotationSets(courtId));

        // Set the displays list (no detail objects required)
        displayPageStateHolder
            .setDisplays(displayService.getDisplays(xhibitCourtSiteId, null,
                displayPageStateHolder.getSites(), null));

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the delete page state selection lists.
     */
    protected void setDeletePageStateSelectionLists(Long xhibitCourtSiteId, Integer courtId) {
        final String methodName = "setDeletePageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        displayPageStateHolder.setSites(displayService.getCourtSites());
        // Set the display types
        displayPageStateHolder.setDisplayTypes(displayService.getDisplayTypes());
        // Set the rotation sets
        displayPageStateHolder.setRotationSets(displayService.getRotationSets(courtId));

        // Set the displays list (populate the detail objects for display)
        displayPageStateHolder.setDisplays(
            displayService.getDisplays(xhibitCourtSiteId, displayPageStateHolder.getDisplayTypes(),
                displayPageStateHolder.getSites(), displayPageStateHolder.getRotationSets()));


        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the selected court site in page state holder.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @return the court site dto
     */
    protected XhibitCourtSiteDto populateSelectedCourtSiteInPageStateHolder(
        final Long xhibitCourtSiteId) {
        final String methodName = "populateSelectedCourtSiteInPageStateHolder ";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        XhibitCourtSiteDto selectedCourtSite = null;
        List<XhibitCourtSiteDto> courtSites = displayPageStateHolder.getSites();
        for (XhibitCourtSiteDto courtSite : courtSites) {
            if (courtSite.getId().equals(xhibitCourtSiteId)) {
                selectedCourtSite = courtSite;
                break;
            }
        }
        displayPageStateHolder.setCourtSite(selectedCourtSite);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return selectedCourtSite;
    }
}
