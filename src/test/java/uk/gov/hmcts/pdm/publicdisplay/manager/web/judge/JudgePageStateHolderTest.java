package uk.gov.hmcts.pdm.publicdisplay.manager.web.judge;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefJudgeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(EasyMockExtension.class)
class JudgePageStateHolderTest extends AbstractJUnit {

    private JudgePageStateHolder classUnderTest;
    private XhibitCourtSiteDto mockCourtSite;
    private List<XhibitCourtSiteDto> mockCourtSiteDtos;
    private List<RefJudgeDto> mockRefJudgeDtos;
    private static final String NOT_EQUAL = "Not equal";
    private static final String NOT_NULL = "Not null";

    @BeforeEach
    protected void setup() {
        // Create a new version of the class under test
        classUnderTest = new JudgePageStateHolder();

        // Setup the mock version of the called classes
        JudgeSearchCommand judgeSearchCommand = createMock(JudgeSearchCommand.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "judgeSearchCommand", judgeSearchCommand);
        ReflectionTestUtils.setField(classUnderTest, "courtSite", mockCourtSite);
        ReflectionTestUtils.setField(classUnderTest, "sitesList", mockCourtSiteDtos);
        ReflectionTestUtils.setField(classUnderTest, "judgeList", mockRefJudgeDtos);
    }

    @Test
    void setSitesTest() {
        List<XhibitCourtSiteDto> xhibitCourtSiteDtos = List.of(new XhibitCourtSiteDto());
        classUnderTest.setSites(xhibitCourtSiteDtos);
        assertEquals(xhibitCourtSiteDtos, classUnderTest.getSites(), NOT_EQUAL);
    }

    @Test
    void setHearingTypeDtosTest() {
        List<RefJudgeDto> refJudgeDtos = List.of(new RefJudgeDto());
        classUnderTest.setJudges(refJudgeDtos);
        assertEquals(refJudgeDtos, classUnderTest.getJudges(), NOT_EQUAL);
    }

    @Test
    void setCourtSiteTest() {
        XhibitCourtSiteDto xhibitCourtSiteDto = new XhibitCourtSiteDto();
        classUnderTest.setCourtSite(xhibitCourtSiteDto);
        assertEquals(xhibitCourtSiteDto, classUnderTest.getCourtSite(), NOT_EQUAL);
    }

    @Test
    void setHearingTypeSearchCommandTest() {
        JudgeSearchCommand judgeSearchCommand = new JudgeSearchCommand();
        classUnderTest.setJudgeSearchCommand(judgeSearchCommand);
        assertEquals(judgeSearchCommand, classUnderTest.getJudgeSearchCommand(), NOT_EQUAL);
    }

    @Test
    void resetTest() {
        classUnderTest.reset();
        assertNull(classUnderTest.getJudgeSearchCommand(), NOT_NULL);
        assertEquals(classUnderTest.getJudges(), new ArrayList<>(), NOT_EQUAL);
        assertEquals(classUnderTest.getSites(), new ArrayList<>(), NOT_EQUAL);
        assertNull(classUnderTest.getCourtSite(), NOT_NULL);
    }

}