package ru.practicum.shareit.response.service;

import ru.practicum.shareit.response.model.Response;

public interface ResponseService {
    Response addResponse(int requestId, int itemId);
}
