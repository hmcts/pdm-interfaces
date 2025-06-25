package uk.gov.hmcts.pdm.publicdisplay.manager.web.users;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.UserDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.security.UserRole;

import java.util.List;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * The Class AddUserTest.
 *
 * @author harrism
 */
@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class AddUserTest extends UserControllerTestBase {

    /**
     * Test add user success.
     *
     * @throws Exception the exception
     */
    @Test
    void testAddUserSuccess() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<UserAddCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();
        final Capture<List<UserDto>> capturedUsers = newCapture();
        final UserAddCommand userCommand =
            getTestUserAddCommand("NewUserName", UserRole.ROLE_ADMIN);

        // Add the mock calls to child classes
        expect(mockUserDetailsService.getUsers()).andReturn(users);
        mockUserDetailsService.addUser(capture(capturedCommand));
        expectLastCall();
        mockUserPageStateHolder.setUsers(capture(capturedUsers));
        expectLastCall();
        expect(mockUserPageStateHolder.getUsers()).andReturn(users);
        expectUserAddValidator(capturedCommand, capturedBindingResult, true);
        replay(mockUserDetailsService);
        replay(mockUserPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameManageUserUrl).param(USERNAME, userCommand.getUserName())
                .param("userRole", userCommand.getUserRole().toString()).param(BTN_ADD, BTN_ADD))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameManageUser);
        assertManageUsersModel(results.getModelAndView().getModelMap());
        assertFalse(capturedBindingResult.getValue().hasErrors(), TRUE);
        assertEquals(users, capturedUsers.getValue(), NOT_EQUAL);
        assertNull(capturedCommand.getValue().getUserName(), NOT_NULL);
        assertNull(capturedCommand.getValue().getUserRole(), NOT_NULL);

        // Verify the expected mocks were called
        verify(mockUserDetailsService);
        verify(mockUserPageStateHolder);
    }

    /**
     * Test add user save error.
     *
     * @throws Exception the exception
     */
    @Test
    void testAddUserSaveError() throws Exception {
        DataRetrievalFailureException dataRetrievalFailureException =
            new DataRetrievalFailureException("Mock data access exception");

        // Capture the cduCommand object and errors passed out
        final Capture<UserAddCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();
        final UserAddCommand userCommand =
            getTestUserAddCommand("NewUserName", UserRole.ROLE_ADMIN);

        // Add the mock calls to child classes
        mockUserDetailsService.addUser(capture(capturedCommand));
        expectLastCall().andThrow(dataRetrievalFailureException);
        expect(mockUserPageStateHolder.getUsers()).andReturn(users);
        expectUserAddValidator(capturedCommand, capturedBindingResult, true);
        replay(mockUserDetailsService);
        replay(mockUserPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameManageUserUrl).param(USERNAME, userCommand.getUserName())
                .param("userRole", userCommand.getUserRole().toString()).param(BTN_ADD, BTN_ADD))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameManageUser);
        assertManageUsersModel(results.getModelAndView().getModelMap());
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(userCommand.getUserName(), capturedCommand.getValue().getUserName(),
            NOT_EQUAL);
        assertEquals(userCommand.getUserRole(), capturedCommand.getValue().getUserRole(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockUserDetailsService);
        verify(mockUserPageStateHolder);
    }

    /**
     * Test add user runtime error.
     *
     * @throws Exception the exception
     */
    @Test
    void testAddUserRuntimeError() throws Exception {
        XpdmException xpdmException = new XpdmException("Mock runtime exception");

        // Capture the cduCommand object and errors passed out
        final Capture<UserAddCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();
        final UserAddCommand userCommand =
            getTestUserAddCommand("NewUserName", UserRole.ROLE_ADMIN);

        // Add the mock calls to child classes
        mockUserDetailsService.addUser(capture(capturedCommand));
        expectLastCall().andThrow(xpdmException);
        expect(mockUserPageStateHolder.getUsers()).andReturn(users);
        expectUserAddValidator(capturedCommand, capturedBindingResult, true);
        replay(mockUserDetailsService);
        replay(mockUserPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameManageUserUrl).param(USERNAME, userCommand.getUserName())
                .param("userRole", userCommand.getUserRole().toString()).param(BTN_ADD, BTN_ADD))
            .andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameManageUser);
        assertManageUsersModel(results.getModelAndView().getModelMap());
        assertEquals(1, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(userCommand.getUserName(), capturedCommand.getValue().getUserName(),
            NOT_EQUAL);
        assertEquals(userCommand.getUserRole(), capturedCommand.getValue().getUserRole(),
            NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockUserDetailsService);
        verify(mockUserPageStateHolder);
    }

    /**
     * Test add user validation failure.
     *
     * @throws Exception the exception
     */
    @Test
    void testAddUserNullUserFailure() throws Exception {
        // Capture the cduCommand object and errors passed out
        final Capture<UserAddCommand> capturedCommand = newCapture();
        final Capture<BindingResult> capturedBindingResult = newCapture();

        // Add the mock calls to child classes
        expect(mockUserPageStateHolder.getUsers()).andReturn(users);
        expectUserAddValidator(capturedCommand, capturedBindingResult, false);
        replay(mockUserPageStateHolder);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameManageUserUrl).param(BTN_ADD, BTN_ADD)).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameManageUser);
        assertManageUsersModel(results.getModelAndView().getModelMap());
        assertEquals(3, capturedBindingResult.getValue().getErrorCount(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockUserPageStateHolder);
    }

    /**
     * Test show manage users success.
     *
     * @throws Exception the exception
     */
    @Test
    void testShowManageUsersSuccess() throws Exception {
        final Capture<List<UserDto>> capturedUsers = newCapture();

        // Add the mock calls to child classes
        expect(mockUserDetailsService.getUsers()).andReturn(users);
        mockUserPageStateHolder.reset();
        expectLastCall();
        mockUserPageStateHolder.setUsers(capture(capturedUsers));
        expect(mockUserPageStateHolder.getUsers()).andReturn(users);
        expectLastCall();
        replay(mockUserDetailsService);
        replay(mockUserPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(get(mappingNameManageUserUrl)).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, viewNameManageUser);
        assertManageUsersModel(results.getModelAndView().getModelMap());
        assertEquals(users, capturedUsers.getValue(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockUserDetailsService);
        verify(mockUserPageStateHolder);
    }

}
