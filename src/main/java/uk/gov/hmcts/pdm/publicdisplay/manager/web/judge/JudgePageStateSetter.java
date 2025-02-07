package uk.gov.hmcts.pdm.publicdisplay.manager.web.judge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRefJudgeService;

import java.util.List;

public class JudgePageStateSetter {

    /** The Constant LOGGER. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(JudgePageStateSetter.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    /**
     * Our JudgePageStateHolder.
     */
    @Autowired
    protected JudgePageStateHolder judgePageStateHolder;

    /**
     * Our refJudgeService class.
     */
    @Autowired
    protected IRefJudgeService refJudgeService;

    /**
     * Sets the view page state selection lists.
     */
    protected void setViewPageStateSelectionLists() {
        final String methodName = "setViewPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the court site list
        judgePageStateHolder.setSites(refJudgeService.getCourtSites());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Sets the amend page state selection lists.
     */
    protected void setAmendPageStateSelectionLists(Long xhibitCourtSiteId) {
        final String methodName = "setAmendPageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the judges list
        judgePageStateHolder.setJudges(refJudgeService.getJudges(xhibitCourtSiteId));
        // Set the judge types list
        judgePageStateHolder.setJudgeTypes(refJudgeService.getJudgeTypes(xhibitCourtSiteId));

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }
    
    /**
     * Sets the delete page state selection lists.
     */
    protected void setDeletePageStateSelectionLists(Long xhibitCourtSiteId) {
        final String methodName = "setDeletePageStateSelectionLists";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Set the judges list
        judgePageStateHolder.setJudges(refJudgeService.getJudges(xhibitCourtSiteId));
        // Set the judge types list
        judgePageStateHolder.setJudgeTypes(refJudgeService.getJudgeTypes(xhibitCourtSiteId));

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
        
        if (judgePageStateHolder == null) {
            LOGGER.info("2. JudgePageStateHolder is null");
        } else {
            LOGGER.info("2. JudgePageStateHolder is not null");
        }
        
        XhibitCourtSiteDto selectedCourtSite = null;
        List<XhibitCourtSiteDto> courtSites = judgePageStateHolder.getSites();
        LOGGER.info(THREE_PARAMS, "JudgePageStateHolder returned: ", courtSites.size(), " courtSites.");
        
        for (XhibitCourtSiteDto courtSite : courtSites) {
            if (courtSite.getId().equals(xhibitCourtSiteId)) {
                selectedCourtSite = courtSite;
                break;
            }
        }
        
        judgePageStateHolder.setCourtSite(selectedCourtSite);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return selectedCourtSite;
    }
}
