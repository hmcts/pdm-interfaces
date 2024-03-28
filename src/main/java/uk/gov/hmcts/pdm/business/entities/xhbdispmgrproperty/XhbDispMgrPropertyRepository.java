package uk.gov.hmcts.pdm.business.entities.xhbdispmgrproperty;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.Property;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class XhbDispMgrPropertyRepository extends AbstractRepository<XhbDispMgrPropertyDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrPropertyRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    public XhbDispMgrPropertyRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrPropertyDao> getDaoClass() {
        return XhbDispMgrPropertyDao.class;
    }

    /**
     * findDaoPropertyValueByName.
     * 
     * @param propertyName String
     * @return XhbDispMgrPropertyDao
     */
    @SuppressWarnings("unchecked")
    private XhbDispMgrPropertyDao findDaoPropertyValueByName(final String propertyName) {
        final String methodName = "findDaoPropertyValueByName";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query =
            getEntityManager().createNamedQuery("XHB_DISP_MGR_PROPERTY.findPropertyValueByName");
        query.setParameter("propertyName", propertyName);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrPropertyDao) query.getResultList().get(0);
    }

    public String findPropertyValueByName(final String propertyName) {
        final String methodName = "findPropertyValueByName";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbDispMgrPropertyDao dao = findDaoPropertyValueByName(propertyName);
        if (dao != null) {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return convertDaoToPropertyBasicValue(dao).getValue();
        } else {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return null;
        }
    }
    
    @Override
    public void delete(Optional<XhbDispMgrPropertyDao> dao) {
        final String methodName = "delete";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        super.delete(dao);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    /**
     * findDaoAllProperties.
     * 
     * @return List
     */
    private List<XhbDispMgrPropertyDao> findDaoAllProperties() {
        final String methodName = "findDaoAllProperties";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return findAll();
    }

    public List<IProperty> findAllProperties() {
        final String methodName = "findAllProperties";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        List<XhbDispMgrPropertyDao> daos = findDaoAllProperties();
        List<IProperty> results = new ArrayList<>();
        for (XhbDispMgrPropertyDao dao : daos) {
            results.add(convertDaoToPropertyBasicValue(dao));
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return results;
    }

    // Converter Method to take in a Dao and convert to IProperty
    private IProperty convertDaoToPropertyBasicValue(XhbDispMgrPropertyDao dao) {
        final String methodName = "convertDaoToPropertyBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        IProperty property = getPropertyFromDao(dao);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return property;
    }

    public static IProperty getPropertyFromDao(XhbDispMgrPropertyDao dao) {
        final String methodName = "getPropertyFromDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        IProperty property = new Property();
        property.setId(Long.valueOf(dao.getId()));
        property.setName(dao.getPropertyName());
        property.setValue(dao.getPropertyValue());
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return property;
    }
}
