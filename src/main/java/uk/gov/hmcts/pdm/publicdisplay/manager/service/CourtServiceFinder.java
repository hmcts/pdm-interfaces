package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import com.pdm.hb.jpa.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtRepository;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@SuppressWarnings("PMD.NullAssignment")
public class CourtServiceFinder extends CourtServiceCreator {

    private EntityManager entityManager;
    private XhbCourtRepository xhbCourtRepository;
    private XhbCourtSiteRepository xhbCourtSiteRepository;

    /**
     * Set up our logger.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CourtServiceFinder.class);
    
    protected void clearRepositories() {
        xhbCourtRepository = null;
        xhbCourtSiteRepository = null;
    }

    protected EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            clearRepositories();
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    protected XhbCourtSiteRepository getXhbCourtSiteRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbCourtSiteRepository)) {
            xhbCourtSiteRepository = new XhbCourtSiteRepository(getEntityManager());
        }
        return xhbCourtSiteRepository;
    }
    
    protected XhbCourtRepository getXhbCourtRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbCourtRepository)) {
            xhbCourtRepository = new XhbCourtRepository(getEntityManager());
        }
        return xhbCourtRepository;
    }
}
