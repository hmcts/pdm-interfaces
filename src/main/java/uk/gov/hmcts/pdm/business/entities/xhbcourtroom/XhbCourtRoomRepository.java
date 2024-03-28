package uk.gov.hmcts.pdm.business.entities.xhbcourtroom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.List;
import java.util.Optional;

public class XhbCourtRoomRepository extends AbstractRepository<XhbCourtRoomDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbCourtRoomRepository.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";

    public XhbCourtRoomRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbCourtRoomDao> getDaoClass() {
        return XhbCourtRoomDao.class;
    }

    /**
     * findByCourtRoomId.
     *
     * @param xhibitCourtSiteId Integer
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<XhbCourtRoomDao> findByCourtSiteId(final Integer xhibitCourtSiteId) {
        final String methodName = "findByCourtSiteId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_COURT_ROOM.findByCourtSiteId");
        query.setParameter("courtSiteId", xhibitCourtSiteId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    /**
     * saveDao.
     *
     * @param dao XhbCourtRoomDao
     */
    public void saveDao(XhbCourtRoomDao dao) {
        final String methodName = "save";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        super.save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * updateDao.
     *
     * @param dao XhbCourtRoomDao
     * @return Optional XhbCourtRoomDao
     */
    public Optional<XhbCourtRoomDao> updateDao(XhbCourtRoomDao dao) {
        final String methodName = "updateDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbCourtRoomDao> result = super.update(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    /**
     * deleteDao.
     *
     * @param dao XhbCourtRoomDao
     */
    public void deleteDao(Optional<XhbCourtRoomDao> dao) {
        final String methodName = "deleteDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        super.delete(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }
}
