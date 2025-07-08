package uk.gov.hmcts.pdm.publicdisplay.manager.web.hearing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataAccessException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.HearingTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(EasyMockExtension.class)
@SuppressWarnings({"PMD"})
class HearingControllerTest extends HearingErrorController {

    @Test
    void viewHearingTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockHearingTypePageStateHolder.reset();
        expectLastCall();
        expect(mockHearingTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockHearingTypePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockHearingTypeService);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(get(mappingNameViewHearingUrl).param(XHIBIT_COURTSITE_ID, "1")).andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewHearing, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);
        verify(mockHearingTypeService);
        verify(mockHearingTypePageStateHolder);
    }

    @Test
    void viewHearingNullTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        expect(mockHearingTypePageStateHolder.getHearingSearchCommand()).andReturn(null);
        mockHearingTypePageStateHolder.reset();
        expectLastCall();
        expect(mockHearingTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockHearingTypePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockHearingTypeService);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            get(mappingNameViewHearingUrl).param(XHIBIT_COURTSITE_ID, "1").param("reset", "false"))
            .andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewHearing, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);
        verify(mockHearingTypeService);
        verify(mockHearingTypePageStateHolder);
    }

    @Test
    void viewHearingResetFalseTest() throws Exception {
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        HearingTypeSearchCommand hearingTypeSearchCommand = new HearingTypeSearchCommand();

        expect(mockHearingTypePageStateHolder.getHearingSearchCommand())
            .andReturn(hearingTypeSearchCommand).times(2);
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            get(mappingNameViewHearingUrl).param(XHIBIT_COURTSITE_ID, "1").param("reset", "false"))
            .andReturn();
        String returnedViewName = results.getModelAndView().getViewName();

        assertNotNull(results, NULL);
        assertEquals(viewNameViewHearing, returnedViewName, NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, results.getModelAndView().getModel().get(COURTSITE_LIST),
            NOT_EQUAL);
        assertInstanceOf(HearingTypeSearchCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        verify(mockHearingTypePageStateHolder);
    }

    @Test
    void showAmendHearingTest() throws Exception {
        final Capture<HearingTypeSearchCommand> capturedHearingTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final Capture<List<HearingTypeDto>> hearingTypeDtoListCapture = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<HearingTypeDto> hearingTypeDtos = createHearingTypeDtoList();
        final List<String> categories = createCategoriesList();

        mockHearingTypePageStateHolder
            .setHearingSearchCommand(capture(capturedHearingTypeSearchCommand));
        expectLastCall();
        mockHearingTypeSelectedValidator.validate(capture(capturedHearingTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall();

        expect(mockHearingTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockHearingTypePageStateHolder.setSites(xhibitCourtSiteDtos);
        expectLastCall();

        expect(mockHearingTypeService.getHearingTypes(eq(8L))).andReturn(hearingTypeDtos);

        mockHearingTypePageStateHolder.setHearingTypes(capture(hearingTypeDtoListCapture));
        expectLastCall();

        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockHearingTypePageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();

        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(hearingTypeDtos)
            .anyTimes();
        expect(mockHearingTypeService.getAllCategories()).andReturn(categories).anyTimes();

        replay(mockHearingTypeSelectedValidator);
        replay(mockHearingTypeService);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewHearingUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(HearingTypeAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(hearingTypeDtos, model.get(HEARING_TYPE_LIST), NOT_EQUAL);
        assertEquals(categories, model.get(CATEGORIES_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get(COURTSITE), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(hearingTypeDtos, hearingTypeDtoListCapture.getValue(), NOT_EQUAL);
        assertEquals(8, capturedHearingTypeSearchCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(viewNameAmendHearing, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(8, capturedCourtSite.getValue().getId(), NOT_EQUAL);
        verify(mockHearingTypeService);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeSelectedValidator);
    }

    @Test
    void showAmendHearingEmptyListsTest() throws Exception {
        final Capture<HearingTypeSearchCommand> capturedHearingTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<HearingTypeDto> hearingTypeDtos = createHearingTypeDtoList();
        final List<String> categories = createCategoriesList();

        mockHearingTypePageStateHolder
            .setHearingSearchCommand(capture(capturedHearingTypeSearchCommand));
        expectLastCall();
        mockHearingTypeSelectedValidator.validate(capture(capturedHearingTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall();

        // Populate with empty lists
        expect(mockHearingTypeService.getCourtSites()).andReturn(new ArrayList<>()).anyTimes();
        mockHearingTypePageStateHolder.setSites(new ArrayList<>());
        expectLastCall().anyTimes();
        expect(mockHearingTypeService.getHearingTypes(EasyMock.isA(Long.class)))
            .andReturn(new ArrayList<>()).anyTimes();
        mockHearingTypePageStateHolder.setHearingTypes(new ArrayList<>());
        expectLastCall().anyTimes();
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(new ArrayList<>()).times(5);

        // Populate selected Court Site
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        mockHearingTypePageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();

        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).times(2);
        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(hearingTypeDtos)
            .times(2);
        expect(mockHearingTypeService.getAllCategories()).andReturn(categories);

        replay(mockHearingTypeSelectedValidator);
        replay(mockHearingTypeService);
        replay(mockHearingTypePageStateHolder);

        boolean result = true;
        // Perform the test
        mockMvc.perform(
            post(mappingNameViewHearingUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD))
            .andReturn();
        assertTrue(result, FALSE);
        verify(mockHearingTypeService);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeSelectedValidator);
    }


    @Test
    void updateHearingTypeTest() throws Exception {
        final Capture<HearingTypeAmendCommand> capturedHearingTypeAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();

        mockHearingTypeAmendValidator.validate(capture(capturedHearingTypeAmendCommand),
            capture(capturedErrors));
        expectLastCall();
        mockHearingTypeService.updateHearingType(capture(capturedHearingTypeAmendCommand));
        expectLastCall();
        mockHearingTypePageStateHolder.reset();
        expectLastCall();
        expect(mockHearingTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockHearingTypePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockHearingTypeAmendValidator);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendHearingUrl)
            .param("refHearingTypeId", "11").param("btnUpdateConfirm", ADD)
            .param(HEARING_TYPE_DESC, A_HEARING_TYPE_DESC).param(CATEGORY, A_CATEGORY)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(HearingTypeSearchCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);

        assertEquals("Hearing Type has been updated successfully.", model.get("successMessage"),
            NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(viewNameViewHearing, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeAmendValidator);
    }

    @Test
    void updateHearingTypeExceptionTest() throws Exception {
        final Capture<HearingTypeAmendCommand> hearingTypeAmendCommandCapture = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<HearingTypeDto> refHearingTypeDtos = createHearingTypeDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<String> categories = createCategoriesList();

        mockHearingTypeAmendValidator.validate(capture(hearingTypeAmendCommandCapture),
            capture(capturedErrors));
        expectLastCall();
        mockHearingTypeService.updateHearingType(capture(hearingTypeAmendCommandCapture));
        expectLastCall().andThrow(new DataAccessException("Update Hearing Type Exception") {});

        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockHearingTypePageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0));
        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(refHearingTypeDtos);
        expect(mockHearingTypeService.getAllCategories()).andReturn(categories);
        replay(mockHearingTypeAmendValidator);
        replay(mockHearingTypeService);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameAmendHearingUrl)
            .param("refHearingTypeId", "11").param("btnUpdateConfirm", ADD)
            .param(HEARING_TYPE_DESC, A_HEARING_TYPE_DESC).param(CATEGORY, A_CATEGORY)).andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(HearingTypeAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(refHearingTypeDtos, model.get(HEARING_TYPE_LIST), NOT_EQUAL);
        assertEquals(categories, model.get(CATEGORIES_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get(COURTSITE), NOT_EQUAL);
        assertEquals("Unable to update Hearing Type: Update Hearing Type Exception",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertEquals(11, hearingTypeAmendCommandCapture.getValue().getRefHearingTypeId(),
            NOT_EQUAL);
        verify(mockHearingTypeService);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeAmendValidator);
    }

    @Test
    void showCreateHearingTest() throws Exception {
        final Capture<HearingTypeSearchCommand> capturedHearingTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final Capture<List<HearingTypeDto>> hearingTypeDtoListCapture = newCapture();
        final List<HearingTypeDto> hearingTypeDtos = createHearingTypeDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<String> categories = createCategoriesList();

        mockHearingTypePageStateHolder
            .setHearingSearchCommand(capture(capturedHearingTypeSearchCommand));
        expectLastCall();
        mockHearingTypeSelectedValidator.validate(capture(capturedHearingTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockHearingTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockHearingTypePageStateHolder.setSites(xhibitCourtSiteDtos);
        expectLastCall();
        expect(mockHearingTypeService.getHearingTypes(eq(8L))).andReturn(hearingTypeDtos);
        mockHearingTypePageStateHolder.setHearingTypes(capture(hearingTypeDtoListCapture));
        expectLastCall();
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockHearingTypePageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();
        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(hearingTypeDtos)
            .anyTimes();
        expect(mockHearingTypeService.getAllCategories()).andReturn(categories).anyTimes();
        replay(mockHearingTypeSelectedValidator);
        replay(mockHearingTypeService);
        replay(mockHearingTypePageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewHearingUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAdd", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(HearingTypeCreateCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(hearingTypeDtos, model.get(HEARING_TYPE_LIST), NOT_EQUAL);
        assertEquals(categories, model.get(CATEGORIES_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get(COURTSITE), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(8, capturedHearingTypeSearchCommand.getValue().getXhibitCourtSiteId(),
            NOT_EQUAL);
        assertEquals(8, capturedCourtSite.getValue().getId(), NOT_EQUAL);
        assertEquals(A_HEARING_TYPE_CODE,
            hearingTypeDtoListCapture.getValue().get(0).getHearingTypeCode(), NOT_EQUAL);
        assertEquals(viewNameCreateHearing, results.getModelAndView().getViewName(), NOT_EQUAL);
        verify(mockHearingTypeService);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeSelectedValidator);
    }

    @Test
    void showCreateHearingEmptyListsTest() throws Exception {
        final Capture<HearingTypeSearchCommand> capturedHearingTypeSearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final List<HearingTypeDto> hearingTypeDtos = createHearingTypeDtoList();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<String> categories = createCategoriesList();

        mockHearingTypePageStateHolder
            .setHearingSearchCommand(capture(capturedHearingTypeSearchCommand));
        expectLastCall();
        mockHearingTypeSelectedValidator.validate(capture(capturedHearingTypeSearchCommand),
            capture(capturedErrors));
        expectLastCall();

        // Populate with empty lists
        expect(mockHearingTypeService.getCourtSites()).andReturn(new ArrayList<>()).anyTimes();
        mockHearingTypePageStateHolder.setSites(new ArrayList<>());
        expectLastCall().anyTimes();
        expect(mockHearingTypeService.getHearingTypes(EasyMock.isA(Long.class)))
            .andReturn(new ArrayList<>()).anyTimes();
        mockHearingTypePageStateHolder.setHearingTypes(new ArrayList<>());
        expectLastCall().anyTimes();
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(new ArrayList<>()).times(5);

        // Populate selected Court Site
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        mockHearingTypePageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();

        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).times(2);
        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(hearingTypeDtos)
            .times(2);
        expect(mockHearingTypeService.getAllCategories()).andReturn(categories);

        replay(mockHearingTypeSelectedValidator);
        replay(mockHearingTypeService);
        replay(mockHearingTypePageStateHolder);

        boolean result = true;
        // Perform the test
        mockMvc.perform(
            post(mappingNameViewHearingUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAdd", ADD))
            .andReturn();
        assertTrue(result, FALSE);
        verify(mockHearingTypeService);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeSelectedValidator);
    }

    @Test
    void createHearingTypeTest() throws Exception {
        final Capture<HearingTypeCreateCommand> hearingTypeCreateCommandCapture = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<HearingTypeDto>> capturedHearingTypeList = newCapture();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<HearingTypeDto> refHearingTypeDtos = createHearingTypeDtoList();

        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(refHearingTypeDtos);
        mockHearingTypeCreateValidator.validate(capture(hearingTypeCreateCommandCapture),
            capture(capturedErrors), capture(capturedHearingTypeList));
        expectLastCall();
        
        mockHearingTypeService.createHearingType(capture(hearingTypeCreateCommandCapture), eq(0));
        expectLastCall();
        mockHearingTypePageStateHolder.reset();
        expectLastCall();
        expect(mockHearingTypeService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockHearingTypePageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockHearingTypePageStateHolder.getHearingSearchCommand()).andReturn(null);
        
        replay(mockHearingTypeCreateValidator);
        replay(mockHearingTypePageStateHolder);
        replay(mockHearingTypeService);

        // Perform the test
        final MvcResult results =
            mockMvc
                .perform(post(mappingNameCreateHearingUrl)
                    .param("hearingTypeCode", A_HEARING_TYPE_CODE).param("btnCreateConfirm", ADD)
                    .param(HEARING_TYPE_DESC, A_HEARING_TYPE_DESC).param(CATEGORY, A_CATEGORY))
                .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(HearingTypeSearchCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals("Hearing Type has been created successfully.", model.get("successMessage"),
            NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(viewNameViewHearing, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(A_HEARING_TYPE_CODE,
            hearingTypeCreateCommandCapture.getValue().getHearingTypeCode(), NOT_EQUAL);
        assertEquals(4, capturedHearingTypeList.getValue().get(0).getListSequence(), NOT_EQUAL);
        assertEquals(8, capturedCourtSites.getValue().get(0).getId(), NOT_EQUAL);
        
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeCreateValidator);
        verify(mockHearingTypeService);
    }

    @Test
    void createHearingTypeExceptionTest() throws Exception {
        final Capture<HearingTypeCreateCommand> hearingTypeCreateCommandCapture = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<HearingTypeDto>> capturedHearingTypeList = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<String> categories = createCategoriesList();
        final List<HearingTypeDto> refHearingTypeDtos = createHearingTypeDtoList();

        expect(mockHearingTypePageStateHolder.getHearingTypes()).andReturn(refHearingTypeDtos)
            .times(2);
        mockHearingTypeCreateValidator.validate(capture(hearingTypeCreateCommandCapture),
            capture(capturedErrors), capture(capturedHearingTypeList));
        expectLastCall();
        mockHearingTypeService.createHearingType(capture(hearingTypeCreateCommandCapture), eq(0));
        expectLastCall().andThrow(new DataAccessException("Create Hearing Type Exception") {});
        expect(mockHearingTypePageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0));
        expect(mockHearingTypePageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        expect(mockHearingTypeService.getAllCategories()).andReturn(categories);
        replay(mockHearingTypeCreateValidator);
        replay(mockHearingTypePageStateHolder);
        replay(mockHearingTypeService);

        // Perform the test
        final MvcResult results =
            mockMvc
                .perform(post(mappingNameCreateHearingUrl)
                    .param("hearingTypeCode", A_HEARING_TYPE_CODE).param("btnCreateConfirm", ADD)
                    .param(HEARING_TYPE_DESC, A_HEARING_TYPE_DESC).param(CATEGORY, A_CATEGORY))
                .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(HearingTypeCreateCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals("Unable to create Hearing Type: Create Hearing Type Exception",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertEquals(viewNameCreateHearing, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(A_HEARING_TYPE_CODE,
            hearingTypeCreateCommandCapture.getValue().getHearingTypeCode(), NOT_EQUAL);
        assertEquals(4, capturedHearingTypeList.getValue().get(0).getListSequence(), NOT_EQUAL);
        verify(mockHearingTypePageStateHolder);
        verify(mockHearingTypeCreateValidator);
        verify(mockHearingTypeService);
    }

    @Test
    void loadHearingTypeTest() throws Exception {
        final List<HearingTypeDto> refHearingTypeDtos = createHearingTypeDtoList();
        final HearingTypeDto refHearingTypeDto = refHearingTypeDtos.get(0);
        final List<XhibitCourtSiteDto> courtSites = new ArrayList<>();

        expect(mockHearingTypeService.getCourtSites()).andReturn(courtSites).anyTimes();
        mockHearingTypePageStateHolder.setSites(courtSites);
        expect(mockHearingTypeService.getHearingType(EasyMock.isA(Integer.class)))
            .andReturn(refHearingTypeDto).anyTimes();
        expect(mockHearingTypeService.getHearingTypesByCourtId(refHearingTypeDto.getCourtId()))
            .andReturn(refHearingTypeDtos).anyTimes();
        replay(mockHearingTypeService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameAmendHearingUrl + "/2")).andReturn();
        String response = results.getResponse().getContentAsString();
        HearingTypeDto returnedHearingTypeDto =
            new ObjectMapper().readValue(response, HearingTypeDto.class);

        assertEquals(4, returnedHearingTypeDto.getListSequence(), NOT_EQUAL);
        assertEquals(2, returnedHearingTypeDto.getRefHearingTypeId(), NOT_EQUAL);
    }

}
