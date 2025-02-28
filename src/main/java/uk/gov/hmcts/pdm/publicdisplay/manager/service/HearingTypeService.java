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
import uk.gov.hmcts.pdm.business.entities.xhbrefhearingtype.XhbRefHearingTypeDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IHearingTypeService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing.HearingTypeAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing.HearingTypeCreateCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The Class HearingTypeService.
 *
 * @author gittinsl
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class HearingTypeService extends HearingTypeServiceFinder implements IHearingTypeService {

    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HearingTypeService.class);
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
     * Gets the hearing types by court site id.
     *
     * @return List of HearingTypeDto
     */
    @Override
    public List<HearingTypeDto> getHearingTypes(Long courtSiteId) {
        final String methodName = "getHearingTypes";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<HearingTypeDto> resultList = new ArrayList<>();
        final List<XhbRefHearingTypeDao> xhbHearingList =
            getXhbRefHearingTypeRepository().findByCourtSiteId(courtSiteId.intValue());
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Hearing Types returned : ",
            xhbHearingList.size());

        if (!xhbHearingList.isEmpty()) {
            for (XhbRefHearingTypeDao xhbRefHearingType : xhbHearingList) {
                final HearingTypeDto dto = getDto(xhbRefHearingType);
                resultList.add(dto);
            }
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }
    
    /**
     * Gets the hearing types by court id.
     *
     * @return List of HearingTypeDto
     */
    @Override
    public List<HearingTypeDto> getHearingTypesByCourtId(Integer courtId) {
        final String methodName = "getHearingTypesByCourtId";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<HearingTypeDto> resultList = new ArrayList<>();
        final List<XhbRefHearingTypeDao> xhbHearingList =
            getXhbRefHearingTypeRepository().findByCourtId(courtId);
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Hearing Types returned : ",
            xhbHearingList.size());

        if (!xhbHearingList.isEmpty()) {
            for (XhbRefHearingTypeDao xhbRefHearingType : xhbHearingList) {
                final HearingTypeDto dto = getDto(xhbRefHearingType);
                resultList.add(dto);
            }
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }

    /**
     * Gets the hearing type categories.
     *
     * @return List of String
     */
    @Override
    public List<String> getAllCategories() {
        final String methodName = "getAllCategories";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<String> resultList = getXhbRefHearingTypeRepository().findAllCategories();
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Category Types returned : ",
            resultList.size());
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }

    /**
     * Updates the selected hearing type.
     *
     */
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateHearingType(final HearingTypeAmendCommand command) {
        final String methodName = "updateHearingType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbRefHearingTypeDao> existingDao =
            getXhbRefHearingTypeRepository().findById(command.getRefHearingTypeId());
        if (existingDao.isPresent()) {
            XhbRefHearingTypeDao hearingTypeDao = existingDao.get();

            // Update the hearingTypeDao
            hearingTypeDao.setHearingTypeDesc(command.getHearingTypeDesc());
            hearingTypeDao.setCategory(command.getCategory());
            hearingTypeDao.setObsInd(command.getObsInd());

            // Update the existing record
            getXhbRefHearingTypeRepository().updateDao(hearingTypeDao);
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createHearingType(final HearingTypeCreateCommand command, Integer courtId) {

        final String methodName = "createHearingType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Create the hearingTypeDao
        XhbRefHearingTypeDao hearingTypeDao = new XhbRefHearingTypeDao();
        hearingTypeDao.setHearingTypeCode(command.getHearingTypeCode());
        hearingTypeDao.setHearingTypeDesc(command.getHearingTypeDesc());
        hearingTypeDao.setCategory(command.getCategory());
        hearingTypeDao.setCourtId(courtId);
        hearingTypeDao.setObsInd(NO);

        // Save
        getXhbRefHearingTypeRepository().saveDao(hearingTypeDao);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Gets the hearing type by id.
     *
     * @return HearingTypeDto
     */
    @Override
    public HearingTypeDto getHearingType(Integer refHearingTypeId) {
        final String methodName = "getHearingType";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final Optional<XhbRefHearingTypeDao> dao =
            getXhbRefHearingTypeRepository().findById(refHearingTypeId);
        HearingTypeDto result = null;
        if (dao.isPresent()) {
            LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - Hearing Type found");
            result = getDto(dao.get());
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    private HearingTypeDto getDto(final XhbRefHearingTypeDao xhbRefHearingType) {
        final HearingTypeDto dto = createHearingTypeDto();
        dto.setRefHearingTypeId(xhbRefHearingType.getRefHearingTypeId());
        dto.setHearingTypeCode(xhbRefHearingType.getHearingTypeCode());
        dto.setHearingTypeDesc(xhbRefHearingType.getHearingTypeDesc());
        dto.setCategory(xhbRefHearingType.getCategory());
        dto.setSeqNo(xhbRefHearingType.getSeqNo());
        dto.setListSequence(xhbRefHearingType.getListSequence());
        dto.setCourtId(xhbRefHearingType.getCourtId());
        return dto;
    }
}
