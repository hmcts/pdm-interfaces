package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CourtAmendValidator extends CourtSelectedValidator {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CourtAmendValidator.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    @Override
    public boolean supports(final Class<?> clazz) {
        return CourtAmendValidator.class.isAssignableFrom(clazz);
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

        final CourtAmendCommand courtAmendCommand = (CourtAmendCommand) command;
        if (courtAmendCommand.getCourtSiteName() == null) {
            LOGGER.warn("validate method - Invalid court name");
            errors.reject("courtAmendCommand.courtSiteName.notBlank");
        } else if (courtAmendCommand.getCourtSiteCode() == null) {
            LOGGER.warn("validate method - Invalid court site code");
            errors.reject("courtAmendCommand.courtSiteCode.notBlank");
        }
        LOGGER.info("validate method ends");
    }

}
