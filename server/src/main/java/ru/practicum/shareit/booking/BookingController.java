package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

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
    public BookingDto approveBookingRequest(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                            @PathVariable int bookingId,
                                            @RequestParam(name = "approved") boolean approved) {
        return bookingService.changeBookingApprovalStatus(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInformation(@RequestHeader("X-Sharer-User-Id") int userId,
                                            @PathVariable int bookingId) {

        return bookingService.getBookingInformation(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                               @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
                                               @RequestParam(name = "from", required = false) Integer from,
                                               @RequestParam(name = "size", required = false) Integer size) {
        return bookingService.getAllUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
                                                @RequestParam(name = "from", required = false) Integer from,
                                                @RequestParam(name = "size", required = false) Integer size) {
        return bookingService.getAllOwnerBookings(userId, state, from, size);
    }

}
