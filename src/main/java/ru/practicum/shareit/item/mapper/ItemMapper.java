package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper {

    /**
     * TODO add database mapper when necessary.
     */
    public static Item makeItemFromDB(ResultSet rs) throws SQLException {
        return Item.builder().build();
    }
    public static ItemDto makeDtoFromItem(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.isAvailable())
                .build();
    }
    public static Item makeItemFromDto(int userId, ItemDto item) {
        return Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.isAvailable())
                .ownerId(userId)
                .build();
    }

}
