package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.Request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                             @RequestBody @Valid Request request) {
        return requestClient.addRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getMyRequests(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId) {
        return requestClient.getMyRequests(userId);
    }

    @GetMapping(path = "{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                             @PathVariable @NotNull @Positive int requestId) {
        return requestClient.getRequest(userId, requestId);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                                        @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                                        @RequestParam(name = "size", required = false) @Positive Integer size) {
        return requestClient.getAllRequests(userId, from, size);
    }


}
