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

package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.annotation.PostConstruct;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ISoftwareUpdateService;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The Class SoftwareUpdateService.
 *
 * @author uphillj
 */
@Component
public class SoftwareUpdateService implements ISoftwareUpdateService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareUpdateService.class);

    /** The update directory. */
    @Value("#{applicationConfiguration.softwareUpdateDirectory}")
    private String updateDirectory;

    /** The filenames of the software updates. */
    @Value("#{applicationConfiguration.softwareUpdateFilenames}")
    private List<String> filenames;

    /** The files. */
    @SuppressWarnings("unchecked")
    private final Map<String, FileDigest> fileDigests = new ConcurrentHashMap();

    /**
     * Initialise the digests for the list of files.
     */
    @PostConstruct
    public void initialise() {
        for (String filename : filenames) {
            fileDigests.put(filename, createFileDigest(filename));
        }
    }

    private FileDigest createFileDigest(String filename) {
        return new FileDigest(filename);
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ISoftwareUpdateService#
     * getChecksum (java.lang.String)
     */
    @Override
    public String getChecksum(final String filename) {
        String checksum = null;
        if (filename != null && fileDigests.containsKey(filename)) {
            checksum = fileDigests.get(filename).getSha1();
        }
        return checksum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ISoftwareUpdateService#
     * getDownloadPath(java.lang. String)
     */
    @Override
    public File getFile(final String filename, final boolean isTest) {
        File file = null;
        if (filename != null && fileDigests.containsKey(filename)) {
            File testFile;
            if (isTest) {
                // Primarily used for unit testing, as updateDirectory is a value that comes from
                // the DB
                testFile = new File(filename);
            } else {
                testFile = new File(updateDirectory, filename);
            }
            if (testFile.exists()) {
                file = testFile;
            }
        }
        return file;
    }

    /**
     * Calculate file checksums which is called by a scheduled task.
     */
    @Override
    public void checksumFiles() {
        LOGGER.info("Checksum files task started");

        for (FileDigest fileDigest : fileDigests.values()) {
            checksumFile(fileDigest);
        }

        LOGGER.info("Checksum files task ended");
    }

    /**
     * Checksum file.
     *
     * @param fileDigest the file digest
     */
    private void checksumFile(final FileDigest fileDigest) {
        try {
            // Create the file from the directory and name
            final String filename = fileDigest.getFilename();
            final File file = new File(updateDirectory, filename);

            // If the file currently exists in the update directory
            if (file.exists()) {
                // If the file has never been hashed (digest last modified is null)
                // or modified date has changed since the last time it was hashed,
                // then re-generate the sha1 hash of the file
                final Long lastModified = Long.valueOf(file.lastModified());
                if (!lastModified.equals(fileDigest.getLastModified())) {
                    InputStream inputStream = null;
                    try {
                        // Hash using an input stream so the whole file is not loaded into
                        // memory in one go but instead the hash is generated in chunks
                        // which is why a buffered input stream is NOT used here
                        inputStream = Files.newInputStream(Paths.get(file.getPath()));
                        final String sha1 = DigestUtils.sha1Hex(inputStream); // NOSONAR
                        LOGGER.info("New checksum for file {} is {}", filename, sha1);

                        // Hash generated successfully so okay to update the file digest
                        fileDigest.setSha1(sha1);
                        fileDigest.setLastModified(lastModified);
                    } finally {
                        close(filename, inputStream);
                    }
                }

                // Else if there previously was a hash then the file has since been
                // deleted so must reset its hash back to the default empty string
            } else if (fileDigest.getSha1().length() > 0) {
                fileDigest.setSha1("");
                LOGGER.info("File {} no longer exists so checksum cleared", filename);
            }
        } catch (final IOException ex) {
            LOGGER.error("Exception occurred generating checksum for {}", fileDigest.getFilename(),
                ex);
        }
    }

    /**
     * Close the file stream and log a warning if an exception occurs.
     *
     * @param filename the filename
     * @param stream the stream
     */
    private void close(final String filename, final Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (final IOException ex) {
            LOGGER.warn("Exception occurred closing file " + filename, ex);
        }
    }

    /**
     * The Class FileDigest.
     */
    private class FileDigest {
        /** The filename. */
        private final String filename;

        /** The last modified. */
        private Long lastModified;

        /** The sha1 hash. */
        private String sha1;

        /**
         * Instantiates a new file digest.
         *
         * @param filename filename
         */
        public FileDigest(final String filename) {
            this.filename = filename;
            this.sha1 = "";
        }

        /**
         * Gets the filename.
         *
         * @return the filename
         */
        public String getFilename() {
            return filename;
        }

        /**
         * Gets the last modified.
         *
         * @return the lastModified
         */
        public Long getLastModified() {
            return lastModified;
        }

        /**
         * Sets the last modified.
         *
         * @param lastModified the lastModified to set
         */
        public void setLastModified(final Long lastModified) {
            this.lastModified = lastModified;
        }

        /**
         * Gets the sha 1.
         *
         * @return the sha1
         */
        public String getSha1() {
            return sha1;
        }

        /**
         * Sets the sha 1.
         *
         * @param sha1 the sha1 to set
         */
        public void setSha1(final String sha1) {
            this.sha1 = sha1;
        }
    }
}
