package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import com.pdm.hb.jpa.RepositoryUtil;
import jakarta.persistence.EntityManager;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrhousekeeping.XhbDispMgrHousekeepingRepository;


@SuppressWarnings("PMD.NullAssignment")
public class HousekeepingRepository {

    private EntityManager entityManager;
    
    private XhbDispMgrHousekeepingRepository xhbDispMgrHousekeepingRepository;

    
    protected void clearRepositories() {
        xhbDispMgrHousekeepingRepository = null;
    }
    
    private EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            clearRepositories();
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
