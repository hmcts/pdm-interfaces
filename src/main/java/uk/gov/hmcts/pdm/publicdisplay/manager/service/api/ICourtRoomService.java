package uk.gov.hmcts.pdm.publicdisplay.manager.service.api;

import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom.CourtRoomAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom.CourtRoomCreateCommand;

import java.util.List;
import java.util.Optional;

public interface ICourtRoomService {

    /**
     * Retrieves all courts.
     * 
     * @return List
     */
    List<CourtDto> getCourts();

    /**
     * Retrieves all court sites.
     * 
     * @return List
     */
    List<XhibitCourtSiteDto> getCourtSites(Integer courtId);

    /**
     * Retrieves all court rooms.
     * 
     * @return List
     */
    List<CourtRoomDto> getCourtRooms(Long xhibitCourtSiteId);

    /**
     * Retrieves a court site from a courtRoomId.
     * 
     * @return XhbCourtSiteDao
     */
    Optional<XhbCourtSiteDao> getXhbCourtSiteFromCourtRoomId(Long courtRoomId);
    
    /**
     * Retrieves court room by courtRoomId.
     * 
     * @return CourtRoomDto
     */
    CourtRoomDto getCourtRoom(Long courtRoomId);
    
    /**
     * Create court room.
     *
     * @param courtRoomCreateCommand the court room create command
     * @param courtRoomDtos list of existing court rooms for this site
     */
    void createCourtRoom(CourtRoomCreateCommand courtRoomCreateCommand,
        List<CourtRoomDto> courtRoomDtos);

    /**
     * Create court room.
     *
     * @param courtRoomAmendCommand the court room amend command
     * @param courtRoomDtos list of existing court rooms for this site
     */
    void updateCourtRoom(CourtRoomAmendCommand courtRoomAmendCommand,
        List<CourtRoomDto> courtRoomDtos);
}
