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

import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayLocationDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RotationSetsDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.display.DisplayAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.display.DisplayCreateCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.display.DisplayDeleteCommand;

import java.util.List;

/**
 * The interface for the DisplayService.
 * 
 * @author harrism
 *
 */
public interface IDisplayService {

    /**
     * Retrieves all court sites.
     * 
     * @return List
     */
    List<XhibitCourtSiteDto> getCourtSites();
    
    /**
     * Retrieves display type from passed in list and id.
     * 
     * @return DisplayTypeDto
     */
    DisplayTypeDto getDisplayType(List<DisplayTypeDto> displayTypes, Integer displayTypeId);
    
    /**
     * Retrieves court site from passed in list and id.
     * 
     * @return XhibitCourtSiteDto
     */
    XhibitCourtSiteDto getCourtSite(List<XhibitCourtSiteDto> courtSites,
        Long xhibitCourtSiteId);

    /**
     * Retrieves rotation set from passed in list and id.
     * 
     * @return RotationSetsDto
     */
    RotationSetsDto getRotationSet(List<RotationSetsDto> rotationSets, Integer rotationSetId);
    
    /**
     * Retrieves all displays by court site id.
     * 
     * @return List
     */
    List<DisplayDto> getDisplays(Long xhibitCourtSiteId, List<DisplayTypeDto> displayTypes,
        List<XhibitCourtSiteDto> xhibitCourtSites, List<RotationSetsDto> rotationSets);

    /**
     * Retrieve the display for the id.
     * 
     * @return DisplayDto
     */
    DisplayDto getDisplay(Integer displayId);

    /**
     * Retrieve the display location for the id.
     * 
     * @return DisplayLocationDto
     */
    DisplayLocationDto getDisplayLocation(Integer displayId);
    
    /**
     * Retrieves all display types.
     * 
     * @return List
     */
    List<DisplayTypeDto> getDisplayTypes();

    /**
     * Retrieves all rotation sets.
     * 
     * @return List
     */
    List<RotationSetsDto> getRotationSets(Integer courtId);

    /**
     * Update display.
     *
     * @param displayAmendCommand the display amend command
     */
    void updateDisplay(DisplayAmendCommand displayAmendCommand);

    /**
     * Create display.
     *
     * @param displayCreateCommand the display create command
     */
    void createDisplay(DisplayCreateCommand displayCreateCommand);
    
    /**
     * Delete display.
     *
     * @param displayDeleteCommand the display delete command
     */
    void deleteDisplay(DisplayDeleteCommand displayDeleteCommand);
}
