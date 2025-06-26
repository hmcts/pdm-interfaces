package uk.gov.hmcts.pdm.publicdisplay.manager.security;

import org.apache.cxf.common.security.SimpleGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The class EncryptDecryptUtilityTest.
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EncryptDecryptUtilityTest extends AbstractJUnit {

    private static final String EQUALS = "Result not equal";
    
    /** The mock security context. */
    @Mock
    private SecurityContext mockSecurityContext;

    /** The mock authentication. */
    @Mock
    private Authentication mockAuthentication;
    
    @Mock
    private SimpleGroup mockPrincipal;

    /**
     * Test Encryption and Decryption.
     */
    @Test
    void testEncryptionDecryption() {
        // Expects
        Mockito.mockStatic(SecurityContextHolder.class);
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);    
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockPrincipal);
        // Run
        String testString = "This is a test";
        String encryptedData = EncryptDecryptUtility.INSTANCE.encryptData(testString);
        String decryptedData = EncryptDecryptUtility.INSTANCE.decryptData(encryptedData);
        // Checks
        assertEquals(testString, decryptedData, EQUALS);
        Mockito.clearAllCaches();
    }
}
