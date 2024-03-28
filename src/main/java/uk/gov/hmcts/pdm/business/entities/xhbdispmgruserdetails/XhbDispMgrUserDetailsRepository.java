package uk.gov.hmcts.pdm.business.entities.xhbdispmgruserdetails;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.pdm.business.entities.AbstractRepository;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.UserDetails;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.IUserDetails;

import java.util.ArrayList;
import java.util.List;

public class XhbDispMgrUserDetailsRepository extends AbstractRepository<XhbDispMgrUserDetailsDao> {

    private static final Logger LOG =
        LoggerFactory.getLogger(XhbDispMgrUserDetailsRepository.class);

    private static final String METHOD = "Method ";
    private static final String THREE_PARAMS = "{}{}{}";
    private static final String STARTS = " - starts";
    private static final String ENDS = " - ends";

    public XhbDispMgrUserDetailsRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Class<XhbDispMgrUserDetailsDao> getDaoClass() {
        return XhbDispMgrUserDetailsDao.class;
    }

    /**
     * findDaoUserDetailsByUserName.
     * 
     * @param userName String
     * @return XhbDispMgrUserDetailsDao
     */
    @SuppressWarnings("unchecked")
    private XhbDispMgrUserDetailsDao findDaoUserDetailsByUserName(final String userName) {
        final String methodName = "findDaoUserDetailsByUserName";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        Query query = getEntityManager()
            .createNamedQuery("XHB_DISP_MGR_USER_DETAILS.findUserDetailsByUserName");
        query.setParameter("userName", userName);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return query.getResultList().isEmpty() ? null
            : (XhbDispMgrUserDetailsDao) query.getResultList().get(0);
    }

    public IUserDetails findUserDetailsByUserName(final String userName) {
        final String methodName = "findUserDetailsByUserName";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        XhbDispMgrUserDetailsDao dao = findDaoUserDetailsByUserName(userName);
        if (dao != null) {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return convertDaoToUserDetailsBasicValue(dao);
        } else {
            LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
            return null;
        }
    }

    /**
     * isUserDetailsWithUserName.
     * 
     * @param userName String
     * @return boolean
     */
    public boolean isUserDetailsWithUserName(final String userName) {
        final String methodName = "isUserDetailsWithUserName";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return findUserDetailsByUserName(userName) != null;
    }

    /**
     * getDaoUserDetails.
     * 
     * @return List
     */
    private List<XhbDispMgrUserDetailsDao> getDaoUserDetails() {
        final String methodName = "getDaoUserDetails";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return findAll();
    }

    public List<IUserDetails> getUserDetails() {
        final String methodName = "getUserDetails";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        List<XhbDispMgrUserDetailsDao> daos = getDaoUserDetails();
        List<IUserDetails> results = new ArrayList<>();
        for (XhbDispMgrUserDetailsDao dao : daos) {
            results.add(convertDaoToUserDetailsBasicValue(dao));
        }
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return results;
    }

    // Converter Method to take in a Dao and convert to IUserDetails
    private IUserDetails convertDaoToUserDetailsBasicValue(XhbDispMgrUserDetailsDao dao) {
        final String methodName = "convertDaoToUserDetailsBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        IUserDetails userDetails = getUserDetailsFromDao(dao);
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return userDetails;
    }

    public static IUserDetails getUserDetailsFromDao(XhbDispMgrUserDetailsDao dao) {
        final String methodName = "getUserDetailsFromDao";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);
        IUserDetails userDetails = new UserDetails();
        userDetails.setId(Long.valueOf(dao.getId()));
        userDetails.setUserName(dao.getUserName());
        userDetails.setUserRole(dao.getUserRole());
        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
        return userDetails;
    }

    // Add new user
    public void saveDaoFromBasicValue(IUserDetails userDetails) {
        final String methodName = "saveDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        XhbDispMgrUserDetailsDao dao = new XhbDispMgrUserDetailsDao();

        setDaoFromBasicValue(dao, userDetails);

        save(dao);

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    private void setDaoFromBasicValue(XhbDispMgrUserDetailsDao dao, IUserDetails userDetails) {
        final String methodName = "setDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        dao.setUserName(userDetails.getUserName());
        dao.setUserRole(userDetails.getUserRole());

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }

    // Remove User
    public void deleteDaoFromBasicValue(IUserDetails userDetails) {
        final String methodName = "deleteDaoFromBasicValue";
        LOG.debug(THREE_PARAMS, METHOD, methodName, STARTS);

        getEntityManager().getTransaction().begin();

        XhbDispMgrUserDetailsDao dao =
            getEntityManager().find(XhbDispMgrUserDetailsDao.class, userDetails.getId().intValue());

        getEntityManager().remove(dao);

        getEntityManager().getTransaction().commit();

        clearEntityManager();

        LOG.debug(THREE_PARAMS, METHOD, methodName, ENDS);
    }
}
