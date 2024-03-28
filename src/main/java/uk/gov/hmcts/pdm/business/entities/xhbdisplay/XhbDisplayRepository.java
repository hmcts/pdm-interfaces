package uk.gov.hmcts.pdm.business.entities.xhbdisplay;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.List;
import java.util.Optional;

public class XhbDisplayRepository extends AbstractRepository<XhbDisplayDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDisplayRepository.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    public XhbDisplayRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDisplayDao> getDaoClass() {
        return XhbDisplayDao.class;
    }

    /**
     * findByCourtSiteId.
     *
     * @param courtSiteId Integer
     * @return XhbDisplayDao
     */
    @SuppressWarnings("unchecked")
    public List<XhbDisplayDao> findByCourtSiteId(final Integer courtSiteId) {
        final String methodName = "findByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISPLAY.findByCourtSiteId");
        query.setParameter("courtSiteId", courtSiteId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    /**
     * saveDao.
     *
     * @param dao XhbDisplayDao
     */
    public void saveDao(XhbDisplayDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        super.save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * updateDao.
     *
     * @param dao XhbDisplayDao
     * @return Optional XhbDisplayDao
     */
    public Optional<XhbDisplayDao> updateDao(XhbDisplayDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbDisplayDao> result = super.update(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }
    
    /**
     * deleteDao.
     *
     * @param dao XhbDisplayDao
     */
    public void deleteDao(Optional<XhbDisplayDao> dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        super.delete(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }
}
