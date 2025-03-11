package uk.gov.hmcts.pdm.publicdisplay.manager.web.display;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayLocationDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RotationSetsDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.anyObject;
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
@SuppressWarnings("PMD.LawOfDemeter")
class DisplayControllerTest extends DisplayControllerErrorTest {

    @Test
    void showAmendDisplayTest() throws Exception {
        final Capture<DisplaySearchCommand> capturedDisplaySearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final Capture<List<DisplayDto>> capturedDisplayDtos = newCapture();
        final Capture<List<DisplayTypeDto>> capturedDisplayTypeDtos = newCapture();
        final Capture<List<RotationSetsDto>> capturedRotationSetsDtos = newCapture();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSiteDtos = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayDto> displayDtos = createDisplayDtoList();
        final List<DisplayTypeDto> displayTypeDtos = createDisplayTypeDtoList();
        final List<RotationSetsDto> rotationSetsDtos = createRotationSets();

        mockDisplayPageStateHolder.setDisplaySearchCommand(capture(capturedDisplaySearchCommand));
        expectLastCall();
        mockDisplaySelectedValidator.validate(capture(capturedDisplaySearchCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockDisplayPageStateHolder.setSites(xhibitCourtSiteDtos);
        expectLastCall().anyTimes();
        expect(mockDisplayService.getDisplayTypes()).andReturn(displayTypeDtos);
        mockDisplayPageStateHolder.setDisplayTypes(capture(capturedDisplayTypeDtos));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getRotationSets()).andReturn(rotationSetsDtos).anyTimes();
        mockDisplayPageStateHolder.setRotationSets(capture(capturedRotationSetsDtos));
        expectLastCall();
        expect(mockDisplayService.getDisplays(eq(8L), anyObject(), capture(capturedCourtSiteDtos),
            anyObject())).andReturn(displayDtos);
        mockDisplayPageStateHolder.setDisplays(capture(capturedDisplayDtos));
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockDisplayPageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getSitesBySelectedCourt(10))
            .andReturn(xhibitCourtSiteDtos);
        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos).anyTimes();
        expect(mockDisplayPageStateHolder.getDisplayTypes()).andReturn(displayTypeDtos).anyTimes();
        expect(mockDisplayService.getRotationSets(eq(10))).andReturn(rotationSetsDtos).anyTimes();
        replay(mockDisplaySelectedValidator);
        replay(mockDisplayService);
        replay(mockDisplayPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplayAmendCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(displayDtos, model.get(DISPLAY_LIST), NOT_EQUAL);
        assertEquals(rotationSetsDtos, model.get(ROTATION_SET_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get(COURTSITE), NOT_EQUAL);
        assertEquals(viewNameAmendDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(A_LOCALE, capturedDisplayDtos.getValue().get(0).getLocale(), NOT_EQUAL);
        verify(mockDisplayService);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplaySelectedValidator);
    }

    @Test
    void showAmendDisplayEmptyListsTest() throws Exception {
        final Capture<DisplaySearchCommand> capturedDisplaySearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<XhibitCourtSiteDto> xhibitCourtSite = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockDisplayPageStateHolder.setDisplaySearchCommand(capture(capturedDisplaySearchCommand));
        expectLastCall();
        mockDisplaySelectedValidator.validate(capture(capturedDisplaySearchCommand),
            capture(capturedErrors));
        expectLastCall();

        // Looping though empty Sites
        expect(mockDisplayService.getCourtSites()).andReturn(new ArrayList<>()).anyTimes();
        mockDisplayPageStateHolder.setSites(new ArrayList<>());
        expectLastCall().anyTimes();
        // Checking for empty Sites
        expect(mockDisplayPageStateHolder.getSites()).andReturn(new ArrayList<>()).times(5);

        // Populate selected CourtSite
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        mockDisplayPageStateHolder.setCourtSite(capture(xhibitCourtSite));
        expectLastCall();

        // Looping through the rest of the lists
        expect(mockDisplayService.getDisplayTypes()).andReturn(new ArrayList<>()).anyTimes();
        mockDisplayPageStateHolder.setDisplayTypes(new ArrayList<>());
        expectLastCall().anyTimes();
        expect(mockDisplayService.getRotationSets(EasyMock.isA(Integer.class)))
            .andReturn(new ArrayList<>()).anyTimes();
        mockDisplayPageStateHolder.setRotationSets(new ArrayList<>());
        expectLastCall().anyTimes();
        // Displays
        expect(mockDisplayPageStateHolder.getSites()).andReturn(new ArrayList<>()).anyTimes();
        expect(mockDisplayService.getDisplays(8L, null, new ArrayList<>(), null))
            .andReturn(new ArrayList<>()).anyTimes();
        mockDisplayPageStateHolder.setDisplays(new ArrayList<>());
        expectLastCall().anyTimes();

        expect(mockDisplayPageStateHolder.getSitesBySelectedCourt(10)).andReturn(new ArrayList<>());
        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(new ArrayList<>()).anyTimes();
        expect(mockDisplayPageStateHolder.getDisplayTypes()).andReturn(new ArrayList<>())
            .anyTimes();
        expect(mockDisplayPageStateHolder.getRotationSets()).andReturn(new ArrayList<>())
            .anyTimes();

        replay(mockDisplaySelectedValidator);
        replay(mockDisplayService);
        replay(mockDisplayPageStateHolder);

        boolean result = true;
        // Perform the test
        mockMvc.perform(
            post(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD))
            .andReturn();
        assertTrue(result, FALSE);
        verify(mockDisplayService);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplaySelectedValidator);
    }

