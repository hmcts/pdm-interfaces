package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.court.CourtAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.court.CourtCreateCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CourtService extends CourtServiceFinder implements ICourtService {

    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CourtService.class);
    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String FOUR_PARAMS = "{}{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";
    private static final String YES = "Y";
    private static final String DISPLAYNAME_PREFIX = "Court Site ";

    /**
     * Gets the court.
     *
     * @return the court
     */
    @Override
    public List<CourtDto> getCourts() {
        final String methodName = "getCourts";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<CourtDto> resultList = new ArrayList<>();
        final List<XhbCourtDao> courtList = getXhbCourtRepository().findAll();
        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Courts returned : ", courtList.size());

        if (!courtList.isEmpty()) {
            // Transfer each court to a dto and save in resultList
            for (XhbCourtDao court : courtList) {
                if (YES.equals(court.getObsInd())) {
                    continue;
                }
                LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - transferring court to dto");
                final CourtDto dto = createCourtDto();

                // need the court details from the main court in 'xhb_court' table
                dto.setId(court.getCourtId());
                dto.setCourtName(court.getCourtName());
                dto.setAddressId(court.getAddressId());
                LOGGER.debug("dto id : {}", dto.getId());
                LOGGER.debug("dto courtName: {}", dto.getCourtName());
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getCourtName(), obj2.getCourtName()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }

    /**
     * Gets the court sites.
     *
     * @return the court sites
     */
    @Override
    public List<XhibitCourtSiteDto> getCourtSites(Integer courtId) {
        final String methodName = "getCourtSites";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<XhibitCourtSiteDto> resultList = new ArrayList<>();
        final List<XhbCourtSiteDao> xhibitCourtSiteList =
            getXhbCourtSiteRepository().findByCourtId(courtId);

        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Court sites returned : ",
            xhibitCourtSiteList.size());

        if (!xhibitCourtSiteList.isEmpty()) {
            // Transfer each court site to a dto and save in resultList
            for (XhbCourtSiteDao xhibitCourtSite : xhibitCourtSiteList) {
                if (YES.equals(xhibitCourtSite.getObsInd())) {
                    continue;
                }
                LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - transferring court site to dto");
                final XhibitCourtSiteDto dto = createXhibitCourtSiteDto();

                // need the court site details from the main court site in 'xhb_court_site' table
                dto.setId(xhibitCourtSite.getId().longValue());
                dto.setCourtSiteName(xhibitCourtSite.getCourtSiteName());
                dto.setCourtSiteCode(xhibitCourtSite.getCourtSiteCode());
                dto.setCourtId(xhibitCourtSite.getCourtId());
                LOGGER.debug("dto id : {}", dto.getId());
                LOGGER.debug("dto courtSiteName: {}", dto.getCourtSiteName());
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getCourtSiteName(), obj2.getCourtSiteName()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }

    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createCourt(final CourtCreateCommand command, Integer courtId, Integer addressId) {

        final String methodName = "createCourt";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Create the courtSiteDao
        XhbCourtSiteDao courtSiteDao = new XhbCourtSiteDao();
        courtSiteDao.setCourtId(courtId);
        courtSiteDao.setAddressId(addressId);
        courtSiteDao.setCourtSiteName(command.getCourtSiteName().toUpperCase(Locale.getDefault()));
        courtSiteDao.setCourtSiteCode(command.getCourtSiteCode().toUpperCase(Locale.getDefault()));
        courtSiteDao.setDisplayName(getDisplayName(courtSiteDao.getCourtSiteCode()));
        courtSiteDao.setObsInd(command.getObsInd());

        // Save
        getXhbCourtSiteRepository().saveDao(courtSiteDao);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateCourt(final CourtAmendCommand command) {

        final String methodName = "updateCourt";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbCourtSiteDao> existingDao =
            getXhbCourtSiteRepository().findById(command.getXhibitCourtSiteId().intValue());
        if (existingDao.isPresent()) {
            XhbCourtSiteDao courtSiteDao = existingDao.get();

            courtSiteDao
                .setCourtSiteName(command.getCourtSiteName().toUpperCase(Locale.getDefault()));
            courtSiteDao
                .setCourtSiteCode(command.getCourtSiteCode().toUpperCase(Locale.getDefault()));
            courtSiteDao.setDisplayName(getDisplayName(courtSiteDao.getCourtSiteCode()));
            courtSiteDao.setObsInd(command.getObsInd());

            // Update
            getXhbCourtSiteRepository().updateDao(courtSiteDao);
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    private String getDisplayName(String courtSiteCode) {
        StringBuilder displayNameSb = new StringBuilder();
        displayNameSb.append(DISPLAYNAME_PREFIX).append(courtSiteCode);
        return displayNameSb.toString();
    }
}
