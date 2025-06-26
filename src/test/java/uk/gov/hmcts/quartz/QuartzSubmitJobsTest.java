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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test for QuartzSubmitJobs.
 *
 * @author harrism
 */
@SuppressWarnings("PMD")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuartzSubmitJobsTest extends AbstractJUnit {

    private static final String NOTNULL = "Result is Null";
    private static final String NULL = "Result is not Null";
    
    @Mock
    private JobDetail mockJobDetail;

    @InjectMocks
    private QuartzSubmitJobs classUnderTest;

    /**
     * Setup.
     */
    @BeforeEach
    public void setup() {
        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, "ragStatusUpdateCron", "dummyRagStatusUpdateCron");
        ReflectionTestUtils.setField(classUnderTest, "housekeepingPkgJobCron", "dummyHousekeepingPkgJobCron");
    }
    
    @Test
    void testRagStatusSetupJob() {
        // Run
        JobDetailFactoryBean result = classUnderTest.ragStatusSetupJob();
        // Checks
        assertNotNull(result, NOTNULL);
    }

    @Test
    void testHousekeepingJob() {
        // Run
        JobDetailFactoryBean result = classUnderTest.housekeepingJob();
        // Checks
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testTriggerRagStatusSetup() {
        // Run
        CronTriggerFactoryBean result = classUnderTest.triggerRagStatusSetup(mockJobDetail);
        // Checks
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testTriggerHousekeeping() {
        // Run
        CronTriggerFactoryBean result = classUnderTest.triggerHousekeeping(mockJobDetail);
        // Checks
        assertNotNull(result, NOTNULL);
    }
    
    @Test
    void testNullCronJons() {
        QuartzSubmitJobs localClassUnderTest = new QuartzSubmitJobs();
        // Run
        CronTriggerFactoryBean result = localClassUnderTest.triggerRagStatusSetup(mockJobDetail);
        // Checks
        assertNull(result, NULL);
        
        // Run
        result = localClassUnderTest.triggerHousekeeping(mockJobDetail);
        // Checks
        assertNull(result, NULL);
    }
}
