package ru.practicum.shareit.booking.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NoSuchBookingException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.utils.Messages;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto createBookingRequest(Booking booking, int bookerId) {
        booking.setBookerId(bookerId);
        bookingRepository.save(booking);
        BookingDto bookingDto = bookingRepository.dtoFromBooking(booking);
        bookingDto.setItemName(itemRepository.findById(booking.getItemId()).get().getName());
        return bookingDto;
    }
    @Override
    public Booking approveBookingRequest(int bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NoSuchBookingException(Messages.NO_SUCH_BOOKING);
        }
        booking.get().setStatus(Status.APPROVED);
        return bookingRepository.save(booking.get());
    }
    @Override
    public Booking rejectBookingRequest(int bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NoSuchBookingException(Messages.NO_SUCH_BOOKING);
        }
        booking.get().setStatus(Status.REJECTED);
        return bookingRepository.save(booking.get());
    }
    @Override
    public Optional<Booking> getBookingInformation(int bookingId, int requesterId) {
        return bookingRepository.findById(bookingId);
    }
    @Override
    public List<Booking> getAllUserBookings(int userId, State state) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getBookerId() == userId)
                .collect(Collectors.toList());
    }
    @Override
    public List<Booking> getAllOwnerBookings(int userId, State state) {
        return bookingRepository.findAll().stream()
                .filter(bookingDto -> itemRepository.findById(bookingDto.getItemId()).get().getOwnerId() == userId)
                .collect(Collectors.toList());
    }

}
