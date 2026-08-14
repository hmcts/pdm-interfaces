package uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrcdu.XhbDispMgrCduDao;
import uk.gov.hmcts.pdm.business.entities.xhbdispmgrurl.XhbDispMgrUrlDao;

import java.time.LocalDateTime;


@Entity(name = "XHB_DISP_MGR_MAPPING")
@NamedQuery(name = "XHB_DISP_MGR_MAPPING.findByCduId",
    query = "SELECT o FROM XHB_DISP_MGR_MAPPING o WHERE o.cduId = :cduId")
@NamedQuery(name = "XHB_DISP_MGR_MAPPING.findByCompositeId",
    query = "SELECT o FROM XHB_DISP_MGR_MAPPING o WHERE o.urlId = :urlId " + "and o.cduId = :cduId")
@IdClass(XhbDispMgrMappingId.class)
public class XhbDispMgrMappingDao extends AbstractDao {

    @Id
    @Column(name = "URL_ID")
    private Integer urlId;

    @Id
    @Column(name = "CDU_ID")
    private Integer cduId;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "CREATED_BY")
    private String createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CDU_ID", insertable = false, updatable = false)
    private XhbDispMgrCduDao xhbDispMgrCduDao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "URL_ID", insertable = false, updatable = false)
    private XhbDispMgrUrlDao xhbDispMgrUrlDao;

    
    public Integer getUrlId() {
        return urlId;
    }

    public void setUrlId(Integer urlId) {
        this.urlId = urlId;
    }

    public Integer getCduId() {
        return cduId;
    }

    public void setCduId(Integer cduId) {
        this.cduId = cduId;
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
    
    public XhbDispMgrCduDao getXhbDispMgrCduDao() {
        return xhbDispMgrCduDao;
    }

    public void setXhbDispMgrCduDao(XhbDispMgrCduDao xhbDispMgrCduDao) {
        this.xhbDispMgrCduDao = xhbDispMgrCduDao;
    }
    
    public XhbDispMgrUrlDao getXhbDispMgrUrlDao() {
        return xhbDispMgrUrlDao;
    }

    public void setXhbDispMgrUrlDao(XhbDispMgrUrlDao xhbDispMgrUrlDao) {
        this.xhbDispMgrUrlDao = xhbDispMgrUrlDao;
    }

    @Override
    public Integer getVersion() {
        return null;
    }

    @Override
    public void setVersion(Integer version) {

    }

}
