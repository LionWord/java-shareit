package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapperDpa;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Messages;
import ru.practicum.shareit.utils.Validators;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBookingRequest(Booking booking, int bookerId) {
        bookingValidationSequence(bookerId, booking.getItemId(), booking);
        booking.setBookerId(bookerId);
        booking = bookingRepository.save(booking);
        return BookingMapperDpa.make(booking, itemRepository);
    }

    @Override
    public BookingDto approveBookingRequest(int bookingId) {
        Booking booking = Validators.returnBookingIfPresent(bookingId, bookingRepository);
        booking.setStatus(Status.APPROVED);
        bookingRepository.save(booking);
        return BookingMapperDpa.make(booking, itemRepository);
    }

    @Override
    public BookingDto rejectBookingRequest(int bookingId) {
        Booking booking = Validators.returnBookingIfPresent(bookingId, bookingRepository);
        booking.setStatus(Status.REJECTED);
        bookingRepository.save(booking);
        return BookingMapperDpa.make(booking, itemRepository);
    }

    @Override
    public BookingDto getBookingInformation(int bookingId) {
        Booking booking = Validators.returnBookingIfPresent(bookingId, bookingRepository);
        return BookingMapperDpa.make(booking, itemRepository);
    }

    @Override
    public List<BookingDto> getAllUserBookings(int userId, String state) {
        Validators.userPresenceValidator(userId, userRepository);
        Validators.checkStateValue(state);
        ArrayList<BookingDto> list = new ArrayList<>();
        bookingRepository.findAll().stream()
                .filter(booking -> booking.getBookerId() == userId)
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .forEach(booking -> list.add(BookingMapperDpa.make(booking, itemRepository)));
        return filterBookingsByState(list, State.valueOf(state));

    }

    @Override
    public List<BookingDto> getAllOwnerBookings(int userId, String state) {
        Validators.userPresenceValidator(userId, userRepository);
        Validators.checkStateValue(state);
        ArrayList<BookingDto> list = new ArrayList<>();
        bookingRepository.findAll().stream()
                .filter(bookingDto -> itemRepository.findById(bookingDto.getItemId()).get().getOwnerId() == userId)
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .forEach(booking -> list.add(BookingMapperDpa.make(booking, itemRepository)));
        return filterBookingsByState(list, State.valueOf(state));
    }

    //----------Service methods-----------

    private List<BookingDto> filterBookingsByState(List<BookingDto> list, State state) {
        switch (state) {
            case WAITING:
                return list.stream()
                        .filter(bookingDto -> bookingDto.getStatus().equals(Status.WAITING))
                        .collect(Collectors.toList());
            case REJECTED:
                return list.stream()
                        .filter(bookingDto -> bookingDto.getStatus().equals(Status.REJECTED))
                        .collect(Collectors.toList());
            case PAST:
                return list.stream()
                        .filter(bookingDto -> bookingDto.getEnd().isBefore(Timestamp.from(Instant.now()).toLocalDateTime()))
                        .collect(Collectors.toList());
            case CURRENT:
                return list.stream()
                        .filter(bookingDto -> bookingDto.getStart().isBefore(Timestamp.from(Instant.now()).toLocalDateTime()))
                        .filter(bookingDto -> bookingDto.getEnd().isAfter(Timestamp.from(Instant.now()).toLocalDateTime()))
                        .collect(Collectors.toList());
            default:
                return list;
        }
    }

    private Item returnItemIfValid(int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.orElseThrow(() -> new NoSuchItemException(Messages.NO_SUCH_ITEM));
    }

    private void checkIfAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new NotAvailableException(Messages.NOT_AVAILABLE);
        }
    }

    private void checkIfNotOwner(int ownerId, int userId) {
        if (ownerId == userId) {
            throw new BookingSelfOwnedItemException(Messages.SELF_OWNED_ITEM);
        }
    }

    private void bookingValidationSequence(int userId, int itemId, Booking booking) {
        Item item = Validators.returnItemIfValid(itemId, itemRepository);
        Validators.userPresenceValidator(userId, userRepository);
        Validators.checkIfItemAvailable(item);
        Validators.checkIfUserIsOwner(item.getOwnerId(), userId);
        Validators.checkBookingDates(booking);
    }

    private void userPresenceValidator(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(Messages.NO_SUCH_USER));
    }

    private Booking returnBookingIfPresent(int bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchBookingException(Messages.NO_SUCH_BOOKING));
    }

    private void timestampIsCorrect(Booking booking) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        LocalDateTime now = Timestamp.from(Instant.now()).toLocalDateTime();
        if (end.isBefore(start) || start.isBefore(now) || start.isAfter(end)) {
            throw new WrongTimestampException(Messages.WRONG_TIMESTAMP);
        }
    }

}
