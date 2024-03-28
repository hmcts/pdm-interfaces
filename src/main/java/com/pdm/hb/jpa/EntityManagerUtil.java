package com.pdm.hb.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import uk.gov.hmcts.pdm.publicdisplay.initialization.InitializationService;



public class EntityManagerUtil {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY;
    
    static {
        try {
            ENTITY_MANAGER_FACTORY = InitializationService.getInstance().getEntityManagerFactory();

        } catch (RuntimeException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    protected EntityManagerUtil() {
        // Protected constructor
    }

    public static EntityManager getEntityManager() {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        entityManager.setFlushMode(FlushModeType.AUTO);
        return entityManager;

    }
}