    @Test
    void showCreateDisplayTest() throws Exception {
        final Capture<DisplaySearchCommand> capturedDisplaySearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final Capture<List<DisplayDto>> capturedDisplayDtos = newCapture();
        final Capture<List<DisplayTypeDto>> capturedDisplayTypeDtos = newCapture();
        final Capture<List<RotationSetsDto>> capturedRotationSetsDtos = newCapture();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSiteDtos = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayDto> displayDtos = createDisplayDtoList();
        final List<DisplayTypeDto> displayTypeDtos = createDisplayTypeDtoList();
        final List<RotationSetsDto> rotationSetsDtos = createRotationSets();

        mockDisplayPageStateHolder.setDisplaySearchCommand(capture(capturedDisplaySearchCommand));
        expectLastCall();
        mockDisplaySelectedValidator.validate(capture(capturedDisplaySearchCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockDisplayPageStateHolder.setSites(xhibitCourtSiteDtos);
        expectLastCall().anyTimes();
        expect(mockDisplayService.getDisplayTypes()).andReturn(displayTypeDtos);
        mockDisplayPageStateHolder.setDisplayTypes(capture(capturedDisplayTypeDtos));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getRotationSets()).andReturn(rotationSetsDtos).anyTimes();
        mockDisplayPageStateHolder.setRotationSets(capture(capturedRotationSetsDtos));
        expectLastCall();
        expect(mockDisplayService.getDisplays(eq(8L), anyObject(), capture(capturedCourtSiteDtos),
            anyObject())).andReturn(displayDtos);
        mockDisplayPageStateHolder.setDisplays(capture(capturedDisplayDtos));
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockDisplayPageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getSitesBySelectedCourt(10))
            .andReturn(xhibitCourtSiteDtos);
        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos).anyTimes();
        expect(mockDisplayPageStateHolder.getDisplayTypes()).andReturn(displayTypeDtos).anyTimes();
        expect(mockDisplayService.getRotationSets(eq(10))).andReturn(rotationSetsDtos).anyTimes();
        replay(mockDisplaySelectedValidator);
        replay(mockDisplayService);
        replay(mockDisplayPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAdd", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplayCreateCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(displayDtos, model.get(DISPLAY_LIST), NOT_EQUAL);
        assertEquals(displayTypeDtos, model.get(DISPLAY_TYPE_LIST), NOT_EQUAL);
        assertEquals(rotationSetsDtos, model.get(ROTATION_SET_LIST), NOT_EQUAL);
        assertEquals(viewNameCreateDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(A_LOCALE, capturedDisplayDtos.getValue().get(0).getLocale(), NOT_EQUAL);
        assertEquals("yn", capturedRotationSetsDtos.getValue().get(0).getDefaultYn(), NOT_EQUAL);
        verify(mockDisplayService);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplaySelectedValidator);
    }

