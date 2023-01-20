package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RequestService {

    Request addRequest(int userId, Request requestDto);

    List<RequestDto> getMyRequests(int userId);

    public RequestDto getRequest(int userId, int requestId);

    public List<RequestDto> getAllRequests(int userId);

}
