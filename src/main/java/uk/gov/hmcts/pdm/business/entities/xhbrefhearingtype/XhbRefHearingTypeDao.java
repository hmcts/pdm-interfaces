package uk.gov.hmcts.pdm.business.entities.xhbrefhearingtype;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;

@Entity(name = "XHB_REF_HEARING_TYPE")
@NamedQuery(name = "XHB_REF_HEARING_TYPE.findByCourtId",
query = "SELECT o FROM XHB_REF_HEARING_TYPE o WHERE o.courtId = :courtId "
    + "AND (o.obsInd IS NULL OR o.obsInd <> 'Y')")
@NamedQuery(name = "XHB_REF_HEARING_TYPE.findByCourtSiteId",
    query = "SELECT o FROM XHB_REF_HEARING_TYPE o WHERE o.courtId = "
        + "(SELECT cs.courtId FROM XHB_COURT_SITE cs WHERE cs.courtSiteId = :courtSiteId) "
        + "AND (o.obsInd IS NULL OR o.obsInd <> 'Y')")
@NamedQuery(name = "XHB_REF_HEARING_TYPE.findAllCategories",
    query = "SELECT DISTINCT category FROM XHB_REF_HEARING_TYPE")
public class XhbRefHearingTypeDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "xhb_ref_hearing_type_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_ref_hearing_type_seq", sequenceName = "xhb_ref_hearing_type_seq",
        allocationSize = 1)
    @Column(name = "REF_HEARING_TYPE_ID")
    private Integer refHearingTypeId;

    @Column(name = "HEARING_TYPE_CODE")
    private String hearingTypeCode;

    @Column(name = "HEARING_TYPE_DESC")
    private String hearingTypeDesc;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "SEQ_NO")
    private Integer seqNo;

    @Column(name = "LIST_SEQUENCE")
    private Integer listSequence;

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

    @Column(name = "COURT_ID")
    private Integer courtId;

    @Column(name = "OBS_IND")
    private String obsInd;

    public Integer getRefHearingTypeId() {
        return refHearingTypeId;
    }

    public void setRefHearingTypeId(Integer refHearingTypeId) {
        this.refHearingTypeId = refHearingTypeId;
    }

    public String getHearingTypeCode() {
        return hearingTypeCode;
    }

    public void setHearingTypeCode(String hearingTypeCode) {
        this.hearingTypeCode = hearingTypeCode;
    }

    public String getHearingTypeDesc() {
        return hearingTypeDesc;
    }

    public void setHearingTypeDesc(String hearingTypeDesc) {
        this.hearingTypeDesc = hearingTypeDesc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getListSequence() {
        return listSequence;
    }

    public void setListSequence(Integer listSequence) {
        this.listSequence = listSequence;
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

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }
}
