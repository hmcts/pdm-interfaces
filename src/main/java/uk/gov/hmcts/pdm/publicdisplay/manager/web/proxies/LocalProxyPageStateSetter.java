package uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.ScheduleDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService;

import java.util.List;

public class LocalProxyPageStateSetter {

    /** The Constant LOGGER. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(LocalProxyPageStateSetter.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    /**
     * Our LocalProxyPageStateHolder.
     */
    @Autowired
    protected LocalProxyPageStateHolder localProxyPageStateHolder;

    /**
     * Our localProxyService class.
     */
    @Autowired
    protected ILocalProxyService localProxyService;
    
    /**
     * Sets the view page state selection lists.
     */
    protected void setViewPageStateSelectionLists() {
        final String methodName = "setViewPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        localProxyPageStateHolder.setSites(localProxyService.getCourtSites());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }
    
    /**
     * Sets the amend page state selection lists.
     */
    protected void setAmendPageStateSelectionLists(Integer courtId) {
        final String methodName = "setAmendPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court sites by courtId
        localProxyPageStateHolder.setSites(localProxyService.getCourtSites(courtId));

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the page state selection lists.
     *
     * @param isWithLocalProxy the new page state xhibit court sites
     */
    protected void setPageStateSelectionLists(final boolean isWithLocalProxy) {
        final String methodName = "setPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        setPageStateXhibitCourtSites(isWithLocalProxy);

        // Set the schedule list
        setPageStateSchedules();

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the page state xhibit court sites.
     *
     * @param isWithLocalProxy the new page state xhibit court sites
     */
    protected void setPageStateXhibitCourtSites(final boolean isWithLocalProxy) {
        final String methodName = "setPageStateXhibitCourtSites";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // retrieve the court sites
        final List<XhibitCourtSiteDto> courtSiteList =
            isWithLocalProxy ? localProxyService.getXhibitCourtSitesWithLocalProxy()
                : localProxyService.getXhibitCourtSitesWithoutLocalProxy();

        // Add the court site data to pageStateHolder
        localProxyPageStateHolder.setSites(courtSiteList);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the page state schedules.
     */
    protected void setPageStateSchedules() {
        final String methodName = "setPageStateSchedules";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // retrieve the schedules
        final List<ScheduleDto> scheduleList = localProxyService.getPowerSaveSchedules();

        // Add the schedule data to pageStateHolder
        localProxyPageStateHolder.setSchedules(scheduleList);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the selected court site in page state holder.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @return the court site dto
     */
    protected CourtSiteDto populateSelectedCourtSiteInPageStateHolder(
        final Long xhibitCourtSiteId) {
        final String methodName = "populateSelectedCourtSiteInPageStateHolder ";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final CourtSiteDto selectedCourtSite =
            localProxyService.getCourtSiteByXhibitCourtSiteId(xhibitCourtSiteId);

        localProxyPageStateHolder.setCourtSite(selectedCourtSite);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return selectedCourtSite;
    }

}
