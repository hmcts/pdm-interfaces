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

package uk.gov.hmcts.config;

import com.pdm.hb.jpa.AuthorizationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit test for AuthoriizationUtil.
 *
 * @author harrism
 */
@SuppressWarnings({"PMD"})
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestMethodOrder(OrderAnnotation.class)
class AuthorizationUtilTest extends AbstractJUnit {

    private static final String EQUALS = "Result is not Equal";
    private static final String FALSE = "Result is True";
    private static final String TRUE = "Result is False";

    private static final String OAUTH2NAME = "username";
    private static final String ANONNAME = "anonusername";
    private static final String EMPTY_STRING = "";

    @Mock
    private SecurityContext mockSecurityContext;
    
    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Mock
    private OAuth2AuthenticationToken mockOAuth2AuthenticationToken;

    @Mock
    private AnonymousAuthenticationToken mockAnonymousAuthenticationToken;

    @Mock
    private DefaultOidcUser mockOAuth2User;
    
    @Mock
    private OidcIdToken mockIdToken;

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
        new LocalAuthorizationUtil();
    }

    @Test
    void testGetUsernameOauth2() {
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication())
            .thenReturn(mockOAuth2AuthenticationToken);
        Mockito.when(mockOAuth2AuthenticationToken.getPrincipal()).thenReturn(mockOAuth2User);
        Mockito.when(mockOAuth2User.getAttribute("name")).thenReturn(OAUTH2NAME);
        Mockito.when(mockOAuth2User.getIdToken()).thenReturn(mockIdToken);

        String result = AuthorizationUtil.getUsername();
        assertEquals(OAUTH2NAME, result, EQUALS);
        assertTrue(AuthorizationUtil.isAuthorised(), TRUE);
    }

    @Test
    void testGetUsernameAnonymous() {
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication())
            .thenReturn(mockAnonymousAuthenticationToken);
        Mockito.when(mockAnonymousAuthenticationToken.getName()).thenReturn(ANONNAME);

        String result = AuthorizationUtil.getUsername();
        assertEquals(ANONNAME, result, EQUALS);
        assertFalse(AuthorizationUtil.isAuthorised(), FALSE);
    }

    @Test
    void testGetUsernameInvalid() {
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(null);

        String result = AuthorizationUtil.getUsername();
        assertEquals(EMPTY_STRING, result, EQUALS);
        assertFalse(AuthorizationUtil.isAuthorised(), FALSE);
    }
    
    @Test
    void testGetUsernameNoPrincipal() {
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication())
            .thenReturn(mockOAuth2AuthenticationToken);
        Mockito.when(mockOAuth2AuthenticationToken.getPrincipal()).thenReturn(null);

        String result = AuthorizationUtil.getUsername();
        assertEquals(EMPTY_STRING, result, EQUALS);
        assertFalse(AuthorizationUtil.isAuthorised(), FALSE);
    }
    
    @Test
    void testIsAuthorised() {
        boolean result = testIsAuthorised("Bearer");
        assertTrue(result, TRUE);
    }
          
    private boolean testIsAuthorised(String authHeader) {
        Mockito.when(mockHttpServletRequest.getHeader(Mockito.isA(String.class))).thenReturn(authHeader);
        return  AuthorizationUtil.isAuthorised(mockHttpServletRequest);        
    }
    
    @Test
    void testIsNotAuthorised() {
        boolean result = testIsAuthorised(EMPTY_STRING);
        assertFalse(result, FALSE);
    }

    protected class LocalAuthorizationUtil extends AuthorizationUtil {
        LocalAuthorizationUtil() {
            super();
        }
    }
}
