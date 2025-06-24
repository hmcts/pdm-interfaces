package uk.gov.hmcts.pdm.business.entities.xhbcourtsite;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;

import java.util.ArrayList;
import java.util.List;


public class XhbCourtSiteRepository extends CourtSiteConverter {

    private static final Logger LOG = LoggerFactory.getLogger(XhbCourtSiteRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";
    private static final String UNCHECKED = "unchecked";

    public XhbCourtSiteRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbCourtSiteDao> getDaoClass() {
        return XhbCourtSiteDao.class;
    }

    /**
     * findByCourtId.
     *
     * @param courtId Integer
     * @return XhbCourtSiteDao
     */
    @SuppressWarnings(UNCHECKED)
    public List<XhbCourtSiteDao> findByCourtId(final Integer courtId) {
        final String methodName = "findByCourtId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_COURT_SITE.findByCourtId");
        query.setParameter("courtId", courtId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
    
    /**
     * findDaoCourtSiteByXhibitCourtSiteId.
     * 
     * @param xhibitCourtSiteId Integer
     * @return XhbCourtSiteDao
     */
    private XhbCourtSiteDao findDaoCourtSiteByXhibitCourtSiteId(final Integer xhibitCourtSiteId) {
        final String methodName = "findDaoCourtSiteByXhibitCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query =
            getEntityManager().createNamedQuery("XHB_COURT_SITE.findCourtSiteByXhibitCourtSiteId");
        query.setParameter("xhibitCourtSiteId", xhibitCourtSiteId);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbCourtSiteDao) query.getResultList().get(0);
    }

    // Wrapper Method to Return an ICourtSite from findDaoCourtSiteByXhibitCourtSiteId
    public ICourtSite findCourtSiteByXhibitCourtSiteId(final Integer xhibitCourtSiteId) {
        final String methodName = "findCourtSiteByXhibitCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbCourtSiteDao dao = findDaoCourtSiteByXhibitCourtSiteId(xhibitCourtSiteId);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return convertDaoToCourtSiteBasicValue(dao);
    }

    /**
     * findDaoCourtSitesWithLocalProxy.
     * 
     * @return List
     */
    @SuppressWarnings(UNCHECKED)
    private List<XhbCourtSiteDao> findDaoCourtSitesWithLocalProxy() {
        final String methodName = "findDaoCourtSitesWithLocalProxy";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query =
            getEntityManager().createNamedQuery("XHB_COURT_SITE.findCourtSitesWithLocalProxy");
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    // Wrapper Method to Return a List<IXhibitCourtSite> from findDaoCourtSitesWithLocalProxy
    public List<IXhibitCourtSite> findCourtSitesWithLocalProxy() {
        final String methodName = "findCourtSitesWithLocalProxy";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        List<XhbCourtSiteDao> daos = findDaoCourtSitesWithLocalProxy();
        List<IXhibitCourtSite> results = new ArrayList<>();
        for (XhbCourtSiteDao dao : daos) {
            results.add(convertDaoToXhibitCourtSiteBasicValue(dao));
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return results;
    }

    /**
     * findDaoCourtSitesWithoutLocalProxy.
     * 
     * @return List
     */
    @SuppressWarnings(UNCHECKED)
    private List<XhbCourtSiteDao> findDaoCourtSitesWithoutLocalProxy() {
        final String methodName = "findDaoCourtSitesWithoutLocalProxy";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query =
            getEntityManager().createNamedQuery("XHB_COURT_SITE.findCourtSitesWithoutLocalProxy");
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    // Wrapper Method to Return a List<IXhibitCourtSite> from findDaoCourtSitesWithoutLocalProxy
    public List<IXhibitCourtSite> findCourtSitesWithoutLocalProxy() {
        final String methodName = "findCourtSitesWithoutLocalProxy";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        List<XhbCourtSiteDao> daos = findDaoCourtSitesWithoutLocalProxy();
        List<IXhibitCourtSite> results = new ArrayList<>();
        for (XhbCourtSiteDao dao : daos) {
            results.add(convertDaoToXhibitCourtSiteBasicValue(dao));
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return results;
    }

    /**
     * findDaoXhibitCourtSitesOrderedByRagStatus.
     * 
     * @return List
     */
    @SuppressWarnings(UNCHECKED)
    private List<XhbCourtSiteDao> findDaoXhibitCourtSitesOrderedByRagStatus() {
        final String methodName = "findDaoXhibitCourtSitesOrderedByRagStatus";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query = getEntityManager()
            .createNamedQuery("XHB_COURT_SITE.findXhibitCourtSitesOrderedByRagStatus");
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    // Wrapper Method to Return a List<IXhibitCourtSite> from
    // findDaoXhibitCourtSitesOrderedByRagStatus
    public List<IXhibitCourtSite> findXhibitCourtSitesOrderedByRagStatus() {
        final String methodName = "findXhibitCourtSitesOrderedByRagStatus";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        List<XhbCourtSiteDao> daos = findDaoXhibitCourtSitesOrderedByRagStatus();
        List<IXhibitCourtSite> results = new ArrayList<>();
        for (XhbCourtSiteDao dao : daos) {
            results.add(convertDaoToXhibitCourtSiteBasicValue(dao));
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return results;
    }

    /**
     * Overridden findAll method to return all XhbCourtSiteDao objects that are not marked as
     * obsolete.
     * 
     * @return List of XhbCourtSiteDao objects.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<XhbCourtSiteDao> findAll() {

        final String methodName = "findAll";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        String hql =
            "FROM " + getDaoClass().getName() + " e WHERE e.obsInd IS NULL OR e.obsInd <> 'Y'";
        Query query = getEntityManager().createQuery(hql);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
}
