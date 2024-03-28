package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefjudge.XhbRefJudgeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeRepository;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class RefJudgeServiceFinder extends RefJudgeServiceCreator {

    private EntityManager entityManager;

    private XhbCourtSiteRepository xhbCourtSiteRepository;
    private XhbRefJudgeRepository xhbRefJudgeRepository;
    private XhbRefSystemCodeRepository xhbRefSystemCodeRepository;

    /**
     * Set up our logger.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(RefJudgeServiceFinder.class);

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

    protected XhbRefJudgeRepository getXhbRefJudgeRepository() {
        if (xhbRefJudgeRepository == null) {
            xhbRefJudgeRepository = new XhbRefJudgeRepository(getEntityManager());
        }
        return xhbRefJudgeRepository;
    }
    
    protected XhbRefSystemCodeRepository getXhbRefSystemCodeRepository() {
        if (xhbRefSystemCodeRepository == null) {
            xhbRefSystemCodeRepository = new XhbRefSystemCodeRepository(getEntityManager());
        }
        return xhbRefSystemCodeRepository;
    }
}
