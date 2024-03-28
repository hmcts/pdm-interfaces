package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplay.XhbDisplayRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation.XhbDisplayLocationDao;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation.XhbDisplayLocationRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaytype.XhbDisplayTypeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrotationsets.XhbRotationSetsRepository;


@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class DisplayServiceFinder extends DisplayServiceCreator {

    private EntityManager entityManager;

    private XhbCourtSiteRepository xhbCourtSiteRepository;
    private XhbDisplayRepository xhbDisplayRepository;
    private XhbDisplayLocationRepository xhbDisplayLocationRepository;
    private XhbDisplayTypeRepository xhbDisplayTypeRepository;
    private XhbRotationSetsRepository xhbRotationSetsRepository;

    /**
     * Set up our logger.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DisplayServiceFinder.class);

    protected XhbDisplayLocationDao getXhbDisplayLocationDao(final Integer xhibitCourtSiteId,
        final String descriptionCode) {
        XhbDisplayLocationDao displayLocationDao =
            getXhbDisplayLocationRepository().findByCourtSiteId(xhibitCourtSiteId);
        if (displayLocationDao == null) {
            displayLocationDao = new XhbDisplayLocationDao();
            displayLocationDao.setCourtSiteId(xhibitCourtSiteId);
            displayLocationDao.setDescriptionCode(descriptionCode);
            // Save the new location
            getXhbDisplayLocationRepository().saveDao(displayLocationDao);
            // Fetch the newly created record (with a generated Id)
            displayLocationDao =
                getXhbDisplayLocationRepository().findByCourtSiteId(xhibitCourtSiteId);
        }
        return displayLocationDao;
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

    protected XhbDisplayRepository getXhbDisplayRepository() {
        if (xhbDisplayRepository == null) {
            xhbDisplayRepository = new XhbDisplayRepository(getEntityManager());
        }
        return xhbDisplayRepository;
    }
    
    protected XhbDisplayLocationRepository getXhbDisplayLocationRepository() {
        if (xhbDisplayLocationRepository == null) {
            xhbDisplayLocationRepository = new XhbDisplayLocationRepository(getEntityManager());
        }
        return xhbDisplayLocationRepository;
    }
    
    protected XhbDisplayTypeRepository getXhbDisplayTypeRepository() {
        if (xhbDisplayTypeRepository == null) {
            xhbDisplayTypeRepository = new XhbDisplayTypeRepository(getEntityManager());
        }
        return xhbDisplayTypeRepository;
    }
    
    protected XhbRotationSetsRepository getXhbRotationSetsRepository() {
        if (xhbRotationSetsRepository == null) {
            xhbRotationSetsRepository = new XhbRotationSetsRepository(getEntityManager());
        }
        return xhbRotationSetsRepository;
    }
}
