package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;

public class RagStatusRepository {

    private EntityManager entityManager;

    private XhbDispMgrCduRepository xhbDispMgrCduRepository;

    private XhbDispMgrCourtSiteRepository xhbDispMgrCourtSiteRepository;

    private XhbDispMgrLocalProxyRepository xhbDispMgrLocalProxyRepository;

    private EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    protected XhbDispMgrCourtSiteRepository getXhbDispMgrCourtSiteRepository() {
        if (xhbDispMgrCourtSiteRepository == null) {
            xhbDispMgrCourtSiteRepository = new XhbDispMgrCourtSiteRepository(getEntityManager());
        }
        return xhbDispMgrCourtSiteRepository;
    }

    protected XhbDispMgrCduRepository getXhbDispMgrCduRepository() {
        if (xhbDispMgrCduRepository == null) {
            xhbDispMgrCduRepository = new XhbDispMgrCduRepository(getEntityManager());
        }
        return xhbDispMgrCduRepository;
    }

    protected XhbDispMgrLocalProxyRepository getXhbDispMgrLocalProxyRepository() {
        if (xhbDispMgrLocalProxyRepository == null) {
            xhbDispMgrLocalProxyRepository = new XhbDispMgrLocalProxyRepository(getEntityManager());
        }
        return xhbDispMgrLocalProxyRepository;
    }

}
