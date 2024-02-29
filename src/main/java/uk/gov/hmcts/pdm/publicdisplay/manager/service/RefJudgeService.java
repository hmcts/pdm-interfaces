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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbrefjudge.XhbRefJudgeDao;
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefJudgeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRefJudgeService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judge.JudgeAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judge.JudgeCreateCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judge.JudgeDeleteCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The Class RefJudgeService.
 *
 * @author toftn
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class RefJudgeService extends RefJudgeServiceFinder implements IRefJudgeService {

    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RefJudgeService.class);
    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String FOUR_PARAMS = "{}{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";
    private static final String YES = "Y";
    private static final String NO = "N";

    /**
     * Gets the court sites.
     *
     * @return the court sites
     */
    @Override
    public List<XhibitCourtSiteDto> getCourtSites() {
        final String methodName = "getCourtSites";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<XhibitCourtSiteDto> resultList = new ArrayList<>();
        final List<XhbCourtSiteDao> xhibitCourtSiteList = getXhbCourtSiteRepository().findAll();
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Court sites returned : ",
            xhibitCourtSiteList.size());

        if (!xhibitCourtSiteList.isEmpty()) {
            // Transfer each court site to a dto and save in resultList
            for (XhbCourtSiteDao xhibitCourtSite : xhibitCourtSiteList) {
                if (YES.equals(xhibitCourtSite.getObsInd())) {
                    continue;
                }
                LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - transferring court site to dto");
                final XhibitCourtSiteDto dto = createXhibitCourtSiteDto();

                // need the court site details from the main court site in 'xhb_court_site' table
                dto.setId(xhibitCourtSite.getId().longValue());
                dto.setCourtSiteName(xhibitCourtSite.getCourtSiteName());
                dto.setCourtSiteCode(xhibitCourtSite.getCourtSiteCode());
                dto.setCourtId(xhibitCourtSite.getCourtId());
                LOGGER.debug("dto id : {}", dto.getId());
                LOGGER.debug("dto courtSiteName: {}", dto.getCourtSiteName());
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getCourtSiteName(), obj2.getCourtSiteName()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }
    
    /**
     * Gets the judges by court site id.
     *
     * @return List
     */
    @Override
    public List<RefJudgeDto> getJudges(Long xhibitCourtSiteId) {
        final String methodName = "getJudges";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<RefJudgeDto> resultList = new ArrayList<>();
        final List<XhbRefJudgeDao> xhbRefJudgeList =
            getXhbRefJudgeRepository().findByCourtSiteId(xhibitCourtSiteId.intValue());
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Judges returned : ",
            xhbRefJudgeList.size());

        if (!xhbRefJudgeList.isEmpty()) {
            for (XhbRefJudgeDao xhbRefJudge : xhbRefJudgeList) {
                final RefJudgeDto dto = createRefJudgeDto();
                dto.setCourtId(xhbRefJudge.getCourtId());
                dto.setCrestJudgeId(xhbRefJudge.getCrestJudgeId());
                dto.setFirstName(xhbRefJudge.getFirstName());
                dto.setFullListTitle1(xhbRefJudge.getFullListTitle1());
                dto.setFullListTitle2(xhbRefJudge.getFullListTitle2());
                dto.setFullListTitle3(xhbRefJudge.getFullListTitle3());
                dto.setHonours(xhbRefJudge.getHonours());
                dto.setInitials(xhbRefJudge.getInitials());
                dto.setJudgeType(xhbRefJudge.getJudgeType());
                dto.setJudVers(xhbRefJudge.getJudVers());
                dto.setMiddleName(xhbRefJudge.getMiddleName());
                dto.setObsInd(xhbRefJudge.getObsInd());
                dto.setRefJudgeId(xhbRefJudge.getRefJudgeId());
                dto.setSourceTable(xhbRefJudge.getSourceTable());
                dto.setStatsCode(xhbRefJudge.getStatsCode());
                dto.setSurname(xhbRefJudge.getSurname());
                dto.setTitle(xhbRefJudge.getTitle());
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getSurname(), obj2.getSurname()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }
    
    /**
     * Gets the judge types by court site id.
     *
     * @return List
     */
    @Override
    public List<RefSystemCodeDto> getJudgeTypes(Long xhibitCourtSiteId) {
        final String methodName = "getJudgeTypes";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<RefSystemCodeDto> resultList = new ArrayList<>();
        final List<XhbRefSystemCodeDao> xhbRefJudgeTypeList =
            getXhbRefSystemCodeRepository().findJudgeTypeByCourtSiteId(xhibitCourtSiteId.intValue());
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Judge types returned : ",
            xhbRefJudgeTypeList.size());

        if (!xhbRefJudgeTypeList.isEmpty()) {
            for (XhbRefSystemCodeDao xhbRefJudgeType : xhbRefJudgeTypeList) {
                final RefSystemCodeDto dto = createRefSystemCodeDto();
                dto.setCode(xhbRefJudgeType.getCode());
                dto.setCodeTitle(xhbRefJudgeType.getCodeTitle());
                dto.setCodeType(xhbRefJudgeType.getCodeType());
                dto.setCourtId(xhbRefJudgeType.getCourtId());
                dto.setCreatedBy(xhbRefJudgeType.getCreatedBy());
                dto.setCreationDate(xhbRefJudgeType.getCreationDate());
                dto.setDeCode(xhbRefJudgeType.getDeCode());
                dto.setLastUpdateDate(xhbRefJudgeType.getLastUpdateDate());
                dto.setLastUpdatedBy(xhbRefJudgeType.getLastUpdatedBy());
                dto.setObsInd(xhbRefJudgeType.getObsInd());
                dto.setRefCodeOrder(xhbRefJudgeType.getRefCodeOrder());
                dto.setRefSystemCodeId(xhbRefJudgeType.getRefSystemCodeId());
                dto.setVersion(xhbRefJudgeType.getVersion());
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getCode(), obj2.getCode()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }
    
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateJudge(final JudgeAmendCommand command) {

        final String methodName = "updateJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbRefJudgeDao> existingDao =
            getXhbRefJudgeRepository().findById(command.getRefJudgeId());
        if (existingDao.isPresent()) {
            XhbRefJudgeDao judgeDao = existingDao.get();

            if (!(command instanceof JudgeDeleteCommand)) {
                judgeDao.setFirstName(command.getFirstName());
                judgeDao.setMiddleName(command.getMiddleName());
                judgeDao.setSurname(command.getSurname());
                judgeDao.setTitle(command.getTitle());
                judgeDao.setFullListTitle1(command.getFullListTitle1());
                judgeDao.setJudgeType(command.getJudgeType());
            }
            judgeDao.setObsInd(command.getObsInd());

            // Update the existing record
            getXhbRefJudgeRepository().updateDao(judgeDao);
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }
    
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createJudge(final JudgeCreateCommand command, Integer courtId) {

        final String methodName = "createJudge";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Create the XhbRefJudgeDao
        XhbRefJudgeDao judgeDao = new XhbRefJudgeDao();
        judgeDao.setCourtId(courtId);
        judgeDao.setFirstName(command.getFirstName());
        judgeDao.setMiddleName(command.getMiddleName());
        judgeDao.setSurname(command.getSurname());
        judgeDao.setTitle(command.getTitle());
        judgeDao.setFullListTitle1(command.getFullListTitle1());
        judgeDao.setJudgeType(command.getJudgeType());
        judgeDao.setObsInd(NO);
        
        // Save
        getXhbRefJudgeRepository().saveDao(judgeDao);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }
}
