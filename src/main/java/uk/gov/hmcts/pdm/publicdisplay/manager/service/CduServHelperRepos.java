package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import com.pdm.hb.jpa.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping.XhbDispMgrMappingRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl.XhbDispMgrUrlRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient;

@SuppressWarnings("PMD.NullAssignment")
public class CduServHelperRepos {

    /**
     * Set up out local proxy rest client.
     */
    @Autowired
    protected ILocalProxyRestClient localProxyRestClient;

    /** The timeout in seconds for socket, connect & request. */
    @Value("#{applicationConfiguration.registerCduNetwork}")
    protected String cduIpNetwork;

    /** The timeout in seconds for socket, connect & request. */
    @Value("#{applicationConfiguration.registerCduHostMin}")
    protected Integer cduIpHostMin;

    /** The timeout in seconds for socket, connect & request. */
    @Value("#{applicationConfiguration.registerCduHostMax}")
    protected Integer cduIpHostMax;

    @Value("${localproxy.communication.enabled}")
    protected Boolean localProxyCommunicationEnabled;

    @Value("${fake.cdus.enabled}")
    protected Boolean fakeCdusEnabled;

    @Value("${fake.cdus.register.enabled}")
    protected Boolean fakeCdusRegisterEnabled;

    private EntityManager entityManager;

    private XhbCourtSiteRepository xhbCourtSiteRepository;

    private XhbDispMgrCduRepository xhbDispMgrCduRepository;

    private XhbDispMgrUrlRepository xhbDispMgrUrlRepository;

    private XhbDispMgrCourtSiteRepository xhbDispMgrCourtSiteRepository;

    private XhbDispMgrMappingRepository xhbDispMgrMappingRepository;

    protected void clearRepositories() {
        xhbCourtSiteRepository = null;
        xhbDispMgrCduRepository = null;
        xhbDispMgrUrlRepository = null;
        xhbDispMgrCourtSiteRepository = null;
        xhbDispMgrMappingRepository = null;
    }
    
    protected XhbDispMgrCduRepository getXhbDispMgrCduRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDispMgrCduRepository)) {
            xhbDispMgrCduRepository = new XhbDispMgrCduRepository(getEntityManager());
        }
        return xhbDispMgrCduRepository;
    }

    protected XhbCourtSiteRepository getXhbCourtSiteRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbCourtSiteRepository)) {
            xhbCourtSiteRepository = new XhbCourtSiteRepository(getEntityManager());
        }
        return xhbCourtSiteRepository;
    }
    
    protected EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            clearRepositories();
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    protected XhbDispMgrUrlRepository getXhbDispMgrUrlRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDispMgrUrlRepository)) {
            xhbDispMgrUrlRepository = new XhbDispMgrUrlRepository(getEntityManager());
        }
        return xhbDispMgrUrlRepository;
    }

    protected XhbDispMgrCourtSiteRepository getXhbDispMgrCourtSiteRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDispMgrCourtSiteRepository)) {
            xhbDispMgrCourtSiteRepository = new XhbDispMgrCourtSiteRepository(getEntityManager());
        }
        return xhbDispMgrCourtSiteRepository;
    }

    protected XhbDispMgrMappingRepository getXhbDispMgrMappingRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDispMgrMappingRepository)) {
            xhbDispMgrMappingRepository = new XhbDispMgrMappingRepository(getEntityManager());
        }
        return xhbDispMgrMappingRepository;
    }

}
