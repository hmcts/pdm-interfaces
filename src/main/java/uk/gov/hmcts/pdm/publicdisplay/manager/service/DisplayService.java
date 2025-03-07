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
import uk.gov.hmcts.pdm.business.entities.xhbdisplay.XhbDisplayDao;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation.XhbDisplayLocationDao;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaytype.XhbDisplayTypeDao;
import uk.gov.hmcts.pdm.business.entities.xhbrotationsets.XhbRotationSetsDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayLocationDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RotationSetsDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IDisplayService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.display.DisplayAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.display.DisplayCreateCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.display.DisplayDeleteCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * The Class DisplayService.
 *
 * @author harrism
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@SuppressWarnings({"PMD.CouplingBetweenObjects", "PMD.TooManyMethods"})
public class DisplayService extends DisplayServiceFinder implements IDisplayService {

    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayService.class);

    /**
     * Gets the displays by court site id.
     *
     * @return List
     */
    @Override
    public List<DisplayDto> getDisplays(Long xhibitCourtSiteId, List<DisplayTypeDto> displayTypes,
        List<XhibitCourtSiteDto> xhibitCourtSites, List<RotationSetsDto> rotationSets) {
        final String methodName = "getDisplays";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<DisplayDto> resultList = new ArrayList<>();
        final List<XhbDisplayDao> xhbDisplayList =
            getXhbDisplayRepository().findByCourtSiteId(xhibitCourtSiteId.intValue());
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Displays returned : ",
            xhbDisplayList.size());

        if (!xhbDisplayList.isEmpty()) {
            for (XhbDisplayDao xhbDisplay : xhbDisplayList) {
                final DisplayDto dto = getDto(xhbDisplay);
                // Display Type
                if (displayTypes != null) {
                    dto.setDisplayType(getDisplayType(displayTypes, xhbDisplay.getDisplayTypeId()));
                }
                // Court Site
                if (xhibitCourtSites != null) {
                    dto.setCourtSite(getCourtSite(xhibitCourtSites, xhibitCourtSiteId));
                }
                // Rotation Set
                if (rotationSets != null) {
                    dto.setRotationSet(getRotationSet(rotationSets, xhbDisplay.getRotationSetId()));
                }
                
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getDescriptionCode(), obj2.getDescriptionCode()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);   
        return resultList;
    }

    /**
     * Retrieve the display for the id.
     * 
     * @return DisplayDto
     */
    @Override
    public DisplayDto getDisplay(Integer displayId) {
        final String methodName = "getDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final Optional<XhbDisplayDao> dao = getXhbDisplayRepository().findById(displayId);
        DisplayDto result = null;
        if (dao.isPresent()) {
            LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - Display found");
            result = getDto(dao.get());
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }
    
    /**
     * Retrieve the display location for the id.
     * 
     * @return DisplayLocationDto
     */
    @Override
    public DisplayLocationDto getDisplayLocation(Integer displayLocationId) {
        final String methodName = "getDisplayLocation";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final Optional<XhbDisplayLocationDao> dao = getXhbDisplayLocationRepository().findById(displayLocationId);
        DisplayLocationDto result = null;
        if (dao.isPresent()) {
            LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - Display Location found");
            result =  createDisplayLocationDto();
            result.setDisplayLocationId(dao.get().getDisplayLocationId());
            result.setCourtSiteId(dao.get().getCourtSiteId());
            result.setDescriptionCode(dao.get().getDescriptionCode());
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return result;
    }

    private DisplayTypeDto getDisplayType(final List<DisplayTypeDto> displayTypes,
        final Integer displayTypeId) {
        for (DisplayTypeDto displayTypeDto : displayTypes) {
            if (displayTypeDto.getDisplayTypeId().equals(displayTypeId)) {
                return displayTypeDto;
            }
        }
        return null;
    }

    private XhibitCourtSiteDto getCourtSite(final List<XhibitCourtSiteDto> courtSites,
        final Long xhibitCourtSiteId) {
        for (XhibitCourtSiteDto courtSiteDto : courtSites) {
            if (courtSiteDto.getId().equals(xhibitCourtSiteId)) {
                return courtSiteDto;
            }
        }
        return null;
    }

    private RotationSetsDto getRotationSet(final List<RotationSetsDto> rotationSets,
        final Integer rotationSetId) {
        for (RotationSetsDto rotationSetsDto : rotationSets) {
            if (rotationSetsDto.getRotationSetId().equals(rotationSetId)) {
                return rotationSetsDto;
            }
        }
        return null;
    }

    /**
     * Gets all the display types.
     *
     * @return the display types
     */
    @Override
    public List<DisplayTypeDto> getDisplayTypes() {
        final String methodName = "getDisplayTypes";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<DisplayTypeDto> resultList = new ArrayList<>();
        final List<XhbDisplayTypeDao> xhbDisplayTypeList = getXhbDisplayTypeRepository().findAll();
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Display Types returned : ",
            xhbDisplayTypeList.size());

        if (!xhbDisplayTypeList.isEmpty()) {
            for (XhbDisplayTypeDao xhbDisplayType : xhbDisplayTypeList) {
                final DisplayTypeDto dto = createDisplayTypeDto();
                dto.setDisplayTypeId(xhbDisplayType.getDisplayTypeId());
                dto.setDescriptionCode(xhbDisplayType.getDescriptionCode());
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getDescriptionCode(), obj2.getDescriptionCode()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }

    /**
     * Gets all the rotation sets.
     *
     * @param courtId courtId
     * @return the rotation sets
     */
    @Override
    public List<RotationSetsDto> getRotationSets(Integer courtId) {
        final String methodName = "getRotationSets";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<RotationSetsDto> resultList = new ArrayList<>();
        final List<XhbRotationSetsDao> xhbRotationSetsList =
            getXhbRotationSetsRepository().findByCourtId(courtId);
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Display Locations returned : ",
            xhbRotationSetsList.size());

        if (!xhbRotationSetsList.isEmpty()) {
            for (XhbRotationSetsDao xhbRotationSetsDao : xhbRotationSetsList) {
                final RotationSetsDto dto = createRotationSetsDto();
                dto.setRotationSetId(xhbRotationSetsDao.getRotationSetId());
                dto.setCourtId(xhbRotationSetsDao.getCourtId());
                dto.setDescription(xhbRotationSetsDao.getDescription());
                dto.setDefaultYn(xhbRotationSetsDao.getDefaultYn());
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getDescription(), obj2.getDescription()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }

    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateDisplay(final DisplayAmendCommand command) {

        final String methodName = "updateDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbDisplayDao> existingDao =
            getXhbDisplayRepository().findById(command.getDisplayId());
        if (existingDao.isPresent()) {
            XhbDisplayDao displayDao = existingDao.get();

            // Get the display location (from the courtSiteId)
            XhbDisplayLocationDao displayLocationDao = getXhbDisplayLocationDao(
                command.getXhibitCourtSiteId().intValue(), displayDao.getDescriptionCode());

            // Update the displayDao
            displayDao.setDisplayTypeId(command.getDisplayTypeId());
            displayDao.setDisplayLocationId(displayLocationDao.getDisplayLocationId());
            displayDao.setRotationSetId(command.getRotationSetId());

            // Update the existing record
            getXhbDisplayRepository().updateDao(displayDao);
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createDisplay(final DisplayCreateCommand command) {

        final String methodName = "createDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Get the display location (from the courtSiteId)
        XhbDisplayLocationDao displayLocationDao = getXhbDisplayLocationDao(
            command.getXhibitCourtSiteId().intValue(), command.getDescriptionCode());

        // Create the displayDao
        XhbDisplayDao displayDao = new XhbDisplayDao();
        displayDao.setDisplayTypeId(command.getDisplayTypeId());
        displayDao.setDescriptionCode(command.getDescriptionCode());
        displayDao.setDisplayTypeId(command.getDisplayTypeId());
        displayDao.setDisplayLocationId(displayLocationDao.getDisplayLocationId());
        displayDao.setRotationSetId(command.getRotationSetId());
        displayDao
            .setLocale(Locale.getDefault().getLanguage() + Locale.getDefault().getCountry());
        displayDao.setShowUnassignedYn(YES);

        // Save
        getXhbDisplayRepository().saveDao(displayDao);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteDisplay(final DisplayDeleteCommand command) {

        final String methodName = "createDisplay";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbDisplayDao> existingDao =
            getXhbDisplayRepository().findById(command.getDisplayId());
        if (existingDao.isPresent()) {

            // Delete the record
            getXhbDisplayRepository().deleteDao(existingDao);
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    private DisplayDto getDto(XhbDisplayDao xhbDisplay) {
        final DisplayDto dto = createDisplayDto();
        dto.setDisplayId(xhbDisplay.getDisplayId());
        dto.setDescriptionCode(xhbDisplay.getDescriptionCode());
        dto.setDisplayLocationId(xhbDisplay.getDisplayLocationId());
        dto.setDisplayTypeId(xhbDisplay.getDisplayTypeId());
        dto.setLocale(xhbDisplay.getLocale());
        dto.setRotationSetId(xhbDisplay.getRotationSetId());
        dto.setShowUnassignedYn(xhbDisplay.getShowUnassignedYn());
        return dto;
    }
}
