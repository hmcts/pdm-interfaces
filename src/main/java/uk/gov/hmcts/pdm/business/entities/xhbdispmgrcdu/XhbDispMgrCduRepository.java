package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;

import java.math.BigDecimal;
import java.util.List;


public class XhbDispMgrCduRepository extends CduFinder {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrCduRepository.class);

    public XhbDispMgrCduRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrCduDao> getDaoClass() {
        return XhbDispMgrCduDao.class;
    }

    /**
     * isCduWithCduNumber.
     * 
     * @param cduNumber String
     * @return boolean
     */
    @SuppressWarnings(UNCHECKED)
    public boolean isCduWithCduNumber(final String cduNumber) {
        final String methodName = "isCduWithCduNumber";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_CDU.findByCduNumber");
        query.setParameter("cduNumber", cduNumber);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return !query.getResultList().isEmpty();
    }

    /**
     * isCduWithMacAddress.
     * 
     * @param macAddress String
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public boolean isCduWithMacAddress(final String macAddress) {
        final String methodName = "isCduWithMacAddress";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return findByMacAddress(macAddress) != null;
    }

    /**
     * getNextIpHost.
     * 
     * @param courtSiteId Long
     * @param minHost Integer
     * @param maxHost Integer
     * @return Integer
     */
    public BigDecimal getNextIpHost(final Integer courtSiteId, final Integer minHost,
        final Integer maxHost) {
        final String methodName = "getNextIpHost";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_CDU.getNextIpHost");
        query.setParameter("courtSiteId", courtSiteId);
        query.setParameter("minHost", BigDecimal.valueOf(minHost));
        query.setParameter("maxHost", BigDecimal.valueOf(maxHost));

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null : (BigDecimal) query.getResultList().get(0);
    }

    public Boolean hostExists(final Integer courtSiteId) {
        final String methodName = "hostExists";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_CDU.hostExists");
        query.setParameter("courtSiteId", courtSiteId);

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return !query.getResultList().isEmpty();
    }

    /**
     * getCduWeightingTotal.
     * 
     * @return long
     */
    @SuppressWarnings(UNCHECKED)
    public long getCduWeightingTotal() {
        final String methodName = "getCduWeightingTotal";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_CDU.getCduWeightingTotal");
        List<Long> result = query.getResultList();
        if (result.get(0) == null) {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return 0;
        } else {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return result.get(0);
        }
    }

    /**
     * getCduWeightingOperational.
     * 
     * @return long
     */
    @SuppressWarnings(UNCHECKED)
    public long getCduWeightingOperational() {
        final String methodName = "getCduWeightingOperational";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        Query query =
            getEntityManager().createNamedQuery("XHB_DISP_MGR_CDU.getCduWeightingOperational");
        List<Long> result = query.getResultList();
        if (result.get(0) == null) {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return 0;
        } else {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return result.get(0);
        }
    }

    public void saveDaoFromBasicValue(ICduModel cdu) {
        final String methodName = "saveDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        XhbDispMgrCduDao dao = new XhbDispMgrCduDao();

        populateDaoFromBasicValue(dao, cdu);

        save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    // Update method to take in ICdu convert to Dao and call update
    public void updateDaoFromBasicValue(ICduModel cdu) {
        final String methodName = "updateDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        XhbDispMgrCduDao dao = findDaoByCduId(cdu.getId().intValue());

        populateDaoFromBasicValue(dao, cdu);

        update(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    // Unregister CDU
    public void deleteDaoFromBasicValue(ICduModel cdu) {
        final String methodName = "deleteDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        // Remove the mappings linked to the CDU that is up for removal.
        getXhbDispMgrMappingRepository().deleteMappingsForCdu(cdu);

        getEntityManager().getTransaction().begin();

        XhbDispMgrCduDao dao =
            getEntityManager().find(XhbDispMgrCduDao.class, cdu.getId().intValue());

        getEntityManager().remove(dao);

        getEntityManager().getTransaction().commit();

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    protected XhbDispMgrCduDao populateDaoFromBasicValue(XhbDispMgrCduDao dao, ICduModel cdu) {
        final String methodName = "setDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        // If save new or update existing
        if (cdu.getCourtSite().getId() != null) {
            dao.setCourtSiteId(cdu.getCourtSite().getId().intValue());
        }
        dao.setCduNumber(cdu.getCduNumber());
        dao.setMacAddress(cdu.getMacAddress());
        dao.setIpAddress(cdu.getIpAddress());
        dao.setTitle(cdu.getTitle());
        dao.setDescription(cdu.getDescription());
        dao.setLocation(cdu.getLocation());
        dao.setRefresh(cdu.getRefresh());
        dao.setWeighting(cdu.getWeighting());
        dao.setNotification(cdu.getNotification());
        dao.setOfflineInd(cdu.getOfflineIndicator());
        dao.setRagStatus(cdu.getRagStatus());
        dao.setRagStatusDate(cdu.getRagStatusDate());

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return dao;
    }
}
