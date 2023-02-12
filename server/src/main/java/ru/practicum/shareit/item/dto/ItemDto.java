package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class ItemDto {

    private String name;

    private String description;

    private Boolean available;

    private Integer requestId;
}
