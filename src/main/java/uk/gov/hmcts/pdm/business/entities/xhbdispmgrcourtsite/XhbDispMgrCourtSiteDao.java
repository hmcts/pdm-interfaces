package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy.XhbDispMgrLocalProxyDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule.XhbDispMgrScheduleDao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;



@Entity(name = "XHB_DISP_MGR_COURT_SITE")
@NamedQuery(name = "XHB_DISP_MGR_COURT_SITE.findByXhibitCourtSiteId",
    query = "SELECT o FROM XHB_DISP_MGR_COURT_SITE o WHERE o.xhibitCourtSiteId = :xhibitCourtSiteId ")
@SuppressWarnings("PMD.TooManyFields")
public class XhbDispMgrCourtSiteDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = 1843356566550775762L;

    @Id
    @GeneratedValue(generator = "DM_COURT_SITE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "DM_COURT_SITE_SEQ", sequenceName = "DM_COURT_SITE_SEQ",
        allocationSize = 1)
    @Column(name = "COURT_SITE_ID")
    private Integer courtSiteId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PAGE_URL")
    private String pageUrl;

    @Column(name = "SCHEDULE_ID")
    private Integer scheduleId;

    @Column(name = "XHIBIT_COURT_SITE_ID")
    private Integer xhibitCourtSiteId;

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

    @Column(name = "RAG_STATUS")
    private String ragStatus;

    @Column(name = "RAG_STATUS_DATE")
    private LocalDateTime ragStatusDate;

    @Column(name = "NOTIFICATION")
    private String notification;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REFRESH }) 
    @JoinColumn(name = "XHIBIT_COURT_SITE_ID", insertable = false, updatable = false)
    private XhbCourtSiteDao xhbCourtSiteDao;

    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID", insertable = false, updatable = false)
    private XhbDispMgrScheduleDao xhbDispMgrScheduleDao;

    @OneToMany
    @JoinColumn(name = "COURT_SITE_ID", insertable = false, updatable = false)
    private Set<XhbDispMgrCduDao> xhbDispMgrCduDao;

    @Transient
    private XhbDispMgrLocalProxyDao xhbDispMgrLocalProxyDao;

    public Integer getId() {
        return courtSiteId;
    }

    public void setId(Integer id) {
        this.courtSiteId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getXhibitCourtSiteId() {
        return xhibitCourtSiteId;
    }

    public void setXhibitCourtSiteId(Integer xhibitCourtSiteId) {
        this.xhibitCourtSiteId = xhibitCourtSiteId;
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

    public String getRagStatus() {
        return ragStatus;
    }

    public void setRagStatus(String ragStatus) {
        this.ragStatus = ragStatus;
    }

    public LocalDateTime getRagStatusDate() {
        return ragStatusDate;
    }

    public void setRagStatusDate(LocalDateTime ragStatusDate) {
        this.ragStatusDate = ragStatusDate;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public XhbCourtSiteDao getXhbCourtSiteDao() {
        return xhbCourtSiteDao;
    }

    public void setXhbCourtSiteDao(XhbCourtSiteDao xhbCourtSiteDao) {
        this.xhbCourtSiteDao = xhbCourtSiteDao;
    }

    public XhbDispMgrScheduleDao getXhbDispMgrScheduleDao() {
        return xhbDispMgrScheduleDao;
    }

    public void setXhbDispMgrScheduleDao(XhbDispMgrScheduleDao xhbDispMgrScheduleDao) {
        this.xhbDispMgrScheduleDao = xhbDispMgrScheduleDao;
    }

    public XhbDispMgrLocalProxyDao getXhbDispMgrLocalProxyDao() {
        return xhbDispMgrLocalProxyDao;
    }

    public void setXhbDispMgrLocalProxyDao(XhbDispMgrLocalProxyDao xhbDispMgrLocalProxyDao) {
        this.xhbDispMgrLocalProxyDao = xhbDispMgrLocalProxyDao;
    }

    public Set<XhbDispMgrCduDao> getXhbDispMgrCduDao() {
        return xhbDispMgrCduDao;
    }

    public void setXhbDispMgrCduDao(Set<XhbDispMgrCduDao> xhbDispMgrCduDao) {
        this.xhbDispMgrCduDao = xhbDispMgrCduDao;
    }
}
