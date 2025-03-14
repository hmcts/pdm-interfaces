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

package uk.gov.hmcts.pdm.publicdisplay.manager.service.api;

import uk.gov.hmcts.pdm.publicdisplay.common.exception.ServiceException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DashboardCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.ScheduleDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies.LocalProxyAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.proxies.LocalProxyRegisterCommand;

import java.util.List;


/**
 * The interface for the LocalProxyService.
 * 
 * @author scullionm
 *
 */
public interface ILocalProxyService {
    
    /**
     * Retrieves all court sites.
     * 
     * @return List
     */
    List<XhibitCourtSiteDto> getCourtSites();
    
    /**
     * Retrieves all court sites.
     * 
     * @return List
     */
    List<XhibitCourtSiteDto> getCourtSites(Integer courtId);
    
    /**
     * Retrieves all court sites with an associated local proxy.
     * 
     * @return List
     */
    List<XhibitCourtSiteDto> getXhibitCourtSitesWithLocalProxy();

    /**
     * Retrieves all court sites without an associated local proxy.
     * 
     * @return List
     */
    List<XhibitCourtSiteDto> getXhibitCourtSitesWithoutLocalProxy();

    /**
     * Gets the xhibit court sites ordered by rag status.
     *
     * @return the xhibit court sites ordered by rag status
     */
    List<XhibitCourtSiteDto> getXhibitCourtSitesOrderedByRagStatus();

    /**
     * Retrieves a court site by xhibitcourtsiteID.
     * 
     * @param xhibitCourtSiteId as Long
     * @return CourtSiteDto
     */
    CourtSiteDto getCourtSiteByXhibitCourtSiteId(Long xhibitCourtSiteId);

    /**
     * Retrieves a court site by xhibitcourtsiteID.
     * 
     * @param xhibitCourtSiteId as Long
     * @return CourtSiteDto
     */
    DashboardCourtSiteDto getDashboardCourtSiteByXhibitCourtSiteId(Long xhibitCourtSiteId);

    /**
     * Unregister the local proxy associated with courtSiteId.
     * 
     * @param courtSiteId as Long
     * @throws ServiceException as ServiceException
     */
    void unregisterLocalProxy(Long courtSiteId);

    /**
     * Register the local proxy using the supplied command object.
     * 
     * @param localProxyRegisterCommand the local proxy register command object to be used to create
     *        the proxy
     * @throws ServiceException as ServiceException
     */
    void registerLocalProxy(LocalProxyRegisterCommand localProxyRegisterCommand);

    /**
     * Checks if is local proxy with ipAddress.
     *
     * @param ipAddress the ipAddress as long
     * @return true, if is local proxy with ipAddress
     */
    boolean isLocalProxyWithIpAddress(String ipAddress);

    /**
     * Gets the power save schedules.
     *
     * @return the power save schedules
     */
    List<ScheduleDto> getPowerSaveSchedules();

    /**
     * Update local proxy.
     *
     * @param courtSiteDto the court site dto
     * @param localProxyAmendCommand the local proxy amend command
     * @throws ServiceException the service exception
     */
    void updateLocalProxy(CourtSiteDto courtSiteDto, LocalProxyAmendCommand localProxyAmendCommand);

}
