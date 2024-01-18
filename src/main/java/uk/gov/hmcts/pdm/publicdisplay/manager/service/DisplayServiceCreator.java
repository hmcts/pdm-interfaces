package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RotationSetsDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class DisplayServiceCreator {

    protected XhibitCourtSiteDto createXhibitCourtSiteDto() {
        return new XhibitCourtSiteDto();
    }

    protected DisplayDto createDisplayDto() {
        return new DisplayDto();
    }

    protected DisplayTypeDto createDisplayTypeDto() {
        return new DisplayTypeDto();
    }

    protected RotationSetsDto createRotationSetsDto() {
        return new RotationSetsDto();
    }
}
