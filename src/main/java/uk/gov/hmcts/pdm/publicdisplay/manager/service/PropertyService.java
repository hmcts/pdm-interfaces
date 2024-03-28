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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrproperty.XhbDispMgrPropertyRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IProperty;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IPropertyService;

import java.util.List;

/**
 * The Class PropertyService.
 *
 * @author uphillj
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class PropertyService implements IPropertyService {
    /** The LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyService.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    private EntityManager entityManager;

    private XhbDispMgrPropertyRepository xhbDispMgrPropertyRepository;

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IPropertyService#
     * getPropertyValueByName(java.lang. String)
     */
    @Override
    public String getPropertyValueByName(final String name) {
        final String value = getXhbDispMgrPropertyRepository().findPropertyValueByName(name);
        LOGGER.info("{}={}", name, value);
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IPropertyService#
     * getAllProperties( )
     */
    @Override
    public List<IProperty> getAllProperties() {
        return getXhbDispMgrPropertyRepository().findAllProperties();
    }

    private EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = EntityManagerUtil.getEntityManager();
        }
        return entityManager;
    }

    private XhbDispMgrPropertyRepository getXhbDispMgrPropertyRepository() {
        if (xhbDispMgrPropertyRepository == null) {
            xhbDispMgrPropertyRepository = new XhbDispMgrPropertyRepository(getEntityManager());
        }
        return xhbDispMgrPropertyRepository;
    }

}
