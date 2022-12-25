package ru.practicum.shareit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemValidator {

    public static boolean isValidItem(ItemDto itemDto) {
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        return name != null
                && description != null
                && !name.isEmpty()
                && !name.isBlank()
                && !description.isEmpty()
                && !description.isBlank();
    }

}
