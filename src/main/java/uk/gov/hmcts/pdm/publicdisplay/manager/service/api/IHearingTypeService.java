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

import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing.HearingTypeAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing.HearingTypeCreateCommand;

import java.util.List;

/**
 * The interface for the HearingTypeService.
 * 
 * @author gittinsl
 *
 */
public interface IHearingTypeService {

    /**
     * Retrieves all court sites.
     * 
     * @return List
     */
    List<XhibitCourtSiteDto> getCourtSites();

    /**
     * Retrieves all hearing types for the courtSiteId.
     * 
     * @return List
     */
    List<HearingTypeDto> getHearingTypes(Long courtSiteId);
    
    /**
     * Retrieves all hearing types for the courtId.
     * 
     * @return List
     */
    List<HearingTypeDto> getHearingTypesByCourtId(Integer courtId);

    /**
     * Retrieve the hearing type for the id.
     * 
     * @return HearingTypeDto
     */
    HearingTypeDto getHearingType(Integer refHearingTypeId);

    /**
     * Retrieves all categories.
     * 
     * @return List
     */
    List<String> getAllCategories();

    /**
     * Update Hearing Type.
     *
     * @param hearingTypeAmendCommand the hearing type amend command
     */
    void updateHearingType(HearingTypeAmendCommand hearingTypeAmendCommand);

    /**
     * Create Hearing Type.
     *
     * @param hearingTypeCreateCommand the hearing type create command
     * @param courtId the Id of the court
     */
    void createHearingType(HearingTypeCreateCommand hearingTypeCreateCommand, Integer courtId);

}
