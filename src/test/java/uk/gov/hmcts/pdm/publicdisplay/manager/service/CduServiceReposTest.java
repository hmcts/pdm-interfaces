package uk.gov.hmcts.pdm.publicdisplay.manager.service;

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
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping.XhbDispMgrMappingRepository;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl.XhbDispMgrUrlRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class CduServiceTestBase.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("PMD")
class CduServiceReposTest extends AbstractJUnit {


    private static final String NOTNULL = "Result is Null";

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private XhbDispMgrCduRepository mockXhbDispMgrCduRepository;

    @Mock
    private XhbCourtSiteRepository mockXhbCourtSiteRepository;

    @Mock
    private XhbDispMgrUrlRepository mockXhbDispMgrUrlRepository;

    @Mock
    private XhbDispMgrCourtSiteRepository mockXhbDispMgrCourtSiteRepository;

    @Mock
    private XhbDispMgrMappingRepository mockXhbDispMgrMappingRepository;

    //
    private CduServHelperRepos classUnderTest;


    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        classUnderTest = new CduServHelperRepos();
        
        classUnderTest.cduIpNetwork = "cduIpNetwork";
        classUnderTest.cduIpHostMin = 1;
        classUnderTest.cduIpHostMax = 999;
        classUnderTest.localProxyCommunicationEnabled = false;
        classUnderTest.fakeCdusEnabled = false;
        classUnderTest.fakeCdusRegisterEnabled = false;
    }
    
    @AfterEach
    public void teardown() {
        classUnderTest.clearRepositories();
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
    void testGetXhbDispMgrUrlRepository() {
        expectEntityManager();
        XhbDispMgrUrlRepository result = classUnderTest.getXhbDispMgrUrlRepository();
        assertNotNull(result, NOTNULL);

        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrUrlRepository",
            mockXhbDispMgrUrlRepository);
        result = classUnderTest.getXhbDispMgrUrlRepository();
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
    void testGetXhbDispMgrMappingRepository() {
        expectEntityManager();
        XhbDispMgrMappingRepository result = classUnderTest.getXhbDispMgrMappingRepository();
        assertNotNull(result, NOTNULL);

        ReflectionTestUtils.setField(classUnderTest, "xhbDispMgrMappingRepository",
            mockXhbDispMgrMappingRepository);
        result = classUnderTest.getXhbDispMgrMappingRepository();
        assertNotNull(result, NOTNULL);
    }


    private void expectEntityManager() {
        ReflectionTestUtils.setField(classUnderTest, "entityManager", mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
    }

}
