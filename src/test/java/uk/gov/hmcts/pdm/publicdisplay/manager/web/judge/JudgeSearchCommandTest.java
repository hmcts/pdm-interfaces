package uk.gov.hmcts.pdm.publicdisplay.manager.web.judge;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("PMD")
class JudgeSearchCommandTest extends AbstractJUnit {

    @Test
    void testToString() {
        JudgeSearchCommand judgeSearchCommand = new JudgeSearchCommand();
        assertEquals("XhibitCourtSiteId :no value at present", judgeSearchCommand.toString(), "Not equal");
        judgeSearchCommand.setXhibitCourtSiteId(4L);
        assertEquals("XhibitCourtSiteId :4", judgeSearchCommand.toString(), "Not equal");
    }
}