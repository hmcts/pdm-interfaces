package uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl;

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
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;

import java.time.LocalDateTime;

@Entity(name = "XHB_DISP_MGR_URL")
@NamedQuery(name = "XHB_DISP_MGR_URL.findByUrlId",
    query = "SELECT o FROM XHB_DISP_MGR_URL o WHERE o.urlId = :urlId")
@NamedQuery(name = "XHB_DISP_MGR_URL.findByCourtSiteId",
    query = "SELECT o FROM XHB_DISP_MGR_URL o WHERE o.courtSiteId = :courtSiteId")
public class XhbDispMgrUrlDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "DM_URL_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "DM_URL_SEQ", sequenceName = "DM_URL_SEQ", allocationSize = 1)
    @Column(name = "URL_ID")
    private Integer urlId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "URL")
    private String url;

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
    private XhbCourtSiteDao xhbCourtSiteDao;

    /*
     * @ManyToMany CDU_ID?? XHB_DISP_MGR_MAPPING? Not sure we need this private XhbDispMgrCduDao
     * cdus;
     */

    public Integer getId() {
        return urlId;
    }

    public void setId(Integer id) {
        this.urlId = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public XhbCourtSiteDao getXhbCourtSiteDao() {
        return xhbCourtSiteDao;
    }

    public void setXhbCourtSiteDao(XhbCourtSiteDao xhbCourtSiteDao) {
        this.xhbCourtSiteDao = xhbCourtSiteDao;
    }
}
