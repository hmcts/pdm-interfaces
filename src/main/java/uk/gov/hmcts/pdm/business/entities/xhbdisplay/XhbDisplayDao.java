package uk.gov.hmcts.pdm.business.entities.xhbdisplay;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;

@Entity(name = "XHB_DISPLAY")
@NamedQuery(name = "XHB_DISPLAY.findByCourtSiteId",
    query = "SELECT o FROM XHB_DISPLAY o WHERE o.displayLocationId IN "
        + "(SELECT dl.displayLocationId FROM XHB_DISPLAY_LOCATION dl WHERE dl.courtSiteId = :courtSiteId)")
public class XhbDisplayDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "xhb_display_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_display_seq", sequenceName = "xhb_display_seq",
        allocationSize = 1)
    @Column(name = "DISPLAY_ID")
    private Integer displayId;

    @Column(name = "DISPLAY_TYPE_ID", nullable = false)
    private Integer displayTypeId;

    @Column(name = "DISPLAY_LOCATION_ID", nullable = false)
    private Integer displayLocationId;

    @Column(name = "ROTATION_SET_ID")
    private Integer rotationSetId;

    @Column(name = "DESCRIPTION_CODE")
    private String descriptionCode;

    @Column(name = "LOCALE")
    private String locale;

    @Column(name = "SHOW_UNASSIGNED_YN")
    private String showUnassignedYn;

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

    public Integer getDisplayId() {
        return displayId;
    }

    public final void setDisplayId(Integer displayId) {
        this.displayId = displayId;
    }

    public Integer getDisplayTypeId() {
        return displayTypeId;
    }

    public final void setDisplayTypeId(Integer displayTypeId) {
        this.displayTypeId = displayTypeId;
    }

    public Integer getDisplayLocationId() {
        return displayLocationId;
    }

    public final void setDisplayLocationId(Integer displayLocationId) {
        this.displayLocationId = displayLocationId;
    }

    public Integer getRotationSetId() {
        return rotationSetId;
    }

    public final void setRotationSetId(Integer rotationSetId) {
        this.rotationSetId = rotationSetId;
    }

    public String getDescriptionCode() {
        return descriptionCode;
    }

    public final void setDescriptionCode(String descriptionCode) {
        this.descriptionCode = descriptionCode;
    }

    public String getLocale() {
        return locale;
    }

    public final void setLocale(String locale) {
        this.locale = locale;
    }

    public String getShowUnassignedYn() {
        return showUnassignedYn;
    }

    public final void setShowUnassignedYn(String showUnassignedYn) {
        this.showUnassignedYn = showUnassignedYn;
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
