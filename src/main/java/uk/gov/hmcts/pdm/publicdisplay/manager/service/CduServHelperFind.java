package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduJson;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICduModel;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings("PMD.LawOfDemeter")
public class CduServHelperFind extends CduServHelperCrud {

    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CduServHelperFind.class);
    private static final String FAKE_TITLE = "FAKE TITLE";
    private static final String THIS_DOES_NOT_EXIST = "THIS DOES NOT EXIST";
    private static final String XHIBIT = "XHIBIT";

    @Value("#{'${fakeIPs}'.split(',')}")
    private List<String> ipValues;


    public CduDto getCduByCduId(final int cduId) {
        final String methodName = "getCduByCduId ";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        
        // First get the CduModel
        final ICduModel cdu = getXhbDispMgrCduRepository().findByCduId(cduId);
        // Convert this into a CduDto
        final CduDto cduDto = createCduDto(cdu);
        cduDto.setRegisteredIndicator(AppConstants.YES_CHAR);
        
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return cduDto;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#
     * getCduByMacAddressWithLike(java.lang. String)
     */
    public List<CduDto> getCduByMacAddressWithLike(final String macAddress) {
        final String methodName = "getCduByMacAddressWithLike ";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        final List<ICduModel> cdus =
            getXhbDispMgrCduRepository().findByMacAddressWithLike(macAddress);
        final List<CduDto> cduDtoList = new ArrayList<>();
        for (ICduModel cdu : cdus) {
            final CduDto cduDto = createCduDto(cdu);
            cduDto.setRegisteredIndicator(AppConstants.YES_CHAR);
            cduDtoList.add(cduDto);
        }

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);

        return cduDtoList;

    }

    /*
     * (non-Javadoc)
     *
     * @param xhibitCourtSiteId
     *
     * @see
     * uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService#getCdusBySiteID(
     * java. lang.Long)
     */
    public List<CduDto> getCdusBySiteID(final Long xhibitCourtSiteId) {
        final String methodName = "getCduBySiteID";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // get the cdus that are registered within the central server for the supplied court
        ICourtSite courtSite = getCourtSiteById(xhibitCourtSiteId.intValue());
        List<ICduModel> centralServerCduList = getCentralServerCdus(courtSite);
        Map<String, CduDto> cdusMap = createCduDtoMap(centralServerCduList);

        List<CduJson> localProxyCduList = getLocalProxyCdus(courtSite);

        updateCdusMapWithLocalProxyData(cdusMap, localProxyCduList, courtSite, methodName,
            centralServerCduList);

        // Create combined list of cdus and then sort them by mac address
        List<CduDto> cdusList = new ArrayList<>(cdusMap.values());
        sortCdusListByMacAddress(cdusList);

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return cdusList;
    }

    protected ICourtSite getCourtSiteById(int xhibitCourtSiteId) {
        return getXhbCourtSiteRepository().findCourtSiteByXhibitCourtSiteId(xhibitCourtSiteId);
    }

    protected List<ICduModel> getCentralServerCdus(ICourtSite courtSite) {
        return new ArrayList<>(courtSite.getCdus());
    }

    protected Map<String, CduDto> createCduDtoMap(List<ICduModel> centralServerCduList) {

        // create a hash map for the cdus to return as a List. This will be populated with cdus to
        // display on the jsp

        @SuppressWarnings("unchecked")
        Map<String, CduDto> cdusMap = new ConcurrentHashMap<>();

        // start by adding registered cdus from the central database. Assume correct and update
        // later if
        // not

        for (ICduModel cdu : centralServerCduList) {
            CduDto cduDto = createCduDto(cdu);
            cduDto.setRegisteredLocalProxy(AppConstants.YES_CHAR);
            cduDto.setRegisteredIndicator(AppConstants.YES_CHAR);
            cdusMap.put(cdu.getMacAddress(), cduDto);
        }
        return cdusMap;
    }

    protected List<CduJson> getLocalProxyCdus(ICourtSite courtSite) {
        if (localProxyCommunicationEnabled && !fakeCdusEnabled) {
            return localProxyRestClient.getCdus(courtSite.getLocalProxy());
        } else {
            return getFakeCdus();
        }
    }

    // Fake CDU data for development purposes
    public List<CduJson> getFakeCdus() {
        final String methodName = "getFakeCdus";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        /************* Start of dummy data ***********************/
        final List<CduJson> cdus = new ArrayList<>();
        List<CduJson> dummyCduJsonList = createCduJsonList();

        cdus.addAll(dummyCduJsonList.stream().limit(2).collect(Collectors.toList()));

        if (fakeCdusRegisterEnabled) {
            cdus.addAll(dummyCduJsonList.stream().skip(2).collect(Collectors.toList()));
        }
        /************* End of dummy data ***********************/
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return cdus;

    }

    private List<CduJson> createCduJsonList() {

        CduJson cduJson1 = createCduJson("CDU_1", "00:00:00:00:00:01", ipValues.get(0), FAKE_TITLE,
            "CROWN COURT", "NOWHERE", "FAKE NOTIFICATION", Long.valueOf(123));
        populateCduJson(cduJson1, 'N', 'G', Long.valueOf(1), "FAKE_USERNAME", 'Y');

        CduJson cduJson2 = createCduJson("CDU_2", "00:00:00:00:00:02", ipValues.get(1), FAKE_TITLE,
            "THE WAITING ROOM", "BRISTOL", THIS_DOES_NOT_EXIST, Long.valueOf(123));
        populateCduJson(cduJson2, 'N', 'G', Long.valueOf(1), "FAKE_USERNAME", 'Y');

        CduJson cduJson3 = createCduJson("CDU_20", "00:00:00:00:00:20", ipValues.get(2), FAKE_TITLE,
            "THE EATING ROOM", "BIRMINGHAM - CANTEEN", THIS_DOES_NOT_EXIST, Long.valueOf(123));
        populateCduJson(cduJson3, 'N', 'G', Long.valueOf(1), XHIBIT, 'N');

        CduJson cduJson4 = createCduJson("CDU_21", "00:00:00:00:00:21", ipValues.get(3), FAKE_TITLE,
            "THE DRINKING ROOM", "BIRMINGHAM - BAR", THIS_DOES_NOT_EXIST, Long.valueOf(123));
        populateCduJson(cduJson4, 'N', 'G', Long.valueOf(1), XHIBIT, 'N');

        CduJson cduJson5 = createCduJson("CDU_22", "00:00:00:00:00:22", ipValues.get(4), FAKE_TITLE,
            "THE SELFRIDGES ROOM", "BIRMINGHAM - BULLRING", THIS_DOES_NOT_EXIST, Long.valueOf(123));
        populateCduJson(cduJson5, 'N', 'G', Long.valueOf(1), XHIBIT, 'N');

        CduJson cduJson6 = createCduJson("CDU_23", "00:00:00:00:00:23", ipValues.get(5), FAKE_TITLE,
            "THE BANKRUPT ROOM", "BIRMINGHAM - CITY COUNCIL", THIS_DOES_NOT_EXIST,
            Long.valueOf(123));
        populateCduJson(cduJson6, 'N', 'G', Long.valueOf(1), XHIBIT, 'N');

        CduJson cduJson7 = createCduJson("CDU_24", "00:00:00:00:00:24", ipValues.get(6), FAKE_TITLE,
            "THE TRAIN ROOM", "BIRMINGHAM - NEW STREET", THIS_DOES_NOT_EXIST, Long.valueOf(123));
        populateCduJson(cduJson7, 'N', 'G', Long.valueOf(1), XHIBIT, 'N');

        CduJson cduJson8 = createCduJson("CDU_25", "00:00:00:00:00:25", ipValues.get(7), FAKE_TITLE,
            "THE JEWELLERY ROOM", "BIRMINGHAM - JEWELLERY QUARTER", THIS_DOES_NOT_EXIST,
            Long.valueOf(123));
        populateCduJson(cduJson8, 'N', 'G', Long.valueOf(1), XHIBIT, 'N');

        CduJson cduJson9 = createCduJson("CDU_25", "00:00:00:00:00:25", ipValues.get(7), FAKE_TITLE,
            "THE JEWELLERY ROOM", "BIRMINGHAM - JEWELLERY QUARTER", THIS_DOES_NOT_EXIST,
            Long.valueOf(123));
        populateCduJson(cduJson9, 'N', 'G', Long.valueOf(1), XHIBIT, 'N');

        CduJson cduJson10 = createCduJson("CDU_26", "00:00:00:00:00:26", ipValues.get(8),
            FAKE_TITLE, "THE FORGOTTEN ROOM", "BIRMINGHAM - MOOR STREET", THIS_DOES_NOT_EXIST,
            Long.valueOf(123));
        populateCduJson(cduJson10, 'N', 'G', Long.valueOf(1), XHIBIT, 'N');
        
        List<CduJson> cdus = new ArrayList<>();

        cdus.add(cduJson1);
        cdus.add(cduJson2);
        cdus.add(cduJson3);
        cdus.add(cduJson4);
        cdus.add(cduJson5);
        cdus.add(cduJson6);
        cdus.add(cduJson7);
        cdus.add(cduJson8);
        cdus.add(cduJson9);
        cdus.add(cduJson10);

        return cdus;
    }

    // Fake screenshot for development purposes
    protected byte[] getFakeCduScreenshot() {
        final String methodName = "getFakeCduScreenshot";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        byte[] dummyScreenshot = null;
        try {
            dummyScreenshot = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("dummydata/cduimage.png").readAllBytes();
        } catch (IOException ex) {
            LOGGER.error("Exception occurred reading dummy screenshot", ex);
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return dummyScreenshot;
    }

    protected void sortCdusListByMacAddress(List<CduDto> cdusList) {
        Collections.sort(cdusList, (cdu1, cdu2) -> String.CASE_INSENSITIVE_ORDER
            .compare(cdu1.getMacAddress(), cdu2.getMacAddress()));

    }
}
