package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ShareItServer.class
)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Sql({"/drop_schema.sql", "/schema.sql", "/test_data.sql"})
class BookingServiceImplTest {

    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void createBookingRequest_shouldReturnDto_withProperNameAndDates() {
        int userId = 2;
        int itemId = 1;
        Booking booking = new Booking();
        booking.setBookerId(userId);
        booking.setItemId(itemId);
        LocalDateTime start = LocalDateTime.of(3000, Month.JANUARY, 12, 12, 0);
        LocalDateTime end = LocalDateTime.of(3000, Month.JANUARY, 13, 12, 0);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(Status.WAITING);
        BookingDto result = bookingService.createBookingRequest(booking, userId);
        assertEquals("drill", result.getItem().getName());
        assertEquals(Status.WAITING, result.getStatus());
        assertEquals(end, result.getEnd());
        assertEquals(start, result.getStart());
    }

    @Test
    void changeBookingApprovalStatus_shouldChangeToApproved() {
        int userId = 1;
        int bookingId = 1;
        bookingService.changeBookingApprovalStatus(userId, bookingId, true);
        assertEquals(Status.APPROVED, bookingRepository.findById(bookingId).get().getStatus());
    }

    @Test
    void changeBookingApprovalStatus_shouldChangeToRejected() {
        int userId = 1;
        int bookingId = 1;
        bookingService.changeBookingApprovalStatus(userId, bookingId, false);
        assertEquals(Status.REJECTED, bookingRepository.findById(bookingId).get().getStatus());
    }

    @Test
    void getBookingInformation_shouldReturnProperInformation() {
        int bookingId = 1;
        int userId = 1;
        BookingDto result = bookingService.getBookingInformation(userId, bookingId);
        assertEquals(Status.WAITING, result.getStatus());
        assertEquals("drill", result.getItem().getName());
        assertEquals(1, result.getItem().getId());
        assertEquals(1, result.getId());
    }

    @Test
    void getAllUserBookings_shouldReturnListWithOneValue_shouldBeProperBookerId() {
        int userId = 2;
        List<BookingDto> list = bookingService.getAllUserBookings(userId, State.ALL.name());
        BookingDto result = list.get(0);
        assertEquals(userId, result.getBooker().getId());
    }

    @Test
    void testGetAllUserBookings_shouldReturnNoBookings_forUserWithoutBookings() {
        int userId = 1;
        List<BookingDto> list = bookingService.getAllUserBookings(userId, State.ALL.name());
        assertEquals(0, list.size());
    }

    @Test
    void getAllUserBookings_approved_shouldReturnListWithOneValue() {
        int userOwnerId = 1;
        int userBookerId = 2;
        int bookingId = 1;
        bookingService.changeBookingApprovalStatus(userOwnerId, bookingId, true);
        List<BookingDto> list = bookingService.getAllUserBookings(userBookerId, State.ALL.name());
        BookingDto result = list.get(0);
        assertEquals(userBookerId, result.getBooker().getId());
        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    void getAllUserBookings_rejected_shouldReturnListWithOneValue() {
        int userOwnerId = 1;
        int userBookerId = 2;
        int bookingId = 1;
        bookingService.changeBookingApprovalStatus(userOwnerId, bookingId, false);
        List<BookingDto> list = bookingService.getAllUserBookings(userBookerId, State.REJECTED.name());
        BookingDto result = list.get(0);
        assertEquals(userBookerId, result.getBooker().getId());
        assertEquals(Status.REJECTED, result.getStatus());
    }

    @Test
    void getAllUserBookings_current_shouldReturnListWithOneValue() {
        addNewBooking();
        int userBookerId = 2;
        List<BookingDto> list = bookingService.getAllUserBookings(userBookerId, State.CURRENT.name());
        BookingDto result = list.get(0);
        assertEquals(userBookerId, result.getBooker().getId());
        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    void getAllUserBookings_pastBooking_shouldReturnListWithOneValue() {
        int userOwnerId = 1;
        int userBookerId = 2;
        int bookingId = 1;
        bookingService.changeBookingApprovalStatus(userOwnerId, bookingId, true);
        List<BookingDto> list = bookingService.getAllUserBookings(userBookerId, State.PAST.name());
        BookingDto result = list.get(0);
        assertEquals(userBookerId, result.getBooker().getId());
        assertEquals(Status.APPROVED, result.getStatus());
    }

    @Test
    void getAllUserBookings_waiting_shouldReturnListWithOneValue() {
        int userBookerId = 2;
        List<BookingDto> list = bookingService.getAllUserBookings(userBookerId, State.WAITING.name());
        BookingDto result = list.get(0);
        assertEquals(userBookerId, result.getBooker().getId());
        assertEquals(Status.WAITING, result.getStatus());
    }

    @Test
    void getAllUserBookings_getWithPagination_firstBookingIdShouldBe2_andListLengthShouldBe1() {
        addNewBooking();
        int userBookerId = 2;
        int expectedId = 2;
        int expectedListLength = 1;
        List<BookingDto> list = bookingService.getAllUserBookings(userBookerId, State.ALL.name(), 0, 1);
        BookingDto result = list.get(0);
        assertEquals(expectedId, result.getId());
        assertEquals(expectedListLength, list.size());
    }

    @Test
    void getAllOwnerBookings_shouldReturnListWithOneValue() {
        int ownerId = 1;
        List<BookingDto> bookings = bookingService.getAllOwnerBookings(ownerId, State.ALL.name());
        assertEquals(1, bookings.size());
    }

    @Test
    void getAllOwnerBookings_shouldReturnEmptyList_notOwner() {
        int ownerId = 2;
        List<BookingDto> bookings = bookingService.getAllOwnerBookings(ownerId, State.ALL.name());
        assertEquals(0, bookings.size());
    }

    @Test
    void getAllOwnerBookings_getWithPagination_firstBookingIdShouldBe2_andListLengthShouldBe1() {
        addNewBooking();
        int ownerId = 1;
        int expectedId = 2;
        int expectedListLength = 1;
        List<BookingDto> bookings = bookingService.getAllOwnerBookings(ownerId, State.ALL.name(), 0, 1);
        BookingDto result = bookings.get(0);
        assertEquals(expectedId, result.getId());
        assertEquals(expectedListLength, bookings.size());
        assertEquals(1, bookings.size());
    }

    private void addNewBooking() {
        Booking currentBooking = new Booking();
        currentBooking.setItemId(1);
        currentBooking.setBookerId(2);
        currentBooking.setStart(LocalDateTime.of(2023, Month.JANUARY, 1, 1, 0, 0));
        currentBooking.setEnd(LocalDateTime.of(3023, Month.JANUARY, 1, 1, 0, 0));
        currentBooking.setStatus(Status.APPROVED);
        bookingRepository.save(currentBooking);
    }

}