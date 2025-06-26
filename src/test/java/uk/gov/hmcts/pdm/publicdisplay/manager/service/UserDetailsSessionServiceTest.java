package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD")
class UserDetailsSessionServiceTest extends LoadUserDetailsServiceTest {
    
    /**
     * Test is session user success.
     */
    @Test
    void testIsSessionUserTrue() {
        // Define a mock version of the called methods via private method
        mockGetSessionUserName(true);

        // Perform the test
        final boolean result = classUnderTest.isSessionUser(USER_NAME);

        // Assert that the objects are as Mockito.whened
        assertTrue(result, FALSE);
    }

    /**
     * Test is session user false.
     */
    @Test
    void testIsSessionUserFalse() {
        // Define a mock version of the called methods via private method
        mockGetSessionUserName(false);

        // Perform the test
        final boolean result = classUnderTest.isSessionUser(USER_NAME);

        // Assert that the objects are as Mockito.whened
        assertFalse(result, TRUE);
    }

    /**
     * Test is session user null.
     */
    @Test
    void testIsSessionUserNull() {
        // Perform the test
        final boolean result = classUnderTest.isSessionUser(null);

        // Assert that the objects are as Mockito.whened
        assertFalse(result, TRUE);
    }

    /**
     * Test is session authentication null.
     */
    @Test
    void testIsSessionAuthenticationNull() {
        // Define a mock version of the called methods
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(null);

        // Perform the test
        final boolean result = classUnderTest.isSessionUser(USER_NAME);

        // Assert that the objects are as Mockito.whened
        assertFalse(result, TRUE);
    }

    /**
     * Test is session user exists true.
     */
    @Test
    void testIsSessionUserExistsTrue() {
        // Capture the UserName
        final ArgumentCaptor<String> capturedUserName = ArgumentCaptor.forClass(String.class);

        // Define a mock version of the called methods via private method
        mockGetSessionUserName(true);
        // Define a mock version of the other called methods
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito.when(mockUserDetailsRepo.isUserDetailsWithUserName(capturedUserName.capture()))
            .thenReturn(true);

        // Perform the test
        final boolean result = classUnderTest.isSessionUserExist();

        // Assert that the objects are as Mockito.whened
        assertTrue(result, FALSE);
        assertEquals(USER_NAME, capturedUserName.getValue(), NOT_EQUAL);
    }
}
