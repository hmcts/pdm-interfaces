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

import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefJudgeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judge.JudgeAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judge.JudgeCreateCommand;

import java.util.List;

/**
 * The interface for the JudgeService.
 * 
 * @author toftn
 *
 */
public interface IRefJudgeService {

    /**
     * Retrieves all court sites.
     * 
     * @return List
     */
    List<XhibitCourtSiteDto> getCourtSites();

    /**
     * Retrieves all judges by court site id.
     * 
     * @return List
     */
    List<RefJudgeDto> getJudges(Long xhibitCourtSiteId);

    /**
     * Retrieves a judge by ref judge id.
     * 
     * @return RefJudgeDto
     */
    RefJudgeDto getJudge(Integer refJudgeId);
    
    /**
     * Retrieves all judge types by court site id.
     * 
     * @return List
     */
    List<RefSystemCodeDto> getJudgeTypes(Long xhibitCourtSiteId);

    /**
     * Retrieves a judge type by refJudgeDto's courtId.
     * 
     * @return XhbRefSystemCodeDao
     */
    XhbRefSystemCodeDao getJudgeType(RefJudgeDto refJudgeDto);
    
    /**
     * Update judge.
     *
     * @param judgeAmendCommand the judge amend command
     */
    void updateJudge(JudgeAmendCommand judgeAmendCommand);
    
    /**
     * Create judge.
     *
     * @param judgeCreateCommand the judge create command
     * @param courtId courtId
     */
    void createJudge(JudgeCreateCommand judgeCreateCommand, Integer courtId);
}
