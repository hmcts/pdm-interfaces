package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.persistence.EntityManager;
import org.easymock.Capture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtRepository;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.court.CourtAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.court.CourtCreateCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class CourtServiceTest extends AbstractJUnit {

    private ICourtService classUnderTest;
    private XhbCourtSiteRepository mockCourtSiteRepo;
    private XhbCourtRepository mockCourtRepo;
    private EntityManager mockEntityManager;

    private static final String NOT_EQUAL = "Not equal";
    private static final String NOT_EMPTY = "Not empty";
    private static final String COURT_SITE_CODE = "COURTSITECODE";
    private static final String COURT_SITE_NAME = "COURTSITENAME";

    @BeforeEach
    public void setup() {

        // Create a new version of the class under test
        classUnderTest = new CourtService();

        // Setup the mock version of the called classes
        mockCourtSiteRepo = createMock(XhbCourtSiteRepository.class);
        mockCourtRepo = createMock(XhbCourtRepository.class);
        mockEntityManager = createMock(EntityManager.class);

        ReflectionTestUtils.setField(classUnderTest, "xhbCourtSiteRepository", mockCourtSiteRepo);
        ReflectionTestUtils.setField(classUnderTest, "xhbCourtRepository", mockCourtRepo);

    }

    @Test
    void courtSitesTest() {

        XhbCourtSiteDao xhbCourtSiteDao = new XhbCourtSiteDao();
        xhbCourtSiteDao.setId(2);
        xhbCourtSiteDao.setCourtSiteName(COURT_SITE_NAME);
        xhbCourtSiteDao.setCourtSiteCode(COURT_SITE_CODE);
        xhbCourtSiteDao.setCourtId(4);

        List<XhbCourtSiteDao> courtSiteDaoList = new ArrayList<>();
        courtSiteDaoList.add(xhbCourtSiteDao);

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findByCourtId(1)).andReturn(courtSiteDaoList);

        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        List<XhibitCourtSiteDto> courtSiteDtoList = classUnderTest.getCourtSites(1);

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
        expect(mockCourtSiteRepo.findByCourtId(1)).andReturn(courtSiteDaoList);

        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        List<XhibitCourtSiteDto> courtSiteDtoList = classUnderTest.getCourtSites(1);

        // Assert that the objects are as expected
        assertTrue(courtSiteDtoList.isEmpty(), NOT_EMPTY);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);

    }

    @Test
    void courtsTest() {

        XhbCourtDao courtDao = new XhbCourtDao();
        courtDao.setCourtId(1);
        courtDao.setCourtName("courtName");
        courtDao.setAddressId(2);
        courtDao.setObsInd("N");

        List<XhbCourtDao> courtDaoList = new ArrayList<>();
        courtDaoList.add(courtDao);

        // Add the mock calls to child classes
        expect(mockCourtRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtRepo.findAll()).andReturn(courtDaoList);

        replay(mockCourtRepo);
        replay(mockEntityManager);

        // Perform the test
        List<CourtDto> courtDtoList = classUnderTest.getCourts();

        // Assert that the objects are as expected
        assertEquals(courtDao.getAddressId(), courtDtoList.get(0).getAddressId(), NOT_EQUAL);
        assertEquals(courtDao.getCourtId(), courtDtoList.get(0).getId(), NOT_EQUAL);
        assertEquals(courtDao.getCourtName(), courtDtoList.get(0).getCourtName(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtRepo);
        verify(mockEntityManager);

    }

    @Test
    void emptyCourtsTest() {

        List<XhbCourtDao> courtDaoList = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockCourtRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtRepo.findAll()).andReturn(courtDaoList);

        replay(mockCourtRepo);
        replay(mockEntityManager);

        // Perform the test
        List<CourtDto> courtDtoList = classUnderTest.getCourts();

        // Assert that the objects are as expected
        assertTrue(courtDtoList.isEmpty(), NOT_EMPTY);

        // Verify the expected mocks were called
        verify(mockCourtRepo);
        verify(mockEntityManager);

    }

    @Test
    void createCourtTest() {

        CourtCreateCommand courtCreateCommand = new CourtCreateCommand();
        courtCreateCommand.setCourtSiteName(COURT_SITE_NAME);
        courtCreateCommand.setCourtSiteCode(COURT_SITE_CODE);

        Capture<XhbCourtSiteDao> capturedCourtSiteDao = newCapture();

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        mockCourtSiteRepo.saveDao(capture(capturedCourtSiteDao));
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.createCourt(courtCreateCommand, 1, 2);

        // Assert that the objects are as expected
        assertEquals(1, capturedCourtSiteDao.getValue().getCourtId(), NOT_EQUAL);
        assertEquals(2, capturedCourtSiteDao.getValue().getAddressId(), NOT_EQUAL);
        assertEquals(courtCreateCommand.getCourtSiteName(), capturedCourtSiteDao.getValue().getCourtSiteName(),
                NOT_EQUAL);
        assertEquals("N", capturedCourtSiteDao.getValue().getObsInd(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);

    }

    @Test
    void updateCourtTest() {

        XhbCourtSiteDao xhbCourtSiteDao = new XhbCourtSiteDao();
        xhbCourtSiteDao.setCourtSiteCode(COURT_SITE_CODE);
        xhbCourtSiteDao.setCourtSiteName(COURT_SITE_NAME);
        xhbCourtSiteDao.setDisplayName(COURT_SITE_CODE);
        xhbCourtSiteDao.setObsInd("N");

        CourtAmendCommand courtAmendCommand = new CourtAmendCommand();
        courtAmendCommand.setCourtSiteName(COURT_SITE_NAME);
        courtAmendCommand.setCourtSiteCode(COURT_SITE_CODE);
        courtAmendCommand.setXhibitCourtSiteId(1L);

        Optional<XhbCourtSiteDao> courtSiteDaoOptional = Optional.of(xhbCourtSiteDao);

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findById(1)).andReturn(courtSiteDaoOptional);
        expect(mockCourtSiteRepo.updateDao(courtSiteDaoOptional.get())).andReturn(courtSiteDaoOptional);
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.updateCourt(courtAmendCommand);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    @Test
    void updateCourtEmptyTest() {

        final Optional<XhbCourtSiteDao> courtSiteDaoOptional = Optional.empty();

        CourtAmendCommand courtAmendCommand = new CourtAmendCommand();
        courtAmendCommand.setXhibitCourtSiteId(1L);

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findById(1)).andReturn(courtSiteDaoOptional);
        
        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.updateCourt(courtAmendCommand);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }
}
