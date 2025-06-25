package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.util.CommandUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("PMD")
class CourtSearchCommandTest extends AbstractJUnit {

    @Test
    void testToString() {
        CourtSearchCommand courtSearchCommand = new CourtSearchCommand();
        assertEquals(CommandUtil.COURT_ID + CommandUtil.NOVALUEPRESENT,
            courtSearchCommand.toString(), "Not equal");
        courtSearchCommand.setCourtId(2);

        assertEquals(CommandUtil.COURT_ID + "2", courtSearchCommand.toString(), "Not equal");
    }
}
