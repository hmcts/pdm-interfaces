package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.List;

public abstract class AbstractCourtValidator extends CourtSiteValidator implements Validator {

    /** The display page state holder. */
    @Autowired
    private CourtPageStateHolder courtPageStateHolder;

    /**
     * Gets the display page state holder.
     *
     * @return the display page state holder
     */
    protected CourtPageStateHolder getCourtPageStateHolder() {
        return courtPageStateHolder;
    }

    /**
     * Xhibit court site id in the valid list of sites.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @return true, if successful
     */
    protected boolean isCourtSiteValid(final Long xhibitCourtSiteId) {
        final XhibitCourtSiteDto selectedCourtSite =
            getCourtSiteFromSearchResults(xhibitCourtSiteId);
        return selectedCourtSite != null;
    }

    /**
     * Checks if is registered court site selected.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @return true, if is registered court site selected
     */
    protected boolean isRegisteredCourtSiteSelected(final Long xhibitCourtSiteId) {
        final XhibitCourtSiteDto selectedCourtSite =
            getCourtSiteFromSearchResults(xhibitCourtSiteId);
        return isRegisteredCourtSite(selectedCourtSite);
    }

    /**
     * Gets the court site from search results.
     *
     * @param xhibitCourtSiteId the xhibit court site id
     * @return the court site from search results
     */
    private XhibitCourtSiteDto getCourtSiteFromSearchResults(final Long xhibitCourtSiteId) {
        XhibitCourtSiteDto selectedCourtSite = null;
        final List<XhibitCourtSiteDto> courtSiteList = courtPageStateHolder.getSites();
        if (courtSiteList != null) {
            for (XhibitCourtSiteDto courtSite : courtSiteList) {
                if (courtSite.getId().equals(xhibitCourtSiteId)) {
                    selectedCourtSite = courtSite;
                    break;
                }
            }
        }
        return selectedCourtSite;
    }

}
