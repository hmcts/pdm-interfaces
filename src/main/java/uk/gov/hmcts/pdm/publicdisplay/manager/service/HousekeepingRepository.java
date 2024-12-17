package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import com.pdm.hb.jpa.RepositoryUtil;
import jakarta.persistence.EntityManager;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrhousekeeping.XhbDispMgrHousekeepingRepository;


public class HousekeepingRepository {

    private EntityManager entityManager;
    
    private XhbDispMgrHousekeepingRepository xhbDispMgrHousekeepingRepository;

    private EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }
    
    protected XhbDispMgrHousekeepingRepository getXhbDispMgrHousekeepingRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDispMgrHousekeepingRepository)) {
            xhbDispMgrHousekeepingRepository = new XhbDispMgrHousekeepingRepository(getEntityManager());
        }
        return xhbDispMgrHousekeepingRepository;
    }

}
