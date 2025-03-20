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

package uk.gov.hmcts.pdm.publicdisplay.manager.web.cdus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.List;


/**
 * The Class AbstractCduSearchValidator.
 *
 * @author harrism
 */
public abstract class AbstractCduSearchValidator extends AbstractCduValidator {
    
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCduSearchValidator.class);
    
    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        return CduSearchCommand.class.isAssignableFrom(clazz);
    }

    /**
     * Re-validate the last cduSearchCommand on GET requests, to ensure data integrity.
     *
     * @param command the command
     * @return true, if passes validation
     */
    public boolean isValid(final Object command) {
        // Only validate if the command is not null
        if (command != null) {
            final CduSearchCommand cduSearchCommand = (CduSearchCommand) command;
            // Bind the Errors object locally
            final BindingResult errors =
                new BeanPropertyBindingResult(cduSearchCommand, "cduSearchCommand");
            validate(cduSearchCommand, errors);
            return !errors.hasErrors();
        }
        return false;
    }

    /**
     * Checks if is valid court site selected.
     *
     * @param xhibitCourtSiteId the xhibit court sited id
     * @return true, if is valid court site
     */
    protected boolean isValidCourtSiteSelected(final Long xhibitCourtSiteId) {
        boolean found = false;
        // Refresh the court site list using local proxy service
        final List<XhibitCourtSiteDto> sites = localProxyService.getXhibitCourtSitesWithLocalProxy();
        
        if (sites != null) {
            LOGGER.info("isValidCourtSiteSelected court site list fetched: {}", sites.size());
            for (XhibitCourtSiteDto site : sites) {
                if (site.getId().equals(xhibitCourtSiteId)) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }
}
