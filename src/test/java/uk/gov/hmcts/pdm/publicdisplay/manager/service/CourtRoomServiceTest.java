package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.persistence.EntityManager;
import org.easymock.Capture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtroom.XhbCourtRoomDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom.CourtRoomAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom.CourtRoomCreateCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.LawOfDemeter")
class CourtRoomServiceTest extends CourtRoomServiceEmptyTest {

    @Test
    void courtsTest() {

        XhbCourtDao courtDao = new XhbCourtDao();
        courtDao.setCourtId(1);
        courtDao.setCourtName("courtName");
        courtDao.setAddressId(1);

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
        assertEquals(courtDao.getCourtId(), courtDtoList.get(0).getId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtRepo);
        verify(mockEntityManager);
    }

    @Test
    void courtSitesTest() {

        XhbCourtSiteDao xhbCourtSiteDao = createCourtSiteDao();

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
    void courtSitesCourtIdNullTest() {

        XhbCourtSiteDao xhbCourtSiteDao = createCourtSiteDao();

        List<XhbCourtSiteDao> courtSiteDaoList = new ArrayList<>();
        courtSiteDaoList.add(xhbCourtSiteDao);

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtSiteRepo.findAll()).andReturn(courtSiteDaoList);

        replay(mockCourtSiteRepo);
        replay(mockEntityManager);

        // Perform the test
        List<XhibitCourtSiteDto> courtSiteDtoList = classUnderTest.getCourtSites(null);

        // Assert that the objects are as expected
        assertEquals(xhbCourtSiteDao.getId(), courtSiteDtoList.get(0).getId().intValue(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockEntityManager);

    }

    @Test
    void courtRoomsTest() {

        XhbCourtRoomDao courtRoomDao = new XhbCourtRoomDao();
        courtRoomDao.setCourtRoomId(1);
        courtRoomDao.setCourtRoomName("courtRoomName");
        courtRoomDao.setDescription(DESCRIPTION);
        courtRoomDao.setCourtRoomNo(2);

        List<XhbCourtRoomDao> courtRoomDaoList = new ArrayList<>();
        courtRoomDaoList.add(courtRoomDao);

        // Add the mock calls to child classes
        expect(mockCourtRoomRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtRoomRepo.findByCourtSiteId(1)).andReturn(courtRoomDaoList);

        replay(mockCourtRoomRepo);
        replay(mockEntityManager);

        // Perform the test
        List<CourtRoomDto> courtSiteDtoList = classUnderTest.getCourtRooms(1L);

        // Assert that the objects are as expected
        assertEquals(courtRoomDao.getCourtRoomId(), courtSiteDtoList.get(0).getId().intValue(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtRoomRepo);
        verify(mockEntityManager);
    }

    @Test
    void createDisplayTest() {

        CourtRoomCreateCommand courtRoomCreateCommand = new CourtRoomCreateCommand();
        courtRoomCreateCommand.setName("courtRoomCreateCommand");
        courtRoomCreateCommand.setXhibitCourtSiteId(1L);
        courtRoomCreateCommand.setDescription(DESCRIPTION);

        // Capture the XhbDisplayDao
        final Capture<XhbCourtRoomDao> courtRoomDaoCapture = newCapture();

        // Add the mock calls to child classes
        expect(mockCourtRoomRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        mockCourtRoomRepo.saveDao(capture(courtRoomDaoCapture));
        expectLastCall();
        
        replay(mockCourtRoomRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.createCourtRoom(courtRoomCreateCommand, new ArrayList<>());

        // Assert that the objects are as expected
        assertEquals(courtRoomCreateCommand.getName(),
                courtRoomDaoCapture.getValue().getCourtRoomName(), NOT_EQUAL);
        assertEquals(courtRoomCreateCommand.getDescription(),
                courtRoomDaoCapture.getValue().getDescription(), NOT_EQUAL);
        assertEquals(courtRoomCreateCommand.getDisplayName(),
                courtRoomDaoCapture.getValue().getDisplayName(), NOT_EQUAL);
        assertEquals(courtRoomCreateCommand.getXhibitCourtSiteId().intValue(),
                courtRoomDaoCapture.getValue().getCourtSiteId(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtRoomRepo);
        verify(mockEntityManager);

    }

    @Test
    void updateCourtRoomTest() {

        CourtRoomAmendCommand courtRoomAmendCommand = createCourtRoomAmendCommand();

        XhbCourtRoomDao xhbCourtRoomDao = new XhbCourtRoomDao();
        Optional<XhbCourtRoomDao> courtRoomDao = Optional.of(xhbCourtRoomDao);

        expect(mockCourtRoomRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockCourtRoomRepo.findById(courtRoomAmendCommand.getCourtRoomId())).andReturn(courtRoomDao);

        // Add the mock calls to child classes
        expect(mockCourtRoomRepo.updateDao(courtRoomDao.get())).andReturn(courtRoomDao);
        replay(mockCourtRoomRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.updateCourtRoom(courtRoomAmendCommand, new ArrayList<>());

        // Verify the expected mocks were called
        verify(mockCourtRoomRepo);
        verify(mockEntityManager);
    }
    
    @Test
    void testEntityManager() {
        CourtRoomServiceFinder localClassUnderTest = new CourtRoomServiceFinder() {
            
            @Override
            public EntityManager getEntityManager() {
                return super.getEntityManager();
            }
        };
        ReflectionTestUtils.setField(localClassUnderTest, "entityManager", mockEntityManager);
        expect(mockEntityManager.isOpen()).andReturn(true);
        mockEntityManager.close();
        replay(mockEntityManager);
        try (EntityManager result = localClassUnderTest.getEntityManager()) {
            assertNotNull(result, NOT_NULL);
        }
    }

    private XhbCourtSiteDao createCourtSiteDao() {
        XhbCourtSiteDao xhbCourtSiteDao = new XhbCourtSiteDao();
        xhbCourtSiteDao.setId(2);
        xhbCourtSiteDao.setCourtSiteName("courtSiteName");
        xhbCourtSiteDao.setCourtSiteCode("courtSiteCode");
        xhbCourtSiteDao.setCourtId(4);
        return xhbCourtSiteDao;
    }

}
