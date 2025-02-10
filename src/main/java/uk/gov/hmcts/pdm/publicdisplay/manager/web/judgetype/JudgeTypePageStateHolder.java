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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JudgeTypePageStateHolder.
 * @author toftn
 *
 */

@Component
public class JudgeTypePageStateHolder implements Serializable {
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
     * list of RefSystemCodeDto objects.
     */
    private List<RefSystemCodeDto> judgeTypeList = new ArrayList<>();
    
    /**
     * The judge type search command.
     */
    private JudgeTypeSearchCommand judgeTypeSearchCommand;

    /**
     * Reset all the variables.
     */
    public void reset() {
        setJudgeTypeSearchCommand(null);
        setCourtSite(null);
        setSites(new ArrayList<>());
        setJudgeTypes(new ArrayList<>());
    }

    /**
     * setSites.
     * @param sitesList list of sites to return to front end.
     */
    public void setSites(final List<XhibitCourtSiteDto> sitesList) {
        this.sitesList = sitesList;
    }

    /**
     * getSites.
     * @return List list of sites
     */
    public List<XhibitCourtSiteDto> getSites() {
        return this.sitesList;
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
     * Sets the judge type search command.
     *
     * @param judgeTypeSearchCommand the new judge type search command
     */
    public void setJudgeTypeSearchCommand(final JudgeTypeSearchCommand judgeTypeSearchCommand) {
        this.judgeTypeSearchCommand = judgeTypeSearchCommand;
    }

    /**
     * Gets the judge type search command.
     *
     * @return the judge type search command
     */
    public JudgeTypeSearchCommand getJudgeTypeSearchCommand() {
        return this.judgeTypeSearchCommand;
    }
    
    /**
     * setJudgeTypes.
     * @param judgeTypeList list of judge types to return to front end.
     */
    public void setJudgeTypes(final List<RefSystemCodeDto> judgeTypeList) {
        this.judgeTypeList = judgeTypeList;
    }

    /**
     * getJudgeTypes.
     * @return List list of judge types
     */
    public List<RefSystemCodeDto> getJudgeTypes() {
        return this.judgeTypeList;
    }
}
