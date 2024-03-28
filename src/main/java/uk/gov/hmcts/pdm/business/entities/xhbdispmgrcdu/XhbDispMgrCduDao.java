package uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu;

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
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;

import java.time.LocalDateTime;



@Entity(name = "XHB_DISP_MGR_CDU")
@NamedQuery(name = "XHB_DISP_MGR_CDU.findByCduId",
    query = "SELECT o FROM XHB_DISP_MGR_CDU o WHERE o.cduId = :cduId")
@NamedQuery(name = "XHB_DISP_MGR_CDU.findByMacAddress",
    query = "SELECT o FROM XHB_DISP_MGR_CDU o WHERE o.macAddress = :macAddress")
@NamedQuery(name = "XHB_DISP_MGR_CDU.findByMacAddressWithLike",
    query = "SELECT DISTINCT o FROM XHB_DISP_MGR_CDU o WHERE o.macAddress LIKE '%' || :macAddress || '%'")
@NamedQuery(name = "XHB_DISP_MGR_CDU.findByCduNumber",
    query = "SELECT o FROM XHB_DISP_MGR_CDU o WHERE o.cduNumber = :cduNumber")
@NamedQuery(name = "XHB_DISP_MGR_CDU.getCduWeightingTotal",
    query = "SELECT SUM(weighting) FROM XHB_DISP_MGR_CDU o WHERE o.offlineInd = 'N'")
@NamedQuery(name = "XHB_DISP_MGR_CDU.getCduWeightingOperational",
    query = "SELECT SUM(weighting) FROM XHB_DISP_MGR_CDU o WHERE o.offlineInd = 'N' AND o.ragStatus = 'G'")
@NamedQuery(name = "XHB_DISP_MGR_CDU.getNextIpHost",
    query = "SELECT TO_NUMBER(SUBSTRING(cdu.ipAddress,(LENGTH(cdu.ipAddress) - "
        + "(STRPOS(REVERSE(cdu.ipAddress), '.')-1))+1),'999')+1 "
        + "FROM XHB_DISP_MGR_CDU cdu WHERE cdu.courtSiteId = :courtSiteId " + "AND NOT EXISTS "
        + "(SELECT 1 FROM XHB_DISP_MGR_CDU nxt_cdu WHERE nxt_cdu.courtSiteId = cdu.courtSiteId AND "
        + "TO_NUMBER(SUBSTRING(nxt_cdu.ipAddress,(LENGTH(nxt_cdu.ipAddress) - "
        + "(STRPOS(REVERSE(nxt_cdu.ipAddress), '.')-1))+1),'999') = "
        + "TO_NUMBER(SUBSTRING(cdu.ipAddress,(LENGTH(cdu.ipAddress) - "
        + "(STRPOS(REVERSE(cdu.ipAddress), '.')-1))+1),'999')+1) AND "
        + "TO_NUMBER(SUBSTRING(cdu.ipAddress,(LENGTH(cdu.ipAddress) - "
        + "(STRPOS(REVERSE(cdu.ipAddress), '.')-1))+1),'999')+1 "
        + "BETWEEN :minHost AND :maxHost ORDER BY 1")
@NamedQuery(name = "XHB_DISP_MGR_CDU.hostExists",
    query = "SELECT 'Y' FROM XHB_DISP_MGR_CDU cdu WHERE cdu.courtSiteId = :courtSiteId")
@SuppressWarnings("PMD.TooManyFields")
public class XhbDispMgrCduDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "DM_CDU_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "DM_CDU_SEQ", sequenceName = "DM_CDU_SEQ", allocationSize = 1)
    @Column(name = "CDU_ID")
    private Integer cduId;

    @Column(name = "CDU_NUMBER")
    private String cduNumber;

    @Column(name = "MAC_ADDRESS")
    private String macAddress;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "NOTIFICATION")
    private String notification;

    @Column(name = "REFRESH")
    private Long refresh;

    @Column(name = "WEIGHTING")
    private Long weighting;

    @Column(name = "OFFLINE_IND")
    private Character offlineInd;

    @Column(name = "RAG_STATUS")
    private Character ragStatus;

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

    @ManyToOne
    @JoinColumn(name = "COURT_SITE_ID", insertable = false, updatable = false)
    private XhbDispMgrCourtSiteDao xhbDispMgrCourtSiteDao;

    /*
     * @Column(name = "CDU_ID") XHB_DISP_MGR_MAPPING?? Might not need this private XhbDispMgrUrlDao
     * urls;
     */

    public Integer getId() {
        return cduId;
    }

    public void setId(Integer id) {
        this.cduId = id;
    }

    public String getCduNumber() {
        return cduNumber;
    }

    public void setCduNumber(String cduNumber) {
        this.cduNumber = cduNumber;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Long getRefresh() {
        return refresh;
    }

    public void setRefresh(Long refresh) {
        this.refresh = refresh;
    }

    public Long getWeighting() {
        return weighting;
    }

    public void setWeighting(Long weighting) {
        this.weighting = weighting;
    }

    public Character getOfflineInd() {
        return offlineInd;
    }

    public void setOfflineInd(Character offlineInd) {
        this.offlineInd = offlineInd;
    }

    public Character getRagStatus() {
        return ragStatus;
    }

    public void setRagStatus(Character ragStatus) {
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
