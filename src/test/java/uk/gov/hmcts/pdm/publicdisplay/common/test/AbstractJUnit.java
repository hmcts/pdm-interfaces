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

package uk.gov.hmcts.pdm.publicdisplay.common.test;

import org.apache.commons.lang.SystemUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The class AbstractJUnitTest.
 * 
 * @author uphillj
 *
 */
@SuppressWarnings({"PMD.DoNotUseThreads", "PMD.CloseResource"})
public class AbstractJUnit {
    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJUnit.class);

    private static final String THREAD = "Thread ";

    /** Test watcher which logs start and finish of every test. */



    public TestWatcher loggerWatcher = new TestWatcher() {

        @Override
        public void testSuccessful(final ExtensionContext context) {
            LOGGER.info("Finished test successfully: " + context.getTestClass().get().getName()
                + "." + context.getDisplayName());
        }

        @Override
        public void testFailed(final ExtensionContext context, Throwable cause) {
            LOGGER.info("Test Failed: " + context.getTestClass().get().getName() + "."
                + context.getDisplayName() + ", with error: " + cause.getMessage());
        }
    };

    protected AbstractJUnit() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    /**
     * Runs a list of runnables concurrently which is useful for testing multi-threaded code.
     * 
     * @param runnables list of runnables
     * @throws InterruptedException if running is interrupted
     */
    protected void runConcurrent(final List<? extends Runnable> runnables)
        throws InterruptedException {
        // Create executor to run all the supplied runnables
        final int threads = runnables.size();
        final ExecutorService threadPool = Executors.newFixedThreadPool(threads);

        try {
            // Cyclic barrier used to make sure all the runnables run at same time
            final CyclicBarrier allThreadsReady = new CyclicBarrier(threads);

            // Count down latch used to make sure this method does not exit
            // until all the runnables have completed their processing
            final CountDownLatch allThreadsDone = new CountDownLatch(threads);

            // Setup all the runnables so they are all waiting to be started
            for (final Runnable runnable : runnables) {
                threadPool.submit(createRunnable(allThreadsReady, runnable, allThreadsDone));
            }

            // Wait until all the threads have completed
            allThreadsDone.await();
        } finally {
            threadPool.shutdownNow();
        }
    }

    private Runnable createRunnable(CyclicBarrier allThreadsReady, Runnable runnable,
        CountDownLatch allThreadsDone) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.info(THREAD + Thread.currentThread().getId() + " waiting");
                    allThreadsReady.await();
                    LOGGER.info(THREAD + Thread.currentThread().getId() + " running");
                    runnable.run();
                } catch (final InterruptedException | BrokenBarrierException ex) { // NOSONAR
                    LOGGER.error("Runnable was interrupted while waiting to start", ex);
                } finally {
                    allThreadsDone.countDown();
                    LOGGER.info(THREAD + Thread.currentThread().getId() + " ends");
                }
            }
        };
    }

    /**
     * Checks if is windows.
     *
     * @return true, if is windows
     */
    protected boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }
}
