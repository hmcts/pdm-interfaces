package uk.gov.hmcts.pdm.business.entities.xhbcourtsite;


import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping.XhbDispMgrMappingDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping.XhbDispMgrMappingRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.XhibitCourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;

import java.util.List;
import java.util.Set;



public abstract class XhbDispMgrCourtSiteProcessor extends XhbDispMgrConverter {

    private static final Logger LOG = LoggerFactory.getLogger(XhbCourtSiteRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";
    private static final String CDU = " - cdu ";
    private static final String URL = " - url ";

    private XhbDispMgrMappingRepository xhbDispMgrMappingRepository;


    protected XhbDispMgrCourtSiteProcessor(EntityManager entityManager) {
        super(entityManager);
    }

    public static IXhibitCourtSite getXhibitCourtSiteFromDao(XhbCourtSiteDao xhbCourtSiteDao) {
        final String methodName = "getXhibitCourtSiteFromDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        IXhibitCourtSite xhibitCourtSite = new XhibitCourtSite();
        xhibitCourtSite.setId(xhbCourtSiteDao.getId().longValue());
        xhibitCourtSite.setCourtSiteName(xhbCourtSiteDao.getCourtSiteName());
        xhibitCourtSite.setDisplayName(xhbCourtSiteDao.getDisplayName());
        xhibitCourtSite.setShortName(xhbCourtSiteDao.getShortName());
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhibitCourtSite;
    }


    protected void processSchedule(ICourtSite courtSite, XhbDispMgrCourtSiteDao courtSiteDao) {
        if (courtSiteDao.getXhbDispMgrScheduleDao() != null) {
            XhbDispMgrScheduleDao scheduleDao = courtSiteDao.getXhbDispMgrScheduleDao();
            ISchedule schedule = XhbDispMgrScheduleRepository.getScheduleFromDao(scheduleDao);
            courtSite.setSchedule(schedule);
        }
    }

    protected void processLocalProxy(ICourtSite courtSite, XhbDispMgrCourtSiteDao courtSiteDao) {
        XhbDispMgrLocalProxyDao localProxyDao =
            getXhbDispMgrLocalProxyRepository().findByCourtSiteId(courtSiteDao.getId());

        if (localProxyDao != null) {
            ILocalProxy localProxy =
                XhbDispMgrLocalProxyRepository.getLocalProxyFromDao(localProxyDao);
            localProxy.setCourtSite(courtSite);
            courtSite.setLocalProxy(localProxy);
        }
    }

    protected void processCdus(ICourtSite courtSite, XhbDispMgrCourtSiteDao courtSiteDao,
        String methodName) {
        if (courtSiteDao.getXhbDispMgrCduDao() != null) {
            Set<ICduModel> cdus = createICduSet();
            Set<XhbDispMgrCduDao> cduDaos = courtSiteDao.getXhbDispMgrCduDao();

            for (XhbDispMgrCduDao cduDao : cduDaos) {
                ICduModel cdu = XhbDispMgrCduRepository.getCduFromDao(cduDao);
                processUrls(cdu, methodName);
                cdu.setCourtSite(courtSite);
                cdus.add(cdu);
            }

            courtSite.setCdus(cdus);
        }
    }

    private void processUrls(ICduModel cdu, String methodName) {
        List<XhbDispMgrMappingDao> mappingDaos =
            getXhbDispMgrMappingRepository().findDaoByCduId(cdu.getId().intValue());

        if (mappingDaos != null) {
            List<IUrlModel> urls = createIUrlList();

            for (XhbDispMgrMappingDao mappingDao : mappingDaos) {
                LOG.debug("{}{}{}{} mapped to{}{}", METHOD, methodName, CDU, mappingDao.getCduId(),
                    URL, mappingDao.getUrlId());
                IUrlModel url = getXhbDispMgrMappingRepository().getUrlFromMappingDao(mappingDao);
                urls.add(url);
            }

            cdu.setUrls(urls);
        }
    }


    protected XhbDispMgrMappingRepository getXhbDispMgrMappingRepository() {
        final String methodName = "getXhbDispMgrMappingRepository";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        if (xhbDispMgrMappingRepository == null) {
            xhbDispMgrMappingRepository = new XhbDispMgrMappingRepository(getEntityManager());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhbDispMgrMappingRepository;
    }

}
