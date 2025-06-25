package uk.gov.hmcts.pdm.business.entities.xhbcourtsite;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsitewelsh.XhbCourtSiteWelshDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcourtsite.XhbDispMgrCourtSiteDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl.XhbDispMgrUrlDao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;


@Entity(name = "XHB_COURT_SITE")
@NamedQuery(name = "XHB_COURT_SITE.findByCourtId",
    query = "SELECT o FROM XHB_COURT_SITE o WHERE o.courtId = :courtId AND (o.obsInd IS NULL OR o.obsInd = 'N')")

@NamedQuery(name = "XHB_COURT_SITE.findByCourtSiteId",
    query = "SELECT o FROM XHB_COURT_SITE o WHERE o.courtSiteId = :courtSiteId AND (o.obsInd IS NULL OR o.obsInd = 'N')")

@NamedQuery(name = "XHB_COURT_SITE.findCourtSiteByXhibitCourtSiteId",
    query = "SELECT o FROM XHB_COURT_SITE o WHERE o.courtSiteId IN "
        + "(SELECT xdmcs.xhibitCourtSiteId FROM XHB_DISP_MGR_COURT_SITE xdmcs "
        + "WHERE xdmcs.xhibitCourtSiteId = :xhibitCourtSiteId) "
        + "AND (o.obsInd IS NULL OR o.obsInd = 'N')")

@NamedQuery(name = "XHB_COURT_SITE.findCourtSitesWithLocalProxy",
    query = "SELECT xcs FROM XHB_COURT_SITE xcs, XHB_DISP_MGR_COURT_SITE xdmcs "
        + "WHERE xcs.courtSiteId = xdmcs.xhibitCourtSiteId "
        + "AND xdmcs.courtSiteId IN (SELECT xdmlp.courtSiteId FROM XHB_DISP_MGR_LOCAL_PROXY xdmlp "
        + "WHERE xdmlp.courtSiteId = xdmcs.courtSiteId) "
        + "AND (xcs.obsInd IS NULL OR xcs.obsInd = 'N')")

@NamedQuery(name = "XHB_COURT_SITE.findCourtSitesWithoutLocalProxy",
    query = "SELECT xcs FROM XHB_COURT_SITE xcs, XHB_DISP_MGR_COURT_SITE xdmcs "
        + "WHERE xcs.courtSiteId = xdmcs.xhibitCourtSiteId "
        + "AND xdmcs.courtSiteId NOT IN (SELECT xdmlp.courtSiteId FROM XHB_DISP_MGR_LOCAL_PROXY xdmlp "
        + "WHERE xdmlp.courtSiteId = xdmcs.courtSiteId) "
        + "AND (xcs.obsInd IS NULL OR xcs.obsInd = 'N')")

@NamedQuery(name = "XHB_COURT_SITE.findXhibitCourtSitesOrderedByRagStatus",
    query = "SELECT xcs FROM XHB_COURT_SITE xcs, XHB_DISP_MGR_COURT_SITE xdmcs "
        + "WHERE xcs.courtSiteId = xdmcs.xhibitCourtSiteId "
        + "AND xdmcs.courtSiteId IN (SELECT xdmlp.courtSiteId FROM XHB_DISP_MGR_LOCAL_PROXY xdmlp "
        + "WHERE xdmlp.courtSiteId = xdmcs.courtSiteId) "
        + "AND (xcs.obsInd IS NULL OR xcs.obsInd = 'N') "
        + "ORDER BY xdmcs.ragStatus DESC, xcs.courtSiteName ASC")
@SuppressWarnings("PMD.TooManyFields")
public class XhbCourtSiteDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = -784306022194075307L;
    private static final String COURT_SITE_ID = "COURT_SITE_ID";

    @Id
    @GeneratedValue(generator = "xhb_court_site_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_court_site_seq", sequenceName = "xhb_court_site_seq",
        allocationSize = 1)
    @Column(name = COURT_SITE_ID)
    private Integer courtSiteId;

    @Column(name = "COURT_SITE_NAME")
    private String courtSiteName;

    @Column(name = "COURT_SITE_CODE")
    private String courtSiteCode;

    @Column(name = "COURT_ID")
    private Integer courtId;

    @Column(name = "ADDRESS_ID")
    private Integer addressId;

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

    @Column(name = "OBS_IND")
    private String obsInd;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "CREST_COURT_ID")
    private String crestCourtId;

    @Column(name = "SHORT_NAME")
    private String shortName;

    @Column(name = "SITE_GROUP")
    private Integer siteGroup;

    @Column(name = "FLOATER_TEXT")
    private String floaterText;

    @Column(name = "LIST_NAME")
    private String listName;

    @Column(name = "TIER")
    private String tier;

    @OneToMany
    @JoinColumn(name = COURT_SITE_ID)
    private Set<XhbDispMgrUrlDao> xhbDispMgrUrlDao;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "XHIBIT_COURT_SITE_ID")
    private Set<XhbDispMgrCourtSiteDao> xhbDispMgrCourtSiteDao;

    // Comment the 3 lines below out and the getter/setter if you don't have XHB_COURT_SITE_WELSH in
    // your database
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = COURT_SITE_ID, referencedColumnName = COURT_SITE_ID, insertable = false,
        updatable = false)
    private XhbCourtSiteWelshDao xhbCourtSiteWelshDao;

    public Integer getId() {
        return courtSiteId;
    }

    public void setId(Integer id) {
        this.courtSiteId = id;
    }

    public String getCourtSiteName() {
        return courtSiteName;
    }

    public void setCourtSiteName(String courtSiteName) {
        this.courtSiteName = courtSiteName;
    }

    public String getCourtSiteCode() {
        return courtSiteCode;
    }

    public void setCourtSiteCode(String courtSiteCode) {
        this.courtSiteCode = courtSiteCode;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
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

    public String getObsInd() {
        return obsInd;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCrestCourtId() {
        return crestCourtId;
    }

    public void setCrestCourtId(String crestCourtId) {
        this.crestCourtId = crestCourtId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getSiteGroup() {
        return siteGroup;
    }

    public void setSiteGroup(Integer siteGroup) {
        this.siteGroup = siteGroup;
    }

    public String getFloaterText() {
        return floaterText;
    }

    public void setFloaterText(String floaterText) {
        this.floaterText = floaterText;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Set<XhbDispMgrUrlDao> getXhbDispMgrUrlDao() {
        return xhbDispMgrUrlDao;
    }

    public void setXhbDispMgrUrlDao(Set<XhbDispMgrUrlDao> xhbDispMgrUrlDao) {
        this.xhbDispMgrUrlDao = xhbDispMgrUrlDao;
    }

    public Set<XhbDispMgrCourtSiteDao> getXhbDispMgrCourtSiteDao() {
        return xhbDispMgrCourtSiteDao;
    }

    public void setXhbDispMgrCourtSiteDao(Set<XhbDispMgrCourtSiteDao> xhbDispMgrCourtSiteDao) {
        this.xhbDispMgrCourtSiteDao = xhbDispMgrCourtSiteDao;
    }

    public XhbCourtSiteWelshDao getXhbCourtSiteWelshDao() {
        return xhbCourtSiteWelshDao;
    }

    public void setXhbCourtSiteWelshDao(XhbCourtSiteWelshDao xhbCourtSiteWelshDao) {
        this.xhbCourtSiteWelshDao = xhbCourtSiteWelshDao;
    }
}
