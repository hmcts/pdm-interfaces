package uk.gov.hmcts.pdm.business.entities.xhbcourtsite;


import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl.XhbDispMgrUrlDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl.XhbDispMgrUrlRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("PMD.CouplingBetweenObjects")
public abstract class XhbDispMgrConverter extends AbstractRepository<XhbCourtSiteDao> {

    private XhbDispMgrLocalProxyRepository xhbDispMgrLocalProxyRepository;

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrConverter.class);
    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String ENDS = " - ends";
    private static final String STARTS = " - starts";



    protected XhbDispMgrConverter(EntityManager entityManager) {
        super(entityManager);
    }


    protected Set<ICduModel> createICduSet() {
        return new HashSet<>();
    }

    protected List<IUrlModel> createIUrlList() {
        return new ArrayList<>();
    }

    protected void convertXhbDispMgrUrls(XhbCourtSiteDao dao, Set<IUrlModel> urls) {
        if (dao.getXhbDispMgrUrlDao() != null) {
            Set<XhbDispMgrUrlDao> daoUrls = dao.getXhbDispMgrUrlDao();

            for (XhbDispMgrUrlDao daoUrl : daoUrls) {
                IUrlModel url = XhbDispMgrUrlRepository.getUrlFromDao(daoUrl);
                Set<ICduModel> cdus = createICduSet();

                convertXhbDispMgrCdus(dao, cdus);

                url.setCdus(cdus);
                urls.add(url);
            }
        }
    }

    protected void convertXhbDispMgrCourtSite(XhbCourtSiteDao dao,
        IXhibitCourtSite xhibitCourtSite) {
        ICourtSite courtSite = null;

        if (dao.getXhbDispMgrCourtSiteDao() != null) {
            Set<XhbDispMgrCourtSiteDao> courtSiteDaos = dao.getXhbDispMgrCourtSiteDao();

            for (XhbDispMgrCourtSiteDao courtSiteDao : courtSiteDaos) {
                courtSite = XhbDispMgrCourtSiteRepository.getCourtSiteFromDao(courtSiteDao);

                convertXhbDispMgrSchedule(courtSiteDao, courtSite);
                convertXhbDispMgrLocalProxy(courtSiteDao, courtSite);
            }
        }

        xhibitCourtSite.setCourtSite(courtSite);
    }


    private void convertXhbDispMgrCdus(XhbCourtSiteDao dao, Set<ICduModel> cdus) {
        if (dao.getXhbDispMgrCourtSiteDao() != null) {
            Set<XhbDispMgrCourtSiteDao> courtSiteDaos = dao.getXhbDispMgrCourtSiteDao();

            for (XhbDispMgrCourtSiteDao courtSiteDao : courtSiteDaos) {
                if (courtSiteDao.getXhbDispMgrCduDao() != null) {

                    Set<XhbDispMgrCduDao> daoCdus =
                        dao.getXhbDispMgrCourtSiteDao().iterator().next().getXhbDispMgrCduDao();

                    for (XhbDispMgrCduDao daoCdu : daoCdus) {
                        ICduModel cdu = XhbDispMgrCduRepository.getCduFromDao(daoCdu);
                        cdus.add(cdu);
                    }
                }
            }
        }
    }

    private void convertXhbDispMgrSchedule(XhbDispMgrCourtSiteDao courtSiteDao,
        ICourtSite courtSite) {
        if (courtSiteDao.getXhbDispMgrScheduleDao() != null) {
            XhbDispMgrScheduleDao scheduleDao = courtSiteDao.getXhbDispMgrScheduleDao();
            ISchedule schedule = XhbDispMgrScheduleRepository.getScheduleFromDao(scheduleDao);
            courtSite.setSchedule(schedule);
        }
    }

    private void convertXhbDispMgrLocalProxy(XhbDispMgrCourtSiteDao courtSiteDao,
        ICourtSite courtSite) {
        final String methodName = "convertXhbDispMgrLocalProxy";
        LOG.info(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbDispMgrLocalProxyDao localProxyDao =
            getXhbDispMgrLocalProxyRepository().findByCourtSiteId(courtSiteDao.getId());

        if (localProxyDao != null) {
            // Make sure we have the latest version
            getEntityManager().refresh(localProxyDao);
            ILocalProxy localProxy =
                XhbDispMgrLocalProxyRepository.getLocalProxyFromDao(localProxyDao);
            courtSite.setLocalProxy(localProxy);
        }
        LOG.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    protected XhbDispMgrLocalProxyRepository getXhbDispMgrLocalProxyRepository() {
        final String methodName = "getXhbDispMgrLocalProxyRepository";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        if (xhbDispMgrLocalProxyRepository == null) {
            xhbDispMgrLocalProxyRepository = new XhbDispMgrLocalProxyRepository(getEntityManager());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhbDispMgrLocalProxyRepository;
    }

}
