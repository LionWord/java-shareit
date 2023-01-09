package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBookingRequest(Booking booking, int bookerId);
    Booking approveBookingRequest(int bookingId);
    Booking rejectBookingRequest(int bookingId);
    Optional<Booking> getBookingInformation(int bookingId, int requesterId);
    List<Booking> getAllUserBookings(int userId, State state);
    List<Booking> getAllOwnerBookings(int userId, State state);
}
