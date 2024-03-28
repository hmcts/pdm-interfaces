package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
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
public class CourtServiceFinder extends CourtServiceCreator {

    private EntityManager entityManager;
    private XhbCourtRepository xhbCourtRepository;
    private XhbCourtSiteRepository xhbCourtSiteRepository;

    /**
     * Set up our logger.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CourtServiceFinder.class);

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
    
    protected XhbCourtRepository getXhbCourtRepository() {
        if (xhbCourtRepository == null) {
            xhbCourtRepository = new XhbCourtRepository(getEntityManager());
        }
        return xhbCourtRepository;
    }
}