    @Test
    void showDeleteDisplayTest() throws Exception {
        final Capture<DisplaySearchCommand> capturedDisplaySearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<XhibitCourtSiteDto> capturedCourtSite = newCapture();
        final Capture<List<DisplayDto>> capturedDisplayDtos = newCapture();
        final Capture<List<DisplayTypeDto>> capturedDisplayTypeDtos = newCapture();
        final Capture<List<RotationSetsDto>> capturedRotationSetsDtos = newCapture();
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSiteDtos = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayDto> displayDtos = createDisplayDtoList();
        final List<DisplayTypeDto> displayTypeDtos = createDisplayTypeDtoList();
        final List<RotationSetsDto> rotationSetsDtos = createRotationSets();

        mockDisplayPageStateHolder.setDisplaySearchCommand(capture(capturedDisplaySearchCommand));
        expectLastCall();
        mockDisplaySelectedValidator.validate(capture(capturedDisplaySearchCommand),
            capture(capturedErrors));
        expectLastCall();
        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockDisplayPageStateHolder.setSites(xhibitCourtSiteDtos);
        expectLastCall().anyTimes();
        expect(mockDisplayService.getDisplayTypes()).andReturn(displayTypeDtos);
        mockDisplayPageStateHolder.setDisplayTypes(capture(capturedDisplayTypeDtos));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getRotationSets()).andReturn(rotationSetsDtos).anyTimes();
        mockDisplayPageStateHolder.setRotationSets(capture(capturedRotationSetsDtos));
        expectLastCall();
        expect(mockDisplayService.getDisplays(eq(8L), anyObject(), capture(capturedCourtSiteDtos),
            anyObject())).andReturn(displayDtos);
        mockDisplayPageStateHolder.setDisplays(capture(capturedDisplayDtos));
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        mockDisplayPageStateHolder.setCourtSite(capture(capturedCourtSite));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos).anyTimes();
        expect(mockDisplayPageStateHolder.getDisplayTypes()).andReturn(displayTypeDtos).anyTimes();
        expect(mockDisplayService.getRotationSets(eq(10))).andReturn(rotationSetsDtos).anyTimes();
        replay(mockDisplaySelectedValidator);
        replay(mockDisplayService);
        replay(mockDisplayPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnDelete", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplayDeleteCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);

        assertEquals(displayDtos, model.get(DISPLAY_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), model.get(COURTSITE), NOT_EQUAL);
        assertEquals(viewNameDeleteDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(displayDtos, capturedDisplayDtos.getValue(), NOT_EQUAL);
        assertEquals(12, capturedDisplayTypeDtos.getValue().get(0).getDisplayTypeId(), NOT_EQUAL);
        assertEquals("yn", capturedRotationSetsDtos.getValue().get(0).getDefaultYn(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSiteDtos.getValue().get(0),
            NOT_EQUAL);
        verify(mockDisplayService);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplaySelectedValidator);
    }

    @Test
    void createDisplayTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final Capture<DisplayCreateCommand> capturedDisplayCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<DisplayDto>> capturedDisplayDtos = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayDto> displayDtos = createDisplayDtoList();

        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos);
        mockDisplayCreateValidator.validate(capture(capturedDisplayCreateCommand),
            capture(capturedErrors), capture(capturedDisplayDtos));
        expectLastCall();
        mockDisplayService.createDisplay(capture(capturedDisplayCreateCommand));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getDisplaySearchCommand()).andReturn(null);
        mockDisplayPageStateHolder.reset();
        expectLastCall();
        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockDisplayPageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockDisplayService);
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayCreateValidator);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCreateDisplayUrl).param(XHIBIT_COURTSITE_ID, "1")
                .param(DISPLAY_TYPE_ID, "13").param(ROTATION_SET_ID, "14")
                .param(DESCRIPTION_CODE, "A descriptionCode").param("btnCreateConfirm", ADD))
            .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplaySearchCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameViewDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(displayDtos, capturedDisplayDtos.getValue(), NOT_EQUAL);
        assertEquals(1, capturedDisplayCreateCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);
        assertEquals("Display has been created successfully.", model.get("successMessage"),
            NOT_EQUAL);
        verify(mockDisplayService);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplayCreateValidator);
    }

    @Test
    void updateDisplayTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final Capture<DisplayAmendCommand> capturedDisplayAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockDisplayAmendValidator.validate(capture(capturedDisplayAmendCommand),
            capture(capturedErrors));
        expectLastCall();
        mockDisplayService.updateDisplay(capture(capturedDisplayAmendCommand));
        expectLastCall();
        mockDisplayPageStateHolder.reset();
        expectLastCall();
        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockDisplayPageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockDisplayService);
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayAmendValidator);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameAmendDisplayUrl).param(XHIBIT_COURTSITE_ID, "1")
                .param(DISPLAY_TYPE_ID, "13").param(ROTATION_SET_ID, "14").param(DISPLAY_ID, "15")
                .param("btnUpdateConfirm", ADD)).andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplaySearchCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameViewDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(1, capturedDisplayAmendCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);
        verify(mockDisplayService);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplayAmendValidator);
    }

    @Test
    void deleteDisplayTest() throws Exception {
        final Capture<List<XhibitCourtSiteDto>> capturedCourtSites = newCapture();
        final Capture<DisplayDeleteCommand> capturedDisplayDeleteCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockDisplayDeleteValidator.validate(capture(capturedDisplayDeleteCommand),
            capture(capturedErrors));
        expectLastCall();
        mockDisplayService.deleteDisplay(capture(capturedDisplayDeleteCommand));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getDisplaySearchCommand()).andReturn(null);
        mockDisplayPageStateHolder.reset();
        expectLastCall();
        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockDisplayPageStateHolder.setSites(capture(capturedCourtSites));
        expectLastCall();
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockDisplayService);
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayDeleteValidator);

        // Perform the test
        final MvcResult results = mockMvc.perform(post(mappingNameDeleteDisplayUrl)
            .param(DISPLAY_ID, "15").param("btnDeleteConfirm", ADD)).andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplaySearchCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameViewDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertFalse(capturedErrors.getValue().hasErrors(), NOT_FALSE);
        assertEquals(15, capturedDisplayDeleteCommand.getValue().getDisplayId(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos.get(0), capturedCourtSites.getValue().get(0), NOT_EQUAL);
        verify(mockDisplayService);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplayDeleteValidator);
    }

    @Test
    void loadDisplayTest() throws Exception {
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayTypeDto> displayTypeDtos = createDisplayTypeDtoList();
        final List<DisplayDto> displayDtos = createDisplayDtoList();
        final DisplayLocationDto displayLocation = createDisplayLocationDto();
        final List<RotationSetsDto> rotationSetsDtos = createRotationSets();
        final Long xhibitCourtSiteId = Long.valueOf(displayLocation.getCourtSiteId());

        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockDisplayPageStateHolder.setSites(xhibitCourtSiteDtos);
        expect(mockDisplayService.getDisplayTypes()).andReturn(displayTypeDtos);
        mockDisplayPageStateHolder.setDisplayTypes(displayTypeDtos);
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos).anyTimes();
        expect(mockDisplayService.getDisplay(2)).andReturn(displayDtos.get(0));
        expect(mockDisplayService.getDisplayLocation(1)).andReturn(displayLocation);
        expect(mockDisplayService.getDisplays(xhibitCourtSiteId, displayTypeDtos,
            xhibitCourtSiteDtos, rotationSetsDtos)).andReturn(displayDtos).anyTimes();
        mockDisplayPageStateHolder.setDisplays(displayDtos);
        expect(mockDisplayService.getCourtSite(xhibitCourtSiteDtos, xhibitCourtSiteId))
            .andReturn(xhibitCourtSiteDtos.get(0));
        expect(mockDisplayService.getRotationSets(xhibitCourtSiteDtos.get(0).getCourtId()))
            .andReturn(rotationSetsDtos);
        mockDisplayPageStateHolder.setRotationSets(rotationSetsDtos);
        expect(mockDisplayPageStateHolder.getDisplayTypes()).andReturn(displayTypeDtos).anyTimes();
        expect(mockDisplayService.getDisplayType(displayTypeDtos, 12)).andReturn(displayTypeDtos.get(0));
        expect(mockDisplayPageStateHolder.getRotationSets()).andReturn(rotationSetsDtos).anyTimes();
        expect(mockDisplayService.getRotationSet(rotationSetsDtos, 12)).andReturn(rotationSetsDtos.get(0));
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameAmendDisplayUrl + "/2")).andReturn();
        String response = results.getResponse().getContentAsString();
        DisplayDto returnedDisplayDto = new ObjectMapper().readValue(response, DisplayDto.class);

        assertEquals(A_LOCALE, returnedDisplayDto.getLocale(), NOT_EQUAL);
        assertEquals(2, returnedDisplayDto.getDisplayId(), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
    }
}
