package uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;


@Entity(name = "XHB_DISP_MGR_MAPPING")
@NamedQuery(name = "XHB_DISP_MGR_MAPPING.findByCduId",
    query = "SELECT o FROM XHB_DISP_MGR_MAPPING o WHERE o.cduId = :cduId")
@NamedQuery(name = "XHB_DISP_MGR_MAPPING.findByCompositeId",
    query = "SELECT o FROM XHB_DISP_MGR_MAPPING o WHERE o.urlId = :urlId " + "and o.cduId = :cduId")
public class XhbDispMgrMappingDao extends AbstractDao {

    @Id
    @Column(name = "URL_ID")
    private Integer urlId;

    @Column(name = "CDU_ID")
    private Integer cduId;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "CREATED_BY")
    private String createdBy;


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

    @Override
    public Integer getVersion() {
        return null;
    }

    @Override
    public void setVersion(Integer version) {

    }

}
