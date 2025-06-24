package uk.gov.hmcts.pdm.business.entities.xhbcourt;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.List;

public class XhbCourtRepository extends AbstractRepository<XhbCourtDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbCourtRepository.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    public XhbCourtRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbCourtDao> getDaoClass() {
        return XhbCourtDao.class;
    }

    /**
     * Overridden findAll method to return all XhbCourtDao objects that are not marked as obsolete.
     * 
     * @return List of XhbCourtDao objects.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<XhbCourtDao> findAll() {

        final String methodName = "findAll";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        String hql =
            "FROM " + getDaoClass().getName() + " e WHERE e.obsInd IS NULL OR e.obsInd <> 'Y'";
        Query query = getEntityManager().createQuery(hql);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
}
