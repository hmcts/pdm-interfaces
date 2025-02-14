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
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrserviceaudit.XhbDispMgrServiceAuditRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.rest.JsonRequest;
import uk.gov.hmcts.pdm.publicdisplay.common.rest.JsonWebTokenType;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.ServiceAudit;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IServiceAudit;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IServiceAuditService;

/**
 * The Class ServiceAuditService.
 * 
 * @author uphillj
 *
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@SuppressWarnings("PMD.NullAssignment")
public class ServiceAuditService implements IServiceAuditService {
    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAuditService.class);

    private EntityManager entityManager;

    private XhbDispMgrServiceAuditRepository xhbDispMgrServiceAuditRepository;

    private static final JsonWebTokenType DISPLAYMANAGER = JsonWebTokenType.DISPLAY_MANAGER;
    private static final JsonWebTokenType JSONWEBTOKENTYPE = JsonWebTokenType.LOCAL_PROXY;

    
    protected void clearRepositories() {
        xhbDispMgrServiceAuditRepository = null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IServiceAuditService#
     * auditService( java.lang.String,
     * uk.gov.hmcts.pdm.publicdisplay.common.rest.JsonRequest)
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void auditService(final String service, final JsonRequest jsonRequest) {
        final String methodName = "auditService";
        LOGGER.info("Method {} - starts", methodName);

        // Create service audit record
        final IServiceAudit serviceAudit = new ServiceAudit();
        serviceAudit.setFromEndpoint(DISPLAYMANAGER.toString());
        serviceAudit.setToEndpoint(JSONWEBTOKENTYPE.toString());
        serviceAudit.setService(service);
        serviceAudit.setUrl(jsonRequest.getUrl());
        serviceAudit.setMessageId(jsonRequest.getMessageId());
        serviceAudit.setMessageStatus(jsonRequest.getStatus());
        serviceAudit.setMessageRequest(jsonRequest.getRequestText());
        serviceAudit.setMessageResponse(jsonRequest.getResponseText());

        // Save the service audit record
        getXhbDispMgrServiceAuditRepository().saveDao(serviceAudit);

        LOGGER.info("Method {} - ends", methodName);
    }

    private EntityManager getEntityManager() {
        if (!EntityManagerUtil.isEntityManagerActive(entityManager)) {
            clearRepositories();
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    protected XhbDispMgrServiceAuditRepository getXhbDispMgrServiceAuditRepository() {
        if (!RepositoryUtil.isRepositoryActive(xhbDispMgrServiceAuditRepository)) {
            xhbDispMgrServiceAuditRepository =
                new XhbDispMgrServiceAuditRepository(getEntityManager());
        }
        return xhbDispMgrServiceAuditRepository;
    }


}
