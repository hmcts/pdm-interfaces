package uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.pdm.publicdisplay.manager.util.CommandUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("PMD")
class CourtRoomSearchCommandTest {

    @Test
    void testToString() {
        CourtRoomSearchCommand courtRoomSearchCommand = new CourtRoomSearchCommand();
        assertEquals(CommandUtil.COURT_ID + CommandUtil.NOVALUEPRESENT,
            courtRoomSearchCommand.toString(), "Not equal");
        courtRoomSearchCommand.setCourtId(1);
        assertEquals(CommandUtil.COURT_ID + "1", courtRoomSearchCommand.toString(), "Not equal");
    }
}
