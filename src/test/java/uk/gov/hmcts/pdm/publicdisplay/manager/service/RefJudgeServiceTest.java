package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"PMD"})
class RefJudgeServiceTest extends RefJudgeServiceUtility {

    private static final String TYPE = "TYPE";
    private static final String INV = "INV";
    
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
        mockEntityManager = createMock(EntityManager.class);

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
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findAll()).andReturn(courtSiteDaoList);

        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        List<XhibitCourtSiteDto> courtSiteDtoList = classUnderTest.getCourtSites();

        // Assert that the objects are as expected
        assertEquals(xhbCourtSiteDao.getId(), courtSiteDtoList.get(0).getId().intValue(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    @Test
    void emptyCourtSitesTest() {

        List<XhbCourtSiteDao> courtSiteDaoList = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findAll()).andReturn(courtSiteDaoList);

        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        List<XhibitCourtSiteDto> courtSiteDtoList = classUnderTest.getCourtSites();

        // Assert that the objects are as expected
        assertTrue(courtSiteDtoList.isEmpty(), NOT_EMPTY);
        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    @Test
    void judgesTest() {

        XhbRefJudgeDao refJudgeDao = createRefJudgeDao();

        List<XhbRefJudgeDao> refJudgeDaoList = new ArrayList<>();
        refJudgeDaoList.add(refJudgeDao);

        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefJudgeRepo.findByCourtSiteId(1)).andReturn(refJudgeDaoList);

        replay(mockRefJudgeRepo);
        replay(mockEntityManager);

        // Perform the test
        List<RefJudgeDto> refJudgeDtoList = classUnderTest.getJudges(1L);

        // Assert that the objects are as expected
        assertEquals(refJudgeDao.getCourtId(), refJudgeDtoList.get(0).getCourtId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);
        verify(mockEntityManager);

    }

    @Test
    void emptyJudgesTest() {

        List<XhbRefJudgeDao> refJudgeDaoList = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefJudgeRepo.findByCourtSiteId(1)).andReturn(refJudgeDaoList);

        replay(mockRefJudgeRepo);
        replay(mockEntityManager);

        // Perform the test
        List<RefJudgeDto> refJudgeDtoList = classUnderTest.getJudges(1L);

        // Assert that the objects are as expected
        assertTrue(refJudgeDtoList.isEmpty(), NOT_EMPTY);

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);
        verify(mockEntityManager);
    }

    @Test
    void testGetJudge() {
        XhbRefJudgeDao xhbRefJudgeDao = new XhbRefJudgeDao();
        
        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefJudgeRepo.findById(EasyMock.isA(Integer.class)))
            .andReturn(Optional.of(xhbRefJudgeDao));
        
        replay(mockRefJudgeRepo);
        replay(mockEntityManager);
        
        // Perform the test
        RefJudgeDto result = classUnderTest.getJudge(1);
        
        // Assert that the objects are as expected
        assertNotNull(result, NULL);
        
        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);
        verify(mockEntityManager);
    }
    
    @Test
    void judgeTypesTest() {

        List<XhbRefSystemCodeDao> refSystemCodeDaos = createRefSystemCodeDao();

        // Add the mock calls to child classes
        expect(mockRefSystemCodeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefSystemCodeRepository.findJudgeTypeByCourtSiteId(1)).andReturn(refSystemCodeDaos);

        replay(mockRefSystemCodeRepository);
        replay(mockEntityManager);

        // Perform the test
        List<RefSystemCodeDto> refSystemCodeDtoList = classUnderTest.getJudgeTypes(1L);

        // Assert that the objects are as expected
        assertEquals(refSystemCodeDaos.get(0).getCourtId(),
                refSystemCodeDtoList.get(0).getCourtId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockRefSystemCodeRepository);
        verify(mockEntityManager);
    }

    @Test
    void emptyJudgeTypesTest() {

        List<XhbRefSystemCodeDao> refSystemCodeDaos = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockRefSystemCodeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefSystemCodeRepository.findJudgeTypeByCourtSiteId(1)).andReturn(refSystemCodeDaos);

        replay(mockRefSystemCodeRepository);
        replay(mockEntityManager);
        
        // Perform the test
        List<RefSystemCodeDto> refSystemCodeDtoList = classUnderTest.getJudgeTypes(1L);

        // Assert that the objects are as expected
        assertTrue(refSystemCodeDtoList.isEmpty(), NOT_EMPTY);

        // Verify the expected mocks were called
        verify(mockRefSystemCodeRepository);
        verify(mockEntityManager);
    }

    @Test
    void testGetJudgeType() {
        XhbRefSystemCodeDao xhbRefSystemCode = testGetJudgeTypeScenarios(TYPE, TYPE);
        assertNotNull(xhbRefSystemCode, NULL);
    }
    
    @Test
    void testGetJudgeTypeNull() {
        XhbRefSystemCodeDao xhbRefSystemCode = testGetJudgeTypeScenarios(TYPE, INV);
        assertNull(xhbRefSystemCode, NOT_NULL);
    }
    
    XhbRefSystemCodeDao testGetJudgeTypeScenarios(String firstType, String secondType) {
        RefJudgeDto refJudgeDto = new RefJudgeDto();
        refJudgeDto.setCourtId(1);
        refJudgeDto.setJudgeType(firstType);
        XhbRefSystemCodeDao xhbRefSystemCodeDao = new XhbRefSystemCodeDao();
        xhbRefSystemCodeDao.setCode(secondType);
        List<XhbRefSystemCodeDao> refSystemCodeDaos = new ArrayList<>();
        refSystemCodeDaos.add(xhbRefSystemCodeDao);
        
        // Add the mock calls to child classes
        expect(mockRefSystemCodeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefSystemCodeRepository.findJudgeTypeByCourtId(EasyMock.isA(Integer.class)))
            .andReturn(refSystemCodeDaos);
        
        replay(mockRefSystemCodeRepository);
        replay(mockEntityManager);
        
        // Perform the test
        XhbRefSystemCodeDao xhbRefSystemCode = classUnderTest.getJudgeType(refJudgeDto);
        
        // Verify the expected mocks were called
        verify(mockRefSystemCodeRepository);
        verify(mockEntityManager);
        
        return xhbRefSystemCode;
    }
    
    @Test
    void updateJudgeTest() {

        Optional<XhbRefJudgeDao> xhbRefJudgeDao = Optional.of(new XhbRefJudgeDao());

        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefJudgeRepo.findById(1)).andReturn(xhbRefJudgeDao);
        expect(mockRefJudgeRepo.updateDao(xhbRefJudgeDao.get())).andReturn(xhbRefJudgeDao);

        replay(mockRefJudgeRepo);
        replay(mockEntityManager);

        JudgeAmendCommand judgeAmendCommand = createJudgeAmendCommand();

        // Perform the test
        classUnderTest.updateJudge(judgeAmendCommand);

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);
        verify(mockEntityManager);
    }

    @Test
    void updateJudgeEmptyTest() {

        JudgeAmendCommand judgeAmendCommand = new JudgeAmendCommand();
        judgeAmendCommand.setRefJudgeId(1);

        Optional<XhbRefJudgeDao> xhbRefJudgeDao = Optional.empty();

        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefJudgeRepo.findById(1)).andReturn(xhbRefJudgeDao);

        replay(mockRefJudgeRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.updateJudge(judgeAmendCommand);

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);
        verify(mockEntityManager);
    }

    @Test
    void createJudgeTest() {

        Capture<XhbRefJudgeDao> capturedRefJudgeDao = newCapture();

        // Add the mock calls to child classes
        expect(mockRefJudgeRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        mockRefJudgeRepo.saveDao(capture(capturedRefJudgeDao));
        expectLastCall();

        replay(mockRefJudgeRepo);
        replay(mockEntityManager);

        JudgeCreateCommand judgeCreateCommand = createJudgeCreateCommand();

        // Perform the test
        classUnderTest.createJudge(judgeCreateCommand, 1);

        // Assert that the objects are as expected
        assertEquals(judgeCreateCommand.getFirstName(), capturedRefJudgeDao.getValue().getFirstName(), "Not equal");

        // Verify the expected mocks were called
        verify(mockRefJudgeRepo);
        verify(mockEntityManager);
    }
}
