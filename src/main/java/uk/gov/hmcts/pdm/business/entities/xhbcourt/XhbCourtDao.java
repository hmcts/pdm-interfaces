package uk.gov.hmcts.pdm.business.entities.xhbcourt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;

@Entity(name = "XHB_COURT")
public class XhbCourtDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "xhb_court_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_court_seq", sequenceName = "xhb_court_seq",
        allocationSize = 1)
    @Column(name = "COURT_ID")
    private Integer courtId;
    
    @Column(name = "COURT_NAME")
    private String courtName;

    @Column(name = "ADDRESS_ID")
    private Integer addressId;
    
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


    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public final void setAddressId(Integer addressId) {
        this.addressId = addressId;
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
