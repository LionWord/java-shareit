package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RequestService {

    RequestDto addRequest(int userId, RequestDto requestDto);

    List<RequestDto> getMyRequests(int userId);

    public RequestDto getRequest(int userId, int requestId);

    public List<RequestDto> getAllRequests(int userId);

}
