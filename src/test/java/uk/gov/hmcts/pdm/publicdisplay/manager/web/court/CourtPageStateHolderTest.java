package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(EasyMockExtension.class)
class CourtPageStateHolderTest extends AbstractJUnit {

    private CourtPageStateHolder classUnderTest;
    private List<XhibitCourtSiteDto> mockCourtSiteDtos;
    private CourtDto mockCourtDto;
    private List<CourtDto> mockCourtDtos;
    private static final String NOT_EQUAL = "Not equal";
    private static final String NOT_NULL = "Not null";

    @BeforeEach
    protected void setup() {
        // Create a new version of the class under test
        classUnderTest = new CourtPageStateHolder();

        // Setup the mock version of the called classes
        CourtSearchCommand mockCourtSearchCommand = createMock(CourtSearchCommand.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "courtSearchCommand", mockCourtSearchCommand);
        ReflectionTestUtils.setField(classUnderTest, "sitesList", mockCourtSiteDtos);
        ReflectionTestUtils.setField(classUnderTest, "court", mockCourtDto);
        ReflectionTestUtils.setField(classUnderTest, "courtsList", mockCourtDtos);
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
        CourtSearchCommand courtSearchCommand = new CourtSearchCommand();
        classUnderTest.setCourtSearchCommand(courtSearchCommand);
        assertEquals(courtSearchCommand, classUnderTest.getCourtSearchCommand(), NOT_EQUAL);
    }

    @Test
    void resetTest() {
        classUnderTest.reset();
        assertNull(classUnderTest.getCourt(), NOT_NULL);
        assertEquals(classUnderTest.getCourts(), new ArrayList<>(), NOT_EQUAL);
        assertEquals(classUnderTest.getSites(), new ArrayList<>(), NOT_EQUAL);
    }
}