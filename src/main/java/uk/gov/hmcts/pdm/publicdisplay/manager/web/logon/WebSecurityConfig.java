package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdm.hb.jpa.AuthorizationUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings({"PMD.SignatureDeclareThrowsException", "PMD.LawOfDemeter", "removal",
    "PMD.ExcessiveImports", "PMD.CouplingBetweenObjects", "squid:S4502"})
public class WebSecurityConfig {

    private static final Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);
    private static final String HOME_URL = "/dashboard/dashboard";
    private static final String LOGIN_URL = "/oauth2/authorization/internal-azure-ad";
    private static final String ROOT_URL = "/";
    private static final String[] AUTH_WHITELIST =
        {"/health/**", "/error**", "/fonts/glyph*", "/css/xhibit.css", "/css/bootstrap.min.css",
            "/js/bootstrap.min.js", "/WEB-INF/jsp/error**", "/css/**", "/js/**", "favicon.ico",
            "/login**", "/oauth2**", "/default-ui.css", "/logout**", "/WEB-INF/jsp/logon**"};
    private HttpCookieOAuth2AuthorizationRequestRepository cookie;

    /**
     * Authorisation Server filterchain.
     */
    @Order(Ordered.LOWEST_PRECEDENCE)
    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
        throws Exception {
        return getAuthServerHttp(http).build();
    }

    /**
     * Get the Authorisation Server HTTP.
     */
    protected HttpSecurity getAuthServerHttp(HttpSecurity http) throws Exception {
        http.sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http;
    }

    /**
     * Authorisation client filterchain.
     */
    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    @Bean
    public SecurityFilterChain authorizationClientSecurityFilterChain(HttpSecurity http)
        throws Exception {
        return getAuthClientHttp(http).build();
    }

    /**
     * Get the Authorisation Client HTTP.
     */
    protected HttpSecurity getAuthClientHttp(HttpSecurity http) throws Exception {
        http.oauth2Login(
            auth -> auth.successHandler(getSuccessHandler()).failureHandler(getFailureHandler())
                .authorizationEndpoint(endPoint -> endPoint
                    .authorizationRequestRepository(cookieAuthorizationRequestRepository())))
            .addFilterAfter(new AuthorisationTokenExistenceFilter(),
                SecurityContextHolderFilter.class).csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(request -> getCorsConfiguration()));
        return http;
    }
            
    protected CorsConfiguration getCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://pdda-public-display-manager");
        configuration.addAllowedMethod("*"); 
        configuration.addAllowedHeader("*"); 
        return configuration;
    }

    /**
     * Store the authorization in the cookie.
     */
    protected HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        if (cookie == null) {
            cookie = new HttpCookieOAuth2AuthorizationRequestRepository();
        }
        return cookie;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(AUTH_WHITELIST);
    }

    @Bean
    public AuthenticationSuccessHandler getSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {
                LOG.info("Login Success");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                LOG.info("The user {} has logged in.",
                    AuthorizationUtil.getUsername(authentication));

                OidcIdToken token = AuthorizationUtil.getToken(authentication);
                cookieAuthorizationRequestRepository().saveAuthorizationToken(token, request,
                    response);
                cookieAuthorizationRequestRepository()
                    .saveUsername(AuthorizationUtil.getUsername(authentication), request, response);

                response.setStatus(HttpStatus.OK.value());
                response.sendRedirect(HOME_URL);
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler getFailureHandler() {
        return new AuthenticationFailureHandler() {

            @Override
            public void onAuthenticationFailure(HttpServletRequest request,
                HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
                List<StackTraceElement> stacktrace = Arrays.asList(exception.getStackTrace());
                String errorMsg =
                    stacktrace.isEmpty() ? exception.getMessage() : stacktrace.get(0).toString();
                LOG.info("Login Failure {}", errorMsg);
                LOG.info("Response Status {}", response.getStatus());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                // Get the error
                Map<String, Object> data = new ConcurrentHashMap<>();
                data.put("timestamp", LocalDateTime.now().toString());
                data.put("exception", exception.getMessage());

                ObjectMapper objectMapper = new ObjectMapper();
                response.getOutputStream().println(objectMapper.writeValueAsString(data));
            }
        };
    }

    public final class AuthorisationTokenExistenceFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
            MutableHttpServletRequest requestWrapper = new MutableHttpServletRequest(request);

            // Check if the request needs the authorisation adding
            OidcIdToken token =
                cookieAuthorizationRequestRepository().loadAuthorizationToken(request);
            if (token != null) {
                String tokenValue = token.getTokenValue();
                LOG.info("Token value: {}", tokenValue);
                requestWrapper.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
                requestWrapper.setAttribute("Username",
                    cookieAuthorizationRequestRepository().loadUsername(requestWrapper));
            }

            // Check if we are secure and authorised, if not return to the login page
            if (isSecureUri(request.getRequestURI())) {
                LOG.info("Secure request {}", request.getRequestURI());
                if (!AuthorizationUtil.isAuthorised(requestWrapper)) {
                    LOG.info("Unauthorised. Return to login");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.sendRedirect(LOGIN_URL);
                    return;
                }
            }
            filterChain.doFilter(requestWrapper, response);
        }
    }

    public static boolean isSecureUri(String uri) {
        if (ROOT_URL.equals(uri)) {
            return false;
        } else {
            for (String entry : AUTH_WHITELIST) {
                String whitelisted = entry.replace("**", "");
                if (uri.startsWith(whitelisted)) {
                    LOG.info("Unsecure request for {} matched on whitelist entry {}", uri, entry);
                    return false;
                }
            }
            return true;
        }
    }

}
