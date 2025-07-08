package uk.gov.hmcts.pdm.publicdisplay.manager.web.logon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("PMD")
@Import(WebSecurityConfig.class)
class WebSecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebSecurityConfig config;


    void testSuccessHandlerExists() {
        assertThat(config.getSuccessHandler()).isNotNull();
    }


    void testDashboardTriggersOAuth2LoginRedirect() throws Exception {
        mockMvc.perform(get("/dashboard"))
            .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/oauth2/authorization/**"));
    }


    void testAuthorizationClientSecurityFilterChainLoads() throws Exception {
        var http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        assertThat(config.authorizationClientSecurityFilterChain(http)).isNotNull();
    }
}




