package uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(EasyMockExtension.class)
class HearingTypePageStateHolderTest extends AbstractJUnit {

    private HearingTypePageStateHolder classUnderTest;
    private XhibitCourtSiteDto mockCourtSite;
    private List<XhibitCourtSiteDto> mockCourtSiteDtos;
    private List<HearingTypeDto> mockHearingTypeDtos;
    private static final String NOT_EQUAL = "Not equal";
    private static final String NOT_NULL = "Not null";

    @BeforeEach
    protected void setup() {
        // Create a new version of the class under test
        classUnderTest = new HearingTypePageStateHolder();

        // Setup the mock version of the called classes
        HearingTypeSearchCommand mockHearingTypeSearchCommand = createMock(HearingTypeSearchCommand.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "hearingSearchCommand", mockHearingTypeSearchCommand);
        ReflectionTestUtils.setField(classUnderTest, "courtSite", mockCourtSite);
        ReflectionTestUtils.setField(classUnderTest, "sitesList", mockCourtSiteDtos);
        ReflectionTestUtils.setField(classUnderTest, "hearingTypeList", mockHearingTypeDtos);
    }

    @Test
    void setSitesTest() {
        List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        classUnderTest.setSites(xhibitCourtSiteDtos);
        assertEquals(xhibitCourtSiteDtos, classUnderTest.getSites(), NOT_EQUAL);
    }

    @Test
    void setHearingTypeDtosTest() {
        List<HearingTypeDto> hearingTypeDtos = List.of(new HearingTypeDto());
        classUnderTest.setHearingTypes(hearingTypeDtos);
        assertEquals(hearingTypeDtos, classUnderTest.getHearingTypes(), NOT_EQUAL);
    }

    @Test
    void setCourtSiteTest() {
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        classUnderTest.setCourtSite(xhibitCourtSiteDto);
        assertEquals(xhibitCourtSiteDto, classUnderTest.getCourtSite(), NOT_EQUAL);
    }

    @Test
    void setHearingTypeSearchCommandTest() {
        HearingTypeSearchCommand hearingTypeSearchCommand = new HearingTypeSearchCommand();
        classUnderTest.setHearingSearchCommand(hearingTypeSearchCommand);
        assertEquals(hearingTypeSearchCommand, classUnderTest.getHearingSearchCommand(), NOT_EQUAL);
    }

    @Test
    void resetTest() {
        classUnderTest.reset();
        assertNull(classUnderTest.getHearingSearchCommand(), NOT_NULL);
        assertEquals(classUnderTest.getHearingTypes(), new ArrayList<>(), NOT_EQUAL);
        assertEquals(classUnderTest.getSites(), new ArrayList<>(), NOT_EQUAL);
        assertNull(classUnderTest.getCourtSite(), NOT_NULL);
    }
}