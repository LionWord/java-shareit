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
    public RequestDto addRequest(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody RequestDto requestDto) {
        //stub
        return null;
    }

    @GetMapping
    public List<RequestDto> getMyRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        //stub
        return null;
    }

    @GetMapping(path = "{requestId}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int requestId) {
        //stub
        return null;
    }

    @GetMapping(path = "/all")
    public List<RequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        //stub
        return null;
    }


}
