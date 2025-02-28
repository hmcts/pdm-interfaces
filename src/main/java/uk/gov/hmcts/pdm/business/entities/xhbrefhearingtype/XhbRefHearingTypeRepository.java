package uk.gov.hmcts.pdm.business.entities.xhbrefhearingtype;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.List;
import java.util.Optional;

public class XhbRefHearingTypeRepository extends AbstractRepository<XhbRefHearingTypeDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbRefHearingTypeRepository.class);
    
    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    public XhbRefHearingTypeRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbRefHearingTypeDao> getDaoClass() {
        return XhbRefHearingTypeDao.class;
    }
    
    /**
     * findByCourtSiteId.
     *
     * @param courtSiteId Integer
     * @return XhbRefHearingTypeDao
     */
    @SuppressWarnings("unchecked")
    public List<XhbRefHearingTypeDao> findByCourtSiteId(final Integer courtSiteId) {
        final String methodName = "findByCourtId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_REF_HEARING_TYPE.findByCourtSiteId");
        query.setParameter("courtSiteId", courtSiteId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
    
    /**
     * findByCourtId.
     *
     * @param courtId Integer
     * @return XhbRefHearingTypeDao
     */
    @SuppressWarnings("unchecked")
    public List<XhbRefHearingTypeDao> findByCourtId(final Integer courtId) {
        final String methodName = "findByCourtId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_REF_HEARING_TYPE.findByCourtId");
        query.setParameter("courtId", courtId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
    
    /**
     * findAllCategories.
     *
     * @return String
     */
    @SuppressWarnings("unchecked")
    public List<String> findAllCategories() {
        final String methodName = "findAllCategories";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query = getEntityManager().createNamedQuery("XHB_REF_HEARING_TYPE.findAllCategories");
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
    
    /**
     * saveDao.
     *
     * @param dao XhbRefHearingTypeDao
     */
    public void saveDao(XhbRefHearingTypeDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        super.save(dao);
        clearEntityManager();
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }
    
    /**
     * updateDao.
     *
     * @param dao XhbRefHearingTypeDao
     * @return Optional XhbRefHearingTypeDao
     */
    public Optional<XhbRefHearingTypeDao> updateDao(XhbRefHearingTypeDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Optional<XhbRefHearingTypeDao> result = super.update(dao);
        clearEntityManager();
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }
}
