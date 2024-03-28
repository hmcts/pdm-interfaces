package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping.XhbDispMgrMappingRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.CduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;


public abstract class CduUtility extends AbstractRepository<XhbDispMgrCduDao> {

    private XhbDispMgrLocalProxyRepository xhbDispMgrLocalProxyRepository;

    private XhbDispMgrMappingRepository xhbDispMgrMappingRepository;


    private static final Logger LOG = LoggerFactory.getLogger(CduUtility.class);
    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";
    protected static final String UNCHECKED = "unchecked";


    protected CduUtility(EntityManager entityManager) {
        super(entityManager);
    }

    protected XhbDispMgrLocalProxyRepository getXhbDispMgrLocalProxyRepository() {
        final String methodName = "getXhbDispMgrLocalProxyRepository";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        if (xhbDispMgrLocalProxyRepository == null) {
            xhbDispMgrLocalProxyRepository = new XhbDispMgrLocalProxyRepository(getEntityManager());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhbDispMgrLocalProxyRepository;
    }

    protected XhbDispMgrMappingRepository getXhbDispMgrMappingRepository() {
        final String methodName = "getXhbDispMgrMappingRepository";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        if (xhbDispMgrMappingRepository == null) {
            xhbDispMgrMappingRepository = new XhbDispMgrMappingRepository(getEntityManager());
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhbDispMgrMappingRepository;
    }

    public static ICduModel getCduFromDao(XhbDispMgrCduDao dao) {
        final String methodName = "getCduFromDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        ICduModel cdu = new CduModel();
        cdu.setId(Long.valueOf(dao.getId()));
        cdu.setCduNumber(dao.getCduNumber());
        cdu.setMacAddress(dao.getMacAddress());
        cdu.setIpAddress(dao.getIpAddress());
        cdu.setTitle(dao.getTitle());
        cdu.setDescription(dao.getDescription());
        cdu.setLocation(dao.getLocation());
        cdu.setRefresh(dao.getRefresh());
        cdu.setWeighting(dao.getWeighting());
        cdu.setNotification(dao.getNotification());
        cdu.setOfflineIndicator(dao.getOfflineInd());
        cdu.setRagStatus(dao.getRagStatus());
        cdu.setRagStatusDate(dao.getRagStatusDate());
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return cdu;
    }

}
