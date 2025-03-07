package uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourtRoomPageStateHolder implements Serializable {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The court site.
     */
    private XhibitCourtSiteDto courtSite;

    /**
     * list of XhibitCourtSiteDto objects.
     */
    private List<XhibitCourtSiteDto> sitesList = new ArrayList<>();

    /**
     * list of CourtRoomDto objects.
     */
    private List<CourtRoomDto> courtRoomsList = new ArrayList<>();
    
    /**
     * The court room search command.
     */
    private CourtRoomSearchCommand courtRoomSearchCommand;
    
    /**
     * The court.
     */
    private CourtDto court;
    
    /**
     * list of XhibitCourtSiteDto objects.
     */
    private List<CourtDto> courtsList = new ArrayList<>();

    /**
     * Reset all the variables.
     */
    public void reset() {
        setCourtSite(null);
        setSites(new ArrayList<>());
        setCourt(null);
        setCourts(new ArrayList<>());
        setCourtRoomsList(new ArrayList<>());
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
     * Gets the court site.
     *
     * @return the court site
     */
    public XhibitCourtSiteDto getCourtSite() {
        return courtSite;
    }

    /**
     * Sets the court site.
     *
     * @param courtSite the courtSite to set
     */
    public void setCourtSite(final XhibitCourtSiteDto courtSite) {
        this.courtSite = courtSite;
    }

    /**
     * Sets the court room search command.
     *
     * @param courtRoomSearchCommand the new court room search command
     */
    public void setCourtRoomSearchCommand(final CourtRoomSearchCommand courtRoomSearchCommand) {
        this.courtRoomSearchCommand = courtRoomSearchCommand;
    }

    /**
     * Gets the court room search command.
     *
     * @return the court room search command
     */
    public CourtRoomSearchCommand getCourtRoomSearchCommand() {
        return this.courtRoomSearchCommand;
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
     */
    public void setCourt(CourtDto court) {
        this.court = court;
    }

    /**
     * Gets the courtsList.
     *
     * @return the courtsList
     */
    public List<CourtDto> getCourts() {
        return courtsList;
    }
    
    /**
     * Sets the courtsList.
     *
     */
    public void setCourts(List<CourtDto> courtsList) {
        this.courtsList = courtsList;
    }

    public List<CourtRoomDto> getCourtRoomsList() {
        return courtRoomsList;
    }

    public void setCourtRoomsList(List<CourtRoomDto> courtRoomsList) {
        this.courtRoomsList = courtRoomsList;
    }
}
