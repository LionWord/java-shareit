package ru.practicum.shareit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemValidator {

    public static boolean isValidItem(ItemDto itemDto) {
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean isAvailable = itemDto.getAvailable();
        return name != null
                && description != null
                && isAvailable != null
                && !name.isEmpty()
                && !name.isBlank()
                && !description.isEmpty()
                && !description.isBlank();
    }

}
