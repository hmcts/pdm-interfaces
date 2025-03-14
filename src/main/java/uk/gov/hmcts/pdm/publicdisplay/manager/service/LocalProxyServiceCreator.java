package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DashboardCduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DashboardUrlDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.ScheduleDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class LocalProxyServiceCreator extends AbstractService {

    /**
     * Gets the local proxy.
     *
     * @param xhibitCourtSite the xhibit court site
     * @return the local proxy
     */
    protected ILocalProxy getLocalProxy(final IXhibitCourtSite xhibitCourtSite) {
        ILocalProxy retVal = null;
        final ICourtSite courtSite = xhibitCourtSite.getCourtSite();
        if (courtSite != null) {
            final ILocalProxy localProxy = courtSite.getLocalProxy();
            if (localProxy != null) {
                retVal = localProxy;
            }

        }
        return retVal;
    }

    @Override
    protected XhibitCourtSiteDto createXhibitCourtSiteDto() {
        return new XhibitCourtSiteDto();
    }

    protected DashboardCduDto createDashboardCduDto() {
        return new DashboardCduDto();
    }

    protected ScheduleDto createScheduleDto() {
        return new ScheduleDto();
    }

    protected DashboardUrlDto createDashboardUrlDto() {
        return new DashboardUrlDto();
    }

}
