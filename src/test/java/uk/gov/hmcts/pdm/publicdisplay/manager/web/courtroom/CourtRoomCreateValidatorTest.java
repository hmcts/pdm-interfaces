package uk.gov.hmcts.pdm.publicdisplay.manager.web.courtroom;

import org.easymock.EasyMock;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import uk.gov.hmcts.pdm.publicdisplay.common.test.AbstractJUnit;
import uk.gov.hmcts.pdm.publicdisplay.manager.dto.CourtRoomDto;
import uk.gov.hmcts.pdm.publicdisplay.manager.service.api.ICourtRoomService;

import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD")
@ExtendWith(EasyMockExtension.class)
class CourtRoomCreateValidatorTest extends AbstractJUnit {
    private static final String NOT_EQUAL = "Not equal";
    private static final String FALSE = "False";
    private static final String COURT_ROOM_CREATE_VALIDATOR = "courtRoomCreateValidator";
    private static final String A_NAME = "A name";
    private CourtRoomCreateValidator classUnderTest;
    private CourtRoomCreateCommand courtRoomCreateCommand;
    private MessageSource mockMessageSource;
    private ICourtRoomService mockCourtRoomService;


    @BeforeEach
    public void setup() {
        // Create a new version of the class under test
        classUnderTest = new CourtRoomCreateValidator();
        courtRoomCreateCommand = new CourtRoomCreateCommand();

        mockCourtRoomService = EasyMock.createMock(ICourtRoomService.class);
        mockMessageSource = EasyMock.createMock(MessageSource.class);
        ReflectionTestUtils.setField(classUnderTest, "messageSource", mockMessageSource);
    }

    @Test
    void supportsTest() {
        final boolean result = classUnderTest.supports(CourtRoomCreateCommand.class);
        assertTrue(result, FALSE);
    }

    @Test
    void validateSuccessTest() {
        courtRoomCreateCommand.setName("A different name");
        courtRoomCreateCommand.setDescription("A Description");
        courtRoomCreateCommand.setXhibitCourtSiteId(1L);

        CourtRoomDto courtRoomDto = new CourtRoomDto();
        courtRoomDto.setCourtRoomName(A_NAME);
        List<CourtRoomDto> courtRoomDtoList = List.of(courtRoomDto);
        final BindingResult errors = new BeanPropertyBindingResult(courtRoomCreateCommand, COURT_ROOM_CREATE_VALIDATOR);

        expect(mockCourtRoomService.getCourtRooms(eq(1L))).andReturn(courtRoomDtoList);
        replay(mockCourtRoomService);

        classUnderTest.validate(courtRoomCreateCommand, errors, mockCourtRoomService);

        assertFalse(errors.hasErrors(), "Not false");
        verify(mockCourtRoomService);
    }

    @Test
    void validateCourtRoomNameTest() {
        courtRoomCreateCommand.setName(null);
        final BindingResult errors = new BeanPropertyBindingResult(courtRoomCreateCommand, COURT_ROOM_CREATE_VALIDATOR);

        classUnderTest.validate(courtRoomCreateCommand, errors, mockCourtRoomService);

        assertEquals("courtRoomAmendCommand.name.notBlank", errors.getGlobalErrors().get(0).getCode(), NOT_EQUAL);
    }

    @Test
    void validateCourtRoomDescriptionTest() {
        courtRoomCreateCommand.setName(A_NAME);
        courtRoomCreateCommand.setDescription(null);
        final BindingResult errors = new BeanPropertyBindingResult(courtRoomCreateCommand, COURT_ROOM_CREATE_VALIDATOR);

        classUnderTest.validate(courtRoomCreateCommand, errors, mockCourtRoomService);

        assertEquals("courtRoomAmendCommand.description.notBlank", errors.getGlobalErrors().get(0).getCode(),
                NOT_EQUAL);
    }

    @Test
    void validateCourtSiteIdTest() {
        courtRoomCreateCommand.setName(A_NAME);
        courtRoomCreateCommand.setDescription("A Description");
        courtRoomCreateCommand.setXhibitCourtSiteId(null);
        final BindingResult errors = new BeanPropertyBindingResult(courtRoomCreateCommand, COURT_ROOM_CREATE_VALIDATOR);

        classUnderTest.validate(courtRoomCreateCommand, errors, mockCourtRoomService);

        assertEquals("courtRoomAmendCommand.xhibitCourtSiteId.notNull", errors.getGlobalErrors().get(0).getCode(),
                NOT_EQUAL);
    }

    @Test
    void validateCourtRoomAlreadyExistsTest() {
        courtRoomCreateCommand.setName(A_NAME);
        courtRoomCreateCommand.setDescription("A Description");
        courtRoomCreateCommand.setXhibitCourtSiteId(1L);

        CourtRoomDto courtRoomDto = new CourtRoomDto();
        courtRoomDto.setCourtRoomName(A_NAME);
        List<CourtRoomDto> courtRoomDtoList = List.of(courtRoomDto);
        final BindingResult errors = new BeanPropertyBindingResult(courtRoomCreateCommand, COURT_ROOM_CREATE_VALIDATOR);

        expect(mockCourtRoomService.getCourtRooms(eq(1L))).andReturn(courtRoomDtoList);
        expect(mockMessageSource.getMessage("courtRoomCreateCommand.name.exists", null,
                Locale.getDefault())).andReturn("Name " + "already exists.");
        replay(mockCourtRoomService);
        replay(mockMessageSource);


        classUnderTest.validate(courtRoomCreateCommand, errors, mockCourtRoomService);

        assertEquals("Name already exists.", errors.getAllErrors().get(0).getDefaultMessage(), NOT_EQUAL);
        verify(mockCourtRoomService);
        verify(mockMessageSource);
    }

}