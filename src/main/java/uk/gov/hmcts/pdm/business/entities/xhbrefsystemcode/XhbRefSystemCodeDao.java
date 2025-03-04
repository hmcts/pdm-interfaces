package uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;


@Entity(name = "XHB_REF_SYSTEM_CODE")
@NamedQuery(name = "XHB_REF_SYSTEM_CODE.findByCourtId",
    query = "SELECT o FROM XHB_REF_SYSTEM_CODE o WHERE o.courtId = :courtId "
    + "AND (o.obsInd IS NULL OR o.obsInd <> 'Y')")
@NamedQuery(name = "XHB_REF_SYSTEM_CODE.findJudgeTypeByCourtSiteId",
    query = "SELECT o FROM XHB_REF_SYSTEM_CODE o WHERE o.codeType = 'JUDGE_TYPE' and "
    + "(o.obsInd IS NULL OR o.obsInd <> 'Y') and "
    + "o.courtId IN (SELECT cs.courtId FROM XHB_COURT_SITE cs WHERE cs.courtSiteId = :courtSiteId)")
@SuppressWarnings({"PMD.LinguisticNaming", "PMD.TooManyFields"})
public class XhbRefSystemCodeDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "xhb_ref_system_code_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_ref_system_code_seq", sequenceName = "xhb_ref_system_code_seq",
        allocationSize = 1)
    @Column(name = "REF_SYSTEM_CODE_ID")
    private Integer refSystemCodeId;
    
    @Column(name = "CODE")
    private String code;

    @Column(name = "CODE_TYPE")
    private String codeType;

    @Column(name = "CODE_TITLE")
    private String codeTitle;

    @Column(name = "DE_CODE")
    private String deCode;

    @Column(name = "REF_CODE_ORDER")
    private Double refCodeOrder;
    
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

    public Integer getRefSystemCodeId() {
        return refSystemCodeId;
    }

    public void setRefSystemCodeId(Integer refSystemCodeId) {
        this.refSystemCodeId = refSystemCodeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeTitle() {
        return codeTitle;
    }

    public void setCodeTitle(String codeTitle) {
        this.codeTitle = codeTitle;
    }

    public String getDeCode() {
        return deCode;
    }

    public void setDeCode(String deCode) {
        this.deCode = deCode;
    }

    public Double getRefCodeOrder() {
        return refCodeOrder;
    }

    public void setRefCodeOrder(Double refCodeOrder) {
        this.refCodeOrder = refCodeOrder;
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
