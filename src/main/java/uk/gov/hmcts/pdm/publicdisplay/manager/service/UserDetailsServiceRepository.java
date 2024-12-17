package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import com.pdm.hb.jpa.RepositoryUtil;
import jakarta.persistence.EntityManager;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgruserdetails.XhbDispMgrUserDetailsRepository;


public class UserDetailsServiceRepository {

    private EntityManager entityManager;

    private XhbDispMgrUserDetailsRepository xhbDispMgrUserDetailsRepository;

    protected EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    protected XhbDispMgrUserDetailsRepository getXhbDispMgrUserDetailsRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDispMgrUserDetailsRepository)) {
            xhbDispMgrUserDetailsRepository =
                new XhbDispMgrUserDetailsRepository(getEntityManager());
        }
        return xhbDispMgrUserDetailsRepository;
    }

}
