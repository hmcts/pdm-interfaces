package uk.gov.hmcts.pdm.business.entities.xhbconfigprop;

import com.pdm.hb.jpa.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;

import java.util.List;

@Repository
public class XhbConfigPropRepository extends AbstractRepository<XhbConfigPropDao> {

    public XhbConfigPropRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbConfigPropDao> getDaoClass() {
        return XhbConfigPropDao.class;
    }

    /**
     * findByPropertyNameSafe.
     * @param propertyName String
     * @return List
     */
    @SuppressWarnings("unchecked")
    public List<XhbConfigPropDao> findByPropertyNameSafe(String propertyName) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            Query query = em.createNamedQuery("XHB_CONFIG_PROP.findByPropertyName");
            query.setParameter("propertyName", propertyName);
            return query.getResultList();
        }
    }
}
