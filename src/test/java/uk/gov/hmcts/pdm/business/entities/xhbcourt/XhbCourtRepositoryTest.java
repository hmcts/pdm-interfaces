package uk.gov.hmcts.pdm.business.entities.xhbcourt;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("PMD")
class XhbCourtRepositoryTest {

    private EntityManager mockEntityManager;
    private Query mockQuery;
    private XhbCourtRepository xhbCourtRepository;

    @BeforeEach
    void setUp() {
        mockEntityManager = mock(EntityManager.class);
        mockQuery = mock(Query.class);
        xhbCourtRepository = new XhbCourtRepository(mockEntityManager);
    }

    @Test
    void testFindAllReturnsNonObsoleteCourts() {
        // Arrange dummy data
        XhbCourtDao court1 = new XhbCourtDao();
        court1.setCourtId(1);
        court1.setCourtName("Alpha");

        XhbCourtDao court2 = new XhbCourtDao();
        court2.setCourtId(2);
        court2.setCourtName("Beta");

        List<XhbCourtDao> mockResult = Arrays.asList(court1, court2);
        String expectedHql =
            "FROM uk.gov.hmcts.pdm.business.entities.xhbcourt.XhbCourtDao e WHERE e.obsInd IS NULL OR e.obsInd <> 'Y'";

        // Static mock
        try (MockedStatic<EntityManagerUtil> mockedStatic = mockStatic(EntityManagerUtil.class)) {
            mockedStatic.when(() -> EntityManagerUtil.isEntityManagerActive(mockEntityManager))
                .thenReturn(false);
            mockedStatic.when(EntityManagerUtil::getEntityManager).thenReturn(mockEntityManager);

            when(mockEntityManager.createQuery(expectedHql)).thenReturn(mockQuery);
            when(mockQuery.getResultList()).thenReturn(mockResult);

            // Act
            List<XhbCourtDao> result = xhbCourtRepository.findAll();

            // Assert
            assertEquals(2, result.size());
            assertEquals("Alpha", result.get(0).getCourtName());
            assertEquals("Beta", result.get(1).getCourtName());

            verify(mockEntityManager).createQuery(expectedHql);
        }
    }
}
