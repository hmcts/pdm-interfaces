package uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl.XhbDispMgrUrlRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;

import java.util.List;

public class XhbDispMgrMappingRepository extends AbstractRepository<XhbDispMgrMappingDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrMappingRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    private XhbDispMgrUrlRepository xhbDispMgrUrlRepository;

    public XhbDispMgrMappingRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrMappingDao> getDaoClass() {
        return XhbDispMgrMappingDao.class;
    }

    /**
     * findDaoByCduId.
     * 
     * @param cduId Integer
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<XhbDispMgrMappingDao> findDaoByCduId(final Integer cduId) {
        final String methodName = "findDaoByCduId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_MAPPING.findByCduId");
        query.setParameter("cduId", cduId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (List<XhbDispMgrMappingDao>) query.getResultList();
    }

    /*
     * This is our 'findById' query as this table has a composite primary key consisting of a URL ID
     * and CDU ID
     */
    public XhbDispMgrMappingDao findDaoByCompositeId(final Integer urlId, final Integer cduId) {
        final String methodName = "findDaoByCompositeId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_MAPPING.findByCompositeId");
        query.setParameter("urlId", urlId);
        query.setParameter("cduId", cduId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrMappingDao) query.getResultList().get(0);
    }

    public IUrlModel getUrlFromMappingDao(XhbDispMgrMappingDao xhbDispMgrMappingDao) {
        final String methodName = "getUrlFromMappingDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        IUrlModel url = getXhbDispMgrUrlRepository().findByUrlId(xhbDispMgrMappingDao.getUrlId());

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return url;
    }

    // Delete ALL mappings for CDU
    public void deleteMappingsForCdu(ICduModel cdu) {
        final String methodName = "deleteMappingsForCdu";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        getEntityManager().getTransaction().begin();

        getEntityManager()
            .createQuery("DELETE FROM XHB_DISP_MGR_MAPPING o WHERE o.cduId in (:cduId)")
            .setParameter("cduId", cdu.getId().intValue()).executeUpdate();

        getEntityManager().getTransaction().commit();

        clearEntityManager();
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    // Remove URL mapping
    public void deleteMappingForCdu(ICduModel cdu, IUrlModel url) {
        final String methodName = "deleteMappingForCdu";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        getEntityManager().getTransaction().begin();

        XhbDispMgrMappingDao dao =
            findDaoByCompositeId(url.getId().intValue(), cdu.getId().intValue());

        getEntityManager().remove(dao);

        getEntityManager().getTransaction().commit();

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    // Add URL mapping
    public void addMappingForCdu(ICduModel cdu, IUrlModel url) {
        final String methodName = "addMappingForCdu";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        XhbDispMgrMappingDao dao = new XhbDispMgrMappingDao();

        setDaoFromBasicValue(cdu, url, dao);

        save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    private void setDaoFromBasicValue(ICduModel cdu, IUrlModel url,
        XhbDispMgrMappingDao xhbDispMgrMappingDao) {
        final String methodName = "setDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        xhbDispMgrMappingDao.setUrlId(url.getId().intValue());
        xhbDispMgrMappingDao.setCduId(cdu.getId().intValue());

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    private XhbDispMgrUrlRepository getXhbDispMgrUrlRepository() {
        final String methodName = "getXhbDispMgrUrlRepository";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        if (xhbDispMgrUrlRepository == null) {
            xhbDispMgrUrlRepository = new XhbDispMgrUrlRepository(getEntityManager());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhbDispMgrUrlRepository;
    }
}
