package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestWithResponsesDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {

    Request addRequest(int userId, Request requestDto);

    List<RequestWithResponsesDto> getMyRequests(int userId);

    RequestWithResponsesDto getRequest(int userId, int requestId);

    List<RequestWithResponsesDto> getAllRequests(int userId, Integer from, Integer size);

}
