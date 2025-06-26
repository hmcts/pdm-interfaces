package uk.gov.hmcts.pdm.business.entities.xhbrotationsets;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;

@Entity(name = "XHB_ROTATION_SETS")
@NamedQuery(name = "XHB_ROTATION_SETS.findByCourtId",
    query = "SELECT o FROM XHB_ROTATION_SETS o WHERE o.courtId = :courtId")
public class XhbRotationSetsDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "xhb_rotation_sets_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_rotation_sets_seq", sequenceName = "xhb_rotation_sets_seq",
        allocationSize = 1)
    @Column(name = "ROTATION_SET_ID")
    private Integer rotationSetId;

    @Column(name = "COURT_ID")
    private Integer courtId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DEFAULT_YN")
    private String defaultYn;

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

    public Integer getRotationSetId() {
        return rotationSetId;
    }

    public void setRotationSetId(Integer rotationSetsId) {
        this.rotationSetId = rotationSetsId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultYn() {
        return defaultYn;
    }

    public void setDefaultYn(String defaultYn) {
        this.defaultYn = defaultYn;
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
