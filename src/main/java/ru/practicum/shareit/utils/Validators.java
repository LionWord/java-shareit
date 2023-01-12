package ru.practicum.shareit.utils;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

public class Validators {

    public static Item returnItemIfValid(int itemId, ItemRepository itemRepository) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.orElseThrow(() -> new NoSuchItemException(Messages.NO_SUCH_ITEM));
    }

    public static void checkIfItemAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new NotAvailableException(Messages.NOT_AVAILABLE);
        }
    }

    public static void checkBookingOwnItem(int ownerId, int userId) {
        if (ownerId == userId) {
            throw new BookingSelfOwnedItemException(Messages.SELF_OWNED_ITEM);
        }
    }

    public static void userPresenceValidator(int userId, UserRepository userRepository) {
        userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(Messages.NO_SUCH_USER));
    }

    public static Booking returnBookingIfPresent(int bookingId, BookingRepository bookingRepository) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchBookingException(Messages.NO_SUCH_BOOKING));
    }

    public static void checkBookingDates(Booking booking) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        LocalDateTime now = Timestamp.from(Instant.now()).toLocalDateTime();
        if (end.isBefore(start) || start.isBefore(now) || start.isAfter(end)) {
            throw new WrongTimestampException(Messages.WRONG_TIMESTAMP);
        }
    }

    public static void checkStateValue(String state) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException(Messages.UNKNOWN_STATE + state);
        }
    }

    public static void checkBookingApprovedAlready(BookingDto booking) {
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new AlreadyApprovedException(Messages.ALREADY_APPROVED);
        }
    }

    public static void checkIfOwnerOrBooker(int userId, int bookerId, int ownerId) {
        if (userId != bookerId & userId != ownerId) {
            throw new NoSuchItemException(Messages.NO_SUCH_ITEM);
        }
    }
    public static void checkIfUserOwnItem(int userId, int ownerId) {
        if (ownerId != userId) {
            throw new NoSuchItemException(Messages.NO_SUCH_ITEM);
        }
    }

    public static void checkIfCanPostComments(int userId, int itemId, BookingService bookingService) {
        BookingDto bookingDto = bookingService.getAllUserBookings(userId, State.ALL.name()).stream()
                .filter(bookingDto1 -> bookingDto1.getItem().getId() == itemId)
                .min(Comparator.comparing(BookingDto::getStart)).orElse(null);

        if (bookingDto == null) {
            throw new CantCommentException(Messages.ITEM_WAS_NOT_USED);
        }
        if (bookingDto.getStart().isAfter(Timestamp.from(Instant.now()).toLocalDateTime()) ) {
            throw new CantCommentException(Messages.ITEM_WAS_NOT_USED);
        };
    }

}
