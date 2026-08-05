/*
 * Copyrights and Licenses
 * 
 * Copyright (c) 2015-2016 by the Ministry of Justice. All rights reserved. Redistribution and use
 * in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer. - Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution. - Products derived
 * from this software may not be called "XHIBIT Public Display Manager" nor may
 * "XHIBIT Public Display Manager" appear in their names without prior written permission of the
 * Ministry of Justice. - Redistributions of any form whatsoever must retain the following
 * acknowledgment: "This product includes XHIBIT Public Display Manager." This software is provided
 * "as is" and any expressed or implied warranties, including, but not limited to, the implied
 * warranties of merchantability and fitness for a particular purpose are disclaimed. In no event
 * shall the Ministry of Justice or its contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of
 * substitute goods or services; loss of use, data, or profits; or business interruption). However
 * caused any on any theory of liability, whether in contract, strict liability, or tort (including
 * negligence or otherwise) arising in any way out of the use of this software, even if advised of
 * the possibility of such damage.
 */

package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.RestException;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduStatusJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CourtSiteStatusJson;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRagStatusService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The Class RagStatusService.
 *
 * @author pattersone
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class RagStatusService extends RagStatusRepository implements IRagStatusService {

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";
    private static final Character GREENCHAR = AppConstants.GREEN_CHAR;
    private static final Character AMBERCHAR = AppConstants.AMBER_CHAR;
    private static final Character REDCHAR = AppConstants.RED_CHAR;
    private static final Character NOCHAR = AppConstants.NO_CHAR;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RagStatusService.class);

    /** The Constant for 100%. */
    private static final double ONE_HUNDRED_PERCENT = 100.0d;

    /** The rag status overall red percent. */
    @Value("#{applicationConfiguration.ragStatusOverallRedPercent}")
    private Integer ragStatusOverallRedPercent;

    /** The rag status overall amber percent. */
    @Value("#{applicationConfiguration.ragStatusOverallAmberPercent}")
    private Integer ragStatusOverallAmberPercent;

    /** The rag status court site red percent. */
    @Value("#{applicationConfiguration.ragStatusCourtSiteRedPercent}")
    private Integer ragStatusCourtSiteRedPercent;

    /** The rag status court site amber percent. */
    @Value("#{applicationConfiguration.ragStatusCourtSiteAmberPercent}")
    private Integer ragStatusCourtSiteAmberPercent;

    /** The local proxy rest client. */
    @Autowired
    private ILocalProxyRestClient localProxyRestClient;

    @Value("${localproxy.communication.enabled}")
    private Boolean localProxyCommunicationEnabled;


    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRagStatusService#
     * getRagStatusOverall()
     */
    @Override
    public String getRagStatusOverall() {
        LOGGER.debug("getRagStatusOverall()");
        // Get the weightings of the total and operational cdus
        final long weightingTotal = getXhbDispMgrCduRepository().getCduWeightingTotal();
        final long weightingOperational = getXhbDispMgrCduRepository().getCduWeightingOperational();

        // Calculate the rag status from the weighting percentage of operational cdus
        return getCalculatedRagStatus(weightingOperational, weightingTotal,
            ragStatusOverallAmberPercent, ragStatusOverallRedPercent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRagStatusService#
     * scheduleRagStatusJob(org.quartz. Scheduler,
     * uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto)
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void scheduleRagStatusJob(final Scheduler scheduler,
        final XhibitCourtSiteDto xhibitCourtSite) throws SchedulerException {
        // Create identifiers for the job and trigger using the xhibit court site id
        final JobKey jobKey = new JobKey("ragStatusJob_" + xhibitCourtSite.getId());
        final TriggerKey triggerKey = new TriggerKey("ragStatusTrigger_" + xhibitCourtSite.getId());

        // Default start date of job is now
        Date startDate = new Date();

        // If a trigger already exists with this key and has maybe not yet fired, use
        // its fire time or the court site refresh will be put at the back of the queue.
        // Unfortunately, having a next fire time does not guarantee the current job will
        // run and that is why a new job is scheduled, otherwise court might never refresh.
        final Trigger currentTrigger = scheduler.getTrigger(triggerKey);
        if (currentTrigger != null && currentTrigger.getNextFireTime() != null) {
            startDate = currentTrigger.getNextFireTime();
            LOGGER.info("used existing rag status job fire time for {}",
                xhibitCourtSite.getCourtSiteName());
        }

        // Create a non-durable job which will run once to retrieve the rag status
        final JobDetail job = JobBuilder.newJob(RagStatusJob.class).withIdentity(jobKey)
            .usingJobData("id", xhibitCourtSite.getId()).build();
        final Trigger trigger =
            TriggerBuilder.newTrigger().withIdentity(triggerKey).startAt(startDate).build();

        // Delete any previous version of the rag status job that failed to retrieve status
        if (scheduler.deleteJob(jobKey)) {
            LOGGER.info("deleted previous rag status job for {}",
                xhibitCourtSite.getCourtSiteName());
        }

        scheduler.scheduleJob(job, trigger);
        LOGGER.info("scheduled new rag status job for {}", xhibitCourtSite.getCourtSiteName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRagStatusService#
     * refreshRagStatus (java.lang.Long)
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,
        noRollbackFor = RestException.class)
    public void refreshRagStatus(final Long xhibitCourtSiteId) {
        final String methodName = "refreshRagStatus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Refresh the court site status
        LOGGER.info("Fetching court site to refresh rag status with xhibitCourtSiteId: {}",
            xhibitCourtSiteId);
        final ICourtSite courtSite = getXhbDispMgrCourtSiteRepository()
            .findByXhibitCourtSiteId(xhibitCourtSiteId.intValue());
        LOGGER.info("refreshing rag status for {}",
            courtSite.getXhibitCourtSite().getCourtSiteName());
        refreshCourtSiteStatus(courtSite);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Gets the calculated rag status.
     *
     * @param noOfOperational the number of operational
     * @param total the total
     * @param ragStatusAmberPercent the rag status amber percent
     * @param ragStatusRedPercent the rag status red percent
     * @return the calculated rag status
     */
    private String getCalculatedRagStatus(final long noOfOperational, final long total,
        final Integer ragStatusAmberPercent, final Integer ragStatusRedPercent) {
        final String methodName = "getCalculatedRagStatus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        LOGGER.debug("{}{} - total= {}", METHOD, methodName, total);
        LOGGER.debug("{}{} - no. of operational = {}", METHOD, methodName, noOfOperational);

        // Calculate the weighting percentage of operational cdus
        final double operationalPercent = ONE_HUNDRED_PERCENT * noOfOperational / total;

        // Calculate the rag status from the weighting percentage of operational cdus
        String ragStatus = GREENCHAR.toString();
        if (operationalPercent < ragStatusAmberPercent) {
            ragStatus = AMBERCHAR.toString();
        }
        if (operationalPercent < ragStatusRedPercent) {
            ragStatus = REDCHAR.toString();
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return ragStatus;
    }

    /**
     * Update cdu status.
     *
     * @param cdu the cdu
     * @param ragStatus the rag status
     * @param ragStatusDate the rag status date
     */
    private void updateCduStatus(final ICduModel cdu, final Character ragStatus,
        final LocalDateTime ragStatusDate) {
        final String methodName = "updateCduStatus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        cdu.setRagStatus(ragStatus);
        if (ragStatusDate != null) {
            cdu.setRagStatusDate(ragStatusDate);
        }
        getXhbDispMgrCduRepository().updateDaoFromBasicValue(cdu);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Update cdu status.
     *
     * @param localProxy the local proxy
     * @param ragStatus the rag status
     * @param ragStatusDate the rag status date
     */
    private void updateCduStatus(final ILocalProxy localProxy, final Character ragStatus,
        final LocalDateTime ragStatusDate) {
        final String methodName = "updateCduStatus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        // Loop through the cdus associated to this local proxy
        for (ICduModel cdu : localProxy.getCourtSite().getCdus()) {
            updateCduStatus(cdu, ragStatus, ragStatusDate);
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Update local proxy status.
     *
     * @param localProxy the local proxy
     * @param ragStatus the rag status
     * @param ragStatusDate the rag status date
     */
    private void updateLocalProxyStatus(final ILocalProxy localProxy, final String ragStatus,
        final LocalDateTime ragStatusDate) {
        final String methodName = "updateLocalProxyStatus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        localProxy.setRagStatus(ragStatus);
        if (ragStatusDate != null) {
            localProxy.setRagStatusDate(ragStatusDate);
        }
        getXhbDispMgrLocalProxyRepository().updateDaoFromBasicValue(localProxy);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Update court site status.
     *
     * @param courtSite the court site
     * @param ragStatus the rag status
     * @param ragStatusDate the rag status date
     */
    public void updateCourtSiteStatus(final ICourtSite courtSite, final String ragStatus,
        final LocalDateTime ragStatusDate) {
        final String methodName = "updateCourtSiteStatus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        courtSite.setRagStatus(ragStatus);
        if (ragStatusDate != null) {
            courtSite.setRagStatusDate(ragStatusDate);
        }
        getXhbDispMgrCourtSiteRepository().updateDaoFromBasicValue(courtSite);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Refresh court site status.
     *
     * @param courtSite the court site
     */
    private void refreshCourtSiteStatus(final ICourtSite courtSite) {
        final String methodName = "refreshCourtSiteStatus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        final ILocalProxy localProxy = courtSite.getLocalProxy();
        String newRagStatus = null;
        CourtSiteStatusJson courtSiteStatus = new CourtSiteStatusJson();
        try {
            long noOfOperationalCdus = 0L;
            long noOfOnlineCdus = 0L;

            // Get the list of cdus with their latest rag status
            if (localProxyCommunicationEnabled) {
                courtSiteStatus = localProxyRestClient.getCourtSiteStatus(localProxy);
            } else {
                // Dummy data for now as we don't have an ipdmanager instance
                CduStatusJson dummyCdu = new CduStatusJson();
                dummyCdu.setGeneratedBy("XHIBIT");
                dummyCdu.setMacAddress("00:00:00:00:00:01");
                dummyCdu.setRagStatus(GREENCHAR);
                List<CduStatusJson> dummyCdus = new ArrayList<>();
                dummyCdus.add(dummyCdu);
                courtSiteStatus.setCdus(dummyCdus);
                courtSiteStatus.setGeneratedBy("XHIBIT");
                courtSiteStatus.setRagStatus(GREENCHAR);
            }

            // Update the local proxy rag status
            updateLocalProxyStatus(localProxy, courtSiteStatus.getRagStatus().toString(),
                LocalDateTime.now());

            // Loop through each cdu selected by MacAddress and update the RagStatus
            for (CduStatusJson cduStatus : courtSiteStatus.getCdus()) {
                // Get the latest cdu object (as other users may have updated it)
                final ICduModel cdu =
                    getXhbDispMgrCduRepository().findByMacAddress(cduStatus.getMacAddress());

                // Only process if the cdu is registered centrally
                if (cdu != null) {
                    // Update the rag status of the cdu
                    updateCduStatus(cdu, cduStatus.getRagStatus(), LocalDateTime.now());

                    // Increment the number of Cdus
                    if (NOCHAR.equals(cdu.getOfflineIndicator())) {
                        noOfOnlineCdus += 1;
                    }

                    // Increment the number of operational cdus
                    if (NOCHAR.equals(cdu.getOfflineIndicator())
                        && GREENCHAR.equals(cduStatus.getRagStatus())) {
                        noOfOperationalCdus += 1;
                    }
                } else {
                    LOGGER.warn(
                        "{}{} - received a ragstatus for a cdu that is not registered centrally",
                        METHOD, methodName);
                }
            }

            // Calculate the court site RAG status
            newRagStatus = getCalculatedRagStatus(noOfOperationalCdus, noOfOnlineCdus,
                ragStatusCourtSiteAmberPercent, ragStatusCourtSiteRedPercent);

            // Update the court site RAG status
            updateCourtSiteStatus(courtSite, newRagStatus, LocalDateTime.now());
        } finally {
            // If exception prevented refresh of rag status from the local proxy,
            // set everything to red but do NOT update the date as that is used
            // to identify when the rag status was last successfully updated
            // from a successful response from the local proxy
            if (newRagStatus == null) {
                updateCduStatus(localProxy, REDCHAR, null);
                updateLocalProxyStatus(localProxy, REDCHAR.toString(), null);
                updateCourtSiteStatus(courtSite, REDCHAR.toString(), null);
            }
        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

}
