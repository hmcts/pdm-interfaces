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
import uk.gov.hmcts.pdm.publicdisplay.common.exception.ServiceException;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.CduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduRegisterCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.MappingCommand;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class CduService.
 *
 * @author groenm
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CduService extends CduServHelperSave implements ICduService {
    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CduService.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#isCduWithMacAddress(
     * java.lang.String)
     */
    @Override
    public boolean isCduWithMacAddress(final String macAddress) {
        return getXhbDispMgrCduRepository().isCduWithMacAddress(macAddress);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#isCduWithCduNumber(
     * java.lang.String)
     */
    @Override
    public boolean isCduWithCduNumber(final String cduNumber) {
        return getXhbDispMgrCduRepository().isCduWithCduNumber(cduNumber);
    }

    /**
     * Creates the cdu dto.
     *
     * @param cduRegisterCommand the cduRegisterCommand
     * @return the cdu dto
     */
    @Override
    public CduDto createCduDtoFromRegisterCommand(CduRegisterCommand cduRegisterCommand) {
        final CduDto cduDto = new CduDto();
        cduDto.setCduNumber(cduRegisterCommand.getCduNumber());
        cduDto.setCourtSiteId(cduRegisterCommand.getCourtSiteId());
        cduDto.setDescription(cduRegisterCommand.getDescription());
        cduDto.setNotification(cduRegisterCommand.getNotification());
        cduDto.setIpAddress(cduRegisterCommand.getIpAddress());
        cduDto.setLocation(cduRegisterCommand.getLocation());
        cduDto.setMacAddress(cduRegisterCommand.getMacAddress());
        cduDto.setRefresh(cduRegisterCommand.getRefresh());
        cduDto.setWeighting(cduRegisterCommand.getWeighting());
        cduDto.setRegisteredIndicator(NO_CHAR);
        return cduDto;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#registerCdu(uk.gov.
     * hmcts.pdm. publicdisplay.manager.web.cdus.CduDto,
     * uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduRegisterCommand)
     */
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void registerCdu(final CduDto cduDto, final CduRegisterCommand cduCommand) {
        final String methodName = "registerCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        
        // get the CDU. it may or may not exist
        ICduModel cdu = getXhbDispMgrCduRepository().findByMacAddress(cduDto.getMacAddress());

        if (cdu == null) {
            // CDU is not registered - create CDU
            cdu = new CduModel();

            // retrieve the next available ip host.
            Integer nextIpHost = getXhbDispMgrCduRepository()
                .getNextIpHost(cduDto.getCourtSiteId().intValue(), cduIpHostMin, cduIpHostMax);
            if (nextIpHost == null) {
                if (getXhbDispMgrCduRepository().hostExists(cduDto.getCourtSiteId().intValue())) {
                    // There are no available ip addresses in the registered range
                    throw new ServiceException(
                        "Unable to register CDU. There are no available ip addresses in the registered range");
                } else {
                    nextIpHost = cduIpHostMin;
                }
            }

            final String nextIpAddress = cduIpNetwork + "." + nextIpHost.toString();

            final ICourtSite courtSite = getXhbDispMgrCourtSiteRepository()
                .findByCourtSiteId(cduDto.getCourtSiteId().intValue());

            cdu.setCduNumber(cduCommand.getCduNumber());
            cdu.setCourtSite(courtSite);
            cdu.setDescription(cduCommand.getDescription());
            cdu.setIpAddress(nextIpAddress);
            cdu.setLocation(cduCommand.getLocation());
            cdu.setMacAddress(cduDto.getMacAddress());
            cdu.setNotification(cduCommand.getNotification());
            cdu.setRefresh(cduCommand.getRefresh());
            cdu.setTitle(cduCommand.getTitle());
            cdu.setOfflineIndicator(AppConstants.NO_CHAR);
            cdu.setRagStatus(AppConstants.GREEN_CHAR);
            cdu.setRagStatusDate(LocalDateTime.now());
            cdu.setWeighting(cduCommand.getWeighting());
        } else {
            // already exists - i.e. it is registered
            // Still need to ensure it is registered on the Local Proxy
            LOGGER.debug("{}{}{}{}", METHOD, methodName, " cdu found for Mac Address ",
                cduDto.getMacAddress());

            final boolean isCduWithCduNumber =
                getXhbDispMgrCduRepository().isCduWithCduNumber(cduCommand.getCduNumber());
            if (!cdu.getCduNumber().equalsIgnoreCase(cduCommand.getCduNumber())
                && isCduWithCduNumber) {
                // CDU Number already exists for another CDU.
                // CANNOT carry on as do not know which CDU should be associated with the provided
                // CDU
                // Number
                throw new ServiceException("Unable to register CDU. CDU Number is already in use.");
            }
            // Set the CDU up correctly as User may have re entered details
            cdu.setCduNumber(cduCommand.getCduNumber());
            cdu.setDescription(cduCommand.getDescription());
            cdu.setLocation(cduCommand.getLocation());
            cdu.setNotification(cduCommand.getNotification());
            cdu.setRefresh(cduCommand.getRefresh());
            cdu.setTitle(cduCommand.getTitle());
            cdu.setOfflineIndicator(AppConstants.NO_CHAR);
            cdu.setRagStatus(AppConstants.GREEN_CHAR);
            cdu.setRagStatusDate(LocalDateTime.now());
            cdu.setWeighting(cduCommand.getWeighting());
        }

        saveNewCdu(cdu);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#updateCdu
     * (uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduDto,
     * uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduRegisterCommand)
     */
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateCdu(final CduDto cduDto, final CduAmendCommand cduCommand) {
        final String methodName = "updateCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Get the CDU
        final ICduModel cdu = getXhbDispMgrCduRepository().findByMacAddress(cduDto.getMacAddress());
        if (cdu == null) {
            throw new ServiceException("Unable to update CDU. No CDU is registered for Mac Address "
                + cduDto.getMacAddress());
        }

        // Update the editable data
        cdu.setLocation(cduCommand.getLocation());
        cdu.setDescription(cduCommand.getDescription());
        cdu.setNotification(cduCommand.getNotification());
        cdu.setRefresh(cduCommand.getRefresh());
        cdu.setWeighting(cduCommand.getWeighting());
        cdu.setOfflineIndicator(cduCommand.getOfflineIndicator());

        // Save the CDU
        saveCdu(cdu);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#unRegisterCdu(uk.
     * gov. hmcts.pdm.publicdisplay.manager.web.cdus.CduCommand)
     */
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void unregisterCdu(final Long cduId) {
        final String methodName = "unRegisterCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Get the CDU. It may or may not exist
        final ICduModel cdu = getXhbDispMgrCduRepository().findByCduId(cduId.intValue());
        if (cdu == null) {
            // cdu not registered on the central server
            throw new ServiceException("Unable to return cdu based on id " + cduId);
        } else {
            // unregister cdu
            LOGGER.debug(THREE_PARAMS, METHOD, methodName,
                " - about to unregister cdu on central server");
            getXhbDispMgrCduRepository().deleteDaoFromBasicValue(cdu);

            LOGGER.debug(THREE_PARAMS, METHOD, methodName,
                " - about to unregister cdu on localProxy server");
            // TODO we need ipdmanager for this to work
            // Rest call to delete cdu data
            if (localProxyCommunicationEnabled) {
                localProxyRestClient.deleteCdu(cdu);
            }

            LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - cdu unregistered");
        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#getCduByMacAddress(
     * java.lang.String)
     */
    @Override
    public CduDto getCduByMacAddress(final String macAddress) {
        final String methodName = "getCduByMacAddress";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        final ICduModel cdu = getXhbDispMgrCduRepository().findByMacAddress(macAddress);

        final CduDto cduDto = createCduDto(cdu);

        // registered indicator flag - without going to the localProxy, assume registered
        cduDto.setRegisteredIndicator(AppConstants.YES_CHAR);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return cduDto;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#addMapping(uk.gov.
     * hmcts.pdm. publicdisplay.manager.web.cdus.MappingCommand)
     */
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void addMapping(final MappingCommand mappingCommand) {
        final String methodName = "addMapping ";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        final ICduModel cdu =
            getXhbDispMgrCduRepository().findByCduId(mappingCommand.getCduId().intValue());

        final IUrlModel url =
            getXhbDispMgrUrlRepository().findByUrlId(mappingCommand.getUrlId().intValue());

        final boolean mapped = isMapped(cdu, url);
        if (mapped) {
            // cdu is already mapped to the url
            throw new ServiceException("The CDU (" + cdu.getCduNumber() + ") and the URL ("
                + url.getUrl() + ") are already mapped ");
        } else {
            cdu.getUrls().add(url);
            LOGGER.debug(THREE_PARAMS, METHOD, methodName,
                " - adding the mapping to central database ");
            getXhbDispMgrMappingRepository().addMappingForCdu(cdu, url);

            if (localProxyCommunicationEnabled) {
                LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - saving the url to local proxy ");
                localProxyRestClient.saveUrl(url);
                LOGGER.debug(THREE_PARAMS, METHOD, methodName,
                    " - adding the mapping to local proxy ");
                localProxyRestClient.saveMapping(cdu, url);
            }

        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#removeMapping(uk.
     * gov. hmcts.pdm.publicdisplay.manager.web.cdus.MappingCommand)
     */
    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeMapping(final MappingCommand mappingCommand) {
        final String methodName = "addMapping ";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        final ICduModel cdu =
            getXhbDispMgrCduRepository().findByCduId(mappingCommand.getCduId().intValue());

        final IUrlModel url =
            getXhbDispMgrUrlRepository().findByUrlId(mappingCommand.getUrlId().intValue());

        final boolean mapped = isMapped(cdu, url);

        if (mapped) {
            LOGGER.info(THREE_PARAMS, METHOD, methodName,
                " - removing mapping on central database ");
            cdu.getUrls().remove(url);
            getXhbDispMgrMappingRepository().deleteMappingForCdu(cdu, url);

            if (localProxyCommunicationEnabled) {
                LOGGER.info(THREE_PARAMS, METHOD, methodName,
                    " - removing mapping on Local Proxy ");
                localProxyRestClient.deleteMapping(cdu, url);
            }

            LOGGER.info(THREE_PARAMS, METHOD, methodName, " - CDU saved ");
        } else {
            // cdu is already mapped to the url
            throw new ServiceException("The CDU (" + cdu.getCduNumber() + ") and the URL ("
                + url.getUrl() + ") are not already mapped ");
        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#restartCdu(java.
     * util. List)
     */
    @Override
    public void restartCdu(final List<CduDto> cdus) {
        final String methodName = "restartCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Get which local proxy the passed in cdus are for
        final ICourtSite courtSite = getXhbDispMgrCourtSiteRepository()
            .findByCourtSiteId(cdus.get(0).getCourtSiteId().intValue());
        final ILocalProxy localProxy = courtSite.getLocalProxy();

        // Get the List of ipAddresses to restart
        final List<String> ipAddresses = new ArrayList<>();
        for (CduDto cdu : cdus) {
            ipAddresses.add(cdu.getIpAddress());
        }

        // TODO We need ipdmanager for this to work
        // Restart the cdus
        if (localProxyCommunicationEnabled) {
            localProxyRestClient.restartCdu(localProxy, ipAddresses);
        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#getCduScreenshot(
     * uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus.CduDto)
     */
    @Override
    public byte[] getCduScreenshot(final CduDto cduDto) {
        final String methodName = "getCduScreenshot";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        final ICourtSite courtSite = getXhbDispMgrCourtSiteRepository()
            .findByCourtSiteId(cduDto.getCourtSiteId().intValue());
        final ILocalProxy localProxy = courtSite.getLocalProxy();

        final byte[] bytes;
        if (localProxyCommunicationEnabled) {
            // TODO We need ipdmanager for this to work
            bytes = localProxyRestClient.getCduScreenshot(localProxy, cduDto.getIpAddress());
        } else {
            bytes = getFakeCduScreenshot();
        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return bytes;
    }

}
