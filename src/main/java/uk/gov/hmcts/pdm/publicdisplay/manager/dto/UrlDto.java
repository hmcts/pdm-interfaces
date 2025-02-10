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

package uk.gov.hmcts.pdm.publicdisplay.manager.dto;

import uk.gov.hmcts.pdm.publicdisplay.manager.security.EncryptedFormat;

import java.io.Serializable;

/**
 * UrlDto.
 * 
 * @author scullionm
 *
 */
public class UrlDto implements Serializable {
    
    /** The Default Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * The id of the Url.
     */
    @EncryptedFormat
    private Long id;

    /** The url. */
    private String url;

    /** The description. */
    private String description;

    /**
     * getId.
     * 
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * setId.
     * 
     * @param id the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * getUrl.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * setUrl.
     * 
     * @param url the url to set
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * getDescription.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription.
     * 
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

}
