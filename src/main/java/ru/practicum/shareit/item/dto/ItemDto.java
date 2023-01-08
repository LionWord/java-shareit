package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(access = AccessLevel.PUBLIC)
public class ItemDto {

    private String name;

    private String description;

    private Boolean available;

}
