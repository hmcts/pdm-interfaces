/* Copyrights and Licenses
 * 
 * Copyright (c) 2015-2016 by the Ministry of Justice. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * - Products derived from this software may not be called "XHIBIT Public Display Manager" nor may
 * "XHIBIT Public Display Manager" appear in their names without prior written permission of the
 * Ministry of Justice.
 * - Redistributions of any form whatsoever must retain the following acknowledgment: "This
 * product includes XHIBIT Public Display Manager."
 * This software is provided "as is" and any expressed or implied warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the Ministry of Justice or its contributors be liable for any
 * direct, indirect, incidental, special, exemplary, or consequential damages (including, but
 * not limited to, procurement of substitute goods or services; loss of use, data, or profits;
 * or business interruption). However caused any on any theory of liability, whether in contract,
 * strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage. */

package uk.gov.hmcts.pdm.mockipdmanager.controllers;

import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.pdm.mockipdmanager.common.json.CduJson;

import java.util.List;

/**
 * The Class RestartCduController.
 * 
 * @author toftn
 */
@RestController
public class RestartCduController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestartCduController.class);
    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{} {} {}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    /**
     * Restart cdu.
     *
     * @param cdus the cdus
     * @return the response
     */
    @RequestMapping(value = "/restartcdu", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON)
    public ResponseEntity restartCdu(@RequestBody final List<CduJson> cdus) {
        final String methodName = "restartCdu";
        LOGGER.info(THREE_PARAMS, METHOD, methodName, STARTS);
        for (CduJson cdu : cdus) {
            LOGGER.info("Restarting CDU: {}", cdu.getIpAddress());
        }
        LOGGER.info(THREE_PARAMS, METHOD, methodName, ENDS);
        return ResponseEntity.ok(null);
    }

}
