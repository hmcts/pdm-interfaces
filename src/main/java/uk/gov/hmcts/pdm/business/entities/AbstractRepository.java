package uk.gov.hmcts.pdm.business.entities;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("PMD.LawOfDemeter")
public abstract class AbstractRepository<T extends AbstractDao> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRepository.class);
    private static final String ERROR = "Error: {}";

    @PersistenceContext
    private EntityManager entityManager;

    protected AbstractRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected abstract Class<T> getDaoClass();

    /**
     * findById.
     * 
     * @param id Integer
     * @return dao
     */
    public Optional<T> findById(Integer id) {
        LOG.debug("findById()");
        T dao = getEntityManager().find(getDaoClass(), id);
        return dao != null ? Optional.of(dao) : Optional.empty();
    }


    /**
     * findAll.
     * 
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        LOG.debug("findAll()");
        Query query = getEntityManager().createQuery(getFindAllQuery(getDaoClass().getName()));
        return query.getResultList();
    }


    private String getFindAllQuery(String className) {
        return "from " + className;
    }



    /**
     * save.
     * 
     * @param dao T
     */
    public void save(T dao) {
        try (EntityManager localEntityManager = getEntityManager()) {
            try {
                LOG.debug("Save()");
                localEntityManager.getTransaction().begin();
                localEntityManager.persist(dao);
                localEntityManager.getTransaction().commit();
            } catch (Exception e) {
                LOG.error(ERROR, e.getMessage());
                if (localEntityManager != null && localEntityManager.getTransaction().isActive()) {
                    localEntityManager.getTransaction().rollback();
                }
                throw e;
            }
        }
    }

    /**
     * update.
     * 
     * @param dao T
     * @return dao
     */
    public Optional<T> update(T dao) {
        try (EntityManager localEntityManager = getEntityManager()) {
            try {
                LOG.debug("Update()");
                localEntityManager.getTransaction().begin();
                T mergedDao = localEntityManager.merge(dao);
                localEntityManager.getTransaction().commit();
                mergedDao
                    .setVersion(mergedDao.getVersion() != null ? mergedDao.getVersion() + 1 : 1);
                return Optional.of(mergedDao);
            } catch (Exception e) {
                LOG.error(ERROR, e.getMessage());
                if (localEntityManager != null && localEntityManager.getTransaction().isActive()) {
                    localEntityManager.getTransaction().rollback();
                }
            }
            return Optional.empty();
        }
    }

    /**
     * delete.
     * 
     * @param dao Optional
     */
    public void delete(Optional<T> dao) {
        try (EntityManager localEntityManager = getEntityManager()) {
            try {
                LOG.debug("delete()");
                if (dao.isPresent()) {
                    localEntityManager.getTransaction().begin();
                    T mergedDao = localEntityManager.merge(dao.get());
                    localEntityManager.remove(mergedDao);
                    localEntityManager.getTransaction().commit();
                }
            } catch (Exception e) {
                LOG.error(ERROR, e.getMessage());
                if (localEntityManager != null && localEntityManager.getTransaction().isActive()) {
                    localEntityManager.getTransaction().rollback();
                }
            }
        }
    }

    /*
     * Main EntityManager for reads, etc
     */
    public EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    public void clearEntityManager() {
        getEntityManager().clear();
    }
}
