package uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;



@Entity(name = "XHB_DISP_MGR_SCHEDULE")
@NamedQuery(name = "XHB_DISP_MGR_SCHEDULE.findByScheduleId",
    query = "SELECT o FROM XHB_DISP_MGR_SCHEDULE o WHERE o.scheduleId = :scheduleId")
@NamedQuery(name = "XHB_DISP_MGR_SCHEDULE.findPowerSaveSchedules",
    query = "SELECT o FROM XHB_DISP_MGR_SCHEDULE o WHERE o.scheduleType = 'POWER_SAVE'")
public class XhbDispMgrScheduleDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = -8306546676700683107L;

    @Id
    @GeneratedValue(generator = "DM_SCHEDULE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "DM_SCHEDULE_SEQ", sequenceName = "DM_SCHEDULE_SEQ",
        allocationSize = 1)
    @Column(name = "SCHEDULE_ID")
    private Integer scheduleId;

    @Column(name = "SCHEDULE_TYPE")
    private String scheduleType;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DETAIL")
    private String detail;

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

    @OneToMany
    @JoinColumn(name = "SCHEDULE_ID", insertable = false, updatable = false)
    private Set<XhbDispMgrCourtSiteDao> xhbDispMgrCourtSiteDao;

    public Integer getId() {
        return scheduleId;
    }

    public void setId(Integer id) {
        this.scheduleId = id;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public Set<XhbDispMgrCourtSiteDao> getXhbDispMgrCourtSiteDao() {
        return xhbDispMgrCourtSiteDao;
    }

    public void setXhbDispMgrCourtSiteDao(Set<XhbDispMgrCourtSiteDao> xhbDispMgrCourtSiteDao) {
        this.xhbDispMgrCourtSiteDao = xhbDispMgrCourtSiteDao;
    }
}
