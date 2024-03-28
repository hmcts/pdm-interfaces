package uk.gov.hmcts.pdm.business.entities.xhbdispmgrserviceaudit;


import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IServiceAudit;


public class XhbDispMgrServiceAuditRepository
    extends AbstractRepository<XhbDispMgrServiceAuditDao> {

    private static final Logger LOG =
        LoggerFactory.getLogger(XhbDispMgrServiceAuditRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    public XhbDispMgrServiceAuditRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrServiceAuditDao> getDaoClass() {
        return XhbDispMgrServiceAuditDao.class;
    }

    /*
     * Converter Method to take in a IServiceAudit and convert to XhbDispMgrServiceAuditDao and then
     * save to the database
     */
    public void saveDao(IServiceAudit serviceAudit) {
        final String methodName = "saveDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbDispMgrServiceAuditDao dao = new XhbDispMgrServiceAuditDao();

        setDaoFromBasicValue(dao, serviceAudit);

        save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    private void setDaoFromBasicValue(XhbDispMgrServiceAuditDao dao, IServiceAudit serviceAudit) {
        final String methodName = "setDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        dao.setFromEndpoint(serviceAudit.getFromEndpoint());
        dao.setToEndpoint(serviceAudit.getToEndpoint());
        dao.setService(serviceAudit.getService());
        dao.setUrl(serviceAudit.getUrl());
        dao.setMessageId(serviceAudit.getMessageId());
        dao.setMessageStatus(serviceAudit.getMessageStatus());
        dao.setMessageRequest(serviceAudit.getMessageRequest());
        dao.setMessageResponse(serviceAudit.getMessageResponse());

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

}
