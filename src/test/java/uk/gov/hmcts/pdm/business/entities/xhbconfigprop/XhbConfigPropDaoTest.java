package uk.gov.hmcts.pdm.business.entities.xhbconfigprop;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(EasyMockExtension.class)
class XhbConfigPropDaoTest {

    private static final String NULL = "Result is null";
    private static final String NOT_EQUAL = "Result is not equal";
    
    @Test
    void testDefaultConstructor() {
        XhbConfigPropDao dao = new XhbConfigPropDao();
        assertNotNull(dao, NULL);
    }
    
    @Test
    void testConstructorWithParameters() {
        XhbConfigPropDao dao = new XhbConfigPropDao(1, "testProperty", "testValue");
        assertNotNull(dao, NULL);
        assertEquals(1, dao.getConfigPropId(), NOT_EQUAL);
        assertEquals("testProperty", dao.getPropertyName(), NOT_EQUAL);
        assertEquals("testValue", dao.getPropertyValue(), NOT_EQUAL);
    }
    
    @Test
    void testConstructorWithOtherData() {
        XhbConfigPropDao otherData = new XhbConfigPropDao(2, "otherProperty", "otherValue");
        XhbConfigPropDao dao = new XhbConfigPropDao(otherData);
        assertNotNull(dao, NULL);
        assertEquals(2, dao.getConfigPropId(), NOT_EQUAL);
        assertEquals("otherProperty", dao.getPropertyName(), NOT_EQUAL);
        assertEquals("otherValue", dao.getPropertyValue(), NOT_EQUAL);
    }
}