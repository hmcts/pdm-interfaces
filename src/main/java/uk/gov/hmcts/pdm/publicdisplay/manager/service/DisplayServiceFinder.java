package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation.XhbDisplayLocationDao;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class DisplayServiceFinder extends DisplayServiceCreator {

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
}
