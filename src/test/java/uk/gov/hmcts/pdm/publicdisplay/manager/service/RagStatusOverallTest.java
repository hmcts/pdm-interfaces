package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.persistence.EntityManager;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class RagStatusOverallTest.
 *
 * @author boparaij
 */
@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
abstract class RagStatusOverallTest extends RagStatusServiceTestBase {

    /**
     * Test get rag status green upper limit.
     */
    @Test
    void testRagStatusOverallTotalsGreenUpperLimit() {
        // Add the mock calls to child classes
        expectWeightingTotal(80L, 80L);

        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_AMBER, 80);
        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_RED, 60);

        // Perform the test
        final String results = classUnderTest.getRagStatusOverall();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(GREEN_CHAR.toString(), results, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
    }

    /**
     * Test get rag status green lower limit.
     */
    @Test
    void testRagStatusOverallTotalsGreenLowerLimit() {
        // Add the mock calls to child classes
        expectWeightingTotal(80L, 64L);

        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_AMBER, 80);
        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_RED, 60);

        // Perform the test
        final String results = classUnderTest.getRagStatusOverall();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(GREEN_CHAR.toString(), results, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
    }

    /**
     * Test get rag status amber upper limit.
     */
    @Test
    void testRagStatusOverallTotalsAmberUpperLimit() {
        // Add the mock calls to child classes
        expectWeightingTotal(800L, 632L);

        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_AMBER, 80);
        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_RED, 60);

        // Perform the test
        final String results = classUnderTest.getRagStatusOverall();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(AMBER_CHAR.toString(), results, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
    }

    /**
     * Test get rag status amber lower limit.
     */
    @Test
    void testRagStatusOverallTotalsAmberLowerLimit() {
        // Add the mock calls to child classes
        expectWeightingTotal(80L, 48L);

        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_AMBER, 80);
        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_RED, 60);

        // Perform the test
        final String results = classUnderTest.getRagStatusOverall();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(AMBER_CHAR.toString(), results, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
    }

    /**
     * Test get rag status red upper limit.
     */
    @Test
    void testRagStatusOverallTotalsRedUpperLimit() {
        // Add the mock calls to child classes
        expectWeightingTotal(800L, 472L);

        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_AMBER, 80);
        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_RED, 60);

        // Perform the test
        final String results = classUnderTest.getRagStatusOverall();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(RED_CHAR.toString(), results, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
    }

    /**
     * Test get rag status red lower limit.
     *
     */
    @Test
    void testRagStatusOverallTotalsRedLowerLimit() {
        // Add the mock calls to child classes
        expectWeightingTotal(800L, 0L);

        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_AMBER, 80);
        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_RED, 60);

        // Perform the test
        final String results = classUnderTest.getRagStatusOverall();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(RED_CHAR.toString(), results, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
    }

    /**
     * Test get rag status zero.
     */
    @Test
    void testRagStatusOverallTotalsZero() {
        // Add the mock calls to child classes
        expectWeightingTotal(0L, 0L);

        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_AMBER, 80);
        ReflectionTestUtils.setField(classUnderTest, RAG_STATUS_RED, 60);

        // Perform the test
        final String results = classUnderTest.getRagStatusOverall();

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(GREEN_CHAR.toString(), results, NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCduRepo);
    }
    
    @Test
    void testEntityManager() {
        RagStatusRepository localClassUnderTest = new RagStatusRepository() {
            
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
            assertNotNull(result, NULL);
        }
    }
    
    protected void expectRepositoryUtilCheck() {
        expect(mockDispMgrCourtSiteRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockCduRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockLocalProxyRepo.getEntityManager()).andReturn(mockEntityManager).anyTimes();
        expect(mockEntityManager.isOpen()).andReturn(true).anyTimes();
    }    

    private void expectWeightingTotal(Long total, Long operational) {
        expectRepositoryUtilCheck();
        expect(mockCduRepo.getCduWeightingTotal()).andReturn(total);
        expect(mockCduRepo.getCduWeightingOperational()).andReturn(operational);
        replay(mockCduRepo);
        replay(mockEntityManager);
    }

}
