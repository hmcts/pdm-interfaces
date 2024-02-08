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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);
    private static final String HOST = "spring.ldap.host";
    private static final String PORT = "spring.ldap.embedded.port";
    private static final String BASE_DN = "spring.ldap.embedded.base-dn";


    @Autowired
    private Environment env;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.authorizeHttpRequests(authorize -> authorize.anyRequest().fullyAuthenticated())
                .formLogin(Customizer.withDefaults());
            // .formLogin(formLogin -> formLogin.loginPage("/login"));

            return http.build();
        } catch (Exception exception) {
            LOG.error("securityFilterChain: {}", exception.getMessage());
            return null;
        }
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) {
        try {
            auth.ldapAuthentication().userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups").contextSource().url(getLdapUrl()).and()
                .passwordCompare().passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPassword");
        } catch (Exception exception) {
            LOG.error("configure: {}", exception.getMessage());
        }
    }

    private String getLdapUrl() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(env.getProperty(HOST)).append(':').append(env.getProperty(PORT))
            .append('/').append(env.getProperty(BASE_DN));
        return stringBuilder.toString();
    }
}