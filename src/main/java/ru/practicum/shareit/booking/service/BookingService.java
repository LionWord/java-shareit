package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingDto createBookingRequest(Booking booking, int bookerId);

    BookingDto getBookingInformation(int userId, int bookingId);

    List<BookingDto> getAllUserBookings(int userId, String state);

    List<BookingDto> getAllOwnerBookings(int userId, String state);

    BookingDto changeBookingApprovalStatus(int bookerId, int bookingId, boolean approvalStatus);
}
