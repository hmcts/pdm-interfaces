package uk.gov.hmcts.pdm.business.entities.xhbdisplaylocation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;


@Entity(name = "XHB_DISPLAY_LOCATION")
@NamedQuery(name = "XHB_DISPLAY_LOCATION.findByCourtSiteId",
    query = "SELECT o FROM XHB_DISPLAY_LOCATION o WHERE o.courtSiteId = :courtSiteId")
public class XhbDisplayLocationDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "xhb_display_location_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_display_location_seq", sequenceName = "xhb_display_location_seq",
        allocationSize = 1)
    @Column(name = "DISPLAY_LOCATION_ID")
    private Integer displayLocationId;

    @Column(name = "DESCRIPTION_CODE")
    private String descriptionCode;

    @Column(name = "COURT_SITE_ID", nullable = false)
    private Integer courtSiteId;

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

    public Integer getDisplayLocationId() {
        return displayLocationId;
    }

    public void setDisplayLocationId(final Integer displayLocationId) {
        this.displayLocationId = displayLocationId;
    }

    public String getDescriptionCode() {
        return descriptionCode;
    }

    public void setDescriptionCode(final String descriptionCode) {
        this.descriptionCode = descriptionCode;
    }
    
    public Integer getCourtSiteId() {
        return courtSiteId;
    }

    public void setCourtSiteId(Integer courtSiteId) {
        this.courtSiteId = courtSiteId;
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
