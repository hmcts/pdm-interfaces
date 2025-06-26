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
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The Class WebSecurityConfig.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings({"PMD"})
class WebSecurityConfigTest extends AbstractJUnit {

    private static final String NOTNULL = "Result is Null";
    private static final String TRUE = "Result is False";

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
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
            HttpSecurity result = classUnderTest.getAuthClientHttp(dummyHttpSecurity);
            assertNotNull(result);
        } catch (Exception e) {
            fail(e.getMessage());
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
        Mockito.when(AuthorizationUtil.getToken(Mockito.isA(Authentication.class)))
            .thenReturn(mockToken);
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

    @Test
    void testSuccessHandlerSetsAuthenticationAndRedirects() throws Exception {
        // Mock static context holder
        var contextMocked = mockStatic(SecurityContextHolder.class);
        contextMocked.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

        when(mockAuthentication.getPrincipal()).thenReturn(mockPrincipal);
        when(mockPrincipal.getIdToken()).thenReturn(mockToken);
        when(mockToken.getTokenValue()).thenReturn("mock-token");
        when(mockHttpCookieOAuth2AuthorizationRequestRepository
            .loadAuthorizationToken(mockHttpServletRequest)).thenReturn(mockToken);
        when(
            mockHttpCookieOAuth2AuthorizationRequestRepository.loadUsername(mockHttpServletRequest))
                .thenReturn("test-user");

        AuthenticationSuccessHandler handler = classUnderTestNoHttp.getSuccessHandler();

        handler.onAuthenticationSuccess(mockHttpServletRequest, mockHttpServletResponse,
            mockAuthentication);

        verify(mockSecurityContext).setAuthentication(mockAuthentication);
        verify(mockHttpServletResponse).sendRedirect("/dashboard/dashboard");
        contextMocked.close();
    }

    @Test
    void testFailureHandlerReturns401AndJsonResponse() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(mockAuthenticationException.getMessage()).thenReturn("error occurred");
        when(mockAuthenticationException.getStackTrace()).thenReturn(new StackTraceElement[0]);

        AuthenticationFailureHandler handler = classUnderTest.getFailureHandler();

        handler.onAuthenticationFailure(mockHttpServletRequest, response,
            mockAuthenticationException);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus()); // line 108
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("timestamp"));
        assertTrue(responseBody.contains("exception"));
        assertTrue(responseBody.contains("error occurred"));
    }


    @Test
    void testFailureHandlerTriggersSetStatusAndWritesJson() throws Exception {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ServletOutputStream outputStream = Mockito.mock(ServletOutputStream.class);

        when(response.getOutputStream()).thenReturn(outputStream);
        when(mockAuthenticationException.getStackTrace()).thenReturn(new StackTraceElement[0]);
        when(mockAuthenticationException.getMessage()).thenReturn("fail-msg");

        AuthenticationFailureHandler handler = classUnderTest.getFailureHandler();
        handler.onAuthenticationFailure(mockHttpServletRequest, response,
            mockAuthenticationException);

        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(outputStream).println(Mockito.contains("fail-msg"));
    }


    @Test
    void testUnauthorisedRequestTriggersRedirectAndStatus() throws Exception {
        try (MockedStatic<AuthorizationUtil> mockedAuthUtil = mockStatic(AuthorizationUtil.class)) {
            when(mockHttpServletRequest.getRequestURI()).thenReturn("/secure/page");
            mockedAuthUtil.when(() -> AuthorizationUtil.isAuthorised(any(HttpServletRequest.class)))
                .thenReturn(false);

            WebSecurityConfig.AuthorisationTokenExistenceFilter filter =
                classUnderTestNoHttp.getAuthorisationTokenExistenceFilter();

            filter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse,
                mockFilterChain);

            verify(mockHttpServletResponse).setStatus(HttpStatus.UNAUTHORIZED.value());
            verify(mockHttpServletResponse).sendRedirect("/oauth2/authorization/internal-azure-ad");
        }
    }



    @Test
    void testAuthorisationTokenExistenceFilterAddsTokenHeaderAndUsername() throws Exception {
        try (MockedStatic<AuthorizationUtil> mockedAuthUtil = mockStatic(AuthorizationUtil.class)) {
            when(mockHttpServletRequest.getRequestURI()).thenReturn("/dashboard/dashboard");
            when(mockToken.getTokenValue()).thenReturn("mock-token");
            when(mockHttpCookieOAuth2AuthorizationRequestRepository
                .loadAuthorizationToken(mockHttpServletRequest)).thenReturn(mockToken);
            when(mockHttpCookieOAuth2AuthorizationRequestRepository
                .loadUsername(mockHttpServletRequest)).thenReturn("user1");

            mockedAuthUtil.when(() -> AuthorizationUtil.isAuthorised(any(HttpServletRequest.class)))
                .thenReturn(true); // or false in the redirect test

            WebSecurityConfig.AuthorisationTokenExistenceFilter filter =
                classUnderTestNoHttp.getAuthorisationTokenExistenceFilter();

            filter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse,
                mockFilterChain);

            verify(mockFilterChain).doFilter(any(), eq(mockHttpServletResponse));
        }

    }


    @Test
    void testFailureHandlerReturns401AndJsonResponse_capturesBody() throws Exception {
        var outputStream = Mockito.mock(ServletOutputStream.class);
        var response = Mockito.mock(HttpServletResponse.class);

        when(response.getOutputStream()).thenReturn(outputStream);
        when(mockAuthenticationException.getMessage()).thenReturn("failure msg");
        when(mockAuthenticationException.getStackTrace()).thenReturn(new StackTraceElement[0]);

        AuthenticationFailureHandler handler = classUnderTest.getFailureHandler();
        handler.onAuthenticationFailure(mockHttpServletRequest, response,
            mockAuthenticationException);

        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(outputStream).println(Mockito.contains("\"exception\":\"failure msg\""));
    }



    @Test
    void testAuthorisationTokenExistenceFilterRedirectsWhenNotAuthorised() throws Exception {
        try (MockedStatic<AuthorizationUtil> mockedAuthUtil = mockStatic(AuthorizationUtil.class)) {
            when(mockHttpServletRequest.getRequestURI()).thenReturn("/dashboard/dashboard");
            when(mockHttpCookieOAuth2AuthorizationRequestRepository
                .loadAuthorizationToken(mockHttpServletRequest)).thenReturn(null); // No token
            when(AuthorizationUtil.isAuthorised(any(HttpServletRequest.class))).thenReturn(false);

            WebSecurityConfig.AuthorisationTokenExistenceFilter filter =
                classUnderTestNoHttp.getAuthorisationTokenExistenceFilter();

            filter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse,
                mockFilterChain);

            verify(mockHttpServletResponse).sendRedirect("/oauth2/authorization/internal-azure-ad");
            verify(mockHttpServletResponse).setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }


    @Test
    void testTokenAddedToRequestWrapper() throws Exception {
        try (MockedStatic<AuthorizationUtil> mocked = mockStatic(AuthorizationUtil.class)) {
            when(mockHttpServletRequest.getRequestURI()).thenReturn("/dashboard");
            when(mockToken.getTokenValue()).thenReturn("token123");
            when(mockHttpCookieOAuth2AuthorizationRequestRepository
                .loadAuthorizationToken(mockHttpServletRequest)).thenReturn(mockToken);
            when(mockHttpCookieOAuth2AuthorizationRequestRepository.loadUsername(any()))
                .thenReturn("userX");
            mocked.when(() -> AuthorizationUtil.isAuthorised(any(HttpServletRequest.class)))
                .thenReturn(true);

            WebSecurityConfig.AuthorisationTokenExistenceFilter filter =
                classUnderTestNoHttp.getAuthorisationTokenExistenceFilter();

            filter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse,
                mockFilterChain);

            verify(mockHttpCookieOAuth2AuthorizationRequestRepository)
                .loadAuthorizationToken(mockHttpServletRequest);
            verify(mockHttpCookieOAuth2AuthorizationRequestRepository).loadUsername(any());
            verify(mockFilterChain).doFilter(any(), eq(mockHttpServletResponse));
        }
    }



    @Test
    void testIsSecureUri_withRootUri_returnsFalse() {
        boolean result = WebSecurityConfig.isSecureUri("/");
        assertFalse(result);
    }

    @Test
    void testIsSecureUri_withWhitelistedUri_returnsFalse() {
        boolean result = WebSecurityConfig.isSecureUri("/css/bootstrap.min.css");
        assertFalse(result);
    }

    @Test
    void testIsSecureUri_withNonWhitelistedUri_returnsTrue() {
        boolean result = WebSecurityConfig.isSecureUri("/admin/settings");
        assertTrue(result);
    }


    @SuppressWarnings("removal")
    private HttpSecurity getDummyHttpSecurity() {
        HttpSecurity httpSecurity = Mockito.mock(HttpSecurity.class, Mockito.RETURNS_DEEP_STUBS);
        // Optionally mock specific chained methods you want to test against
        return httpSecurity;
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
