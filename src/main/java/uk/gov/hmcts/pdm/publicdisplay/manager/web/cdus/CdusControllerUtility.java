package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UrlDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IUrlService;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"PMD.CouplingBetweenObjects", "PMD.LooseCoupling", "squid:S5145"})
public class CdusControllerUtility {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CdusControllerUtility.class);

    protected static final String METHOD = "Method ";
    protected static final String THREE_PARAMS = "{}{}{}";
    protected static final String STARTS = " - starts";
    protected static final String ENDS = " - ends";
    protected static final String VIEW_SET = " view set";
    protected static final String SUCCESS_MESSAGE = "successMessage";
    protected static final String CDU_WITH_MACADDRESS = "CDU with mac Address : ";
    protected static final String UNABLE_TO_REGISTER_CDU = "Unable to register CDU: ";
    protected static final String CDU_ERRORS = "cduErrors";
    protected static final String COMMAND = "command";
    protected static final String CDU = "cdu";

    /** The Constant REQUEST_MAPPING. */
    protected static final String REQUEST_MAPPING = "/cdus";

    /** The Constant for the JSP Folder. */
    protected static final String FOLDER_CDUS = "cdus";
    
    /**
     * View Cdus Url.
     */
    protected static final String VIEW_CDU = "/cdus";

    /**
     * The Constant VIEW_NAME_CDUS.
     */
    protected static final String VIEW_NAME_CDUS = FOLDER_CDUS + VIEW_CDU;

    /**
     * Amend Cdus Url.
     */
    protected static final String AMEND_CDU = "/amend_cdu";

    /**
     * The Constant VIEW_NAME_AMEND_CDU.
     */
    protected static final String VIEW_NAME_AMEND_CDU = FOLDER_CDUS + AMEND_CDU;

    /**
     * Register Cdus Url.
     */
    protected static final String REGISTER_CDU = "/register_cdu";

    /**
     * The Constant VIEW_NAME_REGISTER_CDU.
     */
    protected static final String VIEW_NAME_REGISTER_CDU = FOLDER_CDUS + REGISTER_CDU;

    /**
     * Amend Url Mapping.
     */
    protected static final String MAPPING_REMOVE_URL = "/remove_url";

    /**
     * Add Url Mapping.
     */
    protected static final String MAPPING_ADD_URL = "/add_url";

    /**
     * Add Url.
     */
    protected static final String ADD_URL = FOLDER_CDUS + MAPPING_ADD_URL;

    /**
     * Remove Url.
     */
    protected static final String REMOVE_URL = FOLDER_CDUS + MAPPING_REMOVE_URL;

    /**
     * Cdu screenshot Mapping.
     */
    protected static final String MAPPING_CDU_SCREENSHOT = "/screenshot";

    /** The CduPageStateHolder. */
    @Autowired
    protected CduPageStateHolder cduPageStateHolder;

    /** The CduService class. */
    @Autowired
    protected ICduService cduService;

    /** The localProxyService class. */
    @Autowired
    protected ILocalProxyService localProxyService;

    /** Validator for CduSearchCommand class. */
    @Autowired
    protected CduSearchValidator cduSearchValidator;

    /** Validator for CduSearchCommand class. */
    @Autowired
    protected CduSearchSelectedValidator cduSearchSelectedValidator;

    /** Validator for CduAmendCommand class. */
    @Autowired
    protected CduAmendValidator cduAmendValidator;

    /** The restart all cdu validator. */
    @Autowired
    protected CduRestartAllValidator cduRestartAllValidator;

    /** The cdu unregister validator. */
    @Autowired
    protected CduUnregisterValidator cduUnregisterValidator;

    /** The add url mapping validator. */
    @Autowired
    protected MappingAddValidator mappingAddValidator;

    /** The mapping remove validator. */
    @Autowired
    protected MappingRemoveValidator mappingRemoveValidator;

    /** Validator for cdu register. */
    @Autowired
    protected CduRegisterValidator cduRegisterValidator;

    /**
     * Our Url service class, used to obtain lists of urls.
     */
    @Autowired
    protected IUrlService urlService;

    /**
     * Gets the media resource.
     *
     * @param bytes the bytes
     * @param mediaType the media type
     * @return the media resource
     */
    protected ResponseEntity<ByteArrayResource> getMediaResource(final byte[] bytes,
        final MediaType mediaType) {
        // Create response header for file
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentLength(bytes.length);

        // Create response from file and headers
        final ByteArrayResource resource = new ByteArrayResource(bytes);
        return new ResponseEntity<>(resource, headers, HttpStatus.CREATED);
    }

    /**
     * Gets the cdu list for the search criteria.
     *
     * @param cduSearchCommand the cdu search command
     * @return the cdu list
     */
    protected List<CduDto> getCduList(final CduSearchCommand cduSearchCommand) {
        final String methodName = "getCduList";
        final List<CduDto> cduList;
        if (StringUtils.isBlank(cduSearchCommand.getMacAddress())) {
            LOGGER.debug("{}{} Search CDUs Court Site ", METHOD, methodName);
            cduList = cduService.getCdusBySiteID(cduSearchCommand.getXhibitCourtSiteId());
            final String msg = cduList.isEmpty()
                ? "CDU search failed for " + cduSearchCommand.getXhibitCourtSiteId()
                : "CDUs search successful, " + cduList.size() + " possible matches for "
                    + cduSearchCommand.getXhibitCourtSiteId();
            LOGGER.debug(msg);
        } else {
            LOGGER.debug("{}{} Search CDU by MAC Address", METHOD, methodName);
            cduList = cduService.getCduByMacAddressWithLike(cduSearchCommand.getMacAddress());
            final String msg = cduList.isEmpty()
                ? "CDU partial match failed for " + cduSearchCommand.getMacAddress()
                : "CDUs partial match successful, " + cduList.size() + " possible matches for "
                    + cduSearchCommand.getMacAddress();
            LOGGER.debug(msg);
        }
        return cduList;
    }

    /**
     * Sets the selected cdu in page state holder.
     *
     * @param macAddress the mac address
     * @return the selected cdu
     */
    protected CduDto getSelectedCdu(final String macAddress) {
        CduDto selectedCdu = null;
        if (macAddress != null) {
            // Call to CduService to fetch Cdu's by Mac address
            final List<CduDto> cduList = cduPageStateHolder.getCdus();
            for (CduDto cdu : cduList) {
                if (cdu.getMacAddress().equals(macAddress)) {
                    selectedCdu = cdu;
                    break;
                }
            }
        }
        return selectedCdu;
    }

    /**
     * Sets the selected cdu in page state holder.
     *
     * @param macAddress the new selected cdu in page state holder
     * @return the selected cdu
     */
    protected CduDto populateSelectedCduInPageStateHolder(final String macAddress) {
        final String methodName = "populateSelectedCduInPageStateHolder ";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final CduDto selectedCdu = getSelectedCdu(macAddress);
        cduPageStateHolder.setCdu(selectedCdu);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return selectedCdu;
    }

    /**
     * Sets the cdu lists for the model.
     *
     * @param model the view
     */
    protected void setModelCduList(final ModelAndView model) {
        final String methodName = "setModelCduList";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Add the list of cdus to the model
        model.addObject("cduList", cduPageStateHolder.getCdus());

        // Set the list of court sites from the page holder
        model.addObject("courtSiteList", cduPageStateHolder.getSites());

        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * Remove the currently assigned urls from the list of urls available to the current cdu.
     *
     * @param xhibitCourtSiteId as long
     * @return filtered list of UrlDto objects
     */
    protected List<UrlDto> getAvailableUrlList(final Long xhibitCourtSiteId) {
        LOGGER.info("Retrieving urls by id : {}", xhibitCourtSiteId);
        final List<UrlDto> availableUrlList =
            urlService.getUrlsByXhibitCourtSiteId(xhibitCourtSiteId);
        LOGGER.info("returned url list contains : {} elements", availableUrlList.size());

        /*
         * Now remove the currently assigned url(s) from the list leaving only the urls available
         * for assignment
         */

        final Iterator<UrlDto> it = availableUrlList.iterator();
        while (it.hasNext()) {
            final UrlDto currentUrl = it.next();

            for (UrlDto url : cduPageStateHolder.getCdu().getUrls()) {

                if (url.getId().equals(currentUrl.getId())) {
                    /* Remove from the list */
                    LOGGER.debug("Removing url : {} from list.", currentUrl.getUrl());
                    it.remove();
                }
            }
        }
        LOGGER.info("new url list contains : {} elements", availableUrlList.size());
        return availableUrlList;
    }
    
    protected void setCduSearchCommand(final CduSearchCommand cduSearchCommand) {
        // Populate the sites list
        final List<XhibitCourtSiteDto> courtSiteList =
            localProxyService.getXhibitCourtSitesWithLocalProxy();
        cduPageStateHolder.setSites(courtSiteList);
        
        // Ensure the search command is the latest
        cduPageStateHolder.setCduSearchCommand(cduSearchCommand);
    }

    /**
     * Gets the search for cdu model.
     *
     * @param cduSearchCommand the cdu search command
     * @param result the result
     * @param model the model
     * @param resetSelectionCriteria the reset selection criteria
     */
    protected void setModelForCduSearch(final CduSearchCommand cduSearchCommand,
        final BindingResult result, final ModelAndView model,
        final boolean resetSelectionCriteria) {
        final String methodName = "setModelForCduSearch";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Ensure the search command is the latest
        setCduSearchCommand(cduSearchCommand);
        
        // Reset any previous search results and selected cdu
        cduPageStateHolder.setCdus(null);
        if (resetSelectionCriteria) {
            cduSearchCommand.setSelectedMacAddress(null);
        }

        // Business Specific validation for cdu search
        cduSearchValidator.validate(cduSearchCommand, result);
        if (!result.hasErrors()) {
            try {
                LOGGER.info("{}{} - no binding errors, starting search process", METHOD,
                    methodName);
                final List<CduDto> cduList = getCduList(cduSearchCommand);
                cduPageStateHolder.setCdus(cduList);
            } catch (final DataAccessException | XpdmException ex) {
                // Log the error
                LOGGER.error("{}{} Unable to retrieve Cdus", METHOD, methodName, ex);
                // Reject
                result.reject(CDU_ERRORS, "Unable to retrieve Cdus: " + ex.getMessage());
            }
        }

        setModelCduList(model);
        model.setViewName(VIEW_NAME_CDUS);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }
}
