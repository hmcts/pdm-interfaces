package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;


public abstract class DispMgrCourtSiteConverter extends AbstractRepository<XhbDispMgrCourtSiteDao> {

    private static final Logger LOG = LoggerFactory.getLogger(DispMgrCourtSiteConverter.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String ENDS = " - ends";
    private static final String STARTS = " - starts";

    protected DispMgrCourtSiteConverter(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * findDaoByXhibitCourtSiteId.
     * 
     * @param xhibitCourtSiteId Integer
     * @return XhbDispMgrCourtSiteDao
     */
    public XhbDispMgrCourtSiteDao findDaoByXhibitCourtSiteId(final Integer xhibitCourtSiteId) {
        final String methodName = "findDaoByXhibitCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query =
            getEntityManager().createNamedQuery("XHB_DISP_MGR_COURT_SITE.findByXhibitCourtSiteId");
        query.setParameter("xhibitCourtSiteId", xhibitCourtSiteId);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrCourtSiteDao) query.getResultList().get(0);
    }

    // Update method to take in ICourtSite convert to Dao and call update
    public void updateDaoFromBasicValue(ICourtSite courtSite) {
        final String methodName = "updateDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbDispMgrCourtSiteDao dao =
            findDaoByXhibitCourtSiteId(courtSite.getXhibitCourtSite().getId().intValue());

        if (dao != null) {
            dao.setRagStatus(courtSite.getRagStatus());
            dao.setRagStatusDate(courtSite.getRagStatusDate());
            dao.setPageUrl(courtSite.getPageUrl());
            dao.setTitle(courtSite.getTitle());
            dao.setNotification(courtSite.getNotification());
            dao.setScheduleId(courtSite.getSchedule().getId().intValue());
            update(dao);
            clearEntityManager();
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }
}
