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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.initialization.InitializationService;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * The Class LogonControllerTest.
 *
 * @author boparaij
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings({"PMD"})
class LogonControllerTest extends AbstractJUnit {

    private static final String NOT_EQUAL = "Not equal";

    private static final String FALSE = "False";

    private static final String NULL = "Null";

    /** The Constant for the JSP Folder. */
    private static final String FOLDER_LOGON = "logon";

    /** The view name mapping home. */
    private static final String VIEW_NAME_DASHBOARD = "dashboard/dashboard";

    /** The view name mapping logon. */
    private static final String VIEW_NAME_LOGON_LOGIN = "oauth2/authorization/internal-azure-ad";

    /** The view name logon logout. */
    private static final String VIEW_NAME_LOGON_LOGOUT = FOLDER_LOGON + "/logout";

    /** The mock security context. */
    @Mock
    private SecurityContext mockSecurityContext;

    /** The mock authentication. */
    @Mock
    private OAuth2AuthenticationToken mockAuthentication;

    @Mock
    private DefaultOidcUser mockUser;
    
    @Mock
    private OidcIdToken mockIdToken;

    @Mock
    private InitializationService mockInitializationService;

    @Mock
    private Environment mockEnvironment;

    @Mock
    private URI mockUri;

    /** The mock mvc. */
    private MockMvc mockMvc;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        LogonController classUnderTest = new LogonController();

        // Stop circular view path error
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view");
        viewResolver.setSuffix(".jsp");

        // Setup the mock version of the modelMvc
        mockMvc =
            MockMvcBuilders.standaloneSetup(classUnderTest).setViewResolvers(viewResolver).build();
    }

    /**
     * Test home.
     *
     * @throws Exception the exception
     */
    @Test
    void testHome() throws Exception {
        // Perform the test
        MvcResult results = mockMvc.perform(get("/home")).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, VIEW_NAME_DASHBOARD);
    }

    /**
     * Test logon error valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testLogonErrorValid() throws Exception {
        // Perform the test
        final MvcResult results = mockMvc.perform(get("/loginError")).andReturn();

        // Assert that the objects are as expected
        // assertEquals("true", results.getModelAndView().getModel().get("error"), NOT_EQUAL);
        assertViewName(results, VIEW_NAME_LOGON_LOGIN);
    }

    /**
     * Test logout success valid.
     *
     * @throws Exception the exception
     */
    @Test
    void testLogoutSuccessValid() throws Exception {
        // Perform the test
        MvcResult results = mockMvc.perform(get("/logoutSuccess")).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, VIEW_NAME_LOGON_LOGOUT);

        // Perform the test again for logout
        results = mockMvc.perform(get("/logout")).andReturn();

        // Assert that the objects are as expected
        assertViewName(results, VIEW_NAME_LOGON_LOGOUT);
    }

    /**
     * Test invalid session.
     *
     * @throws Exception the exception
     */
    @Test
    void testInvalidSession() throws Exception {
        // Perform the test
        final MvcResult results = mockMvc.perform(get("/invalidSession")).andReturn();

        // Assert that the objects are as expected
        assertEquals(HttpServletResponse.SC_OK, results.getResponse().getStatus(), NOT_EQUAL);
        assertEquals("invalidSession", results.getModelAndView().getModel().get("error"),
            NOT_EQUAL);
        assertViewName(results, VIEW_NAME_LOGON_LOGOUT);
    }

    /**
     * Test invalid session for ajax calls.
     *
     * @throws Exception the exception
     */
    @Test
    void testInvalidSessionAjax() throws Exception {
        // Perform the test
        final MvcResult results =
            mockMvc.perform(get("/invalidSession").header("X-Requested-With", "XMLHttpRequest"))
                .andReturn();

        // Assert that the objects are as expected
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, results.getResponse().getStatus(),
            NOT_EQUAL);
        assertEquals("invalidSession", results.getResponse().getHeader("X-Logout-URL"), NOT_EQUAL);
    }

    /**
     * Test invalid token.
     *
     * @throws Exception the exception
     */
    @Test
    void testInvalidToken() throws Exception {
        // Perform the test
        final MvcResult results = mockMvc.perform(get("/invalidToken")).andReturn();

        // Assert that the objects are as expected
        assertEquals(HttpServletResponse.SC_OK, results.getResponse().getStatus(), NOT_EQUAL);
        assertEquals("invalidToken", results.getModelAndView().getModel().get("error"), NOT_EQUAL);
        assertViewName(results, VIEW_NAME_LOGON_LOGOUT);
    }

    /**
     * Test invalid token for ajax calls.
     *
     * @throws Exception the exception
     */
    @Test
    void testInvalidTokenAjax() throws Exception {
        // Perform the test
        final MvcResult results = mockMvc
            .perform(get("/invalidToken").header("X-Requested-With", "XMLHttpRequest")).andReturn();

        // Assert that the objects are as expected
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, results.getResponse().getStatus(),
            NOT_EQUAL);
        assertEquals("invalidToken", results.getResponse().getHeader("X-Logout-URL"), NOT_EQUAL);
    }

    /**
     * Assert view name.
     *
     * @param results the results
     * @param viewName the view name
     */
    private void assertViewName(final MvcResult results, final String viewName) {
        assertNotNull(results, NULL);
        final String actualViewName = results.getModelAndView().getViewName();
        if (actualViewName.startsWith("forward:") || actualViewName.startsWith("redirect:")) {
            assertTrue(actualViewName.contains(viewName), FALSE);
        } else {
            assertEquals(actualViewName, viewName, NOT_EQUAL);
        }
    }

}
