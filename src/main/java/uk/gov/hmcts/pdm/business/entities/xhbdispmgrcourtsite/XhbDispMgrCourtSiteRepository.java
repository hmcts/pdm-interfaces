package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite;


import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.CourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class XhbDispMgrCourtSiteRepository extends DispMgrCourtSiteConverter {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrCourtSiteRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    private XhbCourtSiteRepository xhbCourtSiteRepository;

    private XhbDispMgrLocalProxyRepository xhbDispMgrLocalProxyRepository;

    public XhbDispMgrCourtSiteRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrCourtSiteDao> getDaoClass() {
        return XhbDispMgrCourtSiteDao.class;
    }

    // Wrapper Method to Return an ICourtSite from findById
    public ICourtSite findByCourtSiteId(final Integer courtSiteId) {
        final String methodName = "findByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Optional<XhbDispMgrCourtSiteDao> dao = findById(courtSiteId);
        ICourtSite courtSite = new CourtSite();
        if (!dao.isEmpty()) {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            courtSite = convertDaoToCourtSiteBasicValue(dao.get());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return courtSite;
    }

    // Wrapper Method to Return an IXhibitCourtSite from findByXhibitCourtSiteId
    public ICourtSite findByXhibitCourtSiteId(final Integer xhibitCourtSiteId) {
        final String methodName = "findByXhibitCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbDispMgrCourtSiteDao dao = findDaoByXhibitCourtSiteId(xhibitCourtSiteId);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        if (dao != null) {
            return getXhbCourtSiteRepository()
                .convertDaoToCourtSiteBasicValue(dao.getXhbCourtSiteDao());
        } else {
            return null;
        }
    }

    // Converter Method to take in a Dao and convert to ICourtSite
    private ICourtSite convertDaoToCourtSiteBasicValue(XhbDispMgrCourtSiteDao dao) {
        final String methodName = "convertDaoToCourtSiteBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        ICourtSite courtSite;

        courtSite = getCourtSiteFromDao(dao);

        if (dao.getXhbCourtSiteDao() != null) {
            XhbCourtSiteDao xhbCourtSiteDao = dao.getXhbCourtSiteDao();

            IXhibitCourtSite xhibitCourtSite =
                XhbCourtSiteRepository.getXhibitCourtSiteFromDao(xhbCourtSiteDao);
            courtSite.setXhibitCourtSite(xhibitCourtSite);
        }

        if (dao.getXhbDispMgrCduDao() != null) {
            Set<ICduModel> cdus = new HashSet<>();
            Set<XhbDispMgrCduDao> daoCdus = dao.getXhbDispMgrCduDao();

            for (XhbDispMgrCduDao daoCdu : daoCdus) {
                ICduModel cdu = XhbDispMgrCduRepository.getCduFromDao(daoCdu);
                cdus.add(cdu);
            }
            courtSite.setCdus(cdus);
        }

        // Setting Local Proxy Separately through a findByCourtSite query
        XhbDispMgrLocalProxyDao xhbDispMgrLocalProxyDao =
            getXhbDispMgrLocalProxyRepository().findByCourtSiteId(dao.getId());
        if (xhbDispMgrLocalProxyDao != null) {
            ILocalProxy localProxy =
                XhbDispMgrLocalProxyRepository.getLocalProxyFromDao(xhbDispMgrLocalProxyDao);
            localProxy.setCourtSite(courtSite);
            courtSite.setLocalProxy(localProxy);
        }

        if (dao.getXhbDispMgrScheduleDao() != null) {
            XhbDispMgrScheduleDao xhbDispMgrScheduleDao = dao.getXhbDispMgrScheduleDao();
            ISchedule schedule =
                XhbDispMgrScheduleRepository.getScheduleFromDao(xhbDispMgrScheduleDao);
            courtSite.setSchedule(schedule);
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return courtSite;
    }

    public static ICourtSite getCourtSiteFromDao(XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao) {
        final String methodName = "getCourtSiteFromDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        ICourtSite courtSite = new CourtSite();
        courtSite.setId(xhbDispMgrCourtSiteDao.getId().longValue());
        courtSite.setTitle(xhbDispMgrCourtSiteDao.getTitle());
        courtSite.setNotification(xhbDispMgrCourtSiteDao.getNotification());
        courtSite.setPageUrl(xhbDispMgrCourtSiteDao.getPageUrl());
        courtSite.setRagStatus(xhbDispMgrCourtSiteDao.getRagStatus());
        courtSite.setRagStatusDate(xhbDispMgrCourtSiteDao.getRagStatusDate());
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return courtSite;
    }

    private XhbCourtSiteRepository getXhbCourtSiteRepository() {
        final String methodName = "getXhbCourtSiteRepository";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        if (xhbCourtSiteRepository == null) {
            xhbCourtSiteRepository = new XhbCourtSiteRepository(getEntityManager());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhbCourtSiteRepository;
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
