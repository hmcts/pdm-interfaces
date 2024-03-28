package uk.gov.hmcts.pdm.business.entities.xhbdispmgrhousekeeping;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;



public class XhbDispMgrHousekeepingRepository extends AbstractRepository<XhbDispMgrHousekeepingDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrHousekeepingRepository.class);
    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";
    protected static final String UNCHECKED = "unchecked";  
    protected static final String PKG_NAME = "xhb_disp_mgr_housekeeping_pkg";
    protected static final String QUERY = "call " + PKG_NAME + ".initiate_run()";


    public XhbDispMgrHousekeepingRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrHousekeepingDao> getDaoClass() {
        return XhbDispMgrHousekeepingDao.class;
    }

    public void callHousekeeping() {
        final String methodName = "callHousekeeping";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        LOG.info("Calling {}", PKG_NAME);
        Query query = getEntityManager().createNativeQuery(QUERY);
        getEntityManager().getTransaction().begin();
        query.executeUpdate();
        getEntityManager().getTransaction().commit();
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }
    
}
