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
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class LocalProxyServiceFinderTest.
 *
 * @author Luke Gittins
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("PMD")
class LocalProxyServiceFinderTest extends AbstractJUnit {

    private static final String NOTNULL = "Result is Null";

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private XhbCourtSiteRepository mockXhbCourtSiteRepository;
    
    @Mock
    private XhbDispMgrCourtSiteRepository mockXhbDispMgrCourtSiteRepository;
    
    @Mock
    private XhbDispMgrScheduleRepository mockXhbDispMgrScheduleRepository;
    
    @Mock
    private XhbDispMgrLocalProxyRepository mockXhbDispMgrLocalProxyRepository;
    
    @Mock
    private XhbDispMgrCduRepository mockXhbDispMgrCduRepository;

    private LocalProxyServiceFinder classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new LocalProxyServiceFinder();
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

        ReflectionTestUtils.setField(classUnderTest, "xhbCourtSiteRepository",
            mockXhbCourtSiteRepository);
        result = classUnderTest.getXhbCourtSiteRepository();
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testGetXhbDispMgrCourtSiteRepository() {
        expectEntityManager();
        XhbDispMgrCourtSiteRepository result = classUnderTest.getXhbDispMgrCourtSiteRepository();
        assertNotNull(result, NOTNULL);

        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrCourtSiteRepository",
            mockXhbDispMgrCourtSiteRepository);
        result = classUnderTest.getXhbDispMgrCourtSiteRepository();
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testGetXhbDispMgrScheduleRepository() {
        expectEntityManager();
        XhbDispMgrScheduleRepository result = classUnderTest.getXhbDispMgrScheduleRepository();
        assertNotNull(result, NOTNULL);

        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrScheduleRepository",
            mockXhbDispMgrScheduleRepository);
        result = classUnderTest.getXhbDispMgrScheduleRepository();
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testGetXhbDispMgrLocalProxyRepository() {
        expectEntityManager();
        XhbDispMgrLocalProxyRepository result = classUnderTest.getXhbDispMgrLocalProxyRepository();
        assertNotNull(result, NOTNULL);

        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrLocalProxyRepository",
            mockXhbDispMgrLocalProxyRepository);
        result = classUnderTest.getXhbDispMgrLocalProxyRepository();
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testGetXhbDispMgrCduRepository() {
        expectEntityManager();
        XhbDispMgrCduRepository result = classUnderTest.getXhbDispMgrCduRepository();
        assertNotNull(result, NOTNULL);

        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrCduRepository",
            mockXhbDispMgrCduRepository);
        result = classUnderTest.getXhbDispMgrCduRepository();
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
