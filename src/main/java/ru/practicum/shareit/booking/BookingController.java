package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.service.BookingService;

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

    @PostMapping
    public BookingDto createBookingRequest(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody Booking booking) {
        return bookingService.createBookingRequest(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking approveBookingRequest(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                         @PathVariable int bookingId,
                                         @RequestParam(name = "approved") boolean approved) {
        if (approved) {
            return bookingService.approveBookingRequest(bookingId);
        } else {
            return bookingService.rejectBookingRequest(bookingId);
        }

    }

    @GetMapping("/{bookingId}")
    public Optional<Booking> getBookingInformation(@RequestHeader("X-Sharer-User-Id") int userId,
                                                      @PathVariable int bookingId) {
        return bookingService.getBookingInformation(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllUserBookings (@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam(name = "state") State state) {
        return bookingService.getAllUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam(name = "state") State state) {
        return bookingService.getAllOwnerBookings(userId, state);
    }

}
