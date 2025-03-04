package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefJudgeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class RefJudgeServiceCreator extends AbstractService {

    protected RefJudgeDto createRefJudgeDto() {
        return new RefJudgeDto();
    }
    
    protected RefSystemCodeDto createRefSystemCodeDto() {
        return new RefSystemCodeDto();
    }
}
