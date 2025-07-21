package uk.gov.hmcts.pdm.business.entities.xhbconfigprop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import uk.gov.hmcts.pdm.business.entities.AbstractDao;

import java.io.Serializable;

@SuppressWarnings({"PMD.ConstructorCallsOverridableMethod"})
@Entity(name = "XHB_CONFIG_PROP")
@NamedQuery(name = "XHB_CONFIG_PROP.findByPropertyName",
    query = "SELECT o from XHB_CONFIG_PROP o WHERE o.propertyName = :propertyName")
public class XhbConfigPropDao extends AbstractDao implements Serializable {

    private static final long serialVersionUID = 8037260805843296872L;

    @Id
    @GeneratedValue(generator = "xhb_config_prop_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "xhb_config_prop_seq", sequenceName = "xhb_config_prop_seq",
        allocationSize = 1)
    @Column(name = "CONFIG_PROP_ID")
    private Integer configPropId;

    @Column(name = "PROPERTY_NAME")
    private String propertyName;

    @Column(name = "PROPERTY_VALUE")
    private String propertyValue;

    public XhbConfigPropDao() {
        super();
    }

    public XhbConfigPropDao(Integer configPropId, String propertyName, String propertyValue) {
        this();
        setConfigPropId(configPropId);
        setPropertyName(propertyName);
        setPropertyValue(propertyValue);
    }

    public XhbConfigPropDao(XhbConfigPropDao otherData) {
        this();
        setConfigPropId(otherData.getConfigPropId());
        setPropertyName(otherData.getPropertyName());
        setPropertyValue(otherData.getPropertyValue());
    }

    public Integer getConfigPropId() {
        return configPropId;
    }

    private void setConfigPropId(Integer configPropId) {
        this.configPropId = configPropId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    private void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public Integer getVersion() {
        return null;
    }

    @Override
    public void setVersion(Integer version) {
        // Do nothing.
    }
}