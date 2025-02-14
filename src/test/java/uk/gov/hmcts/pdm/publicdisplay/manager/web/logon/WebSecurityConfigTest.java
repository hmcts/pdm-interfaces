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

import com.pdm.hb.jpa.AuthorizationUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Class WebSecurityConfig.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.TooManyFields", "PMD.LawOfDemeter",
    "PMD.CouplingBetweenObjects", "PMD.TooManyMethods", "PMD.LooseCoupling"})
class WebSecurityConfigTest extends AbstractJUnit {

    private static final String NOTNULL = "Result is Null";
    private static final String TRUE = "Result is False";

    @Mock
    private HttpSecurity mockHttpSecurity;

    @Mock
    private DefaultSecurityFilterChain mockSecurityFilterChain;

    @Mock
    private ObjectPostProcessor<Object> mockObjectPostProcessor;

    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @Mock
    private AuthenticationManagerBuilder mockAuthenticationManagerBuilder;

    @Mock
    private ApplicationContext mockApplicationContext;

    @Mock
    private RequestMatcher mockRequestMatcher;

    @Mock
    private HandlerMappingIntrospector mockHandlerMappingIntrospector;

    @Mock
    private HttpServletRequest mockHttpServletRequest;

    @Mock
    private HttpRequest mockHttpRequest;

    @Mock
    private HttpServletResponse mockHttpServletResponse;

    @Mock
    private FilterChain mockFilterChain;

    @Mock
    private OAuth2AuthenticationToken mockAuthentication;

    @Mock
    private DefaultOidcUser mockPrincipal;

    @Mock
    private OidcIdToken mockToken;

    @Mock
    private AuthenticationException mockAuthenticationException;

    @Mock
    private ClientHttpRequestExecution mockClientHttpRequestExecution;

    @Mock
    private ClientHttpResponse mockClientHttpResponse;

    @Mock
    private AbstractOAuth2Token mockOAuth2Token;

    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository mockHttpCookieOAuth2AuthorizationRequestRepository;

    @Mock
    private URI mockUri;

    @Mock
    private SecurityContext mockSecurityContext;

    @Mock
    private HttpHeaders mockHttpHeaders;

    @InjectMocks
    private WebSecurityConfig classUnderTest;

    private LocalWebSecurityConfig classUnderTestNoHttp;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        classUnderTest = new WebSecurityConfig();

