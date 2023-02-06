package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapperDpa;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Validators;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    public BookingDto changeBookingApprovalStatus(int userId, int bookingId, boolean isApproved) {
        BookingDto booking = getBookingInformation(userId, bookingId);
        int ownerId = itemRepository.findById(booking.getItem().getId()).get().getOwnerId();
        Validators.checkIfUserOwnItem(userId, ownerId);
        Validators.checkBookingApprovedAlready(booking);
        return isApproved ? approveBookingRequest(bookingId) : rejectBookingRequest(bookingId);
    }

    @Override
    public BookingDto getBookingInformation(int userId, int bookingId) {
        Booking booking = Validators.returnBookingIfPresent(bookingId, bookingRepository);
        BookingDto returnBooking = BookingMapperDpa.make(booking, itemRepository);
        int bookerId = returnBooking.getBooker().getId();
        int ownerId = itemRepository.findById(returnBooking.getItem().getId()).get().getOwnerId();
        Validators.checkIfOwnerOrBooker(userId, bookerId, ownerId);
        return returnBooking;
    }

    @Override
    public List<BookingDto> getAllUserBookings(int userId, String state) {
        Validators.userPresenceValidator(userId, userRepository);
        ArrayList<BookingDto> list = new ArrayList<>();
        bookingRepository.findAll().stream()
                .filter(booking -> booking.getBookerId() == userId)
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .forEach(booking -> list.add(BookingMapperDpa.make(booking, itemRepository)));
        return filterBookingsByState(list, State.valueOf(state));

    }

    @Override
    public List<BookingDto> getAllUserBookings(int userId, String state, Integer from, Integer size) {
        if (from == null & size == null) {
            return getAllUserBookings(userId, state);
        }
        ArrayList<BookingDto> list = new ArrayList<>();
        Page<Booking> page = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, PageRequest.of(from, size));
        while (page.isEmpty()) {
            from -= 1;
            page = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, PageRequest.of(from, size));
        }
        page.stream()
                .forEach(booking -> list.add(BookingMapperDpa.make(booking, itemRepository)));
        return filterBookingsByState(list, State.valueOf(state));
    }

    @Override
    public List<BookingDto> getAllOwnerBookings(int userId, String state) {
        Validators.userPresenceValidator(userId, userRepository);
        ArrayList<BookingDto> list = new ArrayList<>();
        bookingRepository.findAll().stream()
                .filter(booking -> itemRepository.findById(booking.getItemId()).get().getOwnerId() == userId)
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .forEach(booking -> list.add(BookingMapperDpa.make(booking, itemRepository)));
        return filterBookingsByState(list, State.valueOf(state));
    }

    @Override
    public List<BookingDto> getAllOwnerBookings(int userId, String state, Integer from, Integer size) {
        if (from == null & size == null) {
            return getAllOwnerBookings(userId, state);
        }
        ArrayList<BookingDto> list = new ArrayList<>();
        Page<Booking> page = bookingRepository.findAllOwnerBookings(userId, PageRequest.of(from, size));
        while (page.isEmpty() && from >= 0) {
            from -= 1;
            page = bookingRepository.findAllOwnerBookings(userId, PageRequest.of(from, size));
        }
        page.stream()
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

    private void bookingValidationSequence(int userId, int itemId, Booking booking) {
        Item item = Validators.returnItemIfValid(itemId, itemRepository);
        Validators.userPresenceValidator(userId, userRepository);
        Validators.checkIfItemAvailable(item);
        Validators.checkBookingOwnItem(item.getOwnerId(), userId);
        Validators.checkBookingDates(booking);
    }

    private BookingDto approveBookingRequest(int bookingId) {
        Booking booking = Validators.returnBookingIfPresent(bookingId, bookingRepository);
        booking.setStatus(Status.APPROVED);
        bookingRepository.save(booking);
        return BookingMapperDpa.make(booking, itemRepository);
    }

    private BookingDto rejectBookingRequest(int bookingId) {
        Booking booking = Validators.returnBookingIfPresent(bookingId, bookingRepository);
        booking.setStatus(Status.REJECTED);
        bookingRepository.save(booking);
        return BookingMapperDpa.make(booking, itemRepository);
    }

}
