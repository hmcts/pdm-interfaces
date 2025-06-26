package uk.gov.hmcts.pdm.publicdisplay.manager.web.display;

import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.easymock.IAnswer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataAccessException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.exception.XpdmException;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.DisplayTypeDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.RotationSetsDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.XhibitCourtSiteDto;

import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(EasyMockExtension.class)
@SuppressWarnings("PMD")
abstract class DisplayControllerErrorTest extends DisplayControllerBaseTest {

    @Test
    void showAmendDisplayErrorTest() throws Exception {
        final Capture<DisplaySearchCommand> capturedDisplaySearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockDisplayPageStateHolder.setDisplaySearchCommand(capture(capturedDisplaySearchCommand));
        expectLastCall();
        mockDisplaySelectedValidator.validate(capture(capturedDisplaySearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockDisplaySelectedValidator);
        replay(mockDisplayPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAmend", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplaySearchCommand.class, model.get(COMMAND), NOT_AN_INSTANCE);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(viewNameViewDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedDisplaySearchCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplaySelectedValidator);
    }

    @Test
    void showCreateDisplayErrorTest() throws Exception {
        final Capture<DisplaySearchCommand> capturedDisplaySearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockDisplayPageStateHolder.setDisplaySearchCommand(capture(capturedDisplaySearchCommand));
        expectLastCall();
        mockDisplaySelectedValidator.validate(capture(capturedDisplaySearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockDisplaySelectedValidator);
        replay(mockDisplayPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnAdd", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(viewNameViewDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedDisplaySearchCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplaySelectedValidator);
    }

    @Test
    void showDeleteDisplayErrorTest() throws Exception {
        final Capture<DisplaySearchCommand> capturedDisplaySearchCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();

        mockDisplayPageStateHolder.setDisplaySearchCommand(capture(capturedDisplaySearchCommand));
        expectLastCall();
        mockDisplaySelectedValidator.validate(capture(capturedDisplaySearchCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockDisplayPageStateHolder.getSites()).andReturn(xhibitCourtSiteDtos);
        replay(mockDisplaySelectedValidator);
        replay(mockDisplayPageStateHolder);

        // Perform the test
        final MvcResult results = mockMvc.perform(
            post(mappingNameViewDisplayUrl).param(XHIBIT_COURTSITE_ID, "8").param("btnDelete", ADD))
            .andReturn();
        Map<String, Object> model = results.getModelAndView().getModel();

        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        assertEquals(viewNameViewDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(8, capturedDisplaySearchCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplaySelectedValidator);
    }

    @Test
    void createDisplayErrorTest() throws Exception {
        final Capture<DisplayCreateCommand> capturedDisplayCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<DisplayDto>> capturedDisplayDtos = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayDto> displayDtos = createDisplayDtoList();
        final List<DisplayTypeDto> displayTypeDtos = createDisplayTypeDtoList();
        final List<RotationSetsDto> rotationSetsDtos = createRotationSets();

        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos);
        mockDisplayCreateValidator.validate(capture(capturedDisplayCreateCommand),
            capture(capturedErrors), capture(capturedDisplayDtos));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockDisplayPageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0));
        expect(mockDisplayPageStateHolder.getSitesBySelectedCourt(10))
            .andReturn(xhibitCourtSiteDtos);
        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos);
        expect(mockDisplayPageStateHolder.getDisplayTypes()).andReturn(displayTypeDtos);
        expect(mockDisplayPageStateHolder.getRotationSets()).andReturn(rotationSetsDtos);
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayCreateValidator);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCreateDisplayUrl).param(XHIBIT_COURTSITE_ID, "1")
                .param(DISPLAY_TYPE_ID, "13").param(ROTATION_SET_ID, "14")
                .param(DESCRIPTION_CODE, "A descriptionCode").param("btnCreateConfirm", ADD))
            .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplayCreateCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameCreateDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(displayDtos, capturedDisplayDtos.getValue(), NOT_EQUAL);
        assertEquals(1, capturedDisplayCreateCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplayCreateValidator);
    }

    @Test
    void createDisplayExceptionTest() throws Exception {
        final Capture<DisplayCreateCommand> capturedDisplayCreateCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final Capture<List<DisplayDto>> capturedDisplayDtos = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayDto> displayDtos = createDisplayDtoList();
        final List<DisplayTypeDto> displayTypeDtos = createDisplayTypeDtoList();
        final List<RotationSetsDto> rotationSetsDtos = createRotationSets();

        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos);
        mockDisplayCreateValidator.validate(capture(capturedDisplayCreateCommand),
            capture(capturedErrors), capture(capturedDisplayDtos));
        expectLastCall();
        mockDisplayService.createDisplay(capture(capturedDisplayCreateCommand));
        expectLastCall().andThrow(new DataAccessException("Create Display Exception") {});
        expect(mockDisplayPageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0));
        expect(mockDisplayPageStateHolder.getSitesBySelectedCourt(10))
            .andReturn(xhibitCourtSiteDtos);
        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos);
        expect(mockDisplayPageStateHolder.getDisplayTypes()).andReturn(displayTypeDtos);
        expect(mockDisplayPageStateHolder.getRotationSets()).andReturn(rotationSetsDtos);
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayCreateValidator);
        replay(mockDisplayService);

        // Perform the test
        final MvcResult results = mockMvc
            .perform(post(mappingNameCreateDisplayUrl).param(XHIBIT_COURTSITE_ID, "1")
                .param(DISPLAY_TYPE_ID, "13").param(ROTATION_SET_ID, "14")
                .param(DESCRIPTION_CODE, "A descriptionCode").param("btnCreateConfirm", ADD))
            .andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplayCreateCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameCreateDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals("Unable to create Display: Create Display Exception",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertEquals(displayDtos, capturedDisplayDtos.getValue(), NOT_EQUAL);
        assertEquals(1, capturedDisplayCreateCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplayCreateValidator);
        verify(mockDisplayService);
    }

    @Test
    void updateDisplayErrorTest() throws Exception {
        final Capture<DisplayAmendCommand> capturedDisplayAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayDto> displayDtos = createDisplayDtoList();
        final List<DisplayTypeDto> displayTypeDtos = createDisplayTypeDtoList();
        final List<RotationSetsDto> rotationSetsDtos = createRotationSets();

        mockDisplayAmendValidator.validate(capture(capturedDisplayAmendCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockDisplayPageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0));
        expect(mockDisplayPageStateHolder.getSitesBySelectedCourt(10))
            .andReturn(xhibitCourtSiteDtos);
        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos);
        expect(mockDisplayPageStateHolder.getDisplayTypes()).andReturn(displayTypeDtos);
        expect(mockDisplayPageStateHolder.getRotationSets()).andReturn(rotationSetsDtos);
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayAmendValidator);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameAmendDisplayUrl).param(XHIBIT_COURTSITE_ID, "1")
                .param(DISPLAY_TYPE_ID, "13").param(ROTATION_SET_ID, "14").param(DISPLAY_ID, "15")
                .param("btnUpdateConfirm", ADD)).andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplayAmendCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameAmendDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(1, capturedDisplayAmendCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplayAmendValidator);
    }

    @Test
    void updateDisplayExceptionTest() throws Exception {
        final Capture<DisplayAmendCommand> capturedDisplayAmendCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayDto> displayDtos = createDisplayDtoList();
        final List<DisplayTypeDto> displayTypeDtos = createDisplayTypeDtoList();
        final List<RotationSetsDto> rotationSetsDtos = createRotationSets();

        mockDisplayAmendValidator.validate(capture(capturedDisplayAmendCommand),
            capture(capturedErrors));
        expectLastCall();
        mockDisplayService.updateDisplay(capture(capturedDisplayAmendCommand));
        expectLastCall().andThrow(new DataAccessException("Update Display Exception") {});
        expect(mockDisplayPageStateHolder.getCourtSite()).andReturn(xhibitCourtSiteDtos.get(0));
        expect(mockDisplayPageStateHolder.getSitesBySelectedCourt(10))
            .andReturn(xhibitCourtSiteDtos);
        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos);
        expect(mockDisplayPageStateHolder.getDisplayTypes()).andReturn(displayTypeDtos);
        expect(mockDisplayPageStateHolder.getRotationSets()).andReturn(rotationSetsDtos);
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayAmendValidator);
        replay(mockDisplayService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(post(mappingNameAmendDisplayUrl).param(XHIBIT_COURTSITE_ID, "1")
                .param(DISPLAY_TYPE_ID, "13").param(ROTATION_SET_ID, "14").param(DISPLAY_ID, "15")
                .param("btnUpdateConfirm", ADD)).andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplayAmendCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals("Unable to update Display: Update Display Exception",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertEquals(1, capturedDisplayAmendCommand.getValue().getXhibitCourtSiteId(), NOT_EQUAL);
        assertEquals(xhibitCourtSiteDtos, model.get(COURTSITE_LIST), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplayAmendValidator);
        verify(mockDisplayService);
    }

    @Test
    void deleteDisplayErrorTest() throws Exception {
        final Capture<DisplayDeleteCommand> capturedDisplayDeleteCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();
        final List<DisplayDto> displayDtos = createDisplayDtoList();

        mockDisplayDeleteValidator.validate(capture(capturedDisplayDeleteCommand),
            capture(capturedErrors));
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            ((BindingResult) getCurrentArguments()[1]).reject(MOCK_ERROR_MESSAGE);
            return null;
        });
        expect(mockDisplayPageStateHolder.getDisplays()).andReturn(displayDtos);
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayDeleteValidator);

        final MvcResult results = mockMvc.perform(post(mappingNameDeleteDisplayUrl)
            .param(DISPLAY_ID, "15").param("btnDeleteConfirm", ADD)).andReturn();
        final Map<String, Object> model = results.getModelAndView().getModel();

        assertInstanceOf(DisplayDeleteCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameDeleteDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals(1, capturedErrors.getValue().getErrorCount(), NOT_EQUAL);
        assertEquals(15, capturedDisplayDeleteCommand.getValue().getDisplayId(), NOT_EQUAL);
        assertEquals(displayDtos, model.get(DISPLAY_LIST), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplayDeleteValidator);
    }

    @Test
    void deleteDisplayExceptionTest() throws Exception {
        final Capture<DisplayDeleteCommand> capturedDisplayDeleteCommand = newCapture();
        final Capture<BindingResult> capturedErrors = newCapture();

        mockDisplayDeleteValidator.validate(capture(capturedDisplayDeleteCommand),
            capture(capturedErrors));
        expectLastCall();
        mockDisplayService.deleteDisplay(capture(capturedDisplayDeleteCommand));
        expectLastCall().andThrow(new XpdmException("Delete Display Exception"));
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayDeleteValidator);
        replay(mockDisplayService);

        final MvcResult results = mockMvc.perform(post(mappingNameDeleteDisplayUrl)
            .param(DISPLAY_ID, "15").param("btnDeleteConfirm", ADD)).andReturn();

        assertInstanceOf(DisplayDeleteCommand.class,
            results.getModelAndView().getModel().get(COMMAND), NOT_AN_INSTANCE);
        assertNotNull(results, NULL);
        assertEquals(viewNameDeleteDisplay, results.getModelAndView().getViewName(), NOT_EQUAL);
        assertEquals("Unable to delete Display: Delete Display Exception",
            capturedErrors.getValue().getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        assertEquals(15, capturedDisplayDeleteCommand.getValue().getDisplayId(), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
        verify(mockDisplayDeleteValidator);
        verify(mockDisplayService);
    }

    @Test
    void loadDisplayNullTest() throws Exception {
        final List<XhibitCourtSiteDto> xhibitCourtSiteDtos = createCourtSiteDtoList();
        final List<DisplayTypeDto> displayTypeDtos = createDisplayTypeDtoList();

        expect(mockDisplayService.getCourtSites()).andReturn(xhibitCourtSiteDtos);
        mockDisplayPageStateHolder.setSites(xhibitCourtSiteDtos);
        expect(mockDisplayService.getDisplayTypes()).andReturn(displayTypeDtos);
        mockDisplayPageStateHolder.setDisplayTypes(displayTypeDtos);
        expect(mockDisplayService.getDisplay(6)).andReturn(null);
        replay(mockDisplayPageStateHolder);
        replay(mockDisplayService);

        // Perform the test
        final MvcResult results =
            mockMvc.perform(get(mappingNameAmendDisplayUrl + "/6")).andReturn();

        assertEquals("", results.getResponse().getContentAsString(), NOT_EQUAL);
        verify(mockDisplayPageStateHolder);
    }
}
