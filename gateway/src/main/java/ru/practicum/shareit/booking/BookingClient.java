package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.Booking;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.exceptions.Messages;
import ru.practicum.shareit.booking.exceptions.UnsupportedStatusException;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBookingRequest(int userId, Booking booking) {
        return post("", userId, booking);
    }

    public ResponseEntity<Object> changeBookingApprovalStatus(int ownerId, int bookingId, boolean approved) {
        Map<String, Object> params = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", (long)ownerId, params, new Booking());
    }

    public ResponseEntity<Object> getBookingInformation(int userId, int bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllUserBookings(int userId, String state, Integer from, Integer size) {
        checkStateValue(state);
        Map<String, Object> params;
        if (from == null | size == null) {
            return get("?state={state}", (long) userId, Map.of("state", state));
        }
        params = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", (long) userId, params);
    }

    public ResponseEntity<Object> getAllOwnerBookings(int userId, String state, Integer from, Integer size) {
        checkStateValue(state);
        Map<String, Object> params;
        if (from == null | size == null) {
            return get("/owner?state={state}", (long) userId, Map.of("state", state));
        }
        params = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", (long) userId, params);
    }

    private void checkStateValue(String state) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException(Messages.UNKNOWN_STATE + state);
        }
    }



}
