package uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.Optional;

public class XhbDisplayLocationRepository extends AbstractRepository<XhbDisplayLocationDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDisplayLocationRepository.class);
    
    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    public XhbDisplayLocationRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDisplayLocationDao> getDaoClass() {
        return XhbDisplayLocationDao.class;
    }
    
    /**
     * findByCourtSiteId.
     *
     * @param courtSiteId Integer
     * @return XhbDisplayDao
     */
    public XhbDisplayLocationDao findByCourtSiteId(final Integer courtSiteId) {
        final String methodName = "findByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISPLAY_LOCATION.findByCourtSiteId");
        query.setParameter("courtSiteId", courtSiteId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null : (XhbDisplayLocationDao) query.getResultList().get(0);
    }
    
    /**
     * saveDao.
     *
     * @param dao XhbDisplayLocationDao
     */
    public void saveDao(XhbDisplayLocationDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        super.save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }
    
    /**
     * updateDao.
     *
     * @param dao XhbDisplayLocationDao
     * @return Optional XhbDisplayLocationDao
     */
    public Optional<XhbDisplayLocationDao> updateDao(XhbDisplayLocationDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbDisplayLocationDao> result = super.update(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }
}
