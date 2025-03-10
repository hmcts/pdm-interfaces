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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.display;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RotationSetsDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DisplayPageStateHolder.
 * 
 * @author harrism
 *
 */

@Component
public class DisplayPageStateHolder implements Serializable {
    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The court site.
     */
    private XhibitCourtSiteDto courtSite;

    /**
     * list of XhibitCourtSiteDto objects.
     */
    private List<XhibitCourtSiteDto> sitesList = new ArrayList<>();

    /**
     * list of DisplayDto objects.
     */
    private List<DisplayDto> displayList = new ArrayList<>();

    /**
     * list of DisplayTypeDto objects.
     */
    private List<DisplayTypeDto> displayTypeList = new ArrayList<>();

    /**
     * list of RotationSetDto objects.
     */
    private List<RotationSetsDto> rotationSetsList = new ArrayList<>();

    /**
     * The display search command.
     */
    private DisplaySearchCommand displaySearchCommand;

    /**
     * Reset all the variables.
     */
    public void reset() {
        setCourtSite(null);
        setSites(new ArrayList<>());
        setDisplays(new ArrayList<>());
        setDisplayTypes(new ArrayList<>());
        setRotationSets(new ArrayList<>());
    }

    /**
     * setSites.
     * 
     * @param sitesList list of sites to return to front end.
     */
    public void setSites(final List<XhibitCourtSiteDto> sitesList) {
        this.sitesList = sitesList;
    }

    /**
     * getSites.
     * 
     * @return List list of sites
     */
    public List<XhibitCourtSiteDto> getSites() {
        return this.sitesList;
    }

    /**
     * getSites.
     * 
     * @return List list of sites
     */
    public List<XhibitCourtSiteDto> getSitesBySelectedCourt(Integer courtId) {
        List<XhibitCourtSiteDto> resultList = new ArrayList<>();
        for (XhibitCourtSiteDto site : getSites()) {
            if (site.getCourtId().equals(courtId)) {
                resultList.add(site);
            }
        }
        // Sort by code and name
        Collections.sort(resultList,
            (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER.compare(
                obj1.getCourtSiteCode().concat(obj1.getCourtSiteName()),
                obj2.getCourtSiteCode().concat(obj2.getCourtSiteName())));
        return resultList;
    }

    /**
     * Gets the court site.
     *
     * @return the court site
     */
    public XhibitCourtSiteDto getCourtSite() {
        return courtSite;
    }

    /**
     * Sets the court site.
     *
     * @param courtSite the courtSite to set
     */
    public void setCourtSite(final XhibitCourtSiteDto courtSite) {
        this.courtSite = courtSite;
    }

    /**
     * Sets the display search command.
     *
     * @param displaySearchCommand the new display search command
     */
    public void setDisplaySearchCommand(final DisplaySearchCommand displaySearchCommand) {
        this.displaySearchCommand = displaySearchCommand;
    }

    /**
     * Gets the display search command.
     *
     * @return the display search command
     */
    public DisplaySearchCommand getDisplaySearchCommand() {
        return this.displaySearchCommand;
    }

    /**
     * setDisplays.
     * 
     * @param displayList list of displays to return to front end.
     */
    public void setDisplays(final List<DisplayDto> displayList) {
        this.displayList = displayList;
    }

    /**
     * getDisplays.
     * 
     * @return List list of displays
     */
    public List<DisplayDto> getDisplays() {
        return this.displayList;
    }

    /**
     * setTypes.
     * 
     * @param displayTypeList list of display types to return to front end.
     */
    public void setDisplayTypes(final List<DisplayTypeDto> displayTypeList) {
        this.displayTypeList = displayTypeList;
    }

    /**
     * getTypes.
     * 
     * @return List list of display types
     */
    public List<DisplayTypeDto> getDisplayTypes() {
        return this.displayTypeList;
    }

    /**
     * setRotationSets.
     * 
     * @param rotationSetsList list of rotation sets to return to front end.
     */
    public void setRotationSets(final List<RotationSetsDto> rotationSetsList) {
        this.rotationSetsList = rotationSetsList;
    }

    /**
     * getRotationSets.
     * 
     * @return List list of rotation sets
     */
    public List<RotationSetsDto> getRotationSets() {
        return this.rotationSetsList;
    }
}