        classUnderTestNoHttp = new LocalWebSecurityConfig();
    }

    /**
     * Teardown.
     */
    @AfterEach
    public void teardown() {
        Mockito.clearAllCaches();
    }

    @Test
    void testAuthServerFilterChain() {
        try {
            // Expects
            Mockito.when(mockHttpSecurity.build()).thenReturn(mockSecurityFilterChain);
            // Run
            SecurityFilterChain result =
                classUnderTestNoHttp.authorizationServerSecurityFilterChain(mockHttpSecurity);
            assertNotNull(result, NOTNULL);
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    void testGetAuthServerHttp() {
        try {
            HttpSecurity dummyHttpSecurity = getDummyHttpSecurity();
            // Run
            HttpSecurity result = classUnderTest.getAuthServerHttp(dummyHttpSecurity);
            assertNotNull(result, NOTNULL);
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    void testAuthClientFilterChain() {
        try {
            // Expects
            Mockito.when(mockHttpSecurity.build()).thenReturn(mockSecurityFilterChain);
            // Run
            SecurityFilterChain result =
                classUnderTestNoHttp.authorizationClientSecurityFilterChain(mockHttpSecurity);
            assertNotNull(result, NOTNULL);
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    void testGetAuthClientHttp() {
        try {
            HttpSecurity dummyHttpSecurity = getDummyHttpSecurity();
            // Run
            HttpSecurity result = classUnderTest.getAuthClientHttp(dummyHttpSecurity);
            assertNotNull(result, NOTNULL);
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    void testWebSecurityCustomizer() {
        try {
            // Expects
            Mockito.when(mockHttpSecurity.build()).thenReturn(mockSecurityFilterChain);
            // Run
            WebSecurityCustomizer result = classUnderTestNoHttp.webSecurityCustomizer();
            assertNotNull(result, NOTNULL);
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    void testGetSuccessHandler() {
        try {
            Mockito.mockStatic(SecurityContextHolder.class);
            Mockito.when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
            Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockPrincipal);
            Mockito.when(mockPrincipal.getIdToken()).thenReturn(mockToken);
            // Run
            AuthenticationSuccessHandler result = classUnderTestNoHttp.getSuccessHandler();
            assertNotNull(result, NOTNULL);
            result.onAuthenticationSuccess(mockHttpServletRequest, mockHttpServletResponse,
                mockAuthentication);
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    void testGetFailureHandler() {
        try {
            Mockito.when(mockHttpServletResponse.getOutputStream())
                .thenReturn(Mockito.mock(ServletOutputStream.class));
            Mockito.when(mockAuthenticationException.getStackTrace())
                .thenReturn(new StackTraceElement[] {});
            Mockito.when(mockAuthenticationException.getMessage()).thenReturn("error");
            // Run
            AuthenticationFailureHandler result = classUnderTest.getFailureHandler();
            assertNotNull(result, NOTNULL);
            result.onAuthenticationFailure(mockHttpServletRequest, mockHttpServletResponse,
                mockAuthenticationException);
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }
    
    @Test
    void testGetCorsConfiguration() {
        CorsConfiguration result = classUnderTest.getCorsConfiguration();
        assertNotNull(result, NOTNULL);
    }

    @Test
    void testAuthorisationTokenExistenceFilter() {
        Mockito.mockStatic(AuthorizationUtil.class);
        Mockito.mockStatic(SecurityContextHolder.class);
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
        Mockito.when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        boolean result = testAuthorisationTokenExistenceFilter("/login");
        assertTrue(result, "Unsecure " + TRUE);
        result = testAuthorisationTokenExistenceFilter("/dashboard/dashboard");
        assertTrue(result, "Secure " + TRUE);
        result = testAuthorisationTokenExistenceFilter("/");
        assertTrue(result, "Root " + TRUE);
    }

    private boolean testAuthorisationTokenExistenceFilter(String uri) {
        Mockito.when(AuthorizationUtil.getToken(Mockito.isA(Authentication.class))).thenReturn(mockToken);
        Mockito.when(mockHttpServletRequest.getRequestURI()).thenReturn(uri);
        boolean result = false;
        try {
            WebSecurityConfig.AuthorisationTokenExistenceFilter filter =
                classUnderTestNoHttp.getAuthorisationTokenExistenceFilter();
            filter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse,
                mockFilterChain);
            result = true;
        } catch (ServletException | IOException ex) {
            fail(ex.getMessage());
        }
        return result;
    }

    
    private HttpSecurity getDummyHttpSecurity() {
        String[] emptyStringArray = {};
        Mockito.when(mockApplicationContext.getBeanNamesForType(AuthorizationEventPublisher.class))
            .thenReturn(emptyStringArray);
        Mockito.when(mockApplicationContext.getBeanNamesForType(GrantedAuthorityDefaults.class))
            .thenReturn(emptyStringArray);

        HttpSecurity dummyHttpSecurity = new HttpSecurity(mockObjectPostProcessor,
            mockAuthenticationManagerBuilder, new ConcurrentHashMap<>());
        dummyHttpSecurity.authenticationManager(mockAuthenticationManager);
        dummyHttpSecurity.setSharedObject(ApplicationContext.class, mockApplicationContext);
        dummyHttpSecurity.securityMatcher(mockRequestMatcher);
        return dummyHttpSecurity;
    }
    
    class LocalWebSecurityConfig extends WebSecurityConfig {

        public LocalWebSecurityConfig() {
            super();
        }

        @Override
        protected HttpSecurity getAuthServerHttp(HttpSecurity http) {
            return mockHttpSecurity;
        }

        @Override
        protected HttpSecurity getAuthClientHttp(HttpSecurity http) {
            return mockHttpSecurity;
        }

        protected AuthorisationTokenExistenceFilter getAuthorisationTokenExistenceFilter() {
            return new AuthorisationTokenExistenceFilter();
        }
        
        @Override
        protected HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
            return mockHttpCookieOAuth2AuthorizationRequestRepository;
        }

    }
}
