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

package uk.gov.hmcts.pdm.publicdisplay.manager.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Simple transfer object used to send court site details to the dashboard from json ajax calls.
 * N.B. Any sensitive data MUST be encrypted using the EncryptDecryptUtility because
 * the @EncryptedFormat annotation does not work with json responses.
 * 
 * @author harrism
 *
 */
public class DashboardCourtSiteDto {
    /** The RAG status (R, A or G). */
    private String ragStatus;

    /** The local proxy. */
    private DashboardLocalProxyDto localProxy;

    /** The cdus. */
    private List<DashboardCduDto> cdus;

    /** The last refresh date. */
    private LocalDateTime lastRefreshDate;

    /**
     * getLocalProxy.
     * 
     * @return the localProxy
     */
    public DashboardLocalProxyDto getLocalProxy() {
        return localProxy;
    }

    /**
     * setLocalProxy.
     * 
     * @param localProxy the localProxy to set
     */
    public void setLocalProxy(final DashboardLocalProxyDto localProxy) {
        this.localProxy = localProxy;
    }

    /**
     * getCdus.
     * 
     * @return the cdus
     */
    public List<DashboardCduDto> getCdus() {
        return cdus;
    }

    /**
     * setCdus.
     * 
     * @param cdus the cdus to set
     */
    public void setCdus(final List<DashboardCduDto> cdus) {
        this.cdus = cdus;
    }

    /**
     * getRagStatus.
     * 
     * @return the ragStatus
     */
    public String getRagStatus() {
        return ragStatus;
    }

    /**
     * setRagStatus.
     * 
     * @param ragStatus the ragStatus to set
     */
    public void setRagStatus(final String ragStatus) {
        this.ragStatus = ragStatus;
    }

    /**
     * getLastRefreshDate.
     * 
     * @return the lastRefreshDate
     */
    public LocalDateTime getLastRefreshDate() {
        return lastRefreshDate;
    }

    /**
     * setLastRefreshDate.
     * 
     * @param lastRefreshDate the lastRefreshDate to set
     */
    public void setLastRefreshDate(final LocalDateTime lastRefreshDate) {
        this.lastRefreshDate = lastRefreshDate;
    }

}
