package uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.LocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;

import java.time.LocalDateTime;

public class XhbDispMgrLocalProxyRepository extends AbstractRepository<XhbDispMgrLocalProxyDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrLocalProxyRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    private XhbDispMgrCourtSiteRepository xhbDispMgrCourtSiteRepository;

    public XhbDispMgrLocalProxyRepository(EntityManager em) {
        super(em);
    }

    // Junit constructor
    public XhbDispMgrLocalProxyRepository(EntityManager em,
        XhbDispMgrCourtSiteRepository xhbDispMgrCourtSiteRepository) {
        this(em);
        this.xhbDispMgrCourtSiteRepository = xhbDispMgrCourtSiteRepository;
    }

    @Override
    public Class<XhbDispMgrLocalProxyDao> getDaoClass() {
        return XhbDispMgrLocalProxyDao.class;
    }

    /**
     * findByLocalProxyId.
     * 
     * @param id Integer
     * @return XhbDispMgrLocalProxyDao
     */
    @SuppressWarnings("unchecked")
    public XhbDispMgrLocalProxyDao findByLocalProxyId(final Integer id) {
        final String methodName = "findByLocalProxyId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query =
            getEntityManager().createNamedQuery("XHB_DISP_MGR_LOCAL_PROXY.findByLocalProxyId");
        query.setParameter("localProxyId", id);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrLocalProxyDao) query.getResultList().get(0);
    }

    /**
     * isLocalProxyWithIpAddress.
     * 
     * @param ipAddress String
     * @return boolean
     */
    public boolean isLocalProxyWithIpAddress(final String ipAddress) {
        final String methodName = "isLocalProxyWithIpAddress";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query =
            getEntityManager().createNamedQuery("XHB_DISP_MGR_LOCAL_PROXY.findByIpAddress");
        query.setParameter("ipAddress", ipAddress);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return !query.getResultList().isEmpty();
    }

    /**
     * findByCourtSiteId.
     * 
     * @param courtSiteId Integer
     * @return XhbDispMgrLocalProxyDao
     */
    public XhbDispMgrLocalProxyDao findByCourtSiteId(final Integer courtSiteId) {
        final String methodName = "findByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query =
            getEntityManager().createNamedQuery("XHB_DISP_MGR_LOCAL_PROXY.findByCourtSiteId");
        query.setParameter("courtSiteId", courtSiteId);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrLocalProxyDao) query.getResultList().get(0);
    }

    // Save method to take in ILocalProxy convert to Dao and call save
    public void saveDaoFromBasicValue(ILocalProxy localProxy) {
        final String methodName = "saveDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbDispMgrLocalProxyDao localProxyDao = new XhbDispMgrLocalProxyDao();
        XhbDispMgrCourtSiteDao dispMgrCourtSite = getDispMgrCourtSiteFromXhibitCourtSiteId(
            localProxy.getCourtSite().getXhibitCourtSite().getId());

        localProxyDao.setIpAddress(localProxy.getIpAddress());
        localProxyDao.setHostName(localProxy.getHostname());
        localProxyDao.setRagStatus(localProxy.getRagStatus());
        localProxyDao.setRagStatusDate(localProxy.getRagStatusDate());
        localProxyDao.setCourtSiteId(dispMgrCourtSite.getId());
        localProxyDao.setLastUpdateDate(LocalDateTime.now());
        localProxyDao.setCreationDate(LocalDateTime.now());
        final Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        final String username = authentication != null ? authentication.getName() : null;
        localProxyDao.setCreatedBy(username);
        localProxyDao.setLastUpdatedBy(username);

        save(localProxyDao);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    // Update method to take in ILocalProxy convert to Dao and call save
    public void updateDaoFromBasicValue(ILocalProxy localProxy) {
        final String methodName = "updateDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbDispMgrLocalProxyDao localProxyDao = findByLocalProxyId(localProxy.getId().intValue());
        XhbDispMgrCourtSiteDao dispMgrCourtSite = getDispMgrCourtSiteFromXhibitCourtSiteId(
            localProxy.getCourtSite().getXhibitCourtSite().getId());

        localProxyDao.setIpAddress(localProxy.getIpAddress());
        localProxyDao.setHostName(localProxy.getHostname());
        localProxyDao.setRagStatus(localProxy.getRagStatus());
        localProxyDao.setRagStatusDate(localProxy.getRagStatusDate());
        localProxyDao.setCourtSiteId(dispMgrCourtSite.getId());
        localProxyDao.setLastUpdateDate(LocalDateTime.now());

        update(localProxyDao);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    public void deleteDaoFromBasicValue(ILocalProxy localProxy) {
        final String methodName = "deleteDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        getEntityManager().getTransaction().begin();
        XhbDispMgrLocalProxyDao dao = findByLocalProxyId(localProxy.getId().intValue());
        getEntityManager().remove(dao);
        getEntityManager().getTransaction().commit();
        clearEntityManager();
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    public static ILocalProxy getLocalProxyFromDao(
        XhbDispMgrLocalProxyDao xhbDispMgrLocalProxyDao) {
        final String methodName = "getLocalProxyFromDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        ILocalProxy localProxy = new LocalProxy();
        localProxy.setId(Long.valueOf(xhbDispMgrLocalProxyDao.getId()));
        localProxy.setIpAddress(xhbDispMgrLocalProxyDao.getIpAddress());
        localProxy.setHostname(xhbDispMgrLocalProxyDao.getHostName());
        localProxy.setRagStatus(xhbDispMgrLocalProxyDao.getRagStatus());
        localProxy.setRagStatusDate(xhbDispMgrLocalProxyDao.getRagStatusDate());
        localProxy.setCreatedBy(xhbDispMgrLocalProxyDao.getCreatedBy());
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return localProxy;
    }

    // This will get the courtSiteId for the dispMgrCourtSite
    private XhbDispMgrCourtSiteDao getDispMgrCourtSiteFromXhibitCourtSiteId(
        Long xhibitCourtSiteId) {
        final String methodName = "getDispMgrCourtSiteFromXhibitCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return getXhbDispMgrCourtSiteRepository()
            .findDaoByXhibitCourtSiteId(xhibitCourtSiteId.intValue());
    }

    private XhbDispMgrCourtSiteRepository getXhbDispMgrCourtSiteRepository() {
        final String methodName = "getXhbDispMgrCourtSiteRepository";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        if (xhbDispMgrCourtSiteRepository == null) {
            xhbDispMgrCourtSiteRepository = new XhbDispMgrCourtSiteRepository(getEntityManager());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhbDispMgrCourtSiteRepository;
    }
}
