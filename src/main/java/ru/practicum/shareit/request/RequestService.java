package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {

    Request addRequest(int userId, Request requestDto);

    List<RequestWithResponsesDto> getMyRequests(int userId);

    RequestWithResponsesDto getRequest(int userId, int requestId);

    List<RequestWithResponsesDto> getAllRequests(int userId, int from, int size);

}
