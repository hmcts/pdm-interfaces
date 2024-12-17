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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ISoftwareUpdateService;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class SoftwareController.
 *
 * @author uphillj
 */

@Controller
@RequestMapping("/software")
@SuppressWarnings("PMD.LooseCoupling")
public class SoftwareController {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareController.class);

    /** The software update service. */
    @Autowired
    private ISoftwareUpdateService softwareUpdateService;

    /**
     * Get the checksum for the filename.
     *
     * @param filename the filename
     * @param response the response
     * @return the checksum of the file
     * @throws NoSuchRequestHandlingMethodException thrown when invalid filename is supplied
     */
    @RequestMapping(value = "/checksum/{filename:.+}", method = RequestMethod.GET,
        produces = "text/plain")
    @ResponseBody
    public String checksum(@PathVariable("filename:.+") final String filename,
        final HttpServletResponse response) throws NoHandlerFoundException {
        LOGGER.info("Checksum requested for {}", filename);
        final String checksum = softwareUpdateService.getChecksum(filename);

        // Throw 404 exception if filename is invalid
        if (checksum == null) {
            LOGGER.error("Checksum requested for invalid filename {}", filename);
            throw new NoHandlerFoundException("checksum", filename, new HttpHeaders());
        }

        // Set response headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        LOGGER.info("Checksum for file {} is {}", filename, checksum);
        return checksum;
    }

    /**
     * Get the file for the filename.
     *
     * @param filename the filename
     * @return the file
     * @throws NoSuchRequestHandlingMethodException thrown when invalid filename is supplied
     * @throws IOException io exception
     */
    // CHECKSTYLE:OFF Line is too long
    @RequestMapping(value = "/download/{filename:.+}", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    // CHECKSTYLE:ON
    public ResponseEntity<FileSystemResource> download(
        @PathVariable("filename:.+") final String filename)
        throws NoHandlerFoundException, IOException {
        LOGGER.info("Download requested for {}", filename);
        final File file = softwareUpdateService.getFile(filename, false);

        // Throw 404 exception if filename is invalid
        if (file == null) {
            LOGGER.error("Download requested for non-existent filename {}", filename);
            throw new NoHandlerFoundException("download", filename, new HttpHeaders());
        }

        // Create response header for file download
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Content-Disposition", "attachment; filename=" + filename);
        headers.setCacheControl("no-cache, no-store, max-age=0, must-revalidate");
        headers.setPragma("no-cache");
        headers.setExpires(0);
        headers.setContentLength(file.length());

        // Create response from file and headers
        final FileSystemResource resource = new FileSystemResource(file);
        final ResponseEntity<FileSystemResource> response =
            new ResponseEntity<>(resource, headers, HttpStatus.CREATED);

        LOGGER.info("Download created for {}", filename);
        return response;
    }
}
