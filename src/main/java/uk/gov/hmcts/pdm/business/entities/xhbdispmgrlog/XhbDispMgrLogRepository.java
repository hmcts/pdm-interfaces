package uk.gov.hmcts.pdm.business.entities.xhbdispmgrlog;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.List;

public class XhbDispMgrLogRepository extends AbstractRepository<XhbDispMgrLogDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrLogRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    public XhbDispMgrLogRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrLogDao> getDaoClass() {
        return XhbDispMgrLogDao.class;
    }

    /**
     * findByLogId.
     * @param id Integer
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<XhbDispMgrLogDao> findByLogId(final Integer id) {
        final String methodName = "findByLogId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_LOG.findByLogId");
        query.setParameter("logId", id);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    /**
     * findByCourtSiteId. 
     * @param courtSiteId Integer
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<XhbDispMgrLogDao> findByCourtSiteId(final Integer courtSiteId) {
        final String methodName = "findByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_LOG.findByCourtSiteId");
        query.setParameter("courtSiteId", courtSiteId);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    /**
     * findByCduId.
     * @param cduId Integer
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<XhbDispMgrLogDao> findByCduId(final Integer cduId) {
        final String methodName = "findByCduId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_LOG.findByCduId");
        query.setParameter("cduId", cduId);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
}
