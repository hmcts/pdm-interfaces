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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UrlDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * CduPageStateHolder.
 * @author groenm
 *
 */
@Component
public class CduPageStateHolder implements Serializable {
    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * list of XhibitCourtSiteDto objects.
     */
    private List<XhibitCourtSiteDto> sites = new ArrayList<>();

    /**
     * list of ICdu objects.
     */
    private List<CduDto> cdus = new ArrayList<>();

    /**
     * The cdu.
     */
    private CduDto cdu;

    /**
     * List of available urls.
     */
    private List<UrlDto> availableUrls = new ArrayList<>();

    /**
     * Search criteria.
     */
    private CduSearchCommand cduSearchCommand;

    /**
     * Reset all the variables.
     */
    public void reset() {
        setSites(new ArrayList<>());
        setCdus(new ArrayList<>());
        setCdu(null);
        setAvailableUrls(new ArrayList<>());
        setCduSearchCommand(null);
    }

    /**
     * setSites.
     * @param sitesList list of sites to return to front end.
     */
    public void setSites(final List<XhibitCourtSiteDto> sitesList) {
        this.sites = sitesList;
    }

    /**
     * getSites.
     * @return List list of sites
     */
    public List<XhibitCourtSiteDto> getSites() {
        return this.sites;
    }

    /**
     * getCdus.
     * @return List  list of cdus
     */
    public List<CduDto> getCdus() {
        return this.cdus;
    }

    /**
     * setCdus.
     * @param cduList set the internal list of cdus.
     */
    public void setCdus(final List<CduDto> cduList) {
        this.cdus = cduList;
    }

    /**
     * Gets the cdu.
     *
     * @return the cdu
     */
    public CduDto getCdu() {
        return cdu;
    }

    /**
     * Sets the cdu.
     *
     * @param cdu the cdu to set
     */
    public void setCdu(final CduDto cdu) {
        this.cdu = cdu;
    }

    /**
     * getAvailableUrls.
     * @return the availableUrls
     */
    public List<UrlDto> getAvailableUrls() {
        return availableUrls;
    }

    /**
     * setAvailableUrls.
     * @param availableUrls the availableUrls to set
     */
    public void setAvailableUrls(final List<UrlDto> availableUrls) {
        this.availableUrls = availableUrls;
    }

    /**
     * getCduSearchCommand.
     * @return the cduSearchCommand
     */
    public CduSearchCommand getCduSearchCommand() {
        return cduSearchCommand;
    }

    /**
     * setCduSearchCommand.
     * @param cduSearchCommand the cduSearchCommand to set
     */
    public void setCduSearchCommand(final CduSearchCommand cduSearchCommand) {
        this.cduSearchCommand = cduSearchCommand;
    }

}
