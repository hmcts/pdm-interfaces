package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.initialization.InitializationService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Title: OAuth2HelperTest Test.
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: CGI
 * @author Mark Harris
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OAuth2HelperTest {

    private static final String NOTNULL = "Result is null";
    private static final String TRUE = "Result is false";
    private static final String DUMMY_TENANTID = "tenantIdValue";
    private static final String DUMMY_CLIENTID = "clientIdValue";
    private static final String DUMMY_CLIENTSECRET = "clientSecretValue";

    @Mock
    private Environment mockEnvironment;

    @InjectMocks
    private OAuth2Helper classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new OAuth2Helper(mockEnvironment);

        ReflectionTestUtils.setField(classUnderTest, "tenantId", DUMMY_TENANTID);
        ReflectionTestUtils.setField(classUnderTest, "clientId", DUMMY_CLIENTID);
        ReflectionTestUtils.setField(classUnderTest, "clientSecret", DUMMY_CLIENTSECRET);
    }

    @AfterEach
    public void tearDown() {
        // Clear down statics
        Mockito.clearAllCaches();
    }

    @Test
    void testDefaultConstructor() {
        Mockito.mockStatic(InitializationService.class);
        InitializationService mockInitializationService = Mockito.mock(InitializationService.class);
        Mockito.when(InitializationService.getInstance()).thenReturn(mockInitializationService);
        Mockito.when(mockInitializationService.getEnvironment()).thenReturn(mockEnvironment);
        boolean result = false;
        try {
            new OAuth2Helper();
            result = true;
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
        assertTrue(result, TRUE);
    }

    @Test
    void testGetTenantId() {
        String result = classUnderTest.getTenantId();
        assertNotNull(result, NOTNULL);
    }

    @Test
    void testGetClientId() {
        String result = classUnderTest.getClientId();
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testGetClientSecret() {
        String result = classUnderTest.getClientSecret();
        assertNotNull(result, NOTNULL);
    }
}

