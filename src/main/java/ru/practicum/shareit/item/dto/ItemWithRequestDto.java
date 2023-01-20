package ru.practicum.shareit.item.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class ItemWithRequestDto extends ItemDto{
    private int id;
    private int requestId;

    @Builder
    ItemWithRequestDto(String name, String description, Boolean available, int requestId, int id) {
        super(name, description, available);
        this.requestId = requestId;
        this.id = id;
    }
}
