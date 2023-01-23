package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidatorsTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingService bookingService;
    @Mock
    private RequestRepository requestRepository;

    @Test
    void returnItemIfValid_doThrowException_WhenNotFound() {
        int itemId = 0;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(NoSuchItemException.class, () -> Validators.returnItemIfValid(itemId, itemRepository));
    }

    @Test
    void returnItemIfValid_doReturnValidItem() {
        int itemId = 0;
        Item item = new Item();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(new Item()));
        assertEquals(item, Validators.returnItemIfValid(itemId, itemRepository));
    }

    @Test
    void checkIfItemAvailable_doThrowException_whenNot() {
        Item item = new Item();
        item.setAvailable(false);
        assertThrows(NotAvailableException.class, () -> Validators.checkIfItemAvailable(item));
    }

    @Test
    void checkBookingOwnItem_throwException_ifOwnerBookingOwnItem() {
        int ownerId = 1;
        int userId = 1;
        assertThrows(BookingSelfOwnedItemException.class, () -> Validators.checkBookingOwnItem(ownerId, userId));
    }

    @Test
    void userPresenceValidator_throwException_ifUserNotFound() {
        int userId = 0;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> Validators.userPresenceValidator(userId, userRepository));
    }

    @Test
    void returnBookingIfPresent_throwException_ifBookingNotFound() {
        int bookingId = 0;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());
        assertThrows(NoSuchBookingException.class, () -> Validators.returnBookingIfPresent(bookingId, bookingRepository));
    }

    @Test
    void returnBookingIfPresent_doReturnValidBooking() {
        int bookingId = 0;
        Booking booking = new Booking();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(new Booking()));
        assertEquals(booking, Validators.returnBookingIfPresent(bookingId, bookingRepository));
    }

    @Test
    void checkBookingDates_throwException_ifStartIsBeforeNow() {
        Booking booking = new Booking();
        LocalDateTime start = LocalDateTime.of(2022, Month.JANUARY, 10, 10,0);
        LocalDateTime end = LocalDateTime.of(2022, Month.JANUARY, 11, 10,0);
        booking.setStart(start);
        booking.setEnd(end);
        assertThrows(WrongTimestampException.class, () -> Validators.checkBookingDates(booking));
    }

    @Test
    void checkBookingDates_throwException_ifEndIsBeforeStart() {
        Booking booking = new Booking();
        LocalDateTime start = LocalDateTime.of(2122, Month.JANUARY, 12, 10,0);
        LocalDateTime end = LocalDateTime.of(2122, Month.JANUARY, 11, 10,0);
        booking.setStart(start);
        booking.setEnd(end);
        assertThrows(WrongTimestampException.class, () -> Validators.checkBookingDates(booking));
    }

    @Test
    void checkStateValue_throwExceptionIfStateIsInvalid() {
        String invalidState = "YAHOO";
        assertThrows(UnsupportedStatusException.class, () -> Validators.checkStateValue(invalidState));
    }

    @Test
    void checkBookingApprovedAlready_doThrowException_ifApproved() {
        BookingDto booking = new BookingDto();
        booking.setStatus(Status.APPROVED);
        assertThrows(AlreadyApprovedException.class, () -> Validators.checkBookingApprovedAlready(booking));
    }

    @Test
    void checkIfOwnerOrBooker_throwException_ifNotOwnerOrBookerOfItem() {
        int userId = 0;
        int bookerId = 1;
        int ownerId = 2;
        assertThrows(NoSuchItemException.class, () -> Validators.checkIfOwnerOrBooker(userId, bookerId, ownerId));
    }

    @Test
    void checkIfUserOwnItem_throwException_ifNotOwner() {
        int userId = 0;
        int ownerId = 1;
        assertThrows(NoSuchItemException.class, () -> Validators.checkIfUserOwnItem(userId, ownerId));
    }

    @Test
    void checkIfCanPostComments_throwException_ifItemWasNotUsed_BookingIsNull() {
        int userId = 0;
        int itemId = 0;
        when(bookingService.getAllUserBookings(userId, State.ALL.name())).thenReturn(List.of());
        assertThrows(CantCommentException.class, () -> Validators.checkIfCanPostComments(userId, itemId, bookingService));
    }

    @Test
    void checkIfCanPostComments_throwException_ifItemWasNotUsed_StartIsAfterNow() {
        int userId = 0;
        int itemId = 0;
        ItemBookingDto item = new ItemBookingDto(0, "name");
        LocalDateTime start = LocalDateTime.of(2122, Month.JANUARY, 12, 10,0);
        BookingDto booking = new BookingDto();
        booking.setItem(item);
        booking.setStart(start);
        List<BookingDto> bookings = new ArrayList<>();
        bookings.add(booking);
        when(bookingService.getAllUserBookings(userId, State.ALL.name())).thenReturn(bookings);
        assertThrows(CantCommentException.class, () -> Validators.checkIfCanPostComments(userId, itemId, bookingService));
    }

    @Test
    void checkIfRequestIsNotEmpty_throwException_ifDescriptionIsEmpty() {
        Request request = new Request();
        assertThrows(EmptyRequestException.class, () -> Validators.checkIfRequestIsNotEmpty(request));
    }

    @Test
    void returnRequestIfPresent_throwException_ifRequestIsNotPresent() {
        int requestId = 0;
        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());
        assertThrows(NoSuchRequestException.class, () -> Validators.returnRequestIfPresent(requestId, requestRepository));
    }

    @Test
    void returnRequestIfPresent_doReturnPresentRequest() {
        int requestId = 0;
        Request request = new Request();
        when(requestRepository.findById(requestId)).thenReturn(Optional.of(new Request()));
        assertEquals(request, Validators.returnRequestIfPresent(requestId, requestRepository));
    }

    @Test
    void checkPagination_throwException_ifFromIs0_andSizeIs0() {
        int from = 0;
        int size = 0;
        assertThrows(InvalidPaginationException.class, () -> Validators.checkPagination(from, size));
    }

    @Test
    void checkPagination_throwException_ifFromIsLessThan0() {
        int from = -1;
        int size = 0;
        assertThrows(InvalidPaginationException.class, () -> Validators.checkPagination(from, size));
    }

    @Test
    void checkPagination_throwException_ifSizeIs0() {
        int from = 1;
        int size = 0;
        assertThrows(InvalidPaginationException.class, () -> Validators.checkPagination(from, size));
    }
}