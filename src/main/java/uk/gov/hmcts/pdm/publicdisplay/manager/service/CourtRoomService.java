package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtroom.XhbCourtRoomDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtRoomService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom.CourtRoomAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom.CourtRoomCreateCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom.CourtRoomDeleteCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CourtRoomService extends CourtRoomServiceCreator implements ICourtRoomService {

    /**
     * Set up our logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CourtRoomService.class);
    private static final String EMPTY_STRING = "";
    private static final String REGEXP_NUMBERS = "[^0-9]";
    private static final Integer ROOM_NO_LIMIT = 99;

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
     * Retrieves all court rooms.
     * 
     * @return List
     */
    @Override
    public List<CourtRoomDto> getCourtRooms(final Long xhibitCourtSiteId) {
        final String methodName = "getCourtRooms";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        final List<CourtRoomDto> resultList = new ArrayList<>();
        final List<XhbCourtRoomDao> xhibitCourtRoomList =
            getXhbCourtRoomRepository().findByCourtSiteId(xhibitCourtSiteId.intValue());

        LOGGER.debug(FOUR_PARAMS, METHOD, methodName, " - Court Rooms returned : ",
            xhibitCourtRoomList.size());

        if (!xhibitCourtRoomList.isEmpty()) {
            // Transfer each court room to a dto and save in resultList
            for (XhbCourtRoomDao xhibitCourtRoom : xhibitCourtRoomList) {
                if (YES.equals(xhibitCourtRoom.getObsInd())) {
                    continue;
                }
                LOGGER.debug(THREE_PARAMS, METHOD, methodName, " - transferring court site to dto");
                final CourtRoomDto dto = createCourtRoomDto();

                // need the court site details from the main court site in 'xhb_court_site' table
                dto.setId(xhibitCourtRoom.getCourtRoomId().longValue());
                dto.setCourtRoomName(xhibitCourtRoom.getCourtRoomName());
                dto.setDescription(xhibitCourtRoom.getDescription());
                dto.setCourtRoomNo(xhibitCourtRoom.getCourtRoomNo());
                LOGGER.debug("dto id : {}", dto.getId());
                LOGGER.debug("dto courtRoomName: {}", dto.getCourtRoomName());
                resultList.add(dto);
            }
            // Sort by name
            Collections.sort(resultList, (obj1, obj2) -> String.CASE_INSENSITIVE_ORDER
                .compare(obj1.getCourtRoomName(), obj2.getCourtRoomName()));
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return resultList;
    }

    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createCourtRoom(final CourtRoomCreateCommand command,
        final List<CourtRoomDto> courtRoomDtos) {

        final String methodName = "createCourtRoom";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        // Create the courtRoomDao
        XhbCourtRoomDao courtRoomDao = new XhbCourtRoomDao();
        courtRoomDao.setCourtRoomName(command.getName());
        courtRoomDao.setDescription(command.getDescription());
        courtRoomDao.setDisplayName(command.getDisplayName());
        courtRoomDao.setCourtRoomNo(getCourtRoomNo(command.getName(), courtRoomDtos));
        courtRoomDao.setCourtSiteId(command.getXhibitCourtSiteId().intValue());
        courtRoomDao.setObsInd(command.getObsInd());

        // Save
        getXhbCourtRoomRepository().saveDao(courtRoomDao);
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    @Override
    @Secured(UserRole.ROLE_ADMIN_VALUE)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateCourtRoom(final CourtRoomAmendCommand command,
        final List<CourtRoomDto> courtRoomDtos) {

        final String methodName = "updateCourtRoom";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);

        Optional<XhbCourtRoomDao> existingDao =
            getXhbCourtRoomRepository().findById(command.getCourtRoomId());
        if (existingDao.isPresent()) {
            XhbCourtRoomDao courtRoomDao = existingDao.get();

            if (!(command instanceof CourtRoomDeleteCommand)) {
                courtRoomDao.setCourtRoomName(command.getName());
                courtRoomDao.setDescription(command.getDescription());
                courtRoomDao.setDisplayName(command.getDisplayName());
                courtRoomDao.setCourtRoomNo(getCourtRoomNo(command.getName(), courtRoomDtos));
            }
            courtRoomDao.setObsInd(command.getObsInd());

            // Update
            getXhbCourtRoomRepository().updateDao(courtRoomDao);
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    private Integer getCourtRoomNo(final String courtRoomName,
        final List<CourtRoomDto> courtRoomDtos) {
        String numbers = courtRoomName.replaceAll(REGEXP_NUMBERS, EMPTY_STRING);
        if (EMPTY_STRING.equals(numbers) || ROOM_NO_LIMIT < Integer.parseInt(numbers)) {
            return getNextAvailableCourtRoomNo(courtRoomDtos);
        } else {
            return Integer.valueOf(numbers);
        }
    }

    private Integer getNextAvailableCourtRoomNo(final List<CourtRoomDto> courtRoomDtos) {
        Integer result = 1;
        if (!courtRoomDtos.isEmpty()) {
            // Sort by room number
            Collections.sort(courtRoomDtos,
                (obj1, obj2) -> Integer.compare(obj1.getCourtRoomNo(), obj2.getCourtRoomNo()));
            // Check for the next available number
            for (CourtRoomDto courtRoom : courtRoomDtos) {
                if (result > courtRoom.getCourtRoomNo()) {
                    continue;
                } else if (result < courtRoom.getCourtRoomNo() || ROOM_NO_LIMIT.equals(result)) {
                    break;
                }
                result += 1;

            }
        }
        LOGGER.debug("getNextAvailableCourtRoomNo() - {}", result);
        return result;
    }

}
