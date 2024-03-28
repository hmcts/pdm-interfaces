package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefhearingtype.XhbRefHearingTypeRepository;


@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class HearingTypeServiceFinder extends HearingTypeServiceCreator {

    private EntityManager entityManager;

    private XhbCourtSiteRepository xhbCourtSiteRepository;
    
    private XhbRefHearingTypeRepository xhbRefHearingTypeRepository;

    /**
     * Set up our logger.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(HearingTypeServiceFinder.class);

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
    
    protected XhbRefHearingTypeRepository getXhbRefHearingTypeRepository() {
        if (xhbRefHearingTypeRepository == null) {
            xhbRefHearingTypeRepository = new XhbRefHearingTypeRepository(getEntityManager());
        }
        return xhbRefHearingTypeRepository;
    }
}
