package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.pdm.business.entities.xhbcourtsite.XhbCourtSiteRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefjudge.XhbRefJudgeDao;
import uk.gov.hmcts.pdm.business.entities.xhbrefjudge.XhbRefJudgeRepository;
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeDao;
import uk.gov.hmcts.pdm.business.entities.xhbrefsystemcode.XhbRefSystemCodeRepository;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.IRefJudgeService;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judge.JudgeAmendCommand;
import uk.gov.hmcts.pdm.publicdisplay.manager.web.judge.JudgeCreateCommand;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
abstract class RefJudgeServiceUtility extends AbstractJUnit {

    protected IRefJudgeService classUnderTest;
    protected XhbRefJudgeRepository mockRefJudgeRepo;
    protected XhbCourtSiteRepository mockCourtSiteRepo;
    protected XhbRefSystemCodeRepository mockRefSystemCodeRepository;
    protected EntityManager mockEntityManager;

    protected static final String NOT_EQUAL = "Not equal";
    protected static final String NOT_EMPTY = "Not empty";
    protected static final String NULL = "Result is Null";
    protected static final String NOT_NULL = "Result is Not Null";
    protected static final String FULL_LIST_TITLE_1 = "FullListTitle1";

    protected JudgeAmendCommand createJudgeAmendCommand() {
        JudgeAmendCommand judgeAmendCommand = new JudgeAmendCommand();
        judgeAmendCommand.setFirstName("firstName");
        judgeAmendCommand.setMiddleName("middleName");
        judgeAmendCommand.setSurname("surname");
        judgeAmendCommand.setTitle("title");
        judgeAmendCommand.setFullListTitle1(FULL_LIST_TITLE_1);
        judgeAmendCommand.setJudgeType("JudgeType");
        judgeAmendCommand.setRefJudgeId(1);
        return judgeAmendCommand;
    }

    protected JudgeCreateCommand createJudgeCreateCommand() {
        JudgeCreateCommand judgeCreateCommand = new JudgeCreateCommand();
        judgeCreateCommand.setFirstName("firstName");
        judgeCreateCommand.setMiddleName("middleName");
        judgeCreateCommand.setSurname("surname");
        judgeCreateCommand.setTitle("title");
        judgeCreateCommand.setFullListTitle1(FULL_LIST_TITLE_1);
        judgeCreateCommand.setJudgeType("JudgeType");
        return judgeCreateCommand;
    }


    protected List<XhbRefSystemCodeDao> createRefSystemCodeDao() {
        XhbRefSystemCodeDao xhbRefSystemCodeDao = new XhbRefSystemCodeDao();
        xhbRefSystemCodeDao.setCode("code");
        xhbRefSystemCodeDao.setCodeTitle("codeTitle");
        xhbRefSystemCodeDao.setCodeType("codeType");
        xhbRefSystemCodeDao.setCourtId(2);
        xhbRefSystemCodeDao.setCreatedBy("createdBy");
        xhbRefSystemCodeDao.setCreationDate(LocalDateTime.of(2024, 1, 11, 2, 2));
        xhbRefSystemCodeDao.setDeCode("DeCode");
        xhbRefSystemCodeDao.setLastUpdateDate(LocalDateTime.of(2024, 1, 11, 2, 2));
        xhbRefSystemCodeDao.setLastUpdatedBy("LastUpdatedBy");
        xhbRefSystemCodeDao.setObsInd("obsInd");
        xhbRefSystemCodeDao.setRefCodeOrder(3.0);
        xhbRefSystemCodeDao.setRefSystemCodeId(4);
        xhbRefSystemCodeDao.setVersion(5);
        List<XhbRefSystemCodeDao> xhbRefSystemCodeDaos = new ArrayList<>();
        xhbRefSystemCodeDaos.add(xhbRefSystemCodeDao);
        return xhbRefSystemCodeDaos;
    }

    protected XhbRefJudgeDao createRefJudgeDao() {
        XhbRefJudgeDao refJudgeDao = new XhbRefJudgeDao();
        refJudgeDao.setCourtId(2);
        refJudgeDao.setCrestJudgeId(3);
        refJudgeDao.setFirstName("firstName");
        refJudgeDao.setFullListTitle1(FULL_LIST_TITLE_1);
        refJudgeDao.setFullListTitle2(FULL_LIST_TITLE_1);
        refJudgeDao.setFullListTitle3(FULL_LIST_TITLE_1);
        refJudgeDao.setHonours("honours");
        refJudgeDao.setInitials("initials");
        refJudgeDao.setJudgeType("judgeType");
        refJudgeDao.setJudVers("judvers");
        refJudgeDao.setMiddleName("middleName");
        refJudgeDao.setObsInd("Obsind");
        refJudgeDao.setRefJudgeId(2);
        refJudgeDao.setSourceTable("sourceTable");
        refJudgeDao.setStatsCode("statsCode");
        refJudgeDao.setSurname("surname");
        refJudgeDao.setTitle("Title");
        refJudgeDao.setCreatedBy("User");
        refJudgeDao.setLastUpdatedBy(refJudgeDao.getCreatedBy());
        refJudgeDao.setCreationDate(LocalDateTime.now());
        refJudgeDao.setLastUpdateDate(refJudgeDao.getCreationDate());
        return refJudgeDao;
    }

}
