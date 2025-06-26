/*
 * Copyrights and Licenses
 * 
 * Copyright (c) 2015-2016 by the Ministry of Justice. All rights reserved. Redistribution and use
 * in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer. - Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution. - Products derived
 * from this software may not be called "XHIBIT Public Display Manager" nor may
 * "XHIBIT Public Display Manager" appear in their names without prior written permission of the
 * Ministry of Justice. - Redistributions of any form whatsoever must retain the following
 * acknowledgment: "This product includes XHIBIT Public Display Manager." This software is provided
 * "as is" and any expressed or implied warranties, including, but not limited to, the implied
 * warranties of merchantability and fitness for a particular purpose are disclaimed. In no event
 * shall the Ministry of Justice or its contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of
 * substitute goods or services; loss of use, data, or profits; or business interruption). However
 * caused any on any theory of liability, whether in contract, strict liability, or tort (including
 * negligence or otherwise) arising in any way out of the use of this software, even if advised of
 * the possibility of such damage.
 */

package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgruserdetails.XhbDispMgrUserDetailsRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.ServiceException;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUserDetails;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IUserDetailsService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.users.UserAddCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.users.UserRemoveCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;


/**
 * Unit test for UserDetailsService which uses Mockito in addition to EasyMock to be able to mock a
 * static method on a Spring class.
 *
 * @author boparaij
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("PMD")
abstract class UserDetailsServiceTest extends AbstractJUnit {

    /** The Constant USER_NAME. */
    protected static final String USER_NAME = "User";

    protected static final String NOT_EQUAL = "Not equal";

    protected static final String FALSE = "False";

    protected static final String NULL = "Null";

    protected static final String NOT_NULL = "Not null";

    protected static final String TRUE = "True";

    /** The class under test. */
    @InjectMocks
    protected final IUserDetailsService classUnderTest = new UserDetailsService();

    /** The mock disp mgr user service repo. */
    @Mock
    protected XhbDispMgrUserDetailsRepository mockUserDetailsRepo;

    /** The user add command. */
    protected final UserAddCommand userAddCommand = getTestUserAddCommand();

    /** The user remove command. */
    protected final UserRemoveCommand userRemoveCommand = getTestUserRemoveCommand();

    /** The mock security context. */
    @Mock
    protected SecurityContext mockSecurityContext;

    /** The mock authentication. */
    @Mock
    protected OAuth2AuthenticationToken mockAuthentication;
    
    @Mock
    protected OAuth2User mockOAuth2User;
    
    @Mock
    protected EntityManager mockEntityManager;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        Mockito.mockStatic(SecurityContextHolder.class);
    }

    /**
     * Teardown.
     */
    @AfterEach
    public void teardown() {
        Mockito.clearAllCaches();
    }

    /**
     * Test remove user valid.
     */
    @Test
    void testAddUserValid() {
        // Capture the UserDetails
        final ArgumentCaptor<IUserDetails> capturedUserDetails =
            ArgumentCaptor.forClass(IUserDetails.class);
        
        // Mock the entityManager
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        
        // Perform the test
        classUnderTest.addUser(userAddCommand);

        // Capture
        verify(mockUserDetailsRepo).saveDaoFromBasicValue(capturedUserDetails.capture());

        // Assert that the objects are as Mockito.whened
        assertEquals(capturedUserDetails.getValue().getUserName(), userAddCommand.getUserName(),
            NOT_EQUAL);
        assertEquals(capturedUserDetails.getValue().getUserRole(),
            userAddCommand.getUserRole().toString(), NOT_EQUAL);
    }

    /**
     * Test remove user is null.
     */
    @Test
    void testRemoveUserIsNull() {
        // Capture the UserName
        final ArgumentCaptor<String> capturedUserName = ArgumentCaptor.forClass(String.class);

        // Define a mock version of the called methods
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito.when(mockUserDetailsRepo.findUserDetailsByUserName(capturedUserName.capture()))
            .thenReturn(null);
        try {
            // Perform the test
            classUnderTest.removeUser(userRemoveCommand);
        } catch (Exception e) {
            assertEquals(e.getClass(), ServiceException.class, NOT_EQUAL);
        } finally {
            // Assert that the objects are as Mockito.whened
            assertEquals(userRemoveCommand.getUserName(), capturedUserName.getValue(), NOT_EQUAL);
        }
    }

    /**
     * Test is user with user name success.
     */
    @Test
    void testIsUserWithUserNameTrue() {
        // Capture the UserName
        final ArgumentCaptor<String> capturedUserName = ArgumentCaptor.forClass(String.class);

        // Define a mock version of the called methods
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito.when(mockUserDetailsRepo.isUserDetailsWithUserName(capturedUserName.capture()))
            .thenReturn(true);

        // Perform the test
        final boolean result = classUnderTest.isUserWithUserName(USER_NAME);

        // Assert that the objects are as Mockito.whened
        assertTrue(result, FALSE);
        assertEquals(USER_NAME, capturedUserName.getValue(), NOT_EQUAL);
    }

    /**
     * Test is user with user name false.
     */
    @Test
    void testIsUserWithUserNameFalse() {
        // Capture the UserName
        final ArgumentCaptor<String> capturedUserName = ArgumentCaptor.forClass(String.class);

        // Define a mock version of the called methods
        Mockito.when(mockUserDetailsRepo.getEntityManager()).thenReturn(mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        Mockito.when(mockUserDetailsRepo.isUserDetailsWithUserName(Mockito.isA(String.class)))
            .thenReturn(false);

        // Perform the test
        final boolean result = classUnderTest.isUserWithUserName(USER_NAME);

        // Capture
        verify(mockUserDetailsRepo).isUserDetailsWithUserName(capturedUserName.capture());

        // Assert that the objects are as Mockito.whened
        assertFalse(result, TRUE);
        assertEquals(USER_NAME, capturedUserName.getValue(), NOT_EQUAL);
    }

    @Test
    void testEntityManager() {
        UserDetailsServiceRepository localClassUnderTest = new UserDetailsServiceRepository() {
            
            @Override
            public EntityManager getEntityManager() {
                return super.getEntityManager();
            }
        };
        ReflectionTestUtils.setField(localClassUnderTest, "entityManager", mockEntityManager);
        Mockito.when(mockEntityManager.isOpen()).thenReturn(true);
        try (EntityManager result = localClassUnderTest.getEntityManager()) {
            assertNotNull(result, NULL);
        }
    }
    
    /**
     * Test is user with user name null.
     */
    @Test
    void testIsUserWithUserNameNull() {
        // Perform the test
        final boolean result = classUnderTest.isUserWithUserName(null);

        // Assert that the objects are as Mockito.whened
        assertFalse(result, TRUE);
    }

    /**
     * Gets the test user add command.
     *
     * @return the test user add command
     */
    protected UserAddCommand getTestUserAddCommand() {
        final UserAddCommand addCommand = new UserAddCommand();
        addCommand.setUserName("TEST_USER_NAME");
        addCommand.setUserRole(UserRole.ROLE_USER);
        return addCommand;
    }

    /**
     * Gets the test user remove command.
     *
     * @return the test user remove command
     */
    protected UserRemoveCommand getTestUserRemoveCommand() {
        final UserRemoveCommand removeCommand = new UserRemoveCommand();
        removeCommand.setUserName("TEST_USER_NAME");

        return removeCommand;
    }

    /**
     * Mock get session user name.
     *
     * @param isMatch the is match. Used to determine if user names match.
     */
    protected void mockGetSessionUserName(final boolean isMatch) {
        // set the user dependent on if a match is required. I.e. is same user of not when comparing
        // in assert
        String user;
        if (isMatch) {
            user = USER_NAME;
        } else {
            user = USER_NAME + "_NO_MATCH";
        }

        // Define a mock version of the called methods
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockOAuth2User);
        Mockito.when(mockOAuth2User.getAttribute("name")).thenReturn(user);
    }

}
