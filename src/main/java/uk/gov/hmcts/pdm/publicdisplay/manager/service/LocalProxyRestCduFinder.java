package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.AuthorizationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduJson;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CourtSiteJson;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class LocalProxyRestCduFinder extends LocalProxyRestClientRequest {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalProxyRestCduFinder.class);

    /** The Constant SCREENSHOT_CDU_SERVICE. */
    private static final String SCREENSHOT_CDU_SERVICE = "screenshotCDU";

    /** The Constant SCREENSHOT_CDU_PATH. */
    private static final String SCREENSHOT_CDU_PATH = "/screenshot";

    /** The Constant LIST_CDU_SERVICE. */
    private static final String LIST_CDU_SERVICE = "listCDU";

    /** The Constant LIST_CDU_PATH. */
    private static final String LIST_CDU_PATH = "/listcdu";

    /** The Constant RESTART_CDU_SERVICE. */
    private static final String RESTART_CDU_SERVICE = "restartCDU";

    /** The Constant RESTART_CDU_PATH. */
    private static final String RESTART_CDU_PATH = "/restartcdu";

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";


    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#getCdus()
     */
    public List<CduJson> getCdus(final ILocalProxy localProxy) {
        LOGGER.info("getCdus method starts");
        final ICourtSite courtSite = localProxy.getCourtSite();

        // Create json object and set user for auditing
        final CourtSiteJson courtSiteJson = new CourtSiteJson();
        courtSiteJson.setGeneratedBy(getUsername());

        // Set mandatory court site fields
        courtSiteJson.setSiteId(courtSite.getId());

        final List<CduJson> cdus = Arrays.asList(this.sendRequest(localProxy, LIST_CDU_SERVICE,
            LIST_CDU_PATH, courtSiteJson, CduJson[].class));

        LOGGER.info("getCdus method ends");
        return cdus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient# restartCdu(
     * uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy, java.util.List)
     */
    public void restartCdu(final ILocalProxy localProxy, final List<String> ipAddresses) {
        final String methodName = "restartCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, " - starts");

        // Create json object for the associated CDU IP addresses
        final List<CduJson> cduJsons = new ArrayList<>();
        for (String ipAddress : ipAddresses) {
            // Create json object and set user for auditing
            final CduJson cduJson = createCduJson();
            cduJson.setGeneratedBy(getUsername());
            // Set mandatory fields for cdu restart
            cduJson.setIpAddress(ipAddress);
            // Add the object to the array
            cduJsons.add(cduJson);
        }

        // Send rest request
        this.sendRequest(localProxy, RESTART_CDU_SERVICE, RESTART_CDU_PATH, cduJsons);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, " - ends");
    }

    protected CduJson createCduJson() {
        return new CduJson();
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyRestClient#
     * getCduScreenshot(uk.gov. hmcts.pdm.publicdisplay.manager.domain.api.ILocalProxy,
     * java.lang.String)
     */
    public byte[] getCduScreenshot(final ILocalProxy localProxy, final String ipAddress) {
        final String methodName = "getCduScreenshot";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, " - starts");

        // Create json object and set user for auditing
        final CduJson cduJson = new CduJson();
        cduJson.setGeneratedBy(getUsername());

        // Set mandatory screenshot fields
        cduJson.setIpAddress(ipAddress);

        // Send rest request and return bytes
        // TODO We need ipdmanager for this to work, use dummy data for now
        return this.sendRequest(localProxy, SCREENSHOT_CDU_SERVICE, SCREENSHOT_CDU_PATH, cduJson,
            byte[].class);
    }

    /**
     * Get the username of the current logged on user.
     * 
     * @return username
     */
    protected String getUsername() {
        // Get the Spring security authentication
        // object which has the username of the current logged on user
        if (!"".equals(AuthorizationUtil.getUsername())) {
            return AuthorizationUtil.getUsername();
        }
        // Default username is XHIBIT
        return "XHIBIT";
    }

}
