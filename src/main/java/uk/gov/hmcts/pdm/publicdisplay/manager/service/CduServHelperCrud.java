package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduJson;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUrlModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UrlDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CduServHelperCrud extends CduServHelperRepos {

    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CduServHelperCrud.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    private static final String FIVE_PARAMS = "{}{}{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";
    private static final String CDU = " - cdu ";
    protected static final Character NO_CHAR = 'N';
    private static final Character YES_CHAR = 'Y';

    /**
     * Creates the cdu dto.
     *
     * @param cdu the cdu
     * @return the cdu dto
     */
    protected CduDto createCduDto(final ICduModel cdu) {
        final CduDto cduDto = new CduDto();
        cduDto.setCduNumber(cdu.getCduNumber());
        cduDto.setCourtSiteName(cdu.getCourtSite().getXhibitCourtSite().getCourtSiteName());
        cduDto.setCourtSiteId(cdu.getCourtSite().getId());
        cduDto.setCourtSitePageUrl(cdu.getCourtSite().getPageUrl());
        cduDto.setDescription(cdu.getDescription());
        cduDto.setId(cdu.getId());
        cduDto.setIpAddress(cdu.getIpAddress());
        cduDto.setLocation(cdu.getLocation());
        cduDto.setMacAddress(cdu.getMacAddress());
        cduDto.setXhibitCourtSiteId(cdu.getCourtSite().getXhibitCourtSite().getId());
        cduDto.setNotification(cdu.getNotification());
        cduDto.setRefresh(cdu.getRefresh());
        cduDto.setWeighting(cdu.getWeighting());
        cduDto.setOfflineIndicator(cdu.getOfflineIndicator());
        final List<UrlDto> urlList = new ArrayList<>();
        for (IUrlModel url : cdu.getUrls()) {
            final UrlDto urlDto = getUrlDto();
            urlDto.setId(url.getId());
            urlDto.setDescription(url.getDescription());
            urlDto.setUrl(url.getUrl());
            urlList.add(urlDto);
        }

        cduDto.getUrls().addAll(urlList);

        // assume cdu registered correctly as ICdu implies already registered centrally.
        cduDto.setRegisteredCentrally(AppConstants.YES_CHAR);

        return cduDto;
    }
    
    /**
     * Creates the cdu dto.
     *
     * @param cdu the cdu
     * @param courtSite the court site
     * @return the cdu dto
     */
    protected CduDto createCduDto(final CduJson cdu, final ICourtSite courtSite) {
        final CduDto cduDto = new CduDto();
        cduDto.setCduNumber(cdu.getCduNumber());
        cduDto.setCourtSiteId(courtSite.getId());
        cduDto.setCourtSiteName(courtSite.getXhibitCourtSite().getCourtSiteName());
        cduDto.setDescription(cdu.getDescription());
        cduDto.setIpAddress(cdu.getIpAddress());
        cduDto.setLocation(cdu.getLocation());
        cduDto.setMacAddress(cdu.getMacAddress());
        cduDto.setRegisteredIndicator(cdu.getRegisteredIndicator());
        return cduDto;
    }
    
    private UrlDto getUrlDto() {
        return new UrlDto();
    }

    protected CduJson createCduJson(String cduNumber, String macAddress, String ipAddress,
        String title, String description, String location, String notification, Long refresh) {
        final String methodName = "createCduJson";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        CduJson cduJson = new CduJson();

        cduJson.setCduNumber(cduNumber);
        cduJson.setMacAddress(macAddress);
        cduJson.setIpAddress(ipAddress);
        cduJson.setTitle(title);
        cduJson.setDescription(description);
        cduJson.setLocation(location);
        cduJson.setNotification(notification);
        cduJson.setRefresh(refresh);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return cduJson;
    }

    protected void populateCduJson(CduJson cduJson, Character offlineIndicator, Character ragStatus,
        Long siteId, String generatedBy,
        Character registeredIndicator) {

        cduJson.setOfflineIndicator(offlineIndicator);
        cduJson.setRagStatus(ragStatus);
        cduJson.setSiteId(siteId);
        cduJson.setGeneratedBy(generatedBy);
        cduJson.setRegisteredIndicator(registeredIndicator);
    }



    protected void updateCdusMapWithLocalProxyData(Map<String, CduDto> cdusMap,
        List<CduJson> localProxyCduList, ICourtSite courtSite, String methodName,
        List<ICduModel> centralServerCduList) {

        /*
         * Add the CDUs from the local proxy server to a map to use in identifying if a cdu is known
         * to the central database and also update the return list appropriately.
         */

        @SuppressWarnings("unchecked")
        Map<String, CduJson> cdusFromLocalProxyMap = new ConcurrentHashMap<>();
        for (CduJson localProxyJsonCdu : localProxyCduList) {
            if (cdusMap.containsKey(localProxyJsonCdu.getMacAddress())) {
                handleLocalProxyCduExistingInCentral(cdusMap, localProxyJsonCdu, methodName);
            } else {
                handleLocalProxyCduNotInCentral(cdusMap, localProxyJsonCdu, courtSite,
                    cdusFromLocalProxyMap, methodName);
            }

            /**
             * add cdu to a hashmap of local proxy cdus. This is used to determine when a cdu is
             * registered centrally but not known on the local proxy
             */

            cdusFromLocalProxyMap.put(localProxyJsonCdu.getMacAddress(), localProxyJsonCdu);
        }

        handleCdusRegisteredCentrallyButNotInLocal(cdusMap, courtSite, cdusFromLocalProxyMap,
            methodName, centralServerCduList);
    }

    protected void handleLocalProxyCduExistingInCentral(Map<String, CduDto> cdusMap,
        CduJson localProxyJsonCdu, String methodName) {
        // cdu registered centrally, but unregistered on local proxy
        CduDto centralServerCduDto = cdusMap.get(localProxyJsonCdu.getMacAddress());

        if (NO_CHAR.equals(localProxyJsonCdu.getRegisteredIndicator())) {
            centralServerCduDto.setRegisteredLocalProxy(NO_CHAR);
            centralServerCduDto.setRegisteredIndicator(NO_CHAR);

            LOGGER.warn(FIVE_PARAMS, METHOD, methodName, CDU, localProxyJsonCdu.getMacAddress(),
                " registered centrally, but unregistered on local proxy.");
        }
    }


    protected void handleLocalProxyCduNotInCentral(Map<String, CduDto> cdusMap,
        CduJson localProxyJsonCdu, ICourtSite courtSite, Map<String, CduJson> cdusFromLocalProxyMap,
        String methodName) {
        CduDto localProxyCduDto = createCduDto(localProxyJsonCdu, courtSite);
        if (YES_CHAR.equals(localProxyJsonCdu.getRegisteredIndicator())) {
            localProxyCduDto.setRegisteredCentrally(NO_CHAR);
            localProxyCduDto.setRegisteredLocalProxy(YES_CHAR);
            localProxyCduDto.setRegisteredIndicator(NO_CHAR);
            LOGGER.warn(FIVE_PARAMS, METHOD, methodName, CDU, localProxyJsonCdu.getMacAddress(),
                " not registered centrally, but registered on local proxy");
        } else {

            /*
             * cdu not registered centrally and cdu not registered on local proxy and
             * registeredindicator is set to 'N'. This is correct for a non registered cdu
             */

            localProxyCduDto.setRegisteredCentrally(NO_CHAR);
            localProxyCduDto.setRegisteredLocalProxy(NO_CHAR);
        }
        cdusMap.put(localProxyCduDto.getMacAddress(), localProxyCduDto);
    }

    protected void handleCdusRegisteredCentrallyButNotInLocal(Map<String, CduDto> cdusMap,
        ICourtSite courtSite, Map<String, CduJson> cdusFromLocalProxyMap, String methodName,
        List<ICduModel> centralServerCduList) {
        for (ICduModel cdu : centralServerCduList) {
            if (!cdusFromLocalProxyMap.containsKey(cdu.getMacAddress())) {
                CduDto cduDto = createCduDto(cdu);
                cduDto.setRegisteredIndicator(YES_CHAR);
                cduDto.setRegisteredLocalProxy(NO_CHAR);
                cdusMap.put(cdu.getMacAddress(), cduDto);
                LOGGER.warn(FIVE_PARAMS, METHOD, methodName, CDU, cdu.getMacAddress(),
                    " registered centrally, but not known to the local proxy.");
            }
        }
    }

}
