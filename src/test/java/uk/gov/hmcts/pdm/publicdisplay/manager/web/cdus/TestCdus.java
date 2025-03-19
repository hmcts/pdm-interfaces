package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UrlDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class ShowRegisterCduTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
abstract class TestCdus extends CduExpectValidators {

    /** The Constant COURT_SITE_NAME. */
    protected static final String COURT_SITE_NAME = "COURTSITE";

    protected static final String COMMAND = "command";

    protected static final String MAC_ADDRESS = "macAddress";

    protected static final String LOCATION_STRING = "location";

    /** The cdus. */
    protected final List<CduDto> cdus = getTestCdus();

    /** The cdu. */
    protected final CduDto cdu = cdus.get(1);

    /** The url. */
    protected final UrlDto url = cdu.getUrls().get(0);

    /** The sites. */
    protected final List<XhibitCourtSiteDto> sites = getTestSites();

    /** The urls. */
    protected final List<UrlDto> urls = getTestUrls();

    /**
     * Gets the test cdu search command.
     *
     * @return the test cdu search command
     */
    protected CduSearchCommand getTestCduSearchCommand() {
        final CduSearchCommand cduSearchCommand = new CduSearchCommand();
        cduSearchCommand.setMacAddress(cdu.getMacAddress());
        cduSearchCommand.setSelectedMacAddress(cdu.getMacAddress());
        return cduSearchCommand;
    }

    /**
     * Assert cdu search model.
     *
     * @param modelMap the model map
     * @param isCduRequired the is cdu required
     */
    protected void assertCduSearchModel(final Map modelMap, final boolean isCduRequired) {
        final Object cduCommand = modelMap.get(COMMAND);
        assertTrue(cduCommand instanceof CduSearchCommand, FALSE);
        assertEquals(cdus, modelMap.get("cduList"), NOT_EQUAL);
        assertEquals(sites, modelMap.get("courtSiteList"), NOT_EQUAL);
        if (isCduRequired) {
            assertEquals(cdu, modelMap.get("cdu"), NOT_EQUAL);
        }
    }

    /**
     * Assert cdu register model.
     *
     * @param modelMap the model map
     */
    protected void assertCduRegisterModel(final Map modelMap) {
        final CduRegisterCommand cduCommand =
            (CduRegisterCommand) modelMap.get("cduRegisterCommand");
        assertTrue(cduCommand instanceof CduRegisterCommand, FALSE);
        assertEquals(cdu, modelMap.get(COMMAND), NOT_EQUAL);
        // test the notification filed default correctly
        assertEquals(courtSiteDto.getNotification(), cduCommand.getNotification(), NOT_EQUAL);

    }

    /**
     * Assert cdu amend model.
     *
     * @param modelMap the model map
     */
    protected void assertCduAmendModel(final Map modelMap) {
        final Object cduCommand = modelMap.get(COMMAND);
        assertTrue(cduCommand instanceof CduAmendCommand, FALSE);
        assertEquals(cdu.getLocation(), ((CduAmendCommand) cduCommand).getLocation(), NOT_EQUAL);
        assertEquals(cdu.getDescription(), ((CduAmendCommand) cduCommand).getDescription(),
            NOT_EQUAL);
        assertEquals(cdu.getNotification(), ((CduAmendCommand) cduCommand).getNotification(),
            NOT_EQUAL);
        assertEquals(cdu.getRefresh(), ((CduAmendCommand) cduCommand).getRefresh(), NOT_EQUAL);
        assertEquals(cdu.getWeighting(), ((CduAmendCommand) cduCommand).getWeighting(), NOT_EQUAL);
        assertEquals(cdu.getOfflineIndicator(),
            ((CduAmendCommand) cduCommand).getOfflineIndicator(), NOT_EQUAL);
        assertEquals(cdu, modelMap.get("cdu"), NOT_EQUAL);
    }

    /**
     * Expect set model cdu list.
     */
    protected void expectSetModelCduList() {
        expect(mockCduPageStateHolder.getCdus()).andReturn(cdus).anyTimes();
        expect(mockCduPageStateHolder.getSites()).andReturn(sites).anyTimes();
    }

    /**
     * Gets the test cdu.
     *
     * @param cduId the cdu id
     * @param macAddress the macAddress
     * @return the test cdu
     */
    protected CduDto getTestCdu(final Long cduId, final String macAddress) {
        final CduDto cdu = new CduDto();
        cdu.setId(cduId);
        cdu.setCduNumber("CDU000" + cduId.toString());
        cdu.setMacAddress(macAddress);
        cdu.setXhibitCourtSiteId(-123L);
        cdu.setLocation(LOCATION_STRING);
        cdu.setNotification("NOTIFICATION");
        cdu.setRefresh(Long.valueOf(30));
        cdu.setWeighting(Long.valueOf(1));
        cdu.setOfflineIndicator(AppConstants.NO_CHAR);
        cdu.setUrls(getTestUrls());
        return cdu;
    }

    /**
     * Gets the test cdus.
     *
     * @return the test cdus
     */
    protected List<CduDto> getTestCdus() {
        final List<CduDto> cdus = new ArrayList<>();
        cdus.add(getTestCdu(1L, "dummyMacAddress"));
        cdus.add(getTestCdu(2L, MAC_ADDRESS));
        return cdus;
    }

    /**
     * Gets the test sites.
     *
     * @return the test sites
     */
    protected List<XhibitCourtSiteDto> getTestSites() {
        final List<XhibitCourtSiteDto> sites = new ArrayList<>();
        sites.add(getTestSite(COURT_SITE_NAME + "1"));
        sites.add(getTestSite(COURT_SITE_NAME + "2"));
        return sites;
    }

    /**
     * Gets the test url.
     *
     * @param urlId the url id
     * @param urlString the url string
     * @return the test url
     */
    protected UrlDto getTestUrl(final Long urlId, final String urlString) {
        final UrlDto url = new UrlDto();
        url.setId(urlId);
        url.setUrl(urlString);
        return url;
    }

    /**
     * Gets the test urls.
     *
     * @return the test urls
     */
    protected List<UrlDto> getTestUrls() {
        final List<UrlDto> urls = new ArrayList<>();
        urls.add(getTestUrl(1L, "url1"));
        urls.add(getTestUrl(2L, "url2"));
        return urls;
    }

}
