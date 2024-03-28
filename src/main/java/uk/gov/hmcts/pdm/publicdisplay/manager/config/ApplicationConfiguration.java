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

package uk.gov.hmcts.pdm.publicdisplay.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IPropertyService;

import java.util.Arrays;
import java.util.List;

/**
 * The Class ApplicationConfiguration.
 *
 * @author uphillj
 */
@Component
public class ApplicationConfiguration {

    /** The property service. */
    @Autowired
    private IPropertyService propertyService;

    /**
     * Get the software update directory.
     *
     * @return the software update directory
     */
    public String getSoftwareUpdateDirectory() {
        return propertyService.getPropertyValueByName("software.update.directory");
    }

    /**
     * Gets the software update filenames.
     *
     * @return the software update filenames
     */
    public List<String> getSoftwareUpdateFilenames() {
        final String filenames =
            propertyService.getPropertyValueByName("software.update.filenames");
        return Arrays.asList(filenames.split(","));
    }

    /**
     * Gets the software update checksum cron.
     *
     * @return the software update checksum cron
     */
    public String getSoftwareUpdateChecksumCron() {
        return propertyService.getPropertyValueByName("software.update.checksum.cron");
    }

    /**
     * Gets the xhibit public display url.
     *
     * @return the xhibit public display url
     */
    public String getXhibitPublicDisplayUrl() {
        return propertyService.getPropertyValueByName("xhibit.public.display.url");
    }

    /**
     * Gets the rest client timeout.
     *
     * @return the rest client timeout
     */
    public Integer getRestClientTimeout() {
        return Integer.valueOf(propertyService.getPropertyValueByName("rest.client.timeout"));
    }

    /**
     * Gets the rest token expiry.
     *
     * @return the rest token expiry
     */
    public Integer getRestTokenExpiry() {
        return Integer.valueOf(propertyService.getPropertyValueByName("rest.token.expiry"));
    }

    /**
     * Gets the register cdu network.
     *
     * @return the register cdu network
     */
    public String getRegisterCduNetwork() {
        return propertyService.getPropertyValueByName("register.cdu.network");
    }

    /**
     * Gets the register cdu host min.
     *
     * @return the register cdu host min
     */
    public Integer getRegisterCduHostMin() {
        return Integer.valueOf(propertyService.getPropertyValueByName("register.cdu.host.min"));
    }

    /**
     * Gets the register cdu host max.
     *
     * @return the register cdu host max
     */
    public Integer getRegisterCduHostMax() {
        return Integer.valueOf(propertyService.getPropertyValueByName("register.cdu.host.max"));
    }

    /**
     * Gets the rag status overall red percent.
     *
     * @return the rag status overall red percent
     */
    public Integer getRagStatusOverallRedPercent() {
        return Integer
            .valueOf(propertyService.getPropertyValueByName("rag.status.overall.red.percent"));
    }

    /**
     * Gets the rag status overall amber percent.
     *
     * @return the rag status overall amber percent
     */
    public Integer getRagStatusOverallAmberPercent() {
        return Integer
            .valueOf(propertyService.getPropertyValueByName("rag.status.overall.amber.percent"));
    }

    /**
     * Gets the rag status court site red percent.
     *
     * @return the rag status court site red percent
     */
    public Integer getRagStatusCourtSiteRedPercent() {
        return Integer
            .valueOf(propertyService.getPropertyValueByName("rag.status.courtsite.red.percent"));
    }

    /**
     * Gets the rag status court site amber percent.
     *
     * @return the rag status court site amber percent
     */
    public Integer getRagStatusCourtSiteAmberPercent() {
        return Integer
            .valueOf(propertyService.getPropertyValueByName("rag.status.courtsite.amber.percent"));
    }

    /**
     * Gets the rag status update threads.
     *
     * @return the rag status update threads
     */
    public String getRagStatusUpdateThreads() {
        return propertyService.getPropertyValueByName("rag.status.update.threads");
    }

    /**
     * Gets the rag status update cron.
     *
     * @return the rag status update cron
     */
    public String getRagStatusUpdateCron() {
        return propertyService.getPropertyValueByName("rag.status.update.cron");
    }

    /**
     * Gets the rag status reload interval.
     *
     * @return the rag status reload interval
     */
    public String getRagStatusReloadInterval() {
        return propertyService.getPropertyValueByName("rag.status.reload.interval");
    }

    /**
     * Gets the housekeeping job cron.
     *
     * @return the housekeeping job cron
     */
    public String getHousekeepingPkgJobCron() {
        return propertyService.getPropertyValueByName("housekeeping.pkg.job.cron");
    }
}
