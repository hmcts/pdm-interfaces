package uk.gov.hmcts.pdm.business.entities.xhbdispmgrlocalproxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity(name = "XHB_DISP_MGR_LOCAL_PROXY")
@NamedQuery(name = "XHB_DISP_MGR_LOCAL_PROXY.findByLocalProxyId",
    query = "SELECT o FROM XHB_DISP_MGR_LOCAL_PROXY o WHERE o.localProxyId = :localProxyId")
@NamedQuery(name = "XHB_DISP_MGR_LOCAL_PROXY.findByIpAddress",
    query = "SELECT o FROM XHB_DISP_MGR_LOCAL_PROXY o WHERE o.ipAddress = :ipAddress")
@NamedQuery(name = "XHB_DISP_MGR_LOCAL_PROXY.findByCourtSiteId",
    query = "SELECT o FROM XHB_DISP_MGR_LOCAL_PROXY o WHERE o.courtSiteId = :courtSiteId")
public class XhbDispMgrLocalProxyDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = -6280823421948310403L;

    @Id
    @GeneratedValue(generator = "dm_local_proxy_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "dm_local_proxy_seq", sequenceName = "dm_local_proxy_seq",
        allocationSize = 1)
    @Column(name = "LOCAL_PROXY_ID")
    private Integer localProxyId;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "HOSTNAME")
    private String hostName;

    @Column(name = "RAG_STATUS")
    private String ragStatus;

    @Column(name = "RAG_STATUS_DATE")
    private LocalDateTime ragStatusDate;

    @Column(name = "COURT_SITE_ID")
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

    @Transient
    private XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao;

    public Integer getId() {
        return localProxyId;
    }

    public void setId(Integer id) {
        this.localProxyId = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public XhbDispMgrCourtSiteDao getXhbDispMgrCourtSiteDao() {
        return xhbDispMgrCourtSiteDao;
    }

    public void setXhbDispMgrCourtSiteDao(XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao) {
        this.xhbDispMgrCourtSiteDao = xhbDispMgrCourtSiteDao;
    }
}
