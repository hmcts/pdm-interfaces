package uk.gov.hmcts.pdm.business.entities.xhbcourtsitewelsh;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteDao;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.XhibitCourtSiteWelsh;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IXhibitCourtSiteWelsh;


public class XhbCourtSiteWelshRepository extends AbstractRepository<XhbCourtSiteWelshDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbCourtSiteWelshRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    public XhbCourtSiteWelshRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbCourtSiteWelshDao> getDaoClass() {
        return XhbCourtSiteWelshDao.class;
    }

    public static IXhibitCourtSiteWelsh getXhibitCourtSiteWelshFromDao(
        XhbCourtSiteDao xhbCourtSiteDao) {
        final String methodName = "getXhibitCourtSiteWelshFromDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        IXhibitCourtSiteWelsh xhibitCourtSiteWelsh = new XhibitCourtSiteWelsh();
        xhibitCourtSiteWelsh.setId(xhbCourtSiteDao.getXhbCourtSiteWelshDao().getId().longValue());
        xhibitCourtSiteWelsh
            .setCourtSiteName(xhbCourtSiteDao.getXhbCourtSiteWelshDao().getCourtSiteName());
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return xhibitCourtSiteWelsh;
    }
}
