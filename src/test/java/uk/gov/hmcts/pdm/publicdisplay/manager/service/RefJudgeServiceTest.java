package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.Capture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefjudge.XhbRefJudgeDao;
import uk.gov.hmcts.pdm.business.entities.xhbrefjudge.XhbRefJudgeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeDao;
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefJudgeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RefSystemCodeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judge.JudgeAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judge.JudgeCreateCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RefJudgeServiceTest extends RefJudgeServiceUtility {

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new RefJudgeService();

        // Setup the mock version of the called classes
        mockRefJudgeRepo = createMock(XhbRefJudgeRepository.class);
        mockCourtSiteRepo = createMock(XhbCourtSiteRepository.class);
        mockRefSystemCodeRepository = createMock(XhbRefSystemCodeRepository.class);
        mockRefJudgeRepo = createMock(XhbRefJudgeRepository.class);

        ReflectionTestUtils.setField(classUnderTest, "xhbRefSystemCodeRepository", mockRefSystemCodeRepository);
        ReflectionTestUtils.setField(classUnderTest, "xhbCourtSiteRepository", mockCourtSiteRepo);
        ReflectionTestUtils.setField(classUnderTest, "xhbRefJudgeRepository", mockRefJudgeRepo);
        ReflectionTestUtils.setField(classUnderTest, "xhbRefJudgeRepository", mockRefJudgeRepo);

    }

    @Test
    void courtSitesTest() {

        XhbCourtSiteDao xhbCourtSiteDao = new XhbCourtSiteDao();
        xhbCourtSiteDao.setId(2);
        xhbCourtSiteDao.setCourtSiteName("courtSiteName");
        xhbCourtSiteDao.setCourtSiteCode("courtSiteCode");
        xhbCourtSiteDao.setCourtId(4);

        List<XhbCourtSiteDao> courtSiteDaoList = new ArrayList<>();
        courtSiteDaoList.add(xhbCourtSiteDao);

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.findAll()).andReturn(courtSiteDaoList);

        replay(mockCourtSiteRepo);

        // Perform the test
        List<XhibitCourtSiteDto> courtSiteDtoList = classUnderTest.getCourtSites();

        // Assert that the objects are as expected
        assertEquals(xhbCourtSiteDao.getId(), courtSiteDtoList.get(0).getId().intValue(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);

    }

    @Test
    void emptyCourtSitesTest() {

        List<XhbCourtSiteDao> courtSiteDaoList = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.findAll()).andReturn(courtSiteDaoList);

        replay(mockCourtSiteRepo);

        // Perform the test
        List<XhibitCourtSiteDto> courtSiteDtoList = classUnderTest.getCourtSites();

        // Assert that the objects are as expected
        assertTrue(courtSiteDtoList.isEmpty(), NOT_EMPTY);
        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);

    }

    @Test
    void judgesTest() {

        XhbRefJudgeDao refJudgeDao = createRefJudgeDao();

        List<XhbRefJudgeDao> refJudgeDaoList = new ArrayList<>();
        refJudgeDaoList.add(refJudgeDao);

        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.findByCourtSiteId(1)).andReturn(refJudgeDaoList);

        replay(mockRefJudgeRepo);

        // Perform the test
        List<RefJudgeDto> refJudgeDtoList = classUnderTest.getJudges(1L);

        // Assert that the objects are as expected
        assertEquals(refJudgeDao.getCourtId(), refJudgeDtoList.get(0).getCourtId().intValue(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);

    }

    @Test
    void emptyJudgesTest() {

        List<XhbRefJudgeDao> refJudgeDaoList = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.findByCourtSiteId(1)).andReturn(refJudgeDaoList);

        replay(mockRefJudgeRepo);

        // Perform the test
        List<RefJudgeDto> refJudgeDtoList = classUnderTest.getJudges(1L);

        // Assert that the objects are as expected
        assertTrue(refJudgeDtoList.isEmpty(), NOT_EMPTY);

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);

    }

    @Test
    void judgeTypesTest() {

        List<XhbRefSystemCodeDao> refSystemCodeDaos = createRefSystemCodeDao();

        // Add the mock calls to child classes
        expect(mockRefSystemCodeRepository.findJudgeTypeByCourtSiteId(1)).andReturn(refSystemCodeDaos);

        replay(mockRefSystemCodeRepository);

        // Perform the test
        List<RefSystemCodeDto> refSystemCodeDtoList = classUnderTest.getJudgeTypes(1L);

        // Assert that the objects are as expected
        assertEquals(refSystemCodeDaos.get(0).getCourtId(),
                refSystemCodeDtoList.get(0).getCourtId().intValue(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockRefSystemCodeRepository);

    }

    @Test
    void emptyJudgeTypesTest() {

        List<XhbRefSystemCodeDao> refSystemCodeDaos = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockRefSystemCodeRepository.findJudgeTypeByCourtSiteId(1)).andReturn(refSystemCodeDaos);

        replay(mockRefSystemCodeRepository);

        // Perform the test
        List<RefSystemCodeDto> refSystemCodeDtoList = classUnderTest.getJudgeTypes(1L);

        // Assert that the objects are as expected
        assertTrue(refSystemCodeDtoList.isEmpty(), NOT_EMPTY);

        // Verify the expected mocks were called
        verify(mockRefSystemCodeRepository);

    }

    @Test
    void updateJudgeTest() {


        Optional<XhbRefJudgeDao> xhbRefJudgeDao = Optional.of(new XhbRefJudgeDao());

        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.findById(1)).andReturn(xhbRefJudgeDao);
        expect(mockRefJudgeRepo.updateDao(xhbRefJudgeDao.get())).andReturn(xhbRefJudgeDao);

        replay(mockRefJudgeRepo);

        JudgeAmendCommand judgeAmendCommand = createJudgeAmendCommand();

        // Perform the test
        classUnderTest.updateJudge(judgeAmendCommand);

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);

    }

    @Test
    void updateJudgeEmptyTest() {

        JudgeAmendCommand judgeAmendCommand = new JudgeAmendCommand();
        judgeAmendCommand.setRefJudgeId(1);

        Optional<XhbRefJudgeDao> xhbRefJudgeDao = Optional.empty();

        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.findById(1)).andReturn(xhbRefJudgeDao);

        replay(mockRefJudgeRepo);

        // Perform the test
        classUnderTest.updateJudge(judgeAmendCommand);

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);

    }

    @Test
    void createJudgeTest() {

        Capture<XhbRefJudgeDao> capturedRefJudgeDao = newCapture();

        // Add the mock calls to child classes
        mockRefJudgeRepo.saveDao(capture(capturedRefJudgeDao));
        expectLastCall();

        replay(mockRefJudgeRepo);

        JudgeCreateCommand judgeCreateCommand = createJudgeCreateCommand();

        // Perform the test
        classUnderTest.createJudge(judgeCreateCommand, 1);

        // Assert that the objects are as expected
        assertEquals(judgeCreateCommand.getFirstName(), capturedRefJudgeDao.getValue().getFirstName(), "Not equal");

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);

    }

}
