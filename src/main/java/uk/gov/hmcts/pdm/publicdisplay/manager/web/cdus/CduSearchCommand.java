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

import uk.gov.hmcts.pdm.publicdisplay.manager.security.EncryptedFormat;

import java.io.Serializable;
/**
 * Command object used to hold the values passsed to the CDU search.
 * 
 * @author scullionm
 *
 */

public class CduSearchCommand implements Serializable {

    /**
     * SerialVersionUID of the CduSearchCommand class.
     */
    private static final long serialVersionUID = 1892376688035220960L;

    /**
     * ID of the court site we are looking for.
     */
    @EncryptedFormat
    private Long xhibitCourtSiteId;

    /**
     * Mac Address of the CDU we're looking for.
     */
    private String macAddress;

    /**
     * Currently selected Mac Address from the dropdown.
     */
    @EncryptedFormat
    private String selectedMacAddress;
    
    /** 
     * The court site id. 
     */
    @EncryptedFormat
    private Long courtSiteId;

    /**
     * ipAddress of the CDU.
     */
    @EncryptedFormat
    private String ipAddress;
    
    
    /**
     * getXhibitCourtSiteId.
     * 
     * @return the xhibitCourtSiteId
     */
    public Long getXhibitCourtSiteId() {
        return xhibitCourtSiteId;
    }

    /**
     * setXhibitCourtSiteId.
     * 
     * @param xhibitCourtSiteId the xhibitCourtSiteId to set
     */
    public void setXhibitCourtSiteId(final Long xhibitCourtSiteId) {
        this.xhibitCourtSiteId = xhibitCourtSiteId;
    }

    /**
     * getMacAddress.
     * 
     * @return the macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * setMacAddress.
     * 
     * @param macAddress the macAddress to set
     */
    public void setMacAddress(final String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * getSelectedMacAddress.
     * 
     * @return the selectedMacAddress
     */
    public String getSelectedMacAddress() {
        return selectedMacAddress;
    }

    /**
     * setSelectedMacAddress.
     * 
     * @param selectedMacAddress the selectedMacAddress to set
     */
    public void setSelectedMacAddress(final String selectedMacAddress) {
        this.selectedMacAddress = selectedMacAddress;
    }

    /**
     * getCourtSiteId.
     * 
     * @return the courtSiteId
     */
    public Long getCourtSiteId() {
        return courtSiteId;
    }

    /**
     * setCourtSiteId.
     * 
     * @param courtSiteId the courtSiteId to set
     */
    public void setCourtSiteId(Long courtSiteId) {
        this.courtSiteId = courtSiteId;
    }

    /**
     * getIpAddress.
     * 
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * setIpAddress.
     * 
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
