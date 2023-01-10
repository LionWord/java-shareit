package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapperDpa;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NoSuchBookingException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.utils.Messages;

import java.util.ArrayList;
import java.util.Comparator;
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
        booking = bookingRepository.save(booking);
        BookingDto bookingDto = BookingMapperDpa.make(booking, itemRepository);
        return bookingDto;
    }
    @Override
    public BookingDto approveBookingRequest(int bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NoSuchBookingException(Messages.NO_SUCH_BOOKING);
        }
        booking.get().setStatus(Status.APPROVED);
        bookingRepository.save(booking.get());
        return BookingMapperDpa.make(booking.get(), itemRepository);
    }
    @Override
    public BookingDto rejectBookingRequest(int bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NoSuchBookingException(Messages.NO_SUCH_BOOKING);
        }
        booking.get().setStatus(Status.REJECTED);
        bookingRepository.save(booking.get());
        return BookingMapperDpa.make(booking.get(), itemRepository);
    }
    @Override
    public Optional<BookingDto> getBookingInformation(int bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.map(value -> BookingMapperDpa.make(value, itemRepository));
    }
    @Override
    public List<BookingDto> getAllUserBookings(int userId, State state) {
        ArrayList<BookingDto> result = new ArrayList<>();
        bookingRepository.findAll().stream()
                .filter(booking -> booking.getBookerId() == userId)
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .forEach(booking -> result.add(BookingMapperDpa.make(booking, itemRepository)));
          if (state.equals(State.WAITING)) {
            return result.stream()
                    .filter(bookingDto -> bookingDto.getStatus().equals(Status.WAITING))
                    .collect(Collectors.toList());
        } else if (state.equals(State.REJECTED)) {
            return result.stream()
                    .filter(bookingDto -> bookingDto.getStatus().equals(Status.REJECTED))
                    .collect(Collectors.toList());
        }
          return result;

    }
    @Override
    public List<BookingDto> getAllOwnerBookings(int userId, State state) {
        ArrayList<BookingDto> result = new ArrayList<>();
        bookingRepository.findAll().stream()
                .filter(bookingDto -> itemRepository.findById(bookingDto.getItemId()).get().getOwnerId() == userId)
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .forEach(booking -> result.add(BookingMapperDpa.make(booking, itemRepository)));
        if (state.equals(State.WAITING)) {
            return result.stream()
                    .filter(bookingDto -> bookingDto.getStatus().equals(Status.WAITING))
                    .collect(Collectors.toList());
        } else if (state.equals(State.REJECTED)) {
            return result.stream()
                    .filter(bookingDto -> bookingDto.getStatus().equals(Status.REJECTED))
                    .collect(Collectors.toList());
        }
        return result;
    }

}
