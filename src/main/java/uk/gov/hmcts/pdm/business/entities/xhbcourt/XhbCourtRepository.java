package uk.gov.hmcts.pdm.business.entities.xhbcourt;

import jakarta.persistence.EntityManager;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;


public class XhbCourtRepository extends AbstractRepository<XhbCourtDao> {

    public XhbCourtRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbCourtDao> getDaoClass() {
        return XhbCourtDao.class;
    }
}
