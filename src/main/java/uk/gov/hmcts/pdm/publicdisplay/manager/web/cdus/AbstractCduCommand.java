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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;



/**
 * The main CDU Command object.
 * 
 * @author scullionm
 *
 */
public class AbstractCduCommand {

    /** The Constant REFRESH_MIN. */
    private static final int REFRESH_MIN = 1;

    /** The Constant REFRESH_MAX. */
    private static final int REFRESH_MAX = 30;

    /** The Constant WEIGHTING_MIN. */
    private static final int WEIGHTING_MIN = 1;

    /** The Constant WEIGHTING_MAX. */
    private static final int WEIGHTING_MAX = 2;

    /**
     * Maximum length for title field.
     */
    private static final int TITLE_MAX_LENGTH = 30;

    /**
     * Maximum length for description field.
     */
    private static final int DESCRIPTION_MAX_LENGTH = 500;

    /**
     * Maximum length for Notification field.
     */
    private static final int NOTIFICATION_MAX_LENGTH = 500;

    /**
     * Maximum length for location field.
     */
    private static final int LOCATION_MAX_LENGTH = 50;

    /** The title field. */
    @Length(max = TITLE_MAX_LENGTH, message = "{cduCommand.title.tooLong}")
    private String title;

    /** The description field - optional. */
    @Length(max = DESCRIPTION_MAX_LENGTH, message = "{cduCommand.description.tooLong}")
    private String description;

    /** The location field. */
    @NotBlank(message = "{cduCommand.location.notBlank}")
    @Length(max = LOCATION_MAX_LENGTH, message = "{cduCommand.location.tooLong}")
    private String location;

    /** The notification field. */
    @Length(max = NOTIFICATION_MAX_LENGTH, message = "{cduCommand.notification.tooLong}")
    private String notification;

    /** Refresh interval. */
    @NotNull(message = "{cduCommand.refresh.notNull}")
    @Min(value = REFRESH_MIN, message = "{cduCommand.refresh.invalid}")
    @Max(value = REFRESH_MAX, message = "{cduCommand.refresh.invalid}")
    private Long refresh;

    /** Weighting factor. */
    @NotNull(message = "{cduCommand.weighting.notNull}")
    @Min(value = WEIGHTING_MIN, message = "{cduCommand.weighting.invalid}")
    @Max(value = WEIGHTING_MAX, message = "{cduCommand.weighting.invalid}")
    private Long weighting;
    
    /** Court Site Id. */
    private Long courtSiteId;
    
    /** Mac Address. */
    private String macAddress;
    
    /** Ip Address. */
    private String ipAddress;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location the location to set
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Gets the notification.
     *
     * @return the notification
     */
    public String getNotification() {
        return notification;
    }

    /**
     * Sets the notification.
     *
     * @param notification the notification to set
     */
    public void setNotification(final String notification) {
        this.notification = notification;
    }

    /**
     * Gets the refresh.
     *
     * @return the refresh
     */
    public Long getRefresh() {
        return refresh;
    }

    /**
     * Sets the refresh.
     *
     * @param refresh the refresh to set
     */
    public void setRefresh(final Long refresh) {
        this.refresh = refresh;
    }

    /**
     * Gets the weighting.
     *
     * @return the weighting
     */
    public Long getWeighting() {
        return weighting;
    }

    /**
     * Sets the weighting.
     *
     * @param weighting the weighting to set
     */
    public void setWeighting(final Long weighting) {
        this.weighting = weighting;
    }

    /**
     * Gets the courtSiteId.
     *
     * @return the courtSiteId
     */
    public Long getCourtSiteId() {
        return courtSiteId;
    }

    /**
     * Sets the courtSiteId.
     *
     * @param courtSiteId the courtSiteId to set
     */
    public void setCourtSiteId(Long courtSiteId) {
        this.courtSiteId = courtSiteId;
    }

    /**
     * Gets the macAddress.
     *
     * @return the macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Sets the macAddress.
     *
     * @param macAddress the macAddress to set
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * Gets the ipAddress.
     *
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the ipAddress.
     *
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    
}
