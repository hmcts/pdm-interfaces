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

package uk.gov.hmcts.pdm.publicdisplay.common.task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for thread pool which uses Mockito in addition to EasyMock to be able to mock a static
 * method on a Spring class.
 * 
 * @author uphillj
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("PMD")
class LocalTaskExecutorThreadPoolTest extends AbstractJUnit {
    /** Logger. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(LocalTaskExecutorThreadPoolTest.class);

    /** Number of threads for task executor. */
    private static final int NUMBER_THREADS = 5;

    /** Number of test jobs. */
    private static final int NUMBER_JOBS = 50;

    /** Maximum job sleep time. */
    private static final int TIME_JOB = 5;

    /** Timeout for tests before they error. */
    private static final int TIME_TEST = NUMBER_JOBS * TIME_JOB * 10;

    private static final String NOT_EQUAL = "Not equal";
    private static final String FALSE = "FALSE";

    /** Random number generator. */
    private final Random random = new Random();

    /** The class under test. */
    private LocalTaskExecutorThreadPool classUnderTest;

    /** Task executor given to class under test. */
    private ExecutorService taskExecutor;

    /** Count down of jobs as they complete. */
    private CountDownLatch jobsDoneCountDown;

    /** Number of jobs scheduled. */
    private int jobsScheduledCount;

    /**
     * Setup.
     * 
     * @throws Exception if error in setup
     */
    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new LocalTaskExecutorThreadPool();
        classUnderTest.setThreadCount(NUMBER_THREADS);

        // Create a task executor which class will retrieve
        taskExecutor = Executors.newFixedThreadPool(NUMBER_THREADS);
        Mockito.mockStatic(SchedulerFactoryBean.class);
        Mockito.when(SchedulerFactoryBean.getConfigTimeTaskExecutor()).thenReturn(taskExecutor);

        // Initialise the class under test
        try {
            classUnderTest.initialize();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Teardown.
     */
    @AfterEach
    public void teardown() {
        taskExecutor.shutdownNow();
        Mockito.clearAllCaches();
    }

    /**
     * Test running jobs in thread pool.
     * 
     * @throws InterruptedException if not all jobs run
     */
    @Test
    @Timeout(TIME_TEST)
    void testRunInThread() throws InterruptedException {
        // Setup the test objects
        final Runnable scheduler = createScheduler(createJobs(NUMBER_JOBS));
        final Runnable shutdown = createShutdown(false);

        // Perform the test
        scheduler.run();
        shutdown.run();

        // Assert all the jobs ran after waiting for job count down
        assertTrue(jobsDoneCountDown.await(TIME_JOB * NUMBER_JOBS, TimeUnit.MILLISECONDS), FALSE);
        assertEquals(NUMBER_JOBS, jobsScheduledCount, NOT_EQUAL);
        assertEquals(0, jobsDoneCountDown.getCount(), NOT_EQUAL);
        assertThreadPool();
    }

    /**
     * Test shutdown while running jobs in thread pool.
     * 
     * @throws InterruptedException if unable to run test
     */
    @Test
    @Timeout(TIME_TEST)
    void testShutdown() throws InterruptedException {
        // Setup the test objects
        final Runnable scheduler = createScheduler(createJobs(NUMBER_JOBS));
        final Runnable shutdown = createShutdown(true);
        final List<Runnable> runnables = new ArrayList<>();
        runnables.add(scheduler);
        runnables.add(shutdown);

        // Perform the test
        runConcurrent(runnables);

        // Shutdown is random so first assert thread pool to ensure all
        // the running jobs have finished and only then assert that the
        // scheduled jobs all completed successfully
        assertThreadPool();
        assertTrue(jobsDoneCountDown.getCount() >= 0, FALSE);
        assertTrue(jobsDoneCountDown.getCount() <= NUMBER_JOBS, FALSE);
        assertEquals(NUMBER_JOBS - jobsScheduledCount, jobsDoneCountDown.getCount(), NOT_EQUAL);
    }

    /**
     * Assert thread pool state after performing test.
     */
    protected void assertThreadPool() {
        // Assert thread pool marked as shutdown
        final boolean shutdown =
            (Boolean) ReflectionTestUtils.getField(classUnderTest, "schedulerShutdown");
        assertTrue(shutdown, FALSE);

        // Assert semaphore has all its permits available which
        // may require waiting for any running jobs to complete
        final Semaphore semaphore =
            (Semaphore) ReflectionTestUtils.getField(classUnderTest, "runPermits");
        while (semaphore.availablePermits() < NUMBER_THREADS) {
            try {
                Thread.sleep(TIME_JOB);
            } catch (final InterruptedException ex) {
                LOGGER.error("Thread pool permit assert interrupted", ex);
            }
        }
        assertEquals(NUMBER_THREADS, semaphore.availablePermits(), NOT_EQUAL);

        // Assert lock is not currently locked
        final ReentrantLock lock =
            (ReentrantLock) ReflectionTestUtils.getField(classUnderTest, "lock");
        assertFalse(lock.isLocked(), "True");
    }

    /**
     * Creates a runnable which will shutdown the thread pool.
     *
     * @param waitForJobsToComplete wait for jobs to complete
     * @return the runnable
     */
    protected Runnable createShutdown(final boolean waitForJobsToComplete) {
        return new Runnable() {
            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                try {
                    Thread.sleep(random.nextInt(TIME_JOB * NUMBER_JOBS / NUMBER_THREADS));
                } catch (final InterruptedException ex) {
                    LOGGER.error("Scheduler shutdown interrupted", ex);
                } finally {
                    classUnderTest.shutdown(waitForJobsToComplete);
                }
            }
        };
    }

    /**
     * Creates a runnable which will schedule all the jobs.
     *
     * @param jobs the jobs
     * @return the runnable
     */
    protected Runnable createScheduler(final List<Runnable> jobs) {
        return new Runnable() {
            @Override
            public void run() {
                for (Runnable job : jobs) {
                    final int availableThreads = classUnderTest.blockForAvailableThreads();
                    if (availableThreads > 0 && classUnderTest.runInThread(job)) {
                        jobsScheduledCount++;
                    }
                }
            }
        };
    }

    /**
     * Creates the required number of test jobs.
     *
     * @param numberOfJobs the number of jobs
     * @return the test jobs
     */
    protected List<Runnable> createJobs(final int numberOfJobs) {
        // Reset counts of jobs scheduled and completed
        jobsScheduledCount = 0;
        jobsDoneCountDown = new CountDownLatch(numberOfJobs);

        // Create required number of jobs
        final List<Runnable> jobs = new ArrayList<>(numberOfJobs);
        for (int i = 0; i < numberOfJobs; i++) {
            jobs.add(new DummyJob(random.nextInt(TIME_JOB)));
        }
        return jobs;
    }

    /**
     * Test job which simulates work by sleeping.
     * 
     * @author uphillj
     *
     */
    protected class DummyJob implements Runnable {
        /** Time to sleep to simulate job. */
        private final long millisToSleep;

        /**
         * Instantiates a new test job.
         *
         * @param millisToSleep time to sleep
         */
        public DummyJob(final long millisToSleep) {
            this.millisToSleep = millisToSleep;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                Thread.sleep(millisToSleep);
            } catch (final InterruptedException ex) {
                LOGGER.error("Test job interrupted", ex);
            } finally {
                jobsDoneCountDown.countDown();
            }
        }
    }
}
