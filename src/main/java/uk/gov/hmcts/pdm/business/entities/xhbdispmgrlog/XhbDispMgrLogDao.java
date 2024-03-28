package uk.gov.hmcts.pdm.business.entities.xhbdispmgrlog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;

import java.time.LocalDateTime;


@Entity(name = "XHB_DISP_MGR_LOG")
@NamedQuery(name = "XHB_DISP_MGR_LOG.findByLogId",
    query = "SELECT o FROM XHB_DISP_MGR_LOG o WHERE o.logId = :logId")
@NamedQuery(name = "XHB_DISP_MGR_LOG.findByCourtSiteId",
    query = "SELECT o FROM XHB_DISP_MGR_LOG o WHERE o.courtSiteId = :courtSiteId")
@NamedQuery(name = "XHB_DISP_MGR_LOG.findByCduId",
    query = "SELECT o FROM XHB_DISP_MGR_LOG o WHERE o.cduId = :cduId")
public class XhbDispMgrLogDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "DM_LOG_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "DM_LOG_SEQ", sequenceName = "DM_LOG_SEQ", allocationSize = 1)
    @Column(name = "LOG_ID")
    private Integer logId;

    @Column(name = "COURT_SITE_ID")
    private Integer courtSiteId;

    @Column(name = "CDU_ID")
    private Integer cduId;

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

    @ManyToOne
    @JoinColumn(name = "COURT_SITE_ID", insertable = false, updatable = false)
    private XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao;

    @ManyToOne
    @JoinColumn(name = "CDU_ID", insertable = false, updatable = false)
    private XhbDispMgrCduDao xhbDispMgrCduDao;

    public Integer getId() {
        return logId;
    }

    public void setId(Integer id) {
        this.logId = id;
    }

    public Integer getCourtSiteId() {
        return courtSiteId;
    }

    public void setCourtSiteId(Integer courtSiteId) {
        this.courtSiteId = courtSiteId;
    }

    public Integer getCduId() {
        return cduId;
    }

    public void setCduId(Integer cduId) {
        this.cduId = cduId;
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

    public XhbDispMgrCourtSiteDao getXhbDispMgrCourtSiteDao() {
        return xhbDispMgrCourtSiteDao;
    }

    public void setXhbDispMgrCourtSiteDao(XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao) {
        this.xhbDispMgrCourtSiteDao = xhbDispMgrCourtSiteDao;
    }

    public XhbDispMgrCduDao getXhbDispMgrCduDao() {
        return xhbDispMgrCduDao;
    }

    public void setXhbDispMgrCduDao(XhbDispMgrCduDao xhbDispMgrCduDao) {
        this.xhbDispMgrCduDao = xhbDispMgrCduDao;
    }
}
