package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CourtServiceCreator extends AbstractService {

    protected CourtDto createCourtDto() {
        return new CourtDto();
    }
}
