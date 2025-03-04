package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class HearingTypeServiceCreator extends AbstractService {

    protected HearingTypeDto createHearingTypeDto() {
        return new HearingTypeDto();
    }
}
