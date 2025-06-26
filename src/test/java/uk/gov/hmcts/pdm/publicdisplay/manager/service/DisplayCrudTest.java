package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.persistence.EntityManager;
import org.easymock.Capture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplay.XhbDisplayDao;
import uk.gov.hmcts.pdm.business.entities.xhbdisplay.XhbDisplayRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation.XhbDisplayLocationDao;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation.XhbDisplayLocationRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaytype.XhbDisplayTypeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrotationsets.XhbRotationSetsDao;
import uk.gov.hmcts.pdm.business.entities.xhbrotationsets.XhbRotationSetsRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RotationSetsDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IDisplayService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.display.DisplayAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.display.DisplayCreateCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.display.DisplayDeleteCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"PMD"})
abstract class DisplayCrudTest extends AbstractJUnit {

    /** The class under test. */
    protected IDisplayService classUnderTest;

    /** The mock Display Location Repo. */
    protected XhbDisplayLocationRepository mockDispLocationRepo;

    /** The mock Display Location Repo. */
    protected XhbDisplayRepository mockDisplayRepo;

    protected XhbRotationSetsRepository mockRotationSetsRepo;

    protected XhbDisplayTypeRepository mockDisplayTypeRepository;

    protected XhbCourtSiteRepository mockCourtSiteRepo;
    
    protected EntityManager mockEntityManager;

    protected static final String NOT_EQUAL = "Not equal";
    protected static final String FALSE = "False";
    protected static final String NOTNULL = "Null";


    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new DisplayService();

        // Setup the mock version of the called classes
        mockDispLocationRepo = createMock(XhbDisplayLocationRepository.class);
        mockDisplayRepo = createMock(XhbDisplayRepository.class);
        mockRotationSetsRepo = createMock(XhbRotationSetsRepository.class);
        mockDisplayTypeRepository = createMock(XhbDisplayTypeRepository.class);
        mockCourtSiteRepo = createMock(XhbCourtSiteRepository.class);
        mockEntityManager = createMock(EntityManager.class);

