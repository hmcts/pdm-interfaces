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
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduRegisterCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.MappingCommand;

import java.util.List;


/**
 * The Interface ICduService.
 *
 * @author groenm
 */
public interface ICduService {
    /**
     * Checks if a cdu is using the mac address.
     *
     * @param macAddress the mac address
     * @return true, if a cdu is using the mac address
     */
    boolean isCduWithMacAddress(String macAddress);

    /**
     * Checks if a cdu is using the cduNumber.
     *
     * @param cduNumber the cduNumber
     * @return true, if a cdu is using the cduNumber
     */
    boolean isCduWithCduNumber(String cduNumber);

    /**
     * Gets the cdu by cduId.
     *
     * @param cduId the cdu id
     * @return the CduDto
     */
    CduDto getCduByCduId(int cduId);
    
    /**
     * Gets the cdu by mac address.
     *
     * @param macAddress the mac address
     * @return the cdu by mac address
     */
    CduDto getCduByMacAddress(String macAddress);

    /**
     * Allows Partial matching on mac address and returns a list of cdu objects.
     * 
     * @param macAddress the partial mac address
     * @return list of cdu objects
     */
    List<CduDto> getCduByMacAddressWithLike(String macAddress);

    /**
     * Gets the cdus by site ID.
     *
     * @param courtSiteId the court site id
     * @return the cdus by site ID
     */
    List<CduDto> getCdusBySiteID(Long courtSiteId);

    /**
     * Creates a CduDto using CduRegisterCommand.
     *
     * @param cduRegisterCommand the cdu register command
     * @return the CduDto
     */
    CduDto createCduDtoFromRegisterCommand(CduRegisterCommand cduRegisterCommand);
    
    /**
     * Register cdu.
     *
     * @param cduDto the cdu dto
     * @param cduCommand the cdu command
     * @throws ServiceException the service exception
     */
    void registerCdu(CduDto cduDto, CduRegisterCommand cduCommand);

    /**
     * Unregister cdu.
     *
     * @param cduId the cdu id
     * @throws ServiceException the service exception
     */
    void unregisterCdu(Long cduId);

    /**
     * Adds the mapping.
     *
     * @param mappingCommand the mapping command
     * @throws ServiceException the service exception
     */
    void addMapping(MappingCommand mappingCommand);

    /**
     * Removes the mapping.
     *
     * @param mappingCommand the mapping command
     * @throws ServiceException the service exception
     */
    void removeMapping(MappingCommand mappingCommand);

    /**
     * Restart cdu.
     *
     * @param cdus the cdus
     */
    void restartCdu(List<CduDto> cdus);

    /**
     * Gets the cdu screenshot.
     *
     * @param cduDto the cdu dto
     * @return the cdu screenshot
     * @throws ServiceException the service exception
     */
    byte[] getCduScreenshot(CduDto cduDto);

    /**
     * Update cdu.
     *
     * @param cduDto the cdu dto
     * @param cduCommand the cdu command
     * @throws ServiceException the service exception
     */
    void updateCdu(CduDto cduDto, CduAmendCommand cduCommand);
}
