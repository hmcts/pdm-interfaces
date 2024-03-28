package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu;


import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbDispMgrCourtSiteProcessor;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping.XhbDispMgrMappingDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;

import java.util.ArrayList;
import java.util.List;

public abstract class CduConverter extends CduUtility {

    private static final Logger LOG = LoggerFactory.getLogger(CduConverter.class);
    private static final String CDU = " - cdu ";
    private static final String URL = " - url ";

    protected CduConverter(EntityManager entityManager) {
        super(entityManager);
    }

    // Converter Method to take in a Dao and convert to ICdu
    protected ICduModel convertDaoToCduBasicValue(XhbDispMgrCduDao dao) {
        final String methodName = "convertDaoToCduBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        ICduModel cdu = getCduFromDao(dao);

        ICourtSite courtSite = null;

        if (dao.getXhbDispMgrCourtSiteDao() != null) {
            XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao = dao.getXhbDispMgrCourtSiteDao();
            courtSite = XhbDispMgrCourtSiteRepository.getCourtSiteFromDao(xhbDispMgrCourtSiteDao);

            if (xhbDispMgrCourtSiteDao.getXhbDispMgrScheduleDao() != null) {
                XhbDispMgrScheduleDao xhbDispMgrScheduleDao =
                    xhbDispMgrCourtSiteDao.getXhbDispMgrScheduleDao();
                ISchedule schedule =
                    XhbDispMgrScheduleRepository.getScheduleFromDao(xhbDispMgrScheduleDao);
                courtSite.setSchedule(schedule);
            }

            // Setting Local Proxy Separately through a findByCourtSite query
            XhbDispMgrLocalProxyDao xhbDispMgrLocalProxyDao =
                getXhbDispMgrLocalProxyRepository().findByCourtSiteId(dao.getCourtSiteId());
            if (xhbDispMgrLocalProxyDao != null) {
                ILocalProxy localProxy =
                    XhbDispMgrLocalProxyRepository.getLocalProxyFromDao(xhbDispMgrLocalProxyDao);
                courtSite.setLocalProxy(localProxy);
            }

            // Setting URL separately through a mapping query
            List<XhbDispMgrMappingDao> mappingDaos =
                getXhbDispMgrMappingRepository().findDaoByCduId(cdu.getId().intValue());
            if (mappingDaos != null) {
                List<IUrlModel> urls = new ArrayList<>();
                for (XhbDispMgrMappingDao mappingDao : mappingDaos) {
                    LOG.debug("{}{}{}{} mapped to{}{}", METHOD, methodName, CDU,
                        mappingDao.getCduId(), URL, mappingDao.getUrlId());
                    IUrlModel url = getXhbDispMgrMappingRepository().getUrlFromMappingDao(mappingDao);
                    urls.add(url);
                }
                cdu.setUrls(urls);
            }

            if (xhbDispMgrCourtSiteDao.getXhbCourtSiteDao() != null) {
                XhbCourtSiteDao xhbCourtsiteDao = xhbDispMgrCourtSiteDao.getXhbCourtSiteDao();
                IXhibitCourtSite xhibitCourtSite =
                        XhbDispMgrCourtSiteProcessor.getXhibitCourtSiteFromDao(xhbCourtsiteDao);
                courtSite.setXhibitCourtSite(xhibitCourtSite);
            }
        }
        cdu.setCourtSite(courtSite);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return cdu;
    }
}
