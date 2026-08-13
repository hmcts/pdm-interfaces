package uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping;

import java.io.Serializable;
import java.util.Objects;

public class XhbDispMgrMappingId implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer urlId;
    private Integer cduId;

    public XhbDispMgrMappingId() {
        // Empty Constructor for JPA
    }

    public XhbDispMgrMappingId(Integer urlId, Integer cduId) {
        this.urlId = urlId;
        this.cduId = cduId;
    }

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof XhbDispMgrMappingId)) {
            return false;
        }
        XhbDispMgrMappingId that = (XhbDispMgrMappingId) object;
        return Objects.equals(urlId, that.urlId)
            && Objects.equals(cduId, that.cduId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlId, cduId);
    }
}