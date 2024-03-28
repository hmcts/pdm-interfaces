package uk.gov.hmcts.pdm.publicdisplay.initialization;

import jakarta.persistence.EntityManagerFactory;

public final class InitializationService {

    private EntityManagerFactory entityManagerFactory;
    
    /**
     * Singleton instance.
     */
    private static final InitializationService SELF = new InitializationService();
    
    /**
     * Don't instantiate me.
     */
    private InitializationService() {
        // private constructor
    }
    
    /**
     * Singleton accessor.
     * 
     * @return InitializationService
     */
    public static InitializationService getInstance() {
        return SELF;
    }
    

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}
