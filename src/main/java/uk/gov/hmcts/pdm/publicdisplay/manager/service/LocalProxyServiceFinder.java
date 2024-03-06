package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DashboardUrlDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.ScheduleDto;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class LocalProxyServiceFinder extends LocalProxyServiceCreator {

    private EntityManager entityManager;

    private XhbCourtSiteRepository xhbCourtSiteRepository;

    private XhbDispMgrCourtSiteRepository xhbDispMgrCourtSiteRepository;

    private XhbDispMgrScheduleRepository xhbDispMgrScheduleRepository;

    private XhbDispMgrLocalProxyRepository xhbDispMgrLocalProxyRepository;

    private XhbDispMgrCduRepository xhbDispMgrCduRepository;

    /**
     * Set up our logger.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(LocalProxyServiceFinder.class);


    /**
     * Gets the dashboard urls.
     *
     * @param urls the urls
     * @return the dashboard urls
     */
    protected List<DashboardUrlDto> getDashboardUrls(final List<IUrlModel> urls) {
        final List<DashboardUrlDto> resultUrls = new ArrayList<>();
        for (IUrlModel url : urls) {
            final DashboardUrlDto urlDto = createDashboardUrlDto();
            urlDto.setUrl(url.getUrl());
            resultUrls.add(urlDto);
        }
        return resultUrls;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService#
     * isLocalProxyWithIpAddress(java. lang.String)
     */
    public boolean isLocalProxyWithIpAddress(final String ipAddress) {
        return getXhbDispMgrLocalProxyRepository().isLocalProxyWithIpAddress(ipAddress);
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService#
     * getPowerSaveSchedules()
     */
    public List<ScheduleDto> getPowerSaveSchedules() {
        LOGGER.debug("getPowerSaveSchedules()");
        final List<ScheduleDto> scheduleDtos = new ArrayList<>();
        final List<ISchedule> powerSaveSchedules =
            getXhbDispMgrScheduleRepository().findPowerSaveSchedules();
        for (ISchedule powerSaveSchedule : powerSaveSchedules) {
            final ScheduleDto scheduleDto = createScheduleDto();
            scheduleDto.setId(powerSaveSchedule.getId());
            scheduleDto.setDetail(powerSaveSchedule.getDetail());
            scheduleDto.setTitle(powerSaveSchedule.getTitle());
            scheduleDto.setType(powerSaveSchedule.getType());
            scheduleDtos.add(scheduleDto);
        }
        return scheduleDtos;
    }

    @Override
    protected ScheduleDto createScheduleDto() {
        return new ScheduleDto();
    }

    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    protected XhbCourtSiteRepository getXhbCourtSiteRepository() {
        if (xhbCourtSiteRepository == null) {
            xhbCourtSiteRepository = new XhbCourtSiteRepository(getEntityManager());
        }
        return xhbCourtSiteRepository;
    }

    protected XhbDispMgrCourtSiteRepository getXhbDispMgrCourtSiteRepository() {
        if (xhbDispMgrCourtSiteRepository == null) {
            xhbDispMgrCourtSiteRepository = new XhbDispMgrCourtSiteRepository(getEntityManager());
        }
        return xhbDispMgrCourtSiteRepository;
    }

    protected XhbDispMgrScheduleRepository getXhbDispMgrScheduleRepository() {
        if (xhbDispMgrScheduleRepository == null) {
            xhbDispMgrScheduleRepository = new XhbDispMgrScheduleRepository(getEntityManager());
        }
        return xhbDispMgrScheduleRepository;
    }

    protected XhbDispMgrLocalProxyRepository getXhbDispMgrLocalProxyRepository() {
        if (xhbDispMgrLocalProxyRepository == null) {
            xhbDispMgrLocalProxyRepository = new XhbDispMgrLocalProxyRepository(getEntityManager());
        }
        return xhbDispMgrLocalProxyRepository;
    }

    protected XhbDispMgrCduRepository getXhbDispMgrCduRepository() {
        if (xhbDispMgrCduRepository == null) {
            xhbDispMgrCduRepository = new XhbDispMgrCduRepository(getEntityManager());
        }
        return xhbDispMgrCduRepository;
    }

}
