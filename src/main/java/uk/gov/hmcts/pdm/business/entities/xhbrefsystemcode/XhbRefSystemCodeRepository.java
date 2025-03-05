package uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.List;
import java.util.Optional;

public class XhbRefSystemCodeRepository extends AbstractRepository<XhbRefSystemCodeDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbRefSystemCodeRepository.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    public XhbRefSystemCodeRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbRefSystemCodeDao> getDaoClass() {
        return XhbRefSystemCodeDao.class;
    }

    /**
     * findJudgeTypeByCourtSiteId.
     *
     * @param courtSiteId Integer
     * @return XhbRefSystemCodeDao
     */
    @SuppressWarnings("unchecked")
    public List<XhbRefSystemCodeDao> findJudgeTypeByCourtSiteId(final Integer courtSiteId) {
        final String methodName = "findJudgeTypeByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query =
            getEntityManager().createNamedQuery("XHB_REF_SYSTEM_CODE.findJudgeTypeByCourtSiteId");
        query.setParameter("courtSiteId", courtSiteId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
    
    /**
     * findByCourtId.
     *
     * @param courtId Integer
     * @return XhbRefSystemCodeDao
     */
    @SuppressWarnings("unchecked")
    public List<XhbRefSystemCodeDao> findByCourtId(final Integer courtId) {
        final String methodName = "findByCourtId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_REF_SYSTEM_CODE.findByCourtId");
        query.setParameter("courtId", courtId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    /**
     * saveDao.
     *
     * @param dao xhbRefSystemCodeDao
     */
    public void saveDao(XhbRefSystemCodeDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        super.save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * updateDao.
     *
     * @param dao XhbRefSystemCodeDao
     * @return Optional XhbRefSystemCodeDao
     */
    public Optional<XhbRefSystemCodeDao> updateDao(XhbRefSystemCodeDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbRefSystemCodeDao> result = super.update(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }
}
