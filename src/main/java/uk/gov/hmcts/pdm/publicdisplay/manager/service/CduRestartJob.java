package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.pdm.business.entities.xhbconfigprop.XhbConfigPropDao;
import uk.gov.hmcts.pdm.business.entities.xhbconfigprop.XhbConfigPropRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService;

import java.util.List;

/**
 * Quartz job which runs at a specified time each day to restart all cdu's at specified court sites.
 * 
 * @author gittinsl
 *
 */
@DisallowConcurrentExecution
public class CduRestartJob implements Job {
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CduRestartJob.class);
    
    private static final String SITES_TO_RESTART_CDUS = "SITES_TO_RESTART_CDUS";
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private XhbConfigPropRepository xhbConfigPropRepository;

    /** The cdu service. */
    @Autowired
    private ICduService cduService;

    /*
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("execute() starts ...");
        
        // 1) Get the list of court sites to restart cdu's
        String[] courtSiteIds = getCourtSiteIds();
        
        // 2) Loop through the string[] of courtSiteIds
        for (String courtSiteId : courtSiteIds) {
            LOGGER.info("Fetching Cdu's for courtSiteId: {}", courtSiteId);
            
            // 3) Get the list of cdu's for this court site id
            List<CduDto> cdusForSite =
                cduService.getCdusBySiteID(Long.valueOf(courtSiteId.trim()));
            
            if (!cdusForSite.isEmpty()) {
                LOGGER.info("Cdu's populated for site: {}, total: {}",
                    courtSiteId, cdusForSite.size());
                
                // 4) Restart the cdu's for this site
                // cduService.restartCdus()
            }
        }
        LOGGER.info("execute() ends ...");
    }
    
    private String[] getCourtSiteIds() {
        List<XhbConfigPropDao> sitesToRestartCdus = 
            getXhbConfigPropRepository().findByPropertyNameSafe(SITES_TO_RESTART_CDUS);
        
        if (sitesToRestartCdus != null) {
            return sitesToRestartCdus.get(0).getPropertyValue().split(",");
        }
        return new String[0];
    }
    
    private XhbConfigPropRepository getXhbConfigPropRepository() {
        if (xhbConfigPropRepository == null) {
            xhbConfigPropRepository = new XhbConfigPropRepository(getEntityManager());
        }
        return xhbConfigPropRepository;
    }
    
    private EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }
}
