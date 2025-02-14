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
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl.XhbDispMgrUrlRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UrlDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IUrlService;

import java.util.ArrayList;
import java.util.List;

/**
 * UrlService.
 * 
 * @author groenm
 *
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@SuppressWarnings("PMD.NullAssignment")
public class UrlService implements IUrlService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlService.class);

    private EntityManager entityManager;

    private XhbDispMgrUrlRepository xhbDispMgrUrlRepository;

    
    protected void clearRepositories() {
        xhbDispMgrUrlRepository = null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IUrlService#
     * getUrlsByXhibitCourtSiteId(java.lang. Long)
     */
    @Override
    public List<UrlDto> getUrlsByXhibitCourtSiteId(final Long xhibitCourtSiteId) {
        final String methodName = "getUrlsByXhibitCourtSiteId ";
        LOGGER.info("Method {}{}", methodName, " - starts");
        final List<IUrlModel> urls =
            getXhbDispMgrUrlRepository().findByCourtSiteId(xhibitCourtSiteId.intValue());
        final List<UrlDto> urlDtoList = new ArrayList<>();
        for (IUrlModel url : urls) {
            final UrlDto urlDto = createUrlDto();
            urlDto.setDescription(url.getDescription());
            urlDto.setId(url.getId());
            urlDto.setUrl(url.getUrl());

            urlDtoList.add(urlDto);
        }

        LOGGER.info("Method {}{}", methodName, " - ends");

        return urlDtoList;
    }

    private UrlDto createUrlDto() {
        return new UrlDto();
    }

    protected EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            clearRepositories();
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    protected XhbDispMgrUrlRepository getXhbDispMgrUrlRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDispMgrUrlRepository)) {
            xhbDispMgrUrlRepository = new XhbDispMgrUrlRepository(getEntityManager());
        }
        return xhbDispMgrUrlRepository;
    }

}
