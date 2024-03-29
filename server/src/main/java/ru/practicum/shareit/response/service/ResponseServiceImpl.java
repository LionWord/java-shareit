package ru.practicum.shareit.response.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.response.model.Response;
import ru.practicum.shareit.response.repository.ResponseRepository;
import ru.practicum.shareit.utils.Validators;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {
    private final ResponseRepository responseRepository;
    private final RequestRepository requestRepository;

    public Response addResponse(int requestId, int itemId) {
        Validators.returnRequestIfPresent(requestId, requestRepository);
        Response response = Response.builder()
                .requestId(requestId)
                .itemId(itemId)
                .build();
        return responseRepository.save(response);
    }
}
