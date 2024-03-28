package uk.gov.hmcts.pdm.business.entities.xhbcourtsitewelsh;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;

import java.io.Serializable;
import java.time.LocalDateTime;



@Entity(name = "XHB_COURT_SITE_WELSH")
public class XhbCourtSiteWelshDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = -3065834035097990139L;

    @Id
    @GeneratedValue(generator = "XHB_COURT_SITE_WELSH_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "XHB_COURT_SITE_WELSH_SEQ", sequenceName = "XHB_COURT_SITE_WELSH_SEQ",
        allocationSize = 1)
    @Column(name = "COURT_SITE_WELSH_ID")
    private Integer courtSiteWelshId;

    @Column(name = "COURT_SITE_NAME")
    private String courtSiteName;

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

    public Integer getId() {
        return courtSiteWelshId;
    }

    public void setId(Integer id) {
        this.courtSiteWelshId = id;
    }

    public String getCourtSiteName() {
        return courtSiteName;
    }

    public void setCourtSiteName(String courtSiteName) {
        this.courtSiteName = courtSiteName;
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
