package uk.gov.hmcts.pdm.publicdisplay.manager.service.api;

import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.court.CourtAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.court.CourtCreateCommand;

import java.util.List;

public interface ICourtService {

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
     * Create court.
     *
     * @param courtCreateCommand the court create command
     * @param courtId courtId
     * @param addressId addressId
     */
    void createCourt(CourtCreateCommand courtCreateCommand, Integer courtId, Integer addressId);

    /**
     * Update court.
     *
     * @param courtAmendCommand the court update command
     */
    void updateCourt(CourtAmendCommand courtAmendCommand);

}
