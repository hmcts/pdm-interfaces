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
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeRepository;
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
@SuppressWarnings("PMD.NullAssignment")
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
    private XhbCourtSiteRepository xhbCourtSiteRepository;
    private XhbRefSystemCodeRepository xhbRefSystemCodeRepository;
    
    protected void clearRepositories() {
        xhbCourtSiteRepository = null;
        xhbRefSystemCodeRepository = null;
    }

    /**
     * Gets the court sites.
     *
     * @return the court sites
     */
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

    protected XhibitCourtSiteDto createXhibitCourtSiteDto() {
        return new XhibitCourtSiteDto();
    }
    
    protected EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            clearRepositories();
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
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
}
