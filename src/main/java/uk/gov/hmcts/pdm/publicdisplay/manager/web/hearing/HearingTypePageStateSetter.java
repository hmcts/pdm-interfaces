package uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IHearingTypeService;

import java.util.List;

public class HearingTypePageStateSetter {

    /** The Constant LOGGER. */
    protected static final Logger LOGGER =
        LoggerFactory.getLogger(HearingTypePageStateSetter.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    /**
     * Our HearingPageStateHolder.
     */
    @Autowired
    protected HearingTypePageStateHolder hearingTypePageStateHolder;

    /**
     * Our HearingTypeService class.
     */
    @Autowired
    protected IHearingTypeService hearingTypeService;

    /**
     * Sets the page state selection lists.
     */
    protected void setPageStateSelectionLists() {
        final String methodName = "setPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        hearingTypePageStateHolder.setSites(hearingTypeService.getCourtSites());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the amend page state selection lists.
     */
    protected void setAmendPageStateSelectionLists(Long xhibitCourtSiteId) {
        final String methodName = "setAmendPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        hearingTypePageStateHolder.setSites(hearingTypeService.getCourtSites());
        // Set the hearing type list
        hearingTypePageStateHolder
            .setHearingTypes(hearingTypeService.getHearingTypes(xhibitCourtSiteId));

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
        List<XhibitCourtSiteDto> courtSites = hearingTypePageStateHolder.getSites();
        for (XhibitCourtSiteDto courtSite : courtSites) {
            if (courtSite.getId().equals(xhibitCourtSiteId)) {
                LOGGER.debug("Found Selected CourtSite {}", courtSite.getCourtSiteName());
                selectedCourtSite = courtSite;
                break;
            } else {
                LOGGER.debug("Ingored CourtSite {}", courtSite.getCourtSiteName());
            }
        }
        hearingTypePageStateHolder.setCourtSite(selectedCourtSite);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return selectedCourtSite;
    }

    protected List<HearingTypeDto> getHearingTypes() {
        if (hearingTypePageStateHolder.getHearingTypes().isEmpty()) {
            LOGGER.debug("No HearingTypes populated.");
            Long xhibitCourtSiteId =
                hearingTypePageStateHolder.getHearingSearchCommand().getXhibitCourtSiteId();
            LOGGER.debug("Populating HearingTypes");
            hearingTypePageStateHolder
                .setHearingTypes(hearingTypeService.getHearingTypes(xhibitCourtSiteId));
        }
        return hearingTypePageStateHolder.getHearingTypes();
    }
}
