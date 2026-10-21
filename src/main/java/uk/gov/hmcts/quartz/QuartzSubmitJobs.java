package uk.gov.hmcts.quartz;

import org.quartz.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.CduRestartJob;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.HousekeepingJob;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.RagStatusSetupJob;

@Configuration
public class QuartzSubmitJobs {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzSubmitJobs.class);
    private static final String RAG_STATUS_SETUP_JOB = "ragStatusSetupJob";
    private static final String HOUSEKEEPING_JOB = "housekeepingJob";
    private static final String CDU_RESTART_JOB = "cduRestartJob";


    /** The rag status update cron. */
    @Value("#{applicationConfiguration.ragStatusUpdateCron}")
    private String ragStatusUpdateCron;

    /** The housekeeping pkg job cron. */
    @Value("#{applicationConfiguration.housekeepingPkgJobCron}")
    private String housekeepingPkgJobCron;
    
    /** The cdu restart job cron. */
    @Value("#{applicationConfiguration.cduRestartJobCron}")
    private String cduRestartJobCron;

    @Bean(name = RAG_STATUS_SETUP_JOB)
    public JobDetailFactoryBean ragStatusSetupJob() {
        return QuartzConfig.createJobDetail(RagStatusSetupJob.class, RAG_STATUS_SETUP_JOB);
    }

    @Bean(name = HOUSEKEEPING_JOB)
    public JobDetailFactoryBean housekeepingJob() {
        return QuartzConfig.createJobDetail(HousekeepingJob.class, HOUSEKEEPING_JOB);
    }
    
    @Bean(name = CDU_RESTART_JOB)
    public JobDetailFactoryBean cduRestartJob() {
        return QuartzConfig.createJobDetail(CduRestartJob.class, CDU_RESTART_JOB);
    }

    // Run the job as specified from ragStatusUpdateCron
    @Bean(name = "ragStatusSetupTrigger")
    public CronTriggerFactoryBean triggerRagStatusSetup(
        @Qualifier(RAG_STATUS_SETUP_JOB) JobDetail jobDetail) {
        if (ragStatusUpdateCron == null) {
            LOG.error("rag.status.update.cron entry missing in xhb_disp_mgr_property");
            return null;
        }
        return QuartzConfig.createCronTrigger(jobDetail, ragStatusUpdateCron, RAG_STATUS_SETUP_JOB);
    }

    // Run the job as specified from housekeepingPkgJobCron
    @Bean(name = "housekeepingTrigger")
    public CronTriggerFactoryBean triggerHousekeeping(
        @Qualifier(HOUSEKEEPING_JOB) JobDetail jobDetail) {
        if (housekeepingPkgJobCron == null) {
            LOG.error("housekeeping.pkg.job.cron entry missing in xhb_disp_mgr_property");
            return null;
        }
        return QuartzConfig.createCronTrigger(jobDetail, housekeepingPkgJobCron, HOUSEKEEPING_JOB);
    }
    
    // Run the job as specified from cduRestartJobCron
    @Bean(name = "cduRestartTrigger")
    public CronTriggerFactoryBean triggerCduRestart(
        @Qualifier(CDU_RESTART_JOB) JobDetail jobDetail) {
        if (cduRestartJobCron == null) {
            LOG.error("cdu.restart.job.cron entry missing in xhb_disp_mgr_property");
            return null;
        }
        return QuartzConfig.createCronTrigger(jobDetail, cduRestartJobCron, CDU_RESTART_JOB);
    }

}
