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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICduService;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ILocalProxyService;

import java.util.List;


/**
 * The Class AbstractCduValidator.
 *
 * @author harrism
 */
public abstract class AbstractCduValidator implements Validator {
    /** The cdu page state holder. */
    @Autowired
    private CduPageStateHolder cduPageStateHolder;
    
    /** The localProxyService class. */
    @Autowired
    protected ILocalProxyService localProxyService;
    
    /** The CduService class. */
    @Autowired
    protected ICduService cduService;

    /**
     * Gets the cdu page state holder.
     *
     * @return the cdu page state holder
     */
    protected CduPageStateHolder getCduPageStateHolder() {
        return cduPageStateHolder;
    }

    /**
     * Checks if is valid cdu selected.
     *
     * @param selectedMacAddress the selected mac address
     * @return true, if is valid cdu mac address
     */
    protected boolean isValidCduSelected(final String selectedMacAddress) {
        final CduDto selectedCdu = getCduFromSearchResults(selectedMacAddress);
        return selectedCdu != null;
    }

    /**
     * Checks if is registered cdu selected.
     *
     * @param selectedMacAddress the selected mac address
     * @return true, if is registered cdu selected
     */
    protected boolean isRegisteredCduSelected(final String selectedMacAddress) {
        final CduDto selectedCdu = getCduFromSearchResults(selectedMacAddress);
        return isRegisteredCdu(selectedCdu);
    }

    /**
     * Checks if is registered cdu.
     *
     * @param selectedCdu the selected cdu
     * @return true, if is registered cdu
     */
    protected boolean isRegisteredCdu(final CduDto selectedCdu) {
        return selectedCdu != null
            && AppConstants.YES_CHAR.equals(selectedCdu.getRegisteredIndicator());
    }

    /**
     * Gets the cdu from the search results.
     *
     * @param selectedMacAddress the selected mac address
     * @return the cdu from search results or null if not in search results
     */
    private CduDto getCduFromSearchResults(final String selectedMacAddress) {
        CduDto selectedCdu = null;
        // Call to CduService to fetch Cdu's by Mac address
        final List<CduDto> cduList = cduService.getCduByMacAddressWithLike(selectedMacAddress);
        if (cduList != null) {
            for (CduDto cdu : cduList) {
                if (cdu.getMacAddress().equals(selectedMacAddress)) {
                    selectedCdu = cdu;
                    break;
                }
            }
        }
        return selectedCdu;
    }

}
