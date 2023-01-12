package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Messages;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public BookingDto createBookingRequest(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody Booking booking) {
        return bookingService.createBookingRequest(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBookingRequest(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                            @PathVariable int bookingId,
                                            @RequestParam(name = "approved") boolean approved) {
        int itemId = bookingService.getBookingInformation(bookingId).getItem().getId();
        if (itemService.getItem(itemId).get().getOwnerId() != ownerId) {
            throw new NoSuchItemException(Messages.NO_SUCH_ITEM);
        }
        if (bookingService.getBookingInformation(bookingId).getStatus().equals(Status.APPROVED)) {
            throw new AlreadyApprovedException(Messages.ALREADY_APPROVED);
        }

        if (approved) {
            return bookingService.approveBookingRequest(bookingId);
        } else {
            return bookingService.rejectBookingRequest(bookingId);
        }

    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInformation(@RequestHeader("X-Sharer-User-Id") int userId,
                                                      @PathVariable int bookingId) {

        BookingDto bookingDto = bookingService.getBookingInformation(bookingId);
        int bookerId = bookingDto.getBooker().getId();
        int ownerId = itemService.getItem(bookingDto.getItem().getId()).get().getOwnerId();
        if (userId != bookerId & userId != ownerId) {
            throw new NoSuchItemException(Messages.NO_SUCH_ITEM);
        }
        return bookingDto;
    }

    @GetMapping
    public List<BookingDto> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                               @RequestParam(name = "state", defaultValue = "ALL", required = false) String state) {
        if (userService.getUser(userId).isEmpty()) {
            throw new NoSuchUserException(Messages.NO_SUCH_USER);
        }

        if (!stateIsValid(state)) {
            throw new UnsupportedStatusException(Messages.UNKNOWN_STATE + state);
        }

        return bookingService.getAllUserBookings(userId, State.valueOf(state));
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(name = "state", defaultValue = "ALL", required = false) String state) {
        if (userService.getUser(userId).isEmpty()) {
            throw new NoSuchUserException(Messages.NO_SUCH_USER);
        }

        if (!stateIsValid(state)) {
            throw new UnsupportedStatusException(Messages.UNKNOWN_STATE + state);
        }

        return bookingService.getAllOwnerBookings(userId, State.valueOf(state));
    }

    private boolean stateIsValid(String state) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}
