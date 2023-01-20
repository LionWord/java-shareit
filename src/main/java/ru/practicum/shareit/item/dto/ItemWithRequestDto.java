package ru.practicum.shareit.item.dto;

import lombok.*;

public class ItemWithRequestDto extends ItemDto{
    private int id;

    @Builder(access = AccessLevel.PUBLIC, builderMethodName = "childBuilder")
    ItemWithRequestDto(String name, String description, Boolean available, int requestId, int id) {
        super(name, description, available, requestId);
        this.id = id;
    }
}
