package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookingServiceImpl {

    private final BookingRepository bookingRepository;

    public Booking createBookingRequest(Booking booking, int bookerId) {
        booking.setBookerId(bookerId);
        return bookingRepository.save(booking);
    }
    public Booking approveBookingRequest(int bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NoSuchBookingException(Messages.NO_SUCH_BOOKING);
        }
        booking.get().setStatus(Status.APPROVED);
        return bookingRepository.save(booking.get());
    }

    public Booking rejectBookingRequest(int bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NoSuchBookingException(Messages.NO_SUCH_BOOKING);
        }
        booking.get().setStatus(Status.REJECTED);
        return bookingRepository.save(booking.get());
    }

    public Optional<Booking> getBookingInformation(int bookingId, int requesterId) {
        return bookingRepository.findById(bookingId);
    }

    public List<Booking> getAllUserBookings(int userId, State state) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getBookerId() == userId)
                .collect(Collectors.toList());
    }

    public List<Booking> getAllOwnerBookings(int userId, State state) {

    }

}
