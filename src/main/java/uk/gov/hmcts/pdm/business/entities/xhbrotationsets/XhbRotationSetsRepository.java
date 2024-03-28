package uk.gov.hmcts.pdm.business.entities.xhbrotationsets;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.List;

public class XhbRotationSetsRepository extends AbstractRepository<XhbRotationSetsDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbRotationSetsRepository.class);
    
    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    public XhbRotationSetsRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbRotationSetsDao> getDaoClass() {
        return XhbRotationSetsDao.class;
    }

    @SuppressWarnings("unchecked")
    public List<XhbRotationSetsDao> findByCourtId(Integer courtId) {
        final String methodName = "findByCourtId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_ROTATION_SETS.findByCourtId");
        query.setParameter("courtId", courtId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }
}
