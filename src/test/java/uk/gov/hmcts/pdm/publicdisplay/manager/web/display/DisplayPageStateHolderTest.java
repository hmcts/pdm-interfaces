package uk.gov.hmcts.pdm.publicdisplay.manager.web.display;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RotationSetsDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(EasyMockExtension.class)
class DisplayPageStateHolderTest extends AbstractJUnit {
    private DisplayPageStateHolder classUnderTest;
    private XhibitCourtSiteDto mockCourtSite;
    private List<XhibitCourtSiteDto> mockCourtSiteDtos;
    private List<RotationSetsDto> mockRotationSetsDtos;
    private List<DisplayTypeDto> mockDisplayTypeDtos;
    private static final String NOT_EQUAL = "Not equal";
    private static final String NOT_NULL = "Not null";

    @BeforeEach
    protected void setup() {
        // Create a new version of the class under test
        classUnderTest = new DisplayPageStateHolder();

        // Setup the mock version of the called classes
        DisplaySearchCommand mockDisplaySearchCommand = createMock(DisplaySearchCommand.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "displaySearchCommand", mockDisplaySearchCommand);
        ReflectionTestUtils.setField(classUnderTest, "courtSite", mockCourtSite);
        ReflectionTestUtils.setField(classUnderTest, "sitesList", mockCourtSiteDtos);
        ReflectionTestUtils.setField(classUnderTest, "rotationSetsList", mockDisplayTypeDtos);
        ReflectionTestUtils.setField(classUnderTest, "displayTypeList", mockDisplayTypeDtos);
    }

    @Test
    void setSitesTest() {
        List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        classUnderTest.setSites(xhibitCourtSiteDtos);
        assertEquals(xhibitCourtSiteDtos, classUnderTest.getSites(), NOT_EQUAL);
    }

    @Test
    void setRotationSetsDtosTest() {
        List<RotationSetsDto> rotationSetsDtos = List.of(new RotationSetsDto());
        classUnderTest.setRotationSets(rotationSetsDtos);
        assertEquals(rotationSetsDtos, classUnderTest.getRotationSets(), NOT_EQUAL);
    }

    @Test
    void setDisplayTypesTest() {
        List<DisplayTypeDto> displayTypeDtos = List.of(new DisplayTypeDto());
        classUnderTest.setDisplayTypes(displayTypeDtos);
        assertEquals(displayTypeDtos, classUnderTest.getDisplayTypes(), NOT_EQUAL);
    }

    @Test
    void setDisplaysTest() {
        List<DisplayDto> displayDtos = List.of(new DisplayDto());
        classUnderTest.setDisplays(displayDtos);
        assertEquals(displayDtos, classUnderTest.getDisplays(), NOT_EQUAL);
    }

    @Test
    void setCourtSiteTest() {
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        classUnderTest.setCourtSite(xhibitCourtSiteDto);
        assertEquals(xhibitCourtSiteDto, classUnderTest.getCourtSite(), NOT_EQUAL);
    }

    @Test
    void setDisplaySearchCommandTest() {
        DisplaySearchCommand displaySearchCommand = new DisplaySearchCommand();
        classUnderTest.setDisplaySearchCommand(displaySearchCommand);
        assertEquals(displaySearchCommand, classUnderTest.getDisplaySearchCommand(), NOT_EQUAL);
    }

    @Test
    void testSitesBySelectedCourt() {
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        xhibitCourtSiteDto.setCourtId(2);
        List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(xhibitCourtSiteDto);

        classUnderTest.setSites(xhibitCourtSiteDtos);
        assertEquals(xhibitCourtSiteDtos, classUnderTest.getSitesBySelectedCourt(2), NOT_EQUAL);
    }

    @Test
    void resetTest() {
        classUnderTest.reset();
        assertEquals(classUnderTest.getRotationSets(), new ArrayList<>(), NOT_EQUAL);
        assertEquals(classUnderTest.getDisplayTypes(), new ArrayList<>(), NOT_EQUAL);
        assertEquals(classUnderTest.getSites(), new ArrayList<>(), NOT_EQUAL);
        assertNull(classUnderTest.getCourtSite(), NOT_NULL);
    }
}
