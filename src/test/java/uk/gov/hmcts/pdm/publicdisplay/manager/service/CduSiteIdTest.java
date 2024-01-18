package uk.gov.hmcts.pdm.publicdisplay.manager.service;

import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.pdm.publicdisplay.common.json.CduJson;
import uk.gov.hmcts.pdm.publicdisplay.common.util.AppConstants;
import uk.gov.hmcts.pdm.publicdisplay.manager.domain.api.ICourtSite;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CduDto;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Class CduSiteIdTest.
 */
@ExtendWith(EasyMockExtension.class)
class CduSiteIdTest extends CduUpdateTest {

    /**
     * Test get cdus by site Id valid.
     */
    @Test
    void testGetCdusBySiteIdValid() {
        // Local variables
        final ICourtSite courtSite = cdus.get(0).getCourtSite();
        final List<CduJson> testCduJsons = new ArrayList<>();
        testCduJsons.add(getTestCduJson(1L));
        testCduJsons.add(getTestCduJson(10L));
        testCduJsons.add(getTestCduJson(11L));

        // Add the mock calls to child classes
        expect(mockCourtSiteRepo
            .findCourtSiteByXhibitCourtSiteId(courtSite.getXhibitCourtSite().getId().intValue()))
                .andReturn(courtSite);
        replay(mockCourtSiteRepo);
        expect(mockLocalProxyRestClient.getCdus(courtSite.getLocalProxy())).andReturn(testCduJsons);
        replay(mockLocalProxyRestClient);

        // Set the class variables
        ReflectionTestUtils.setField(classUnderTest, LOCAL_PROXY_COMM_ENABLED, true);
        ReflectionTestUtils.setField(classUnderTest, "fakeCdusEnabled", false);

        // Perform the test
        final List<CduDto> results =
            classUnderTest.getCdusBySiteID(courtSite.getXhibitCourtSite().getId());

        // Assert that the objects are as expected
        assertNotNull(results, NULL);
        assertEquals(4, results.size(), NOT_EQUAL);

        // Verify the expected mocks were called
        verify(mockCourtSiteRepo);
        verify(mockLocalProxyRestClient);
    }


    /**
     * Gets the test cdu json.
     *
     * @param id the id
     * @return the test cdu json
     */
    private CduJson getTestCduJson(final Long id) {
        final CduJson cdu = new CduJson();
        cdu.setMacAddress(MACADDRESS + id.toString());
        cdu.setRegisteredIndicator(id % 2 == 0 ? AppConstants.YES_CHAR : AppConstants.NO_CHAR);
        return cdu;
    }

}
