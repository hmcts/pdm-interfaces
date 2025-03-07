package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourtPageStateHolder implements Serializable {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * list of XhibitCourtSiteDto objects.
     */
    private List<XhibitCourtSiteDto> sitesList = new ArrayList<>();
    
    /**
     * The court.
     */
    private CourtDto court;

    /**
     * list of CourtDto objects.
     */
    private List<CourtDto> courtsList = new ArrayList<>();

    /**
     * The court search command.
     */
    private CourtSearchCommand courtSearchCommand;

    /**
     * Reset all the variables.
     */
    public void reset() {
        setSites(new ArrayList<>());
        setCourt(null);
        setCourts(new ArrayList<>());
    }


    /**
     * setSites.
     * 
     * @param sitesList list of sites to return to front end.
     */
    public void setSites(final List<XhibitCourtSiteDto> sitesList) {
        this.sitesList = sitesList;
    }

    /**
     * getSites.
     * 
     * @return List list of sites
     */
    public List<XhibitCourtSiteDto> getSites() {
        return this.sitesList;
    }
    
    /**
     * setCourts.
     * 
     * @param courtsList list of courts to return to front end.
     */
    public void setCourts(final List<CourtDto> courtsList) {
        this.courtsList = courtsList;
    }

    /**
     * getCourts.
     * 
     * @return List list of courts
     */
    public List<CourtDto> getCourts() {
        return this.courtsList;
    }

    /**
     * Gets the court.
     *
     * @return the court
     */
    public CourtDto getCourt() {
        return court;
    }

    /**
     * Sets the court.
     *
     * @param court the court to set
     */
    public void setCourt(final CourtDto court) {
        this.court = court;
    }

    /**
     * Sets the court search command.
     *
     * @param courtSearchCommand the new display search command
     */
    public void setCourtSearchCommand(final CourtSearchCommand courtSearchCommand) {
        this.courtSearchCommand = courtSearchCommand;
    }

    /**
     * Gets the court search command.
     *
     * @return the court search command
     */
    public CourtSearchCommand getCourtSearchCommand() {
        return this.courtSearchCommand;
    }

}
