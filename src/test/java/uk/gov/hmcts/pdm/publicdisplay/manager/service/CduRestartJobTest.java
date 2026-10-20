package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.quartz.JobExecutionContext;
import uk.gov.hmcts.pdm.business.entities.xhbconfigprop.XhbConfigPropDao;
import uk.gov.hmcts.pdm.business.entities.xhbconfigprop.XhbConfigPropRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * The Class CduRestartJobTest.
 *
 * @author gittinsl
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CduRestartJobTest extends AbstractJUnit {

    private static final String SITES_TO_RESTART_CDUS = "SITES_TO_RESTART_CDUS";

    @Mock
    private EntityManager mockEntityManager;
    
    @Mock
    private XhbConfigPropRepository mockXhbConfigPropRepository;

    @Mock
    private ICduService mockCduService;

    @Mock
    private JobExecutionContext mockJobExecutionContext;

    @Mock
    private Query mockQuery;

    @InjectMocks
    private CduRestartJob classUnderTest;

    @Test
    void testExecuteRestartsCdusForConfiguredCourtSites() throws Exception {
        XhbConfigPropDao configProperty =
            new XhbConfigPropDao(1, SITES_TO_RESTART_CDUS, "1, 2");
        List<CduDto> cdusForFirstSite = List.of(new CduDto(), new CduDto());
        List<CduDto> cdusForSecondSite = List.of(new CduDto());

        when(mockXhbConfigPropRepository.findByPropertyNameSafe(SITES_TO_RESTART_CDUS))
            .thenReturn(List.of(configProperty));
        when(mockCduService.getCdusBySiteID(1L)).thenReturn(cdusForFirstSite);
        when(mockCduService.getCdusBySiteID(2L)).thenReturn(cdusForSecondSite);

        classUnderTest.execute(mockJobExecutionContext);

        verify(mockXhbConfigPropRepository).findByPropertyNameSafe(SITES_TO_RESTART_CDUS);
        verify(mockCduService).getCdusBySiteID(1L);
        verify(mockCduService).restartCdu(cdusForFirstSite);
        verify(mockCduService).getCdusBySiteID(2L);
        verify(mockCduService).restartCdu(cdusForSecondSite);
    }

    @Test
    void testExecuteWhenNoCourtSiteIdsFound() throws Exception {
        when(mockXhbConfigPropRepository.findByPropertyNameSafe(SITES_TO_RESTART_CDUS))
            .thenReturn(List.of());

        classUnderTest.execute(mockJobExecutionContext);

        verify(mockXhbConfigPropRepository).findByPropertyNameSafe(SITES_TO_RESTART_CDUS);
        verifyNoInteractions(mockCduService);
    }

    @Test
    void testExecuteCreatesRepositoryWhenNoneIsInjected() throws Exception {
        CduRestartJob jobWithoutInjectedRepository = new CduRestartJob();

        try (MockedStatic<EntityManagerUtil> entityManagerUtil =
                 Mockito.mockStatic(EntityManagerUtil.class)) {
            entityManagerUtil.when(() -> EntityManagerUtil.isEntityManagerActive(null))
                .thenReturn(false);
            entityManagerUtil.when(EntityManagerUtil::getEntityManager)
                .thenReturn(mockEntityManager);
            when(mockEntityManager.createNamedQuery("XHB_CONFIG_PROP.findByPropertyName"))
                .thenReturn(mockQuery);
            when(mockQuery.setParameter("propertyName", SITES_TO_RESTART_CDUS))
                .thenReturn(mockQuery);
            when(mockQuery.getResultList()).thenReturn(List.of());

            jobWithoutInjectedRepository.execute(mockJobExecutionContext);

            entityManagerUtil.verify(() -> EntityManagerUtil.isEntityManagerActive(null));
            entityManagerUtil.verify(EntityManagerUtil::getEntityManager, Mockito.times(2));
            verify(mockEntityManager).createNamedQuery("XHB_CONFIG_PROP.findByPropertyName");
            verify(mockQuery).setParameter("propertyName", SITES_TO_RESTART_CDUS);
            verify(mockQuery).getResultList();
            verify(mockEntityManager).close();
        }
    }

}
