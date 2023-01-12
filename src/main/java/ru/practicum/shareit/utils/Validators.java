package ru.practicum.shareit.utils;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Validators {
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

    public static Item returnItemIfValid(int itemId, ItemRepository itemRepository) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.orElseThrow(() -> new NoSuchItemException(Messages.NO_SUCH_ITEM));
    }

    public static void checkIfItemAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new NotAvailableException(Messages.NOT_AVAILABLE);
        }
    }

    public static void checkIfUserIsOwner(int ownerId, int userId) {
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

}
