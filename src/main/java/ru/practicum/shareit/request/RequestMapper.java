package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemWithRequestDto;

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
