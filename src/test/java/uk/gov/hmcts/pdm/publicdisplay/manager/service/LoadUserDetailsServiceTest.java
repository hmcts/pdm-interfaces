package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUserDetails;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UserDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Unit test for UserDetailsService which uses Mockito in addition to EasyMock to be able to mock a
 * static method on a Spring class.
 *
 * @author boparaij
 */
@SuppressWarnings("PMD")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
abstract class LoadUserDetailsServiceTest extends UserDetailsServiceTest {

    /** The user details list. */
    protected final List<IUserDetails> userDetailsList = getTestUserDetailList();

    /** The user details. */
    protected final IUserDetails userDetails = getTestUserDetails("");

    /**
     * Test valid username.
     */
    @Test
    void testLoadUserByUsernameValid() {
        // Capture the UserName
        final ArgumentCaptor<String> capturedUserName = ArgumentCaptor.forClass(String.class);

        // Define a mock version of the called methods, to retrieve user details via a passed in
        // user
        // name
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito.when(mockUserDetailsRepo.findUserDetailsByUserName(capturedUserName.capture()))
            .thenReturn(userDetails);

        // Perform the test
        final UserDetails result = classUnderTest.loadUserByUsername(USER_NAME);

        // Capture
        verify(mockUserDetailsRepo).findUserDetailsByUserName(capturedUserName.capture());

        // Check the results
        assertNotNull(result, NULL);
        assertEquals(result.getUsername(), capturedUserName.getValue(), NOT_EQUAL);
        assertTrue(
            result.getAuthorities().contains(new SimpleGrantedAuthority(UserRole.ROLE_USER_VALUE)),
            NOT_EQUAL);
    }

    /**
     * Test load user error.
     * 
     * @throws Exception the exception
     */
    @Test
    void testLoadUserByUsernameWithException() throws Exception {

        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException("Mock data access exception");

        // Capture the UserName
        final ArgumentCaptor<String> capturedUserName = ArgumentCaptor.forClass(String.class);

        // Define a mock version of method calls with exception handling
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito.when(mockUserDetailsRepo.findUserDetailsByUserName(Mockito.isA(String.class)))
            .thenThrow(dataRetrievalFailureException);

        UserDetails result = null;
        try {
            // Perform the test
            result = classUnderTest.loadUserByUsername(USER_NAME);
        } catch (Exception e) {
            assertEquals(dataRetrievalFailureException.getClass(), e.getClass(), NOT_EQUAL);
        } finally {
            // Capture
            verify(mockUserDetailsRepo).findUserDetailsByUserName(capturedUserName.capture());

            // Check the results
            assertNull(result, NOT_NULL);
            assertEquals(USER_NAME, capturedUserName.getValue(), NOT_EQUAL);
        }
    }

    /**
     * Test invalid username.
     */
    @Test
    void testLoadUserByUsernameInvalid() {
        // Capture the UserName
        final ArgumentCaptor<String> capturedUserName = ArgumentCaptor.forClass(String.class);

        // Define a mock version of the called methods with invalid/null user name
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito.when(mockUserDetailsRepo.findUserDetailsByUserName(capturedUserName.capture()))
            .thenReturn(null);

        // Perform the test
        final UserDetails result = classUnderTest.loadUserByUsername(USER_NAME);

        // Check the results
        assertNotNull(result, NULL);
        assertEquals(result.getUsername(), capturedUserName.getValue(), NOT_EQUAL);
        assertEquals(0, result.getAuthorities().size(), NOT_EQUAL);
    }

    /**
     * Test get users valid.
     */
    @Test
    void testGetUsersValid() {
        // Define a mock version of the called methods
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito.when(mockUserDetailsRepo.getUserDetails()).thenReturn(userDetailsList);

        // Perform the test
        final List<UserDto> results = classUnderTest.getUsers();

        // Assert that the objects are as Mockito.whened
        assertNotNull(results, NULL);
        assertEquals(results.size(), userDetailsList.size(), NOT_EQUAL);
    }

    /**
     * Test add user valid.
     */
    @Test
    void testRemoveUserValid() {
        // Capture the UserName
        final ArgumentCaptor<String> capturedUserName = ArgumentCaptor.forClass(String.class);

        // Define a mock version of the called methods
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito.when(mockUserDetailsRepo.findUserDetailsByUserName(capturedUserName.capture()))
            .thenReturn(userDetails);
        mockUserDetailsRepo.deleteDaoFromBasicValue(userDetails);
        Mockito.atLeastOnce();

        // Perform the test
        classUnderTest.removeUser(userRemoveCommand);

        // Assert that the objects are as Mockito.whened
        assertEquals(userRemoveCommand.getUserName(), capturedUserName.getValue(), NOT_EQUAL);
    }

    /**
     * Gets the test user detail list.
     *
     * @return the test user detail list
     */
    private List<IUserDetails> getTestUserDetailList() {
        final List<IUserDetails> userDetailsList = new ArrayList<>();
        final IUserDetails user1 = getTestUserDetails("ONE");
        final IUserDetails user2 = getTestUserDetails("TWO");
        final IUserDetails user3 = getTestUserDetails("THREE");

        userDetailsList.add(user1);
        userDetailsList.add(user2);
        userDetailsList.add(user3);

        return userDetailsList;
    }

    /**
     * Gets the test user details.
     *
     * @param appendToUserName the append to user name
     * @return the test user details
     */
    protected IUserDetails getTestUserDetails(final String appendToUserName) {
        final IUserDetails user =
            new uk.gov.hmcts.pdm.publicdisplay.manager.domain.UserDetails();
        user.setUserName(USER_NAME + "_" + appendToUserName);
        user.setUserRole(UserRole.ROLE_USER_VALUE);
        return user;
    }
}
