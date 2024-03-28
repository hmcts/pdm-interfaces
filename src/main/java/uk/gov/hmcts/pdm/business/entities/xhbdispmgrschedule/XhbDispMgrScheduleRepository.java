package uk.gov.hmcts.pdm.business.entities.xhbdispmgrschedule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.Schedule;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ISchedule;

import java.util.ArrayList;
import java.util.List;

public class XhbDispMgrScheduleRepository extends AbstractRepository<XhbDispMgrScheduleDao> {

    private static final Logger LOG = LoggerFactory.getLogger(XhbDispMgrScheduleRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    public XhbDispMgrScheduleRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrScheduleDao> getDaoClass() {
        return XhbDispMgrScheduleDao.class;
    }

    /**
     * findDaoByScheduleId.
     * 
     * @param id Integer
     * @return XhbDispMgrScheduleDao
     */
    @SuppressWarnings("unchecked")
    private XhbDispMgrScheduleDao findDaoByScheduleId(final Integer id) {
        final String methodName = "findDaoByScheduleId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query = getEntityManager().createNamedQuery("XHB_DISP_MGR_SCHEDULE.findByScheduleId");
        query.setParameter("scheduleId", id);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrScheduleDao) query.getResultList().get(0);
    }

    // Wrapper Method to Return an ISchedule from findDaoByScheduleId
    public ISchedule findByScheduleId(final Integer id) {
        final String methodName = "findByScheduleId";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbDispMgrScheduleDao dao = findDaoByScheduleId(id);
        ISchedule schedule = new Schedule();
        if (dao != null) {
            schedule = getScheduleFromDao(dao);
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return schedule;
    }

    /**
     * findDaoPowerSaveSchedules.
     * 
     * @return List
     */
    @SuppressWarnings("unchecked")
    private List<XhbDispMgrScheduleDao> findDaoPowerSaveSchedules() {
        final String methodName = "findDaoPowerSaveSchedules";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query =
            getEntityManager().createNamedQuery("XHB_DISP_MGR_SCHEDULE.findPowerSaveSchedules");
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList();
    }

    // Wrapper Method to Return a List<ISchedule> from findDaoPowerSaveSchedules
    public List<ISchedule> findPowerSaveSchedules() {
        final String methodName = "findPowerSaveSchedules";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        List<XhbDispMgrScheduleDao> daos = findDaoPowerSaveSchedules();
        List<ISchedule> results = new ArrayList<>();
        for (XhbDispMgrScheduleDao dao : daos) {
            results.add(getScheduleFromDao(dao));
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return results;
    }

    public static ISchedule getScheduleFromDao(XhbDispMgrScheduleDao xhbDispMgrScheduleDao) {
        final String methodName = "getScheduleFromDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        ISchedule schedule = new Schedule();
        schedule.setId(xhbDispMgrScheduleDao.getId().longValue());
        schedule.setType(xhbDispMgrScheduleDao.getScheduleType());
        schedule.setTitle(xhbDispMgrScheduleDao.getTitle());
        schedule.setDetail(xhbDispMgrScheduleDao.getDetail());
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return schedule;
    }
}
