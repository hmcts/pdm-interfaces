package uk.gov.hmcts.pdm.business.entities.xhbrefjudge;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.List;
import java.util.Optional;

public class XhbRefJudgeRepository extends AbstractRepository<XhbRefJudgeDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbRefJudgeRepository.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    public XhbRefJudgeRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbRefJudgeDao> getDaoClass() {
        return XhbRefJudgeDao.class;
    }

    /**
     * findByCourtSiteId.
     *
     * @param courtSiteId Integer
     * @return XhbRefJudgeDao
     */
    @SuppressWarnings("unchecked")
    public List<XhbRefJudgeDao> findByCourtSiteId(final Integer courtSiteId) {
        final String methodName = "findByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_REF_JUDGE.findByCourtSiteId");
        query.setParameter("courtSiteId", courtSiteId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
    
    /**
     * saveDao.
     *
     * @param dao XhbRefJudgeDao
     */
    public void saveDao(XhbRefJudgeDao dao) {
        final String methodName = "saveDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        super.save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * updateDao.
     *
     * @param dao XhbRefJudgeDao
     * @return Optional XhbRefJudgeDao
     */
    public Optional<XhbRefJudgeDao> updateDao(XhbRefJudgeDao dao) {
        final String methodName = "updateDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbRefJudgeDao> result = super.update(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }
    
    /**
     * deleteDao.
     *
     * @param dao XhbRefJudgeDao
     */
    public void deleteDao(Optional<XhbRefJudgeDao> dao) {
        final String methodName = "deleteDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        super.delete(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }
}
