package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.Booking;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBookingRequest(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                                       @RequestBody @Valid Booking booking) {
        return bookingClient.createBookingRequest(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    @CachePut(value = "bookings")
    public ResponseEntity<Object> approveBookingRequest(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int ownerId,
                                            @PathVariable @NotNull @Positive int bookingId,
                                            @RequestParam(name = "approved") @NotNull boolean approved) {
        return bookingClient.changeBookingApprovalStatus(ownerId, bookingId, approved);
    }
    @Cacheable(value = "bookings")
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingInformation(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                            @PathVariable @NotNull @Positive int bookingId) {

        return bookingClient.getBookingInformation(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                               @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
                                               @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                               @RequestParam(name = "size", required = false) @Positive Integer size) {
        return bookingClient.getAllUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnerBookings(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                                @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
                                                @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                                @RequestParam(name = "size", required = false) @Positive Integer size) {
        return bookingClient.getAllOwnerBookings(userId, state, from, size);
    }

    @CacheEvict(value = "bookings", allEntries = true)
    @Scheduled(initialDelayString = "${cache.ttl}", fixedDelayString = "${cache.ttl}")
    public void evictAllCache(){}

}
