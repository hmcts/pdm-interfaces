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
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRefJudgeTypeService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype.JudgeTypeAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype.JudgeTypeCreateCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * The Class RefJudgeTypeService.
 *
 * @author toftn
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class RefJudgeTypeService extends RefJudgeTypeServiceFinder implements IRefJudgeTypeService {

    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RefJudgeTypeService.class);
    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String FOUR_PARAMS = "{}{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";
    private static final String NO = "N";
    private static final String YES = "Y";
    private static final String CODE_TYPE = "JUDGE_TYPE";

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
     * Gets the judge types by court site id.
     *
     * @return List
     */
    @Override
    public List<RefSystemCodeDto> getJudgeTypes(Long xhibitCourtSiteId) {
        final String methodName = "getJudgeTypes";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<RefSystemCodeDto> resultList = new ArrayList<>();
        final List<XhbRefSystemCodeDao> xhbRefJudgeTypeList = getXhbRefSystemCodeRepository()
            .findJudgeTypeByCourtSiteId(xhibitCourtSiteId.intValue());
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Judge types returned : ",
            xhbRefJudgeTypeList.size());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return getJudgeTypeResultList(xhbRefJudgeTypeList, resultList);
    }
    
    /**
     * Gets the judge types by court id.
     *
     * @return List of RefSystemCodeDto
     */
    @Override
    public List<RefSystemCodeDto> getJudgeTypesByCourtId(Integer courtId) {
        final String methodName = "getJudgeTypesByCourtId";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<RefSystemCodeDto> resultList = new ArrayList<>();
        final List<XhbRefSystemCodeDao> xhbRefJudgeTypeList =
            getXhbRefSystemCodeRepository().findByCourtId(courtId);
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Judge Types returned : ",
            xhbRefJudgeTypeList.size());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return getJudgeTypeResultList(xhbRefJudgeTypeList, resultList);
    }

    /**
     * Create the judge type record.
     */
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createJudgeType(final JudgeTypeCreateCommand command, Integer courtId) {

        final String methodName = "createJudgeType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Create the courtSiteDao
        XhbRefSystemCodeDao dao = new XhbRefSystemCodeDao();
        dao.setCode(command.getCode());
        dao.setCodeType(CODE_TYPE);
        dao.setCourtId(courtId);
        dao.setDeCode(command.getDescription().toUpperCase(Locale.getDefault()));
        dao.setObsInd(NO);

        // Save
        getXhbRefSystemCodeRepository().saveDao(dao);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Update the judge type record.
     */
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateJudgeType(final JudgeTypeAmendCommand command) {

        final String methodName = "updateJudgeType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Get the existing record
        Optional<XhbRefSystemCodeDao> existingDao =
            getXhbRefSystemCodeRepository().findById(command.getRefSystemCodeId());
        if (existingDao.isPresent()) {
            XhbRefSystemCodeDao dao = existingDao.get();

            // Update the existing fields
            dao.setDeCode(command.getDescription().toUpperCase(Locale.getDefault()));
            dao.setObsInd(command.getObsInd());

            // Update record
            getXhbRefSystemCodeRepository().updateDao(dao);
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }
    
    /**
     * Gets the judge type by id.
     *
     * @return RefSystemCodeDto
     */
    @Override
    public RefSystemCodeDto getJudgeType(Integer refSystemCodeId) {
        final String methodName = "getJudgeType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final Optional<XhbRefSystemCodeDao> dao =
            getXhbRefSystemCodeRepository().findById(refSystemCodeId);
        RefSystemCodeDto result = null;
        if (dao.isPresent()) {
            LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - Judge Type found");
            result = getDto(dao.get());
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }
    
    private RefSystemCodeDto getDto(final XhbRefSystemCodeDao xhbRefJudgeType) {
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
        return dto;
    }
    
    private List<RefSystemCodeDto> getJudgeTypeResultList(List<XhbRefSystemCodeDao> xhbRefJudgeTypeList,
        List<RefSystemCodeDto> resultList) {
        if (!xhbRefJudgeTypeList.isEmpty()) {
            for (XhbRefSystemCodeDao xhbRefJudgeType : xhbRefJudgeTypeList) {
                final RefSystemCodeDto dto = getDto(xhbRefJudgeType);
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getCode(), obj2.getCode()));
        }
        return resultList;
    }
}
