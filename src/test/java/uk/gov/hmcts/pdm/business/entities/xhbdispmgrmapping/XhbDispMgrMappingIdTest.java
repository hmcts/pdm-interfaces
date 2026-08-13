
package uk.gov.hmcts.pdm.business.entities.xhbdispmgrmapping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class XhbDispMgrMappingIdTest {

    private static final String EQUAL = "Result is not equal";
    private static final String NOT_EQUAL = "Result is equal";
    
    @Test
    void shouldSetAndGetCompositeKeyFields() {
        XhbDispMgrMappingId id = new XhbDispMgrMappingId();

        id.setUrlId(10);
        id.setCduId(20);

        assertEquals(10, id.getUrlId(), EQUAL);
        assertEquals(20, id.getCduId(), EQUAL);
    }

    @Test
    void shouldCreateKeyUsingAllArgumentsConstructor() {
        XhbDispMgrMappingId id = new XhbDispMgrMappingId(10, 20);

        assertEquals(10, id.getUrlId(), EQUAL);
        assertEquals(20, id.getCduId(), EQUAL);
    }

    @Test
    void shouldBeEqualWhenBothKeyFieldsMatch() {
        XhbDispMgrMappingId first = new XhbDispMgrMappingId(10, 20);
        XhbDispMgrMappingId second = new XhbDispMgrMappingId(10, 20);

        assertEquals(first, second, EQUAL);
        assertEquals(first.hashCode(), second.hashCode(), EQUAL);
    }

    @Test
    void shouldNotBeEqualWhenEitherKeyFieldDiffers() {
        XhbDispMgrMappingId id = new XhbDispMgrMappingId(10, 20);

        assertNotEquals(id, new XhbDispMgrMappingId(11, 20), NOT_EQUAL);
        assertNotEquals(id, new XhbDispMgrMappingId(10, 21), NOT_EQUAL);
    }

    @Test
    void shouldNotBeEqualToNullOrAnotherType() {
        XhbDispMgrMappingId id = new XhbDispMgrMappingId(10, 20);

        assertNotEquals(id, null, NOT_EQUAL);
        assertNotEquals(id, "not a mapping id", NOT_EQUAL);
    }
}
