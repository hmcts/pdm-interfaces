package uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.UrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class XhbDispMgrUrlRepository extends AbstractRepository<XhbDispMgrUrlDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrUrlRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    private XhbDispMgrLocalProxyRepository xhbDispMgrLocalProxyRepository;

    public XhbDispMgrUrlRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrUrlDao> getDaoClass() {
        return XhbDispMgrUrlDao.class;
    }

    /**
     * findDaoByUrlId.
     * 
     * @param id Integer
     * @return XhbDispMgrUrlDao
     */
    private XhbDispMgrUrlDao findDaoByUrlId(final Integer id) {
        final String methodName = "findDaoByUrlId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_URL.findByUrlId");
        query.setParameter("urlId", id);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrUrlDao) query.getResultList().get(0);
    }


    // Wrapper Method to Return a IUrl from findDaoByUrlId
    public IUrlModel findByUrlId(final Integer id) {
        final String methodName = "findByUrlId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        XhbDispMgrUrlDao dao = findDaoByUrlId(id);
        if (dao != null) {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return convertDaoToUrlBasicValue(dao);
        } else {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return null;
        }
    }

    /**
     * findDaoByCourtSiteId.
     * 
     * @param courtSiteId Integer
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<XhbDispMgrUrlDao> findDaoByCourtSiteId(final Integer courtSiteId) {
        final String methodName = "findDaoByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_URL.findByCourtSiteId");
        query.setParameter("courtSiteId", courtSiteId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    public List<IUrlModel> findByCourtSiteId(final Integer id) {
        final String methodName = "findByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        List<XhbDispMgrUrlDao> daos = findDaoByCourtSiteId(id);
        List<IUrlModel> results = new ArrayList<>();
        for (XhbDispMgrUrlDao dao : daos) {
            results.add(convertDaoToUrlBasicValue(dao));
        }

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return results;
    }

    // Converter Method to take in a Dao and convert to IUrl
    private IUrlModel convertDaoToUrlBasicValue(XhbDispMgrUrlDao dao) {
        final String methodName = "convertDaoToUrlBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        IUrlModel url = getUrlFromDao(dao);
        IXhibitCourtSite xhibitCourtSite;
        ICourtSite courtSite = null;

        if (dao.getXhbCourtSiteDao() != null) {
            XhbCourtSiteDao xhbCourtSiteDao = dao.getXhbCourtSiteDao();
            xhibitCourtSite = XhbCourtSiteRepository.getXhibitCourtSiteFromDao(xhbCourtSiteDao);

            // Setting the disp_mgr_court_site and Local Proxy
            if (xhbCourtSiteDao.getXhbDispMgrCourtSiteDao() != null) {
                Set<XhbDispMgrCourtSiteDao> xhbDispMgrCourtSiteDaos =
                    xhbCourtSiteDao.getXhbDispMgrCourtSiteDao();
                for (XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao : xhbDispMgrCourtSiteDaos) {
                    courtSite =
                        XhbDispMgrCourtSiteRepository.getCourtSiteFromDao(xhbDispMgrCourtSiteDao);
                    // Setting Local Proxy Separately through a findByCourtSite query
                    XhbDispMgrLocalProxyDao xhbDispMgrLocalProxyDao =
                        getXhbDispMgrLocalProxyRepository()
                            .findByCourtSiteId(xhbDispMgrCourtSiteDao.getId());
                    if (xhbDispMgrLocalProxyDao != null) {
                        ILocalProxy localProxy = XhbDispMgrLocalProxyRepository
                            .getLocalProxyFromDao(xhbDispMgrLocalProxyDao);
                        localProxy.setCourtSite(courtSite);
                        courtSite.setLocalProxy(localProxy);
                    }
                }
                xhibitCourtSite.setCourtSite(courtSite);
            }

            url.setXhibitCourtSite(xhibitCourtSite);
        }

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return url;
    }

    public static IUrlModel getUrlFromDao(XhbDispMgrUrlDao xhbDispMgrUrlDao) {
        final String methodName = "convertDaoToUrlBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        IUrlModel url = new UrlModel();
        url.setId(xhbDispMgrUrlDao.getId().longValue());
        url.setUrl(xhbDispMgrUrlDao.getUrl());
        url.setDescription(xhbDispMgrUrlDao.getDescription());
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return url;
    }

    private XhbDispMgrLocalProxyRepository getXhbDispMgrLocalProxyRepository() {
        final String methodName = "getXhbDispMgrLocalProxyRepository";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        if (xhbDispMgrLocalProxyRepository == null) {
            xhbDispMgrLocalProxyRepository = new XhbDispMgrLocalProxyRepository(getEntityManager());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhbDispMgrLocalProxyRepository;
    }

}
