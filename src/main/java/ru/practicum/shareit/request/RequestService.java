package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {

    Request addRequest(int userId, Request requestDto);

    List<RequestWithResponsesDto> getMyRequests(int userId);

    public RequestWithResponsesDto getRequest(int userId, int requestId);

    public List<RequestWithResponsesDto> getAllRequests(int userId);

}
