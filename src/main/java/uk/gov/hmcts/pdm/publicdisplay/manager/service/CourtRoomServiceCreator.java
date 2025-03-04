package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CourtRoomServiceCreator extends AbstractService {

    protected CourtRoomDto createCourtRoomDto() {
        return new CourtRoomDto();
    }
    
    protected CourtDto createCourtDto() {
        return new CourtDto();
    }

}
