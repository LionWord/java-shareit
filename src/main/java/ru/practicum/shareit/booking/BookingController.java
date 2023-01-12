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
import ru.practicum.shareit.utils.Validators;

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
         return bookingService.changeBookingApprovalStatus(ownerId, bookingId, approved);
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
        return bookingService.getAllUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(name = "state", defaultValue = "ALL", required = false) String state) {
        return bookingService.getAllOwnerBookings(userId, state);
    }

}
