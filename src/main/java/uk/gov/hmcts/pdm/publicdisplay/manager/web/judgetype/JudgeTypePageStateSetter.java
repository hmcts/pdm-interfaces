package uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRefJudgeTypeService;

import java.util.List;

public class JudgeTypePageStateSetter {

    /** The Constant LOGGER. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(JudgeTypePageStateSetter.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    /**
     * Our JudgeTypePageStateHolder.
     */
    @Autowired
    protected JudgeTypePageStateHolder judgeTypePageStateHolder;

    /**
     * Our refJudgeService class.
     */
    @Autowired
    protected IRefJudgeTypeService refJudgeTypeService;

    /**
     * Sets the view page state selection lists.
     */
    protected void setViewPageStateSelectionLists() {
        final String methodName = "setViewPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        judgeTypePageStateHolder.setSites(refJudgeTypeService.getCourtSites());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }
    
    /**
     * Sets the amend page state selection lists.
     */
    protected void setAmendPageStateSelectionLists(Long xhibitCourtSiteId) {
        final String methodName = "setAmendPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        judgeTypePageStateHolder.setSites(refJudgeTypeService.getCourtSites());
        // Set the judge types list
        judgeTypePageStateHolder.setJudgeTypes(refJudgeTypeService.getJudgeTypes(xhibitCourtSiteId));

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
        List<XhibitCourtSiteDto> courtSites = judgeTypePageStateHolder.getSites();
        for (XhibitCourtSiteDto courtSite : courtSites) {
            if (courtSite.getId().equals(xhibitCourtSiteId)) {
                selectedCourtSite = courtSite;
                break;
            }
        }
        judgeTypePageStateHolder.setCourtSite(selectedCourtSite);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return selectedCourtSite;
    }
}
