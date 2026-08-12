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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CourtSiteJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CourtSiteStatusJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.MappingJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.UrlJson;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSiteWelsh;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient;

/**
 * The Class LocalProxyRestClient.
 *
 * @author boparaij
 */
@Component
public class LocalProxyRestClient extends LocalProxyRestCduFinder
    implements ILocalProxyRestClient {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalProxyRestClient.class);

    /** The Constant SITE_SAVE_SERVICE. */
    private static final String SAVE_SITE_SERVICE = "saveSite";

    /** The Constant SITE_SAVE_PATH. */
    private static final String SAVE_SITE_PATH = "/syncdata/site/save";

    /** The Constant SITE_DELETE_SERVICE. */
    private static final String DELETE_SITE_SERVICE = "deleteSite";

    /** The Constant SITE_DELETE_PATH. */
    private static final String DELETE_SITE_PATH = "/syncdata/site/delete";

    /** The Constant RAG_STATUS_SERVICE. */
    private static final String RAG_STATUS_SERVICE = "ragStatus";

    /** The Constant RAG_STATUS_PATH. */
    private static final String RAG_STATUS_PATH = "/ragstatus";

    /** The Constant DELETE_CDU_SERVICE. */
    private static final String DELETE_CDU_SERVICE = "deleteCDU";

    /** The Constant DELETE_CDU_PATH. */
    private static final String DELETE_CDU_PATH = "/syncdata/cdu/delete";

    /** The Constant SAVE_URL_SERVICE. */
    private static final String SAVE_URL_SERVICE = "saveURL";

    /** The Constant SAVE_URL_PATH. */
    private static final String SAVE_URL_PATH = "/syncdata/url/save";

    /** The Constant DELETE_URL_SERVICE. */
    private static final String DELETE_URL_SERVICE = "deleteURL";

    /** The Constant DELETE_URL_PATH. */
    private static final String DELETE_URL_PATH = "/syncdata/url/delete";

    /** The Constant SAVE_MAPPING_SERVICE. */
    private static final String SAVE_MAPPING_SERVICE = "saveMapping";

    /** The Constant SAVE_MAPPING_PATH. */
    private static final String SAVE_MAPPING_PATH = "/syncdata/mapping/save";

    /** The Constant DELETE_MAPPING_SERVICE. */
    private static final String DELETE_MAPPING_SERVICE = "deleteMapping";

    /** The Constant DELETE_MAPPING_PATH. */
    private static final String DELETE_MAPPING_PATH = "/syncdata/mapping/delete";


    /** The Constant SAVE_CDU_SERVICE. */
    private static final String SAVE_CDU_SERVICE = "saveCDU";

    /** The Constant SAVE_CDU_PATH. */
    private static final String SAVE_CDU_PATH = "/syncdata/cdu/save";

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#
     * saveLocalProxy(uk.gov. hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy,
     * boolean)
     */
    @Override
    public String saveLocalProxy(final ILocalProxy localProxy, final boolean updateNotification) {
        LOGGER.info("saveLocalProxy method starts");

        // Create JSON object and set user for auditing
        final CourtSiteJson courtSiteJson = new CourtSiteJson();
        courtSiteJson.setGeneratedBy(getUsername());

        // Set mandatory court site fields
        final ICourtSite courtSite = localProxy.getCourtSite();
        courtSiteJson.setSiteId(courtSite.getId());
        courtSiteJson.setTitle(courtSite.getTitle());
        courtSiteJson.setPageUrl(courtSite.getPageUrl());
        courtSiteJson.setPowersaveSchedule(courtSite.getSchedule().getDetail());
        if (updateNotification) {
            // notification is being set as part of registerLocalProxy or has been amended as part
            // of
            // amendLocalProxy
            courtSiteJson.setNotification(courtSite.getNotification());
        } else {
            // set notification on the courtSiteJson to null so ipdManager will not update all CDUs
            courtSiteJson.setNotification(null);
        }

        // Welsh title is an optional field
        final IXhibitCourtSiteWelsh welsh =
            courtSite.getXhibitCourtSite().getXhibitCourtSiteWelsh();
        if (welsh != null) {
            courtSiteJson.setWelshTitle(welsh.getCourtSiteName());
        }

        // Send rest request
        final CourtSiteJson courtSiteJsonWithHostName = this.sendRequest(localProxy,
            SAVE_SITE_SERVICE, SAVE_SITE_PATH, courtSiteJson, CourtSiteJson.class);
        LOGGER.info("saveLocalProxy method ends");
        return courtSiteJsonWithHostName.getHostName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#
     * deleteLocalProxy(uk.gov. hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy)
     */
    @Override
    public void deleteLocalProxy(final ILocalProxy localProxy) {
        LOGGER.info("deleteLocalProxy method starts");

        // Create json object and set user for auditing
        final CourtSiteJson courtSiteJson = new CourtSiteJson();
        courtSiteJson.setGeneratedBy(getUsername());

        // Set mandatory court site fields for a delete
        final ICourtSite courtSite = localProxy.getCourtSite();
        courtSiteJson.setSiteId(courtSite.getId());

        // Send rest request
        this.sendRequest(localProxy, DELETE_SITE_SERVICE, DELETE_SITE_PATH, courtSiteJson);
        LOGGER.info("deleteLocalProxy method ends");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#saveCdu(
     * uk. gov.hmcts.pdm.publicdisplay.manager.domain.api.ICdu)
     */
    @Override
    public void saveCdu(final ICduModel cdu) {
        LOGGER.info("saveCdu method starts");
        final ILocalProxy localProxy = cdu.getCourtSite().getLocalProxy();

        // Create json object and set user for auditing
        final CduJson cduJson = new CduJson();
        cduJson.setGeneratedBy(getUsername());

        // Set mandatory cdu fields
        cduJson.setSiteId(cdu.getCourtSite().getId());
        cduJson.setCduNumber(cdu.getCduNumber());
        cduJson.setTitle(cdu.getTitle());
        cduJson.setDescription(cdu.getDescription());
        cduJson.setLocation(cdu.getLocation());
        cduJson.setRefresh(cdu.getRefresh());
        cduJson.setIpAddress(cdu.getIpAddress());
        cduJson.setMacAddress(cdu.getMacAddress());
        cduJson.setNotification(cdu.getNotification());
        cduJson.setOfflineIndicator(cdu.getOfflineIndicator());

        // Send rest request
        this.sendRequest(localProxy, SAVE_CDU_SERVICE, SAVE_CDU_PATH, cduJson);
        LOGGER.info("saveCdu method ends");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#deleteCdu(
     * uk .gov.hmcts.pdm.publicdisplay.manager.domain.api.ICdu)
     */
    @Override
    public void deleteCdu(final ICduModel cdu) {
        LOGGER.info("deleteCdu method starts");
        final ILocalProxy localProxy = cdu.getCourtSite().getLocalProxy();

        // Create json object and set user for auditing
        final CduJson cduJson = new CduJson();
        cduJson.setGeneratedBy(getUsername());

        // Set mandatory cdu fields for delete
        cduJson.setMacAddress(cdu.getMacAddress());

        // Send rest request
        this.sendRequest(localProxy, DELETE_CDU_SERVICE, DELETE_CDU_PATH, cduJson);
        LOGGER.info("deleteCdu method ends");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#saveUrl(
     * uk. gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrl)
     */
    @Override
    public void saveUrl(final IUrlModel url) {
        LOGGER.info("saveUrl method starts");
        final ILocalProxy localProxy = url.getXhibitCourtSite().getCourtSite().getLocalProxy();

        // Create json object and set user for auditing
        final UrlJson urlJson = new UrlJson();
        urlJson.setGeneratedBy(getUsername());

        // Set mandatory url fields
        urlJson.setUniqueUrlId(url.getId());
        urlJson.setUrl(url.getUrl());
        urlJson.setDescription(url.getDescription());

        this.sendRequest(localProxy, SAVE_URL_SERVICE, SAVE_URL_PATH, urlJson);
        LOGGER.info("saveUrl method ends");
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#
     * saveMapping( uk.gov. hmcts.pdm.publicdisplay.manager.domain.api.ICdu,
     * uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrl)
     */
    @Override
    public void saveMapping(final ICduModel cdu, final IUrlModel url) {
        LOGGER.info("saveMapping method starts");
        final ILocalProxy localProxy = cdu.getCourtSite().getLocalProxy();

        // Create json object and set user for auditing
        final MappingJson mappingJson = new MappingJson();
        mappingJson.setGeneratedBy(getUsername());

        // Set mandatory mapping fields
        mappingJson.setUniqueUrlId(url.getId());
        mappingJson.setMacAddress(cdu.getMacAddress());

        this.sendRequest(localProxy, SAVE_MAPPING_SERVICE, SAVE_MAPPING_PATH, mappingJson);
        LOGGER.info("saveMapping method ends");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#deleteUrl(
     * uk .gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrl)
     */
    @Override
    public void deleteUrl(final IUrlModel url) {
        LOGGER.info("deleteUrl method starts");
        final ILocalProxy localProxy = url.getXhibitCourtSite().getCourtSite().getLocalProxy();

        // Create json object and set user for auditing
        final UrlJson urlJson = new UrlJson();
        urlJson.setGeneratedBy(getUsername());

        // Set mandatory url fields for delete
        urlJson.setUniqueUrlId(url.getId());

        this.sendRequest(localProxy, DELETE_URL_SERVICE, DELETE_URL_PATH, urlJson);
        LOGGER.info("deleteUrl method ends");
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#
     * deleteMapping(uk.gov. hmcts.pdm.publicdisplay.manager.domain.api.ICdu,
     * uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrl)
     */
    @Override
    public void deleteMapping(final ICduModel cdu, final IUrlModel url) {
        LOGGER.info("deleteMapping method starts");
        final ILocalProxy localProxy = cdu.getCourtSite().getLocalProxy();

        // Create json object and set user for auditing
        final MappingJson mappingJson = new MappingJson();
        mappingJson.setGeneratedBy(getUsername());

        // Set mandatory mapping fields
        mappingJson.setUniqueUrlId(url.getId());
        mappingJson.setMacAddress(cdu.getMacAddress());

        this.sendRequest(localProxy, DELETE_MAPPING_SERVICE, DELETE_MAPPING_PATH, mappingJson);
        LOGGER.info("deleteMapping method ends");
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#
     * getCourtSiteStatus()
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public CourtSiteStatusJson getCourtSiteStatus(final ILocalProxy localProxy) {
        LOGGER.info("getCourtSiteStatus method starts");
        final ICourtSite courtSite = localProxy.getCourtSite();

        // Create json object and set user for auditing
        final CourtSiteJson courtSiteJson = new CourtSiteJson();
        courtSiteJson.setGeneratedBy(getUsername());

        // Set mandatory status fields
        courtSiteJson.setSiteId(courtSite.getId());

        // Send rest request and return updated json
        final CourtSiteStatusJson courtSiteStatusJson = this.sendRequest(localProxy,
            RAG_STATUS_SERVICE, RAG_STATUS_PATH, courtSiteJson, CourtSiteStatusJson.class);
        LOGGER.info("getCourtSiteStatus method ends");
        return courtSiteStatusJson;
    }

}
