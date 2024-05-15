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

package uk.gov.hmcts.quartz;

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
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for QuartzConfig.
 *
 * @author harrism
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuartzConfigTest extends AbstractJUnit {

    private static final String NOTNULL = "Result is Null";

    @Mock
    private Environment mockEnvironment;

    @Mock
    private ApplicationContext mockApplicationContext;


    @InjectMocks
    private QuartzConfig classUnderTest;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        classUnderTest = new QuartzConfig(mockApplicationContext);
        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, "env", mockEnvironment);
        ReflectionTestUtils.setField(classUnderTest, "ragStatusUpdateThreads", "2");
    }

    /**
     * Test quartzProperties.
     */
    @Test
    void testQuartzProperties() {
        // Expects
        Mockito.when(mockEnvironment.getProperty(QuartzConfig.DB_USER_NAME))
            .thenReturn("dbusername");
        Mockito.when(mockEnvironment.getProperty(QuartzConfig.DB_PASSWORD))
            .thenReturn("dbpassword");
        Mockito.when(mockEnvironment.getProperty(QuartzConfig.DB_HOST)).thenReturn("dbhost");
        Mockito.when(mockEnvironment.getProperty(QuartzConfig.DB_PORT)).thenReturn("dbport");
        Mockito.when(mockEnvironment.getProperty(QuartzConfig.DB_NAME)).thenReturn("dbname");
        // Run
        Properties properties = classUnderTest.quartzProperties();

        assertNotNull(properties, NOTNULL);
    }


}
