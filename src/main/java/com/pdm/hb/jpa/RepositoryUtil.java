package com.pdm.hb.jpa;

import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

public class RepositoryUtil {

    protected RepositoryUtil() {
        // Protected constructor
    }
    
    public static boolean isRepositoryActive(AbstractRepository<?> repository) {
        return repository != null
            && EntityManagerUtil.isEntityManagerActive(repository.getEntityManager());
    }
}
