package uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class JudgeTypeSearchCommandTest extends AbstractJUnit {
    @Test
    void testToString() {
        JudgeTypeSearchCommand judgeTypeSearchCommand = new JudgeTypeSearchCommand();
        assertEquals("XhibitCourtSiteId :no value at present", judgeTypeSearchCommand.toString(), "Not equal");
        judgeTypeSearchCommand.setXhibitCourtSiteId(1L);
        assertEquals("XhibitCourtSiteId :1", judgeTypeSearchCommand.toString(), "Not equal");
    }
}