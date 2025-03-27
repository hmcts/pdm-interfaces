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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService;

import java.util.Locale;

/**
 * The Class CduRegisterValidator.
 *
 * @author pattersone
 */
@Component
public class CduRegisterValidator extends AbstractCduValidator {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CduRegisterValidator.class);

    @Autowired
    private MessageSource messageSource;
    
    /**
     * CduService instance.
     */
    @Autowired
    private ICduService cduService;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        return CduRegisterCommand.class.isAssignableFrom(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#validate(java.lang.Object,
     * org.springframework.validation.Errors)
     */
    @Override
    public void validate(final Object command, final Errors errors) {
        LOGGER.info("validate method starts");

        final CduRegisterCommand cduCommand = (CduRegisterCommand) command;
        
        LOGGER.info("Cdu Register Validator, the cdu to process: {}{}{}",
            getCduPageStateHolder().getCdu().getCduNumber(), " with Site Id: ",
            getCduPageStateHolder().getCdu().getCourtSiteId());
        
        // Validate a CDU has been selected from the list and that it is not currently registered
        if (getCduPageStateHolder().getCdu() == null
            || isRegisteredCdu(getCduPageStateHolder().getCdu())) {
            LOGGER.info("validate method - Invalid value selected");
            errors.reject("cduSearchCommand.noselectionmade");

            // Else if CDU Number is already in use
        } else if (StringUtils.isNotBlank(cduCommand.getCduNumber())
            && cduService.isCduWithCduNumber(cduCommand.getCduNumber())) {
            // If this Mac Address does not exist, then CDU Number is in use by different CDU
            if (cduService.isCduWithMacAddress(getCduPageStateHolder().getCdu().getMacAddress())) {
                // If the CDU Number is already associated with this Mac Address then this is
                // effectively an update after a previous failed unregister, i.e. exists on
                // central but currently not registered on localproxy. If this is not true
                // as the CDU Number is not being used by this Mac Address, it is an error.
                final CduDto cduDto =
                    cduService.getCduByMacAddress(getCduPageStateHolder().getCdu().getMacAddress());
                if (!cduDto.getCduNumber().equalsIgnoreCase(cduCommand.getCduNumber())) {
                    String message = messageSource.getMessage("cduCommand.cduNumber.notUnique",
                        null, Locale.getDefault());
                    errors.rejectValue("cduNumber", null, message);
                }
            } else {
                // Else is the CDU Number already associated with this Mac Address due to previous
                // unregister
                // failure
                String message = messageSource.getMessage("cduCommand.cduNumber.notUnique",
                    null, Locale.getDefault());
                errors.rejectValue("cduNumber", null, message);
            }
        }
        LOGGER.info("validate method ends");
    }

}
