package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public Request addRequest(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody Request request) {
        return requestService.addRequest(userId, request);
    }

    @GetMapping
    public List<RequestWithResponsesDto> getMyRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return requestService.getMyRequests(userId);
    }

    @GetMapping(path = "{requestId}")
    public RequestWithResponsesDto getRequest(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int requestId) {
        return requestService.getRequest(userId, requestId);
    }

    @GetMapping(path = "/all")
    public List<RequestWithResponsesDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @RequestParam(name = "from", required = false) Integer from,
                                                        @RequestParam(name = "size", required = false) Integer size) {
        return requestService.getAllRequests(userId, from, size);
    }


}
