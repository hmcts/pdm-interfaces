package uk.gov.hmcts.pdm.business.entities.xhbcourtroom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;

@Entity(name = "XHB_COURT_ROOM")
@NamedQuery(name = "XHB_COURT_ROOM.findByCourtSiteId",
    query = "SELECT o FROM XHB_COURT_ROOM o WHERE o.courtSiteId = :courtSiteId "
        + "AND (o.obsInd IS NULL OR o.obsInd <> 'Y')")
public class XhbCourtRoomDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "xhb_court_room_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_court_room_seq", sequenceName = "xhb_court_room_seq",
        allocationSize = 1)
    @Column(name = "COURT_ROOM_ID")
    private Integer courtRoomId;

    @Column(name = "COURT_ROOM_NAME")
    private String courtRoomName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "CREST_COURT_ROOM_NO")
    private Integer courtRoomNo;

    @Column(name = "COURT_SITE_ID")
    private Integer courtSiteId;

    @Column(name = "OBS_IND")
    private String obsInd;

    @Column(name = "LAST_UPDATE_DATE")
    private LocalDateTime lastUpdateDate;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;

    @Column(name = "VERSION")
    private Integer version;


    public Integer getCourtRoomId() {
        return courtRoomId;
    }

    public void setCourtRoomId(Integer courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public String getCourtRoomName() {
        return courtRoomName;
    }

    public void setCourtRoomName(String courtRoomName) {
        this.courtRoomName = courtRoomName;
    }

    public String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public Integer getCourtRoomNo() {
        return courtRoomNo;
    }

    public void setCourtRoomNo(Integer courtRoomNo) {
        this.courtRoomNo = courtRoomNo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getCourtSiteId() {
        return courtSiteId;
    }

    public void setCourtSiteId(Integer courtSiteId) {
        this.courtSiteId = courtSiteId;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }
}