        // Map the mock to the class under tests called class
        ReflectionTestUtils.setField(classUnderTest, "xhbDisplayLocationRepository",
            mockDispLocationRepo);
        ReflectionTestUtils.setField(classUnderTest, "xhbDisplayRepository", mockDisplayRepo);
        ReflectionTestUtils.setField(classUnderTest, "xhbRotationSetsRepository",
            mockRotationSetsRepo);
        ReflectionTestUtils.setField(classUnderTest, "xhbDisplayTypeRepository",
            mockDisplayTypeRepository);
        ReflectionTestUtils.setField(classUnderTest, "xhbCourtSiteRepository", mockCourtSiteRepo);
    }

    @Test
    void rotationSetsTest() {

        XhbRotationSetsDao xhbRotationSetsDao = new XhbRotationSetsDao();
        xhbRotationSetsDao.setRotationSetId(1);
        xhbRotationSetsDao.setCourtId(2);
        xhbRotationSetsDao.setDescription("description");
        xhbRotationSetsDao.setDefaultYn("Yn");

        List<XhbRotationSetsDao> rotationSetsDaos = new ArrayList<>();
        rotationSetsDaos.add(xhbRotationSetsDao);

        // Add the mock calls to child classes
        expect(mockRotationSetsRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockRotationSetsRepo.findByCourtId(1)).andReturn(rotationSetsDaos);

        replay(mockRotationSetsRepo);
        replay(mockEntityManager);

        // Perform the test
        List<RotationSetsDto> rotationSetsDtos = classUnderTest.getRotationSets(1);

        // Assert that the objects are as expected
        assertEquals(rotationSetsDaos.get(0).getDescription(),
            rotationSetsDtos.get(0).getDescription(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockRotationSetsRepo);
        verify(mockEntityManager);
    }

    @Test
    void createDisplayTest() {

        DisplayCreateCommand displayCreateCommand = new DisplayCreateCommand();
        displayCreateCommand.setXhibitCourtSiteId(10L);
        displayCreateCommand.setDescriptionCode("A Description Code");
        displayCreateCommand.setDisplayTypeId(1);
        displayCreateCommand.setRotationSetId(2);

        XhbDisplayLocationDao xhbDisplayLocationDao = new XhbDisplayLocationDao();
        xhbDisplayLocationDao.setDisplayLocationId(1);

        // Create the displayDao
        XhbDisplayDao displayDao = new XhbDisplayDao();
        displayDao.setDisplayTypeId(displayCreateCommand.getDisplayTypeId());
        displayDao.setDescriptionCode(displayCreateCommand.getDescriptionCode());
        displayDao.setDisplayLocationId(xhbDisplayLocationDao.getDisplayLocationId());
        displayDao.setRotationSetId(displayCreateCommand.getRotationSetId());
        displayDao.setLocale(Locale.getDefault().getLanguage() + Locale.getDefault().getCountry());
        displayDao.setShowUnassignedYn("Y");

        // Capture the XhbDisplayDao
        final Capture<XhbDisplayDao> capturedDisplayDao = newCapture();

        // Add the mock calls to child classes
        expect(mockDispLocationRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockDisplayRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockDispLocationRepo
            .findByCourtSiteId(displayCreateCommand.getXhibitCourtSiteId().intValue()))
                .andReturn(xhbDisplayLocationDao);
        mockDisplayRepo.saveDao(capture(capturedDisplayDao));
        expectLastCall();
        
        replay(mockDispLocationRepo);
        replay(mockEntityManager);
        replay(mockDisplayRepo);

        // Perform the test
        classUnderTest.createDisplay(displayCreateCommand);

        // Assert that the objects are as expected
        assertEquals(capturedDisplayDao.getValue().getDisplayTypeId(),
            displayDao.getDisplayTypeId(), NOT_EQUAL);
        assertEquals(capturedDisplayDao.getValue().getDescriptionCode(),
            displayDao.getDescriptionCode(), NOT_EQUAL);
        assertEquals(capturedDisplayDao.getValue().getDisplayLocationId(),
            xhbDisplayLocationDao.getDisplayLocationId(), NOT_EQUAL);
        assertEquals(capturedDisplayDao.getValue().getRotationSetId(),
            displayDao.getRotationSetId(), NOT_EQUAL);
        assertEquals(capturedDisplayDao.getValue().getLocale(), displayDao.getLocale(), NOT_EQUAL);
        assertEquals(capturedDisplayDao.getValue().getShowUnassignedYn(),
            displayDao.getShowUnassignedYn(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockDispLocationRepo);
        verify(mockEntityManager);
        verify(mockDisplayRepo);

    }

    @Test
    void deleteDisplayTest() {

        DisplayDeleteCommand displayDeleteCommand = new DisplayDeleteCommand();

        // Create the displayDao
        XhbDisplayDao displayDao = new XhbDisplayDao();

        Optional<XhbDisplayDao> displayDaoOptional = Optional.of(displayDao);

        // Capture the XhbDisplayDao
        final Capture<Optional<XhbDisplayDao>> capturedDisplayDao = newCapture();

        // Add the mock calls to child classes
        expect(mockDisplayRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockDisplayRepo.findById(displayDeleteCommand.getDisplayId()))
            .andReturn(displayDaoOptional);
        mockDisplayRepo.deleteDao(capture(capturedDisplayDao));
        expectLastCall();
        
        replay(mockDisplayRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.deleteDisplay(displayDeleteCommand);

        // Assert that the objects are as expected
        assertEquals(displayDao.getDisplayId(), capturedDisplayDao.getValue().get().getDisplayId(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockDisplayRepo);
        verify(mockEntityManager);

    }

    @Test
    void deleteDisplayEmptyTest() {

        DisplayDeleteCommand displayDeleteCommand = new DisplayDeleteCommand();
        displayDeleteCommand.setDisplayId(1);

        Optional<XhbDisplayDao> displayDaoOptional = Optional.empty();

        // Add the mock calls to child classes
        expect(mockDisplayRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockDisplayRepo.findById(displayDeleteCommand.getDisplayId()))
            .andReturn(displayDaoOptional);

        replay(mockDisplayRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.deleteDisplay(displayDeleteCommand);

        // Verify the expected mocks were called
        verify(mockDisplayRepo);
        verify(mockEntityManager);

    }

    @Test
    void updateDisplayTest() {

        DisplayAmendCommand displayAmendCommand = new DisplayAmendCommand();
        displayAmendCommand.setDisplayId(1);
        displayAmendCommand.setDisplayTypeId(2);
        displayAmendCommand.setRotationSetId(3);
        displayAmendCommand.setXhibitCourtSiteId(10L);

        XhbDisplayLocationDao xhbDisplayLocationDao = new XhbDisplayLocationDao();

        // Create the displayDao
        XhbDisplayDao displayDao = new XhbDisplayDao();

        Optional<XhbDisplayDao> xhbDisplayDao = Optional.of(displayDao);

        // Add the mock calls to child classes
        expect(mockDisplayRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockDispLocationRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockDisplayRepo.findById(displayAmendCommand.getDisplayId()))
            .andReturn(xhbDisplayDao);
        expect(mockDispLocationRepo
            .findByCourtSiteId(displayAmendCommand.getXhibitCourtSiteId().intValue()))
                .andReturn(xhbDisplayLocationDao);
        expect(mockDisplayRepo.updateDao(displayDao)).andReturn(xhbDisplayDao);
        
        replay(mockDisplayRepo);
        replay(mockEntityManager);
        replay(mockDispLocationRepo);
        

        // Perform the test
        classUnderTest.updateDisplay(displayAmendCommand);

        // Verify the expected mocks were called
        verify(mockDisplayRepo);
        verify(mockEntityManager);
        verify(mockDispLocationRepo);
        

    }

    @Test
    void updateDisplayEmptyTest() {

        DisplayAmendCommand displayAmendCommand = new DisplayAmendCommand();

        Optional<XhbDisplayDao> xhbDisplayDao = Optional.empty();

        // Add the mock calls to child classes
        expect(mockDisplayRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
        expect(mockDisplayRepo.findById(displayAmendCommand.getDisplayId()))
            .andReturn(xhbDisplayDao);

        replay(mockDisplayRepo);
        replay(mockEntityManager);

        // Perform the test
        classUnderTest.updateDisplay(displayAmendCommand);

        // Verify the expected mocks were called
        verify(mockDisplayRepo);
        verify(mockEntityManager);

    }

    protected List<DisplayTypeDto> createDisplayTypeDtoList() {
        List<DisplayTypeDto> displayTypeDtos = new ArrayList<>();
        DisplayTypeDto displayTypeDto = new DisplayTypeDto();
        displayTypeDto.setDisplayTypeId(6);
        displayTypeDtos.add(displayTypeDto);
        return displayTypeDtos;
    }

    protected List<XhibitCourtSiteDto> createCourtSiteDtoList() {
        List<XhibitCourtSiteDto> xhibitCourtSiteDtos = new ArrayList<>();
        XhibitCourtSiteDto courtsiteDto = new XhibitCourtSiteDto();
        courtsiteDto.setId(8L);
        xhibitCourtSiteDtos.add(courtsiteDto);
        return xhibitCourtSiteDtos;
    }

    protected List<RotationSetsDto> createRotationSetsDtoList() {
        List<RotationSetsDto> rotationSetsDtos = new ArrayList<>();
        RotationSetsDto rotationSetsDto = new RotationSetsDto();
        rotationSetsDto.setRotationSetId(7);
        rotationSetsDtos.add(rotationSetsDto);
        return rotationSetsDtos;
    }

}
