package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto createBookingRequest(Booking booking, int bookerId);

    BookingDto getBookingInformation(int userId, int bookingId);

    List<BookingDto> getAllUserBookings(int userId, String state);

    List<BookingDto> getAllOwnerBookings(int userId, String state);

    BookingDto changeBookingApprovalStatus(int bookerId, int bookingId, boolean approvalStatus);
}
