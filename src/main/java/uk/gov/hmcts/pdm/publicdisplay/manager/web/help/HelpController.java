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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.help;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;

/**
 * The Class HelpController.
 *
 * @author uphillj
 */
@Controller
@RequestMapping("/help")
public class HelpController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpController.class);

    /** The Constant for the JSP Folder. */
    private static final String FOLDER_HELP = "help";

    /** The Constant VALID_HELP_PAGES. */
    private static final String[] VALID_HELP_PAGES =
        {"dashboard", "cdus", "login", "logout", "register_localproxy", "register_cdu", "add_url",
            "remove_url", "view_localproxy", "amend_cdu", "amend_localproxy", "users",
            "view_display", "amend_display", "create_display", "delete_display", "view_judge",
            "amend_judge", "create_judge", "view_hearing", "amend_hearing", "create_hearing",
            "view_court", "delete_judge", "view_judgetype", "amend_judgetype", "create_judgetype",
            "create_court", "amend_court", "view_courtroom", "create_courtroom", "amend_courtroom",
            "delete_courtroom"};

    /**
     * Help.
     *
     * @param page the page
     * @return the string
     * @throws NoSuchRequestHandlingMethodException thrown when invalid page name is supplied
     */
    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public String help(@PathVariable("page") final String page) throws NoHandlerFoundException {
        LOGGER.debug("help({})", page);
        // Throw 404 exception if page is invalid
        if (!isValidPage(page)) {
            throw new NoHandlerFoundException("help", page, new HttpHeaders());
        }
        return FOLDER_HELP + "/" + page;
    }

    /**
     * Checks if is valid page.
     *
     * @param page the page
     * @return true, if is valid page
     */
    private boolean isValidPage(final String page) {
        return Arrays.asList(VALID_HELP_PAGES).contains(page);
    }
}
