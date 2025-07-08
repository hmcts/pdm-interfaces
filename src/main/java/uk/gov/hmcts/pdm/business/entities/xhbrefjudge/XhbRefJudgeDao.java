package uk.gov.hmcts.pdm.business.entities.xhbrefjudge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;


@Entity(name = "XHB_REF_JUDGE")
@NamedQuery(name = "XHB_REF_JUDGE.findByCourtSiteId",
    query = "SELECT o FROM XHB_REF_JUDGE o WHERE o.courtId IN "
        + "(SELECT cs.courtId FROM XHB_COURT_SITE cs WHERE cs.courtSiteId = :courtSiteId) and"
        + "(o.obsInd IS NULL OR o.obsInd <> 'Y')")
@SuppressWarnings({"PMD.TooManyFields"})
public class XhbRefJudgeDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "xhb_ref_judge_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_ref_judge_seq", sequenceName = "xhb_ref_judge_seq",
        allocationSize = 1)
    @Column(name = "REF_JUDGE_ID")
    private Integer refJudgeId;

    @Column(name = "JUDGE_TYPE")
    private String judgeType;

    @Column(name = "CREST_JUDGE_ID")
    private Integer crestJudgeId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "FULL_LIST_TITLE1")
    private String fullListTitle1;

    @Column(name = "FULL_LIST_TITLE2")
    private String fullListTitle2;

    @Column(name = "FULL_LIST_TITLE3")
    private String fullListTitle3;

    @Column(name = "STATS_CODE")
    private String statsCode;

    @Column(name = "INITIALS")
    private String initials;

    @Column(name = "HONOURS")
    private String honours;

    @Column(name = "JUD_VERS")
    private String judVers;

    @Column(name = "OBS_IND")
    private String obsInd;

    @Column(name = "SOURCE_TABLE")
    private String sourceTable;

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

    public Integer getRefJudgeId() {
        return refJudgeId;
    }

    public void setRefJudgeId(Integer refJudgeId) {
        this.refJudgeId = refJudgeId;
    }

    public String getJudgeType() {
        return judgeType;
    }

    public void setJudgeType(String judgeType) {
        this.judgeType = judgeType;
    }

    public Integer getCrestJudgeId() {
        return crestJudgeId;
    }

    public void setCrestJudgeId(Integer crestJudgeId) {
        this.crestJudgeId = crestJudgeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullListTitle1() {
        return fullListTitle1;
    }

    public void setFullListTitle1(String fullListTitle1) {
        this.fullListTitle1 = fullListTitle1;
    }

    public String getFullListTitle2() {
        return fullListTitle2;
    }

    public void setFullListTitle2(String fullListTitle2) {
        this.fullListTitle2 = fullListTitle2;
    }

    public String getFullListTitle3() {
        return fullListTitle3;
    }

    public void setFullListTitle3(String fullListTitle3) {
        this.fullListTitle3 = fullListTitle3;
    }

    public String getStatsCode() {
        return statsCode;
    }

    public void setStatsCode(String statsCode) {
        this.statsCode = statsCode;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getHonours() {
        return honours;
    }

    public void setHonours(String honours) {
        this.honours = honours;
    }

    public String getJudVers() {
        return judVers;
    }

    public void setJudVers(String judVers) {
        this.judVers = judVers;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
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
}
