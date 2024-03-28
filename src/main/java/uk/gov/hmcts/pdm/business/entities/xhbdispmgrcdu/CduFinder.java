package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;

import java.util.ArrayList;
import java.util.List;

public abstract class CduFinder extends CduConverter {

    private static final Logger LOG = LoggerFactory.getLogger(CduFinder.class);

    protected CduFinder(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * findDaoByCduId.
     *
     * @param id Integer
     * @return XhbDispMgrCduDao
     */
    @SuppressWarnings("unchecked")
    protected XhbDispMgrCduDao findDaoByCduId(final Integer id) {
        final String methodName = "findDaoByCduId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_CDU.findByCduId");
        query.setParameter("cduId", id);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrCduDao) query.getResultList().get(0);
    }

    // Wrapper Method to Return a ICdu from findDaoByCduId
    public ICduModel findByCduId(final Integer id) {
        final String methodName = "findByCduId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        XhbDispMgrCduDao dao = findDaoByCduId(id);
        if (dao != null) {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return convertDaoToCduBasicValue(dao);
        } else {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return null;
        }
    }

    /**
     * findDaoByMacAddress.
     *
     * @param macAddress String
     * @return XhbDispMgrCduDao
     */
    @SuppressWarnings("unchecked")
    private XhbDispMgrCduDao findDaoByMacAddress(final String macAddress) {
        final String methodName = "findDaoByMacAddress";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_CDU.findByMacAddress");
        query.setParameter("macAddress", macAddress);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrCduDao) query.getResultList().get(0);
    }

    // Wrapper Method to Return a ICdu from findDaoByMacAddress
    public ICduModel findByMacAddress(final String macAddress) {
        final String methodName = "findByMacAddress";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        XhbDispMgrCduDao dao = findDaoByMacAddress(macAddress);
        if (dao != null) {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return convertDaoToCduBasicValue(dao);
        } else {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return null;
        }
    }

    /**
     * findDaoByMacAddressWithLike.
     *
     * @param macAddress String
     * @return List
     */
    @SuppressWarnings(UNCHECKED)
    private List<XhbDispMgrCduDao> findDaoByMacAddressWithLike(final String macAddress) {
        final String methodName = "findDaoByMacAddressWithLike";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query =
            getEntityManager().createNamedQuery("XHB_DISP_MGR_CDU.findByMacAddressWithLike");
        query.setParameter("macAddress", macAddress);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    // Wrapper Method to Return a ICdu from findDaoByMacAddressWithLike
    public List<ICduModel> findByMacAddressWithLike(final String macAddress) {
        final String methodName = "findByMacAddressWithLike";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        List<XhbDispMgrCduDao> daos = findDaoByMacAddressWithLike(macAddress);
        List<ICduModel> results = new ArrayList<>();
        for (XhbDispMgrCduDao dao : daos) {
            results.add(convertDaoToCduBasicValue(dao));
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return results;
    }
}
