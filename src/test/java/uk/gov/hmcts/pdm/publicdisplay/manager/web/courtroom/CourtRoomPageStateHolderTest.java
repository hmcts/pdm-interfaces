package uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class CourtRoomPageStateHolderTest {

    private CourtRoomPageStateHolder classUnderTest;
    private XhibitCourtSiteDto mockCourtSiteDto;
    private List<XhibitCourtSiteDto> mockCourtSiteDtos;
    private List<CourtRoomDto> mockCourtRoomDtos;
    private static final String NOT_EQUAL = "Not equal";
    private static final String NOT_NULL = "Not null";

    @BeforeEach
    protected void setup() {
        // Create a new version of the class under test
        classUnderTest = new CourtRoomPageStateHolder();

        // Setup the mock version of the called classes
        CourtRoomSearchCommand mockCourtRoomSearchCommand = createMock(CourtRoomSearchCommand.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "courtSite", mockCourtSiteDto);
        ReflectionTestUtils.setField(classUnderTest, "sitesList", mockCourtSiteDtos);
        ReflectionTestUtils.setField(classUnderTest, "courtRoomsList", mockCourtRoomDtos);
        ReflectionTestUtils.setField(classUnderTest, "courtRoomSearchCommand", mockCourtRoomSearchCommand);

    }

    @Test
    void setSitesTest() {
        List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        classUnderTest.setSites(xhibitCourtSiteDtos);
        assertEquals(xhibitCourtSiteDtos, classUnderTest.getSites(), NOT_EQUAL);
    }

    @Test
    void setCourtsTest() {

        List<CourtDto> courtDtos = List.of(new CourtDto());
        classUnderTest.setCourts(courtDtos);
        assertEquals(courtDtos, classUnderTest.getCourts(), NOT_EQUAL);

    }

    @Test
    void setCourtTest() {
        CourtDto courtDto = new CourtDto();
        classUnderTest.setCourt(courtDto);
        assertEquals(courtDto, classUnderTest.getCourt(), NOT_EQUAL);
    }

    @Test
    void setCourtSearchCommandTest() {
        CourtRoomSearchCommand courtRoomSearchCommand = new CourtRoomSearchCommand();
        classUnderTest.setCourtRoomSearchCommand(courtRoomSearchCommand);
        assertEquals(courtRoomSearchCommand, classUnderTest.getCourtRoomSearchCommand(), NOT_EQUAL);
    }

    @Test
    void resetTest() {
        classUnderTest.reset();
        assertNull(classUnderTest.getCourt(), NOT_NULL);
        assertEquals(classUnderTest.getCourts(), new ArrayList<>(), NOT_EQUAL);
        assertEquals(classUnderTest.getSites(), new ArrayList<>(), NOT_EQUAL);
        assertEquals(classUnderTest.getCourtRoomsList(), new ArrayList<>(), NOT_EQUAL);
    }

}