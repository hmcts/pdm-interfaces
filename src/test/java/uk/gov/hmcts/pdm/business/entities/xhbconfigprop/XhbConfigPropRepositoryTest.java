package uk.gov.hmcts.pdm.business.entities.xhbconfigprop;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("PMD.CloseResource")
class XhbConfigPropRepositoryTest {

    private static final String NOT_EQUAL = "Result is not equal";
    
    @Test
    void testFindByPropertyNameSafe() {
        EntityManager mockEm = Mockito.mock(EntityManager.class);
        Query mockQuery = Mockito.mock(Query.class);
        List<XhbConfigPropDao> expectedList = Arrays.asList(Mockito.mock(XhbConfigPropDao.class));

        try (MockedStatic<EntityManagerUtil> emUtilMock = Mockito.mockStatic(EntityManagerUtil.class)) {
            emUtilMock.when(EntityManagerUtil::getEntityManager).thenReturn(mockEm);
            Mockito.when(mockEm.createNamedQuery("XHB_CONFIG_PROP.findByPropertyName")).thenReturn(mockQuery);
            Mockito.when(mockQuery.setParameter(Mockito.eq("propertyName"), Mockito.any())).thenReturn(mockQuery);
            Mockito.when(mockQuery.getResultList()).thenReturn(expectedList);

            XhbConfigPropRepository repo = new XhbConfigPropRepository(mockEm);
            List<XhbConfigPropDao> result = repo.findByPropertyNameSafe("testName");

            assertEquals(expectedList, result, NOT_EQUAL);
            Mockito.verify(mockEm).close();
        }
    }
}