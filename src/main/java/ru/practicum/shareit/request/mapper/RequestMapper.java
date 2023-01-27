package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.request.dto.RequestWithResponsesDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public class RequestMapper {

    public static RequestWithResponsesDto mapToDto(Request request, List<ItemWithRequestDto> items) {
        return RequestWithResponsesDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(items)
                .build();
    }
}
