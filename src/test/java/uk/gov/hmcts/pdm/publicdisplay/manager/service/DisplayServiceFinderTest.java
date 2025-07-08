package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplay.XhbDisplayRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation.XhbDisplayLocationRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdisplaytype.XhbDisplayTypeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrotationsets.XhbRotationSetsRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class DisplayServiceFinder.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("PMD")
class DisplayServiceFinderTest extends AbstractJUnit {


    private static final String NOTNULL = "Result is Null";
    
    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private XhbDisplayRepository mockXhbDisplayRepository;
    
    @Mock
    private XhbCourtSiteRepository mockXhbCourtSiteRepository;
    
    @Mock
    private XhbDisplayTypeRepository mockXhbDisplayTypeRepository;
    
    @Mock
    private XhbDisplayLocationRepository mockXhbDisplayLocationRepository;
    
    @Mock
    private XhbRotationSetsRepository mockXhbRotationSetsRepository;
    
    private DisplayServiceFinder classUnderTest;


    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        classUnderTest = new DisplayServiceFinder();
    }

    @AfterEach
    public void teardown() {
        Mockito.clearAllCaches();
    }

    @Test
    void testGetXhbCourtSiteRepository() {
        expectEntityManager();
        XhbCourtSiteRepository result = classUnderTest.getXhbCourtSiteRepository();
        assertNotNull(result, NOTNULL);
        
        ReflectionTestUtils.setField(classUnderTest, "xhbCourtSiteRepository", mockXhbCourtSiteRepository);
        result = classUnderTest.getXhbCourtSiteRepository();
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testGetXhbDisplayRepository() {
        expectEntityManager();
        XhbDisplayRepository result = classUnderTest.getXhbDisplayRepository();
        assertNotNull(result, NOTNULL);
        
        ReflectionTestUtils.setField(classUnderTest, "xhbDisplayRepository", mockXhbDisplayRepository);
        result = classUnderTest.getXhbDisplayRepository();
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testGetXhbDisplayTypeRepository() {
        expectEntityManager();
        XhbDisplayTypeRepository result = classUnderTest.getXhbDisplayTypeRepository();
        assertNotNull(result, NOTNULL);
        
        ReflectionTestUtils.setField(classUnderTest, "xhbDisplayTypeRepository", mockXhbDisplayTypeRepository);
        result = classUnderTest.getXhbDisplayTypeRepository();
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testGetXhbDisplayLocationRepository() {
        expectEntityManager();
        XhbDisplayLocationRepository result = classUnderTest.getXhbDisplayLocationRepository();
        assertNotNull(result, NOTNULL);
        
        ReflectionTestUtils.setField(classUnderTest, "xhbDisplayLocationRepository", mockXhbDisplayLocationRepository);
        result = classUnderTest.getXhbDisplayLocationRepository();
        assertNotNull(result, NOTNULL);
    }
   
    @Test
    void testGetXhbRotationSetsRepository() {
        expectEntityManager();
        XhbRotationSetsRepository result = classUnderTest.getXhbRotationSetsRepository();
        assertNotNull(result, NOTNULL);
        
        ReflectionTestUtils.setField(classUnderTest, "xhbRotationSetsRepository", mockXhbRotationSetsRepository);
        result = classUnderTest.getXhbRotationSetsRepository();
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testNullEntityManager() {
        Mockito.mockStatic(EntityManagerUtil.class);
        Mockito.when(EntityManagerUtil.getEntityManager()).thenReturn(mockEntityManager);
        XhbCourtSiteRepository result = classUnderTest.getXhbCourtSiteRepository();
        assertNotNull(result, NOTNULL);
    }
    
    private void expectEntityManager() {
        ReflectionTestUtils.setField(classUnderTest, "entityManager", mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
    }

}
