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

import com.pdm.hb.jpa.EntityManagerUtil;
import com.pdm.hb.jpa.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtRepository;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplay.XhbDisplayRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation.XhbDisplayLocationRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaytype.XhbDisplayTypeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefhearingtype.XhbRefHearingTypeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefjudge.XhbRefJudgeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeDao;
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrotationsets.XhbRotationSetsRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class AbstractService.
 *
 * @author Luke Gittins
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@SuppressWarnings({"PMD.NullAssignment", "PMD.TooManyMethods"})
public class AbstractService {

    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);
    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String FOUR_PARAMS = "{}{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";
    protected static final String NO = "N";
    protected static final String YES = "Y";
    
    private EntityManager entityManager;
    private XhbCourtRepository xhbCourtRepository;
    private XhbCourtSiteRepository xhbCourtSiteRepository;
    private XhbRefSystemCodeRepository xhbRefSystemCodeRepository;
    private XhbDisplayRepository xhbDisplayRepository;
    private XhbDisplayLocationRepository xhbDisplayLocationRepository;
    private XhbDisplayTypeRepository xhbDisplayTypeRepository;
    private XhbRotationSetsRepository xhbRotationSetsRepository;
    private XhbRefHearingTypeRepository xhbRefHearingTypeRepository;
    private XhbRefJudgeRepository xhbRefJudgeRepository;
    
    protected void clearRepositories() {
        xhbCourtRepository = null;
        xhbCourtSiteRepository = null;
        xhbRefSystemCodeRepository = null;
        xhbDisplayRepository = null;
        xhbDisplayLocationRepository = null;
        xhbDisplayTypeRepository = null;
        xhbRotationSetsRepository = null;
        xhbRefHearingTypeRepository = null;
        xhbRefJudgeRepository = null;
    }

    /**
     * Parent wrapper for getCourtSites() for instances where no courtId is passed in.
     *
     * @return the court sites
     */
    public List<XhibitCourtSiteDto> getCourtSites() {
        return getCourtSites(null);
    }
    
    /**
     * Gets the court sites.
     *
     * @return the court sites
     */
    public List<XhibitCourtSiteDto> getCourtSites(Integer courtId) {
        final String methodName = "getCourtSites";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<XhibitCourtSiteDto> resultList = new ArrayList<>();
        List<XhbCourtSiteDao> xhibitCourtSiteList;
        
        if (courtId != null) {
            xhibitCourtSiteList = getXhbCourtSiteRepository().findByCourtId(courtId);
        } else {
            xhibitCourtSiteList = getXhbCourtSiteRepository().findAll();
        }
        
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

    protected XhibitCourtSiteDto createXhibitCourtSiteDto() {
        return new XhibitCourtSiteDto();
    }
    
    protected RefSystemCodeDto createRefSystemCodeDto(XhbRefSystemCodeDao dao) {
        final RefSystemCodeDto dto = new RefSystemCodeDto();
        dto.setCode(dao.getCode());
        dto.setCodeTitle(dao.getCodeTitle());
        dto.setCodeType(dao.getCodeType());
        dto.setCourtId(dao.getCourtId());
        dto.setCreatedBy(dao.getCreatedBy());
        dto.setCreationDate(dao.getCreationDate());
        dto.setDeCode(dao.getDeCode());
        dto.setLastUpdateDate(dao.getLastUpdateDate());
        dto.setLastUpdatedBy(dao.getLastUpdatedBy());
        dto.setObsInd(dao.getObsInd());
        dto.setRefCodeOrder(dao.getRefCodeOrder());
        dto.setRefSystemCodeId(dao.getRefSystemCodeId());
        dto.setVersion(dao.getVersion());
        return dto;
    }
    
    protected EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            clearRepositories();
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }
    
    protected XhbCourtRepository getXhbCourtRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbCourtRepository)) {
            xhbCourtRepository = new XhbCourtRepository(getEntityManager());
        }
        return xhbCourtRepository;
    }
    
    protected XhbCourtSiteRepository getXhbCourtSiteRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbCourtSiteRepository)) {
            xhbCourtSiteRepository = new XhbCourtSiteRepository(getEntityManager());
        }
        return xhbCourtSiteRepository;
    }
    
    protected XhbRefSystemCodeRepository getXhbRefSystemCodeRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbRefSystemCodeRepository)) {
            xhbRefSystemCodeRepository = new XhbRefSystemCodeRepository(getEntityManager());
        }
        return xhbRefSystemCodeRepository;
    }

    protected XhbDisplayRepository getXhbDisplayRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDisplayRepository)) {
            xhbDisplayRepository = new XhbDisplayRepository(getEntityManager());
        }
        return xhbDisplayRepository;
    }
    
    protected XhbDisplayLocationRepository getXhbDisplayLocationRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDisplayLocationRepository)) {
            xhbDisplayLocationRepository = new XhbDisplayLocationRepository(getEntityManager());
        }
        return xhbDisplayLocationRepository;
    }
    
    protected XhbDisplayTypeRepository getXhbDisplayTypeRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDisplayTypeRepository)) {
            xhbDisplayTypeRepository = new XhbDisplayTypeRepository(getEntityManager());
        }
        return xhbDisplayTypeRepository;
    }
    
    protected XhbRotationSetsRepository getXhbRotationSetsRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbRotationSetsRepository)) {
            xhbRotationSetsRepository = new XhbRotationSetsRepository(getEntityManager());
        }
        return xhbRotationSetsRepository;
    }
    
    protected XhbRefHearingTypeRepository getXhbRefHearingTypeRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbRefHearingTypeRepository)) {
            xhbRefHearingTypeRepository = new XhbRefHearingTypeRepository(getEntityManager());
        }
        return xhbRefHearingTypeRepository;
    }
    
    protected XhbRefJudgeRepository getXhbRefJudgeRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbRefJudgeRepository)) {
            xhbRefJudgeRepository = new XhbRefJudgeRepository(getEntityManager());
        }
        return xhbRefJudgeRepository;
    }
}
