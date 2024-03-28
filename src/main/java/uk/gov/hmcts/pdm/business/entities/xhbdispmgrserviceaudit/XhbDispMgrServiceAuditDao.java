package uk.gov.hmcts.pdm.business.entities.xhbdispmgrserviceaudit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.time.LocalDateTime;


@Entity(name = "XHB_DISP_MGR_SERVICE_AUDIT")
public class XhbDispMgrServiceAuditDao extends AbstractDao {

    @Id
    @GeneratedValue(generator = "DM_SERVICE_AUDIT_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "DM_SERVICE_AUDIT_SEQ", sequenceName = "DM_SERVICE_AUDIT_SEQ",
        allocationSize = 1)
    @Column(name = "SERVICE_AUDIT_ID")
    private Integer serviceAuditId;

    @Column(name = "FROM_ENDPOINT")
    private String fromEndpoint;

    @Column(name = "TO_ENDPOINT")
    private String toEndpoint;

    @Column(name = "SERVICE")
    private String service;

    @Column(name = "URL")
    private String url;

    @Column(name = "MESSAGE_ID")
    private String messageId;

    @Column(name = "MESSAGE_STATUS")
    private String messageStatus;

    @Column(name = "MESSAGE_REQUEST")
    private String messageRequest;

    @Column(name = "MESSAGE_RESPONSE")
    private String messageResponse;

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

    public Integer getId() {
        return serviceAuditId;
    }

    public void setId(Integer id) {
        this.serviceAuditId = id;
    }

    public String getFromEndpoint() {
        return fromEndpoint;
    }

    public void setFromEndpoint(String fromEndpoint) {
        this.fromEndpoint = fromEndpoint;
    }

    public String getToEndpoint() {
        return toEndpoint;
    }

    public void setToEndpoint(String toEndpoint) {
        this.toEndpoint = toEndpoint;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageRequest() {
        return messageRequest;
    }

    public void setMessageRequest(String messageRequest) {
        this.messageRequest = messageRequest;
    }

    public String getMessageResponse() {
        return messageResponse;
    }

    public void setMessageResponse(String messageResponse) {
        this.messageResponse = messageResponse;
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
}
