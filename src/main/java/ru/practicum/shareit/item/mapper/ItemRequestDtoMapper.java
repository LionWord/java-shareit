package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.model.Item;

public class ItemRequestDtoMapper {

    public static ItemWithRequestDto mapToDto(Item item, int requestId) {
        return ItemWithRequestDto.childBuilder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .build();
    }
}
