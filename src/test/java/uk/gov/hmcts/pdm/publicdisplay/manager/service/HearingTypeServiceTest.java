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
import uk.gov.hmcts.pdm.business.entities.xhbrefhearingtype.XhbRefHearingTypeDao;
import uk.gov.hmcts.pdm.business.entities.xhbrefhearingtype.XhbRefHearingTypeRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IHearingTypeService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing.HearingTypeAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing.HearingTypeCreateCommand;

import java.time.LocalDateTime;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.TooManyMethods"})
class HearingTypeServiceTest {

    protected IHearingTypeService classUnderTest;
    protected XhbRefHearingTypeRepository mockRefHearingTypeRepository;
    protected XhbCourtSiteRepository mockCourtSiteRepo;
    protected EntityManager mockEntityManager;

    protected static final String NOT_EQUAL = "Not equal";
    protected static final String NOT_EMPTY = "Not empty";
    protected static final String NULL = "Result is Null";

    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new HearingTypeService();

        // Setup the mock version of the called classes
        mockRefHearingTypeRepository = createMock(XhbRefHearingTypeRepository.class);
        mockCourtSiteRepo = createMock(XhbCourtSiteRepository.class);
        mockEntityManager = createMock(EntityManager.class);

        ReflectionTestUtils.setField(classUnderTest, "xhbRefHearingTypeRepository", mockRefHearingTypeRepository);
        ReflectionTestUtils.setField(classUnderTest, "xhbCourtSiteRepository", mockCourtSiteRepo);

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
    void hearingTypesTest() {

        XhbRefHearingTypeDao refHearingTypeDao = getXhbRefHearingTypeDao().get();

        List<XhbRefHearingTypeDao> refHearingTypeDaoList = new ArrayList<>();
        refHearingTypeDaoList.add(refHearingTypeDao);

        // Add the mock calls to child classes
        expect(mockRefHearingTypeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefHearingTypeRepository.findByCourtSiteId(1)).andReturn(refHearingTypeDaoList);

        replay(mockRefHearingTypeRepository);
        replay(mockEntityManager);

        // Perform the test
        List<HearingTypeDto> hearingTypeDtoList = classUnderTest.getHearingTypes(1L);

        // Assert that the objects are as expected
        assertEquals(refHearingTypeDao.getHearingTypeCode(), hearingTypeDtoList.get(0).getHearingTypeCode(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockRefHearingTypeRepository);
        verify(mockEntityManager);
    }
    
    @Test
    void testHearingTypesByCourt() {

        XhbRefHearingTypeDao refHearingTypeDao = getXhbRefHearingTypeDao().get();

        List<XhbRefHearingTypeDao> refHearingTypeDaoList = new ArrayList<>();
        refHearingTypeDaoList.add(refHearingTypeDao);

        // Add the mock calls to child classes
        expect(mockRefHearingTypeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefHearingTypeRepository.findByCourtId(1)).andReturn(refHearingTypeDaoList);

        replay(mockRefHearingTypeRepository);
        replay(mockEntityManager);

        // Perform the test
        List<HearingTypeDto> hearingTypeDtoList = classUnderTest.getHearingTypesByCourtId(1);

        // Assert that the objects are as expected
        assertEquals(refHearingTypeDao.getHearingTypeCode(), hearingTypeDtoList.get(0).getHearingTypeCode(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockRefHearingTypeRepository);
        verify(mockEntityManager);
    }

    @Test
    void hearingTypesEmptyTest() {

        List<XhbRefHearingTypeDao> refHearingTypeDaoList = new ArrayList<>();

        // Add the mock calls to child classes
        expect(mockRefHearingTypeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefHearingTypeRepository.findByCourtSiteId(1)).andReturn(refHearingTypeDaoList);

        replay(mockRefHearingTypeRepository);
        replay(mockEntityManager);

        // Perform the test
        List<HearingTypeDto> hearingTypeDtoList = classUnderTest.getHearingTypes(1L);

        // Assert that the objects are as expected
        assertTrue(hearingTypeDtoList.isEmpty(), "Not empty");

        // Verify the expected mocks were called
        verify(mockRefHearingTypeRepository);
        verify(mockEntityManager);
    }

    @Test
    void allCategoriesTest() {

        List<String> categoriesList = new ArrayList<>();
        categoriesList.add("Category A");
        categoriesList.add("Category B");

        // Add the mock calls to child classes
        expect(mockRefHearingTypeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefHearingTypeRepository.findAllCategories()).andReturn(categoriesList);

        replay(mockRefHearingTypeRepository);
        replay(mockEntityManager);

        // Perform the test
        List<String> returnedCategoriesList = classUnderTest.getAllCategories();

        // Assert that the objects are as expected
        assertEquals(categoriesList.size(), returnedCategoriesList.size(), NOT_EQUAL);
        assertEquals(categoriesList.get(0), returnedCategoriesList.get(0), NOT_EQUAL);
        assertEquals(categoriesList.get(1), returnedCategoriesList.get(1), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockRefHearingTypeRepository);
        verify(mockEntityManager);
    }

    @Test
    void updateHearingTypeTest() {

        Optional<XhbRefHearingTypeDao> refHearingTypeDao = getXhbRefHearingTypeDao();

        // Add the mock calls to child classes
        expect(mockRefHearingTypeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefHearingTypeRepository.findById(1)).andReturn(refHearingTypeDao);

        HearingTypeAmendCommand hearingTypeAmendCommand = new HearingTypeAmendCommand();
        hearingTypeAmendCommand.setRefHearingTypeId(1);
        hearingTypeAmendCommand.setHearingTypeDesc("description");

        expect(mockRefHearingTypeRepository.updateDao(refHearingTypeDao.get())).andReturn(refHearingTypeDao);
        
        replay(mockRefHearingTypeRepository);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.updateHearingType(hearingTypeAmendCommand);

        // Verify the expected mocks were called
        verify(mockRefHearingTypeRepository);
        verify(mockEntityManager);
    }

    @Test
    void updateHearingTypeEmptyTest() {

        Optional<XhbRefHearingTypeDao> refHearingTypeDao = Optional.empty();

        // Add the mock calls to child classes
        expect(mockRefHearingTypeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefHearingTypeRepository.findById(1)).andReturn(refHearingTypeDao);

        HearingTypeAmendCommand hearingTypeAmendCommand = new HearingTypeAmendCommand();
        hearingTypeAmendCommand.setRefHearingTypeId(1);

        replay(mockRefHearingTypeRepository);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.updateHearingType(hearingTypeAmendCommand);

        // Verify the expected mocks were called
        verify(mockRefHearingTypeRepository);
        verify(mockEntityManager);

    }
    
    @Test
    void testEntityManager() {
        HearingTypeServiceFinder localClassUnderTest = new HearingTypeServiceFinder() {
            
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
            assertNotNull(result, NOT_EMPTY);
        }
    }

    @Test
    void createHearingTypeTest() {

        HearingTypeCreateCommand hearingTypeCreateCommand = new HearingTypeCreateCommand();
        hearingTypeCreateCommand.setHearingTypeCode("code");
        hearingTypeCreateCommand.setHearingTypeDesc("description");
        hearingTypeCreateCommand.setHearingTypeCode("category");

        final Capture<XhbRefHearingTypeDao> capturedRefHearingTypeDao = newCapture();

        expect(mockRefHearingTypeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        mockRefHearingTypeRepository.saveDao(capture(capturedRefHearingTypeDao));
        expectLastCall();
        
        replay(mockRefHearingTypeRepository);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.createHearingType(hearingTypeCreateCommand, 1);

        // Assert that the objects are as expected
        assertEquals(hearingTypeCreateCommand.getHearingTypeCode(),
                capturedRefHearingTypeDao.getValue().getHearingTypeCode(), NOT_EQUAL);
        assertEquals(hearingTypeCreateCommand.getHearingTypeDesc(),
                capturedRefHearingTypeDao.getValue().getHearingTypeDesc(), NOT_EQUAL);
        assertEquals(hearingTypeCreateCommand.getCategory(),
                capturedRefHearingTypeDao.getValue().getCategory(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockRefHearingTypeRepository);
        verify(mockEntityManager);

    }
    
    @Test
    void testGetHearingType() {
        expect(mockRefHearingTypeRepository.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRefHearingTypeRepository.findById(EasyMock.isA(Integer.class)))
            .andReturn(getXhbRefHearingTypeDao());
        
        replay(mockEntityManager);
        replay(mockRefHearingTypeRepository);
        
        HearingTypeDto result = classUnderTest.getHearingType(1);
        
        verify(mockEntityManager);
        verify(mockRefHearingTypeRepository);
        assertNotNull(result, NULL);
    }

    private static Optional<XhbRefHearingTypeDao> getXhbRefHearingTypeDao() {
        XhbRefHearingTypeDao refHearingTypeDao = new XhbRefHearingTypeDao();
        refHearingTypeDao.setRefHearingTypeId(2);
        refHearingTypeDao.setHearingTypeCode("hearingTypeCode");
        refHearingTypeDao.setHearingTypeDesc("description");
        refHearingTypeDao.setCategory("category");
        refHearingTypeDao.setSeqNo(3);
        refHearingTypeDao.setListSequence(4);
        refHearingTypeDao.setCourtId(5);
        refHearingTypeDao.setCreatedBy("User");
        refHearingTypeDao.setLastUpdatedBy(refHearingTypeDao.getCreatedBy());
        refHearingTypeDao.setCreationDate(LocalDateTime.now());
        refHearingTypeDao.setLastUpdateDate(refHearingTypeDao.getCreationDate());
        return Optional.of(refHearingTypeDao);
    }
}
