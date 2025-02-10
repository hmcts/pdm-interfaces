package uk.gov.hmcts.pdm.publicdisplay.manager.web.judgetype;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(EasyMockExtension.class)
class JudgeTypePageStateHolderTest extends AbstractJUnit {
    private JudgeTypePageStateHolder classUnderTest;
    private XhibitCourtSiteDto mockCourtSite;
    private List<XhibitCourtSiteDto> mockCourtSiteDtos;
    private List<RefSystemCodeDto> mockRefSystemCodeDtos;
    private static final String NOT_EQUAL = "Not equal";
    private static final String NOT_NULL = "Not null";

    @BeforeEach
    protected void setup() {
        // Create a new version of the class under test
        classUnderTest = new JudgeTypePageStateHolder();

        // Setup the mock version of the called classes
        JudgeTypeSearchCommand judgeTypeSearchCommand = createMock(JudgeTypeSearchCommand.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "judgeTypeSearchCommand", judgeTypeSearchCommand);
        ReflectionTestUtils.setField(classUnderTest, "courtSite", mockCourtSite);
        ReflectionTestUtils.setField(classUnderTest, "sitesList", mockCourtSiteDtos);
        ReflectionTestUtils.setField(classUnderTest, "judgeTypeList", mockRefSystemCodeDtos);
    }

    @Test
    void setSitesTest() {
        List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        classUnderTest.setSites(xhibitCourtSiteDtos);
        assertEquals(xhibitCourtSiteDtos, classUnderTest.getSites(), NOT_EQUAL);
    }

    @Test
    void setHearingTypeDtosTest() {
        List<RefSystemCodeDto> refJudgeTypeDtos = List.of(new RefSystemCodeDto());
        classUnderTest.setJudgeTypes(refJudgeTypeDtos);
        assertEquals(refJudgeTypeDtos, classUnderTest.getJudgeTypes(), NOT_EQUAL);
    }

    @Test
    void setCourtSiteTest() {
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        classUnderTest.setCourtSite(xhibitCourtSiteDto);
        assertEquals(xhibitCourtSiteDto, classUnderTest.getCourtSite(), NOT_EQUAL);
    }

    @Test
    void setHearingTypeSearchCommandTest() {
        JudgeTypeSearchCommand judgeSearchCommand = new JudgeTypeSearchCommand();
        classUnderTest.setJudgeTypeSearchCommand(judgeSearchCommand);
        assertEquals(judgeSearchCommand, classUnderTest.getJudgeTypeSearchCommand(), NOT_EQUAL);
    }

    @Test
    void resetTest() {
        classUnderTest.reset();
        assertNull(classUnderTest.getJudgeTypeSearchCommand(), NOT_NULL);
        assertEquals(classUnderTest.getJudgeTypes(), new ArrayList<>(), NOT_EQUAL);
        assertEquals(classUnderTest.getSites(), new ArrayList<>(), NOT_EQUAL);
        assertNull(classUnderTest.getCourtSite(), NOT_NULL);
    }
}