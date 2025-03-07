package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtRepository;
import uk.gov.hmcts.pdm.business.entities.xhbcourtroom.XhbCourtRoomDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtroom.XhbCourtRoomRepository;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtRoomService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom.CourtRoomAmendCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
abstract class CourtRoomServiceEmptyTest extends AbstractJUnit {

    /**
     * The class under test.
     */
    protected ICourtRoomService classUnderTest;

    /**
     * The mock Display Location Repo.
     */
    protected XhbCourtRepository mockCourtRepo;
    protected XhbCourtSiteRepository mockCourtSiteRepo;
    protected XhbCourtRoomRepository mockCourtRoomRepo;
    protected EntityManager mockEntityManager;

    protected static final String NOT_EQUAL = "Not equal";
    protected static final String NOT_NULL = "Is null";
    protected static final String FALSE = "False";
    protected static final String COURT_ROOM_NAME = "Court Room Name";
    protected static final String DESCRIPTION = "description";


    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new CourtRoomService();

        // Setup the mock version of the called classes
        mockCourtRepo = createMock(XhbCourtRepository.class);
        mockCourtSiteRepo = createMock(XhbCourtSiteRepository.class);
        mockCourtRoomRepo = createMock(XhbCourtRoomRepository.class);
        mockEntityManager = createMock(EntityManager.class);

        ReflectionTestUtils.setField(classUnderTest, "xhbCourtRepository", mockCourtRepo);
        ReflectionTestUtils.setField(classUnderTest, "xhbCourtSiteRepository", mockCourtSiteRepo);
        ReflectionTestUtils.setField(classUnderTest, "xhbCourtRoomRepository", mockCourtRoomRepo);

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
        assertTrue(courtDtoList.isEmpty(), "NOT Empty");

        // Verify the expected mocks were called
        verify(mockCourtRepo);
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
        List<XhibitCourtSiteDto> courtSiteDtoList = classUnderTest.getCourtSites(null);

        // Assert that the objects are as expected
        assertTrue(courtSiteDtoList.isEmpty(), "Not empty");

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);
    }

    @Test
    void emptyCourtRoomsTest() {

        List<XhbCourtRoomDao> courtRoomDaoList = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockCourtRoomRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtRoomRepo.findByCourtSiteId(1)).andReturn(courtRoomDaoList);

        replay(mockCourtRoomRepo);
        replay(mockEntityManager);

        // Perform the test
        List<CourtRoomDto> courtSiteDtoList = classUnderTest.getCourtRooms(1L);

        // Assert that the objects are as expected
        assertTrue(courtSiteDtoList.isEmpty(), "Not empty");

        // Verify the expected mocks were called
        verify(mockCourtRoomRepo);
        verify(mockEntityManager);

    }

    @Test
    void updateCourtRoomEmptyDaoTest() {

        CourtRoomAmendCommand courtRoomAmendCommand = createCourtRoomAmendCommand();

        Optional<XhbCourtRoomDao> emptyDao = Optional.empty();

        expect(mockCourtRoomRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtRoomRepo.findById(courtRoomAmendCommand.getCourtRoomId())).andReturn(emptyDao);

        replay(mockCourtRoomRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.updateCourtRoom(courtRoomAmendCommand, new ArrayList<>());

        // Verify the expected mocks were called
        verify(mockCourtRoomRepo);
        verify(mockEntityManager);

    }

    protected CourtRoomAmendCommand createCourtRoomAmendCommand() {
        CourtRoomAmendCommand courtRoomAmendCommand = new CourtRoomAmendCommand();
        courtRoomAmendCommand.setName("name");
        courtRoomAmendCommand.setDescription(DESCRIPTION);
        courtRoomAmendCommand.setCourtRoomId(1);
        return courtRoomAmendCommand;
    }
}
