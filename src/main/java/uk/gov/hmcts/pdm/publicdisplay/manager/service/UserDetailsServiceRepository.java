package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgruserdetails.XhbDispMgrUserDetailsRepository;


public class UserDetailsServiceRepository {

    private EntityManager entityManager;

    private XhbDispMgrUserDetailsRepository xhbDispMgrUserDetailsRepository;

    private EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    protected XhbDispMgrUserDetailsRepository getXhbDispMgrUserDetailsRepository() {
        if (xhbDispMgrUserDetailsRepository == null) {
            xhbDispMgrUserDetailsRepository =
                new XhbDispMgrUserDetailsRepository(getEntityManager());
        }
        return xhbDispMgrUserDetailsRepository;
    }

}
