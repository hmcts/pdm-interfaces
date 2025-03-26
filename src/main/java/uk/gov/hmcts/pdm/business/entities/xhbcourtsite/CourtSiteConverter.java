package uk.gov.hmcts.pdm.business.entities.xhbcourtsite;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsitewelsh.XhbCourtSiteWelshRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSiteWelsh;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;



public abstract class CourtSiteConverter extends XhbDispMgrCourtSiteProcessor {

    private XhbDispMgrLocalProxyRepository xhbDispMgrLocalProxyRepository;

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String ENDS = " - ends";
    private static final String STARTS = " - starts";

    private static final Logger LOG = LoggerFactory.getLogger(CourtSiteConverter.class);


    public CourtSiteConverter(EntityManager em) {
        super(em);
    }


    // Converter Method to take in a Dao and convert to IXhibitCourtSite
    public IXhibitCourtSite convertDaoToXhibitCourtSiteBasicValue(XhbCourtSiteDao dao) {
        final String methodName = "convertDaoToXhibitCourtSiteBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Set<IUrlModel> urls = new HashSet<>();
        
        // Make sure we have the latest version
        getEntityManager().refresh(dao);
        
        IXhibitCourtSite xhibitCourtSite = getXhibitCourtSiteFromDao(dao);

        convertXhbCourtSiteWelsh(dao, xhibitCourtSite);
        convertXhbDispMgrUrls(dao, urls);
        xhibitCourtSite.setUrls(urls);
        convertXhbDispMgrCourtSite(dao, xhibitCourtSite);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhibitCourtSite;
    }


    // Converter Method to take in a Dao and convert to ICourtSite
    public ICourtSite convertDaoToCourtSiteBasicValue(XhbCourtSiteDao dao) {
        final String methodName = "convertDaoToCourtSiteBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        ICourtSite courtSite = null;
        
        // Make sure we have the latest version
        getEntityManager().refresh(dao);

        IXhibitCourtSite xhibitCourtSite = getXhibitCourtSiteFromDao(dao);

        if (dao.getXhbCourtSiteWelshDao() != null) {
            IXhibitCourtSiteWelsh xhibitCourtSiteWelsh =
                XhbCourtSiteWelshRepository.getXhibitCourtSiteWelshFromDao(dao);
            xhibitCourtSite.setXhibitCourtSiteWelsh(xhibitCourtSiteWelsh);
        }

        Set<XhbDispMgrCourtSiteDao> courtSiteDaos = dao.getXhbDispMgrCourtSiteDao();

        for (XhbDispMgrCourtSiteDao courtSiteDao : courtSiteDaos) {
            courtSite = XhbDispMgrCourtSiteRepository.getCourtSiteFromDao(courtSiteDao);
            processSchedule(courtSite, courtSiteDao);
            processLocalProxy(courtSite, courtSiteDao);
            processCdus(courtSite, courtSiteDao, methodName);
        }

        if (courtSite != null) {
            courtSite.setXhibitCourtSite(xhibitCourtSite);
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return courtSite;
    }

    @Override
    protected XhbDispMgrLocalProxyRepository getXhbDispMgrLocalProxyRepository() {
        final String methodName = "getXhbDispMgrLocalProxyRepository";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        if (xhbDispMgrLocalProxyRepository == null) {
            xhbDispMgrLocalProxyRepository = new XhbDispMgrLocalProxyRepository(getEntityManager());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhbDispMgrLocalProxyRepository;
    }

    // Update method to take in ICourtSite convert to Dao and call update
    public void updateDaoFromBasicValue(ICourtSite courtSite) {
        final String methodName = "updateDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbCourtSiteDao dao =
            findDaoByCourtSiteId(courtSite.getXhibitCourtSite().getId().intValue());

        if (dao != null) {
            dao.setCourtSiteName(courtSite.getXhibitCourtSite().getCourtSiteName());
            dao.setDisplayName(courtSite.getXhibitCourtSite().getDisplayName());
            dao.setShortName(courtSite.getXhibitCourtSite().getShortName());

            for (XhbDispMgrCourtSiteDao dispMgrCourtSite : dao.getXhbDispMgrCourtSiteDao()) {
                dispMgrCourtSite.setRagStatus(courtSite.getRagStatus());
                dispMgrCourtSite.setRagStatusDate(courtSite.getRagStatusDate());
                dispMgrCourtSite.setPageUrl(courtSite.getPageUrl());
                dispMgrCourtSite.setTitle(courtSite.getTitle());
                dispMgrCourtSite.setNotification(courtSite.getNotification());

                dispMgrCourtSite.setScheduleId(courtSite.getSchedule().getId().intValue());
            }
            update(dao);
            clearEntityManager();
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * findDaoByCourtSiteId.
     * 
     * @param id Integer
     * @return XhbCourtSiteDao
     */
    private XhbCourtSiteDao findDaoByCourtSiteId(final Integer id) {
        final String methodName = "findDaoByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query = getEntityManager().createNamedQuery("XHB_COURT_SITE.findByCourtSiteId");
        query.setParameter("courtSiteId", id);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbCourtSiteDao) query.getResultList().get(0);
    }

    // Wrapper Method to Return an ICourtSite from findDaoByCourtSiteId
    public ICourtSite findByCourtSiteId(final Integer id) {
        final String methodName = "findByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbCourtSiteDao dao = findDaoByCourtSiteId(id);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return convertDaoToCourtSiteBasicValue(dao);
    }

    private void convertXhbCourtSiteWelsh(XhbCourtSiteDao dao, IXhibitCourtSite xhibitCourtSite) {
        if (dao.getXhbCourtSiteWelshDao() != null) {
            IXhibitCourtSiteWelsh xhibitCourtSiteWelsh =
                XhbCourtSiteWelshRepository.getXhibitCourtSiteWelshFromDao(dao);
            xhibitCourtSite.setXhibitCourtSiteWelsh(xhibitCourtSiteWelsh);
        }
    }

    /**
     * saveDao.
     *
     * @param dao XhbCourtSiteDao
     */
    public void saveDao(XhbCourtSiteDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        super.save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * updateDao.
     *
     * @param dao XhbCourtSiteDao
     * @return Optional XhbCourtSiteDao
     */
    public Optional<XhbCourtSiteDao> updateDao(XhbCourtSiteDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbCourtSiteDao> result = super.update(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }
}
